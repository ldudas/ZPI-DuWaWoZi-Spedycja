/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureTemplate.DRAWING_TOOL;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.Symbol;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.MapOverlay;
import com.esri.toolkit.PrototypeFeature;

/**
 * Class used to draw features on the map using the mouse. A drawing overlay
 * can be set up to draw features according to:
 * <ul>
 * <li>a particular renderer {@link #setUp(DrawingMode, Renderer, Map)}
 * <li>a particular symbol {@link #setUp(DrawingMode, Symbol, Map)}
 * <li>a prototype feature {@link #setUp(PrototypeFeature)}
 * </ul>
 * 
 * Once a drawing overlay is created and activated, features of the specified 
 * type will be drawn on the map via the mouse. To specify what action to 
 * take when drawing a feature is complete, add a DrawingCompleteListener to 
 * the overlay and override the drawingCompleted method. There you can for 
 * example obtain the drawn feature for further use, such as to add it to your 
 * own GraphicsLayer or FeatureLayer, or to do some computation with the 
 * feature. Get the drawn feature from the overlay using {@link #getFeature()}, 
 * or both get the feature and clear it from the overlay using 
 * {@link #getAndClearFeature()}. 
 * <p>
 * 
 * @usage
 * <code><pre>
 * drawingOverlay = new DrawingOverlay();
 * // set it up to draw polylines with the specified symbol, and no attributes 
 * drawingOverlay.setUp(DrawingMode.POLYLINE, new SimpleLineSymbol(new Color(200, 0, 0), 2), null);
 * drawingOverlay.addDrawingCompleteListener(new DrawingCompleteListener() {
 * 
 *   {@literal @}Override
 *   public void drawingCompleted(DrawingCompleteEvent event) {
 *     Feature f = drawingOverlay.getAndClearFeature();
 *   }
 * });
 * jMap.addMapOverlay(drawingOverlay);
 * ...
 * // activate the overlay when desired
 * drawingOverlay.setActive(true);
 * </pre></code>
 * @since 10.2.3
 */
public class DrawingOverlay extends MapOverlay {

  private static final long serialVersionUID = 1L;
  private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(
      this);

  GraphicsLayer _graphicsLayer;
  DrawingProperties _drawingProperties;
  boolean _started = false;
  MultiPath _path = null;
  MultiPoint _points = null;
  Graphic _graphic = null;
  // this flag is useful to remove any temporary point that could have been added to the path.
  // e.g., a point added while drawing a polygon - to provide a live feedback of the drawing.
  private boolean removeLastPoint = false;

  Point _mouseMapPoint = null;
  Point _clickedMapPoint = null;

  private boolean _mapPanToleranceChanged;
  private boolean _mapPanToleranceEnabled;
  private int _mapPanTolerance;

  /**
   * Defines what the mouse right click does.
   */
  enum RightClickFunction {
    CONTEXT_MENU,
    FINISH_FEATURE,
    START_NEW_PATH,
    CANCEL_FEATURE,
    NONE,
  }

  /**
   * Modes for the drawing overlay, specifying the drawing tool and the 
   * geometry type for each mode.
   */
  public enum DrawingMode {
    NONE(DRAWING_TOOL.NONE, Type.UNKNOWN),
    POINT(DRAWING_TOOL.POINT, Type.POINT),
    MULTIPOINT(DRAWING_TOOL.POINT, Type.MULTIPOINT),
    POLYLINE(DRAWING_TOOL.LINE, Type.POLYLINE),
    POLYLINE_FREEHAND(DRAWING_TOOL.FREEHAND, Type.POLYLINE),
    POLYLINE_RECTANGLE(DRAWING_TOOL.RECTANGLE, Type.POLYLINE),
    POLYGON(DRAWING_TOOL.POLYGON, Type.POLYGON),
    POLYGON_FREEHAND(DRAWING_TOOL.FREEHAND, Type.POLYGON),
    POLYGON_RECTANGLE(DRAWING_TOOL.RECTANGLE, Type.POLYGON);

    DRAWING_TOOL _drawingTool;
    Geometry.Type _geometryType;

    public DRAWING_TOOL getDrawingTool() {
      return _drawingTool;
    }

    public Geometry.Type getGeometryType() {
      return _geometryType;
    }

    private DrawingMode(DRAWING_TOOL drawingTool, Geometry.Type geometryType) {
      _drawingTool = drawingTool;
      _geometryType = geometryType;
    }

    public static DrawingMode get(DRAWING_TOOL drawingTool, Geometry.Type geometryType) {
      for (DrawingMode mode : DrawingMode.values() ) {
        if (mode.getDrawingTool().equals(drawingTool) &&
            mode.getGeometryType().equals(geometryType)) {
          return mode;
        }
      }
      return DrawingMode.NONE;
    }
  }

  RightClickFunction _rightClickFunction = RightClickFunction.CONTEXT_MENU;

  EventListenerList _drawingCompleteListenerList = new EventListenerList();

  /**
   * Instantiates a new drawing overlay.
   */
  public DrawingOverlay() {
    _drawingProperties = new DrawingProperties(
        DrawingMode.NONE,
        (Renderer) null,
        null);
  }

  /**
   * Sets up the drawing overlay using a drawing mode and an 
   * appropriate/compatible renderer to render the features created. 
   * A map of attributes can optionally be specified for the features created.
   *
   * @param mode the drawing mode
   * @param renderer the renderer for the features drawn
   * @param attributes a map of attributes, or null
   */
  public void setUp(DrawingMode mode, Renderer renderer,
      Map<String, Object> attributes) {
    setUp(mode.getDrawingTool(), mode.getGeometryType(), renderer, attributes);
  }

  /**
   * Sets up the drawing overlay using a drawing mode and an 
   * appropriate/compatible symbol to render the features created. 
   * A map of attributes can optionally be specified for the features created.
   *
   * @param mode the drawing mode
   * @param symbol the symbol for the features drawn
   * @param attributes a map of attributes, or null
   */
  public void setUp(DrawingMode mode, Symbol symbol,
      Map<String, Object> attributes) {
    setUp(mode.getDrawingTool(), mode.getGeometryType(), symbol, attributes);
  }

  /**
   * Sets up the drawing overlay using a prototype feature to define 
   * the geometry, rendering, and attributes of the features created.
   *
   * @param prototypeFeature the prototype feature
   */
  public void setUp(PrototypeFeature prototypeFeature) {
    setUp(prototypeFeature.getDrawingTool(),
        prototypeFeature.getGeometryType(),
        prototypeFeature.getFeatureLayer().getRenderer(),
        prototypeFeature.getAttributes());
  }

  private void setUp(DRAWING_TOOL drawingTool,
      Geometry.Type geometryType,
      Symbol symbol,
      Map<String, Object> attributes) {
    setUp(drawingTool, geometryType, new SimpleRenderer(symbol), attributes);
  }

  private void setUp(DRAWING_TOOL drawingTool,
      Geometry.Type geometryType,
      Renderer renderer,
      Map<String, Object> attributes) {
    cancelCurrentDrawing();

    DrawingProperties oldDrawingProps = _drawingProperties;
    _drawingProperties = new DrawingProperties(DrawingMode.get(drawingTool, geometryType),
        renderer, attributes);
    _propertyChangeSupport.firePropertyChange(
        "drawingProperties", oldDrawingProps, _drawingProperties);

    setActive(drawingTool != DRAWING_TOOL.NONE);
    updateGraphicsLayer();
  }

  /**
   * Adds a listener for the event fired when drawing a feature has completed.
   *
   * @param l
   *    Listener to add
   */
   public void addDrawingCompleteListener(DrawingCompleteListener l) {
     _drawingCompleteListenerList.add(DrawingCompleteListener.class, l);
   }

   /**
    * Removes a listener for the event fired when drawing a feature has completed.
    *
    * @param l
    *     Listener to remove
    */
   public void removeDrawingCompleteListener(DrawingCompleteListener l) {
     _drawingCompleteListenerList.remove(DrawingCompleteListener.class, l);
   }

   /**
    * Fires a drawing complete event.
    */
   protected void fireDrawingComplete() {
     Object[] listeners = _drawingCompleteListenerList.getListenerList();
     for (int i = listeners.length - 2; i >= 0; i -= 2) {
       if (listeners[i] == DrawingCompleteListener.class) {
         ((DrawingCompleteListener)listeners[i+1]).drawingCompleted(new DrawingCompleteEvent(this, DrawingCompleteEvent.DRAWING_COMPLETED));
       }
     }
   }

   /**
    * Gets the currently active drawing tool.
    *
    * @return The currently active drawing tool
    */
   public FeatureTemplate.DRAWING_TOOL getDrawingTool() {
     return _drawingProperties.getDrawingMode().getDrawingTool();
   }

   /**
    * Gets the drawing mode.
    *
    * @return the drawing mode
    */
   public DrawingMode getDrawingMode() {
     return _drawingProperties.getDrawingMode();
   }

   /**
    * @return
    *     Right mouse click function
    */
   public RightClickFunction getRightClickFunction() {
     return _rightClickFunction;
   }

   /**
    * @param rightClickFunction
    */
   public void setRightClickFunction(RightClickFunction rightClickFunction) {
     _rightClickFunction = rightClickFunction;
   }

   /**
    * Gets the current feature from the overlay.
    *
    * @return
    *     A newly created feature
    */
   public Feature getFeature() {
     Feature g = null;
     if (_graphicsLayer != null) {
       g = _graphicsLayer.getGraphic(id);
     }

     return g;
   }

   /**
    * Clears the feature from the overlay including incomplete features.
    */
   public void clearFeature() {
     if (getMap() != null) {
       _started = false;
       if (_graphicsLayer != null) {
         _graphicsLayer.removeAll();
       }
     }
   }

   /**
    * Gets the current feature from the overlay and clears it.
    *
    * @return
    *     A newly created feature
    */
   public Feature getAndClearFeature() {
     Feature g = getFeature();
     clearFeature();

     return g;
   }

   /**
    * Cancels an in progress drawing
    */
   private void cancelCurrentDrawing() {
     removeLastPoint = false;

     if (_graphicsLayer != null) {
       _graphicsLayer.removeAll();
     }

     _started = false;

     repaint();
   }

   private void endDrawing() {
     repaint();
     removeLastPointIfRequired(true);
     fireDrawingComplete();
   }

   /* (non-Javadoc)
    * @see com.esri.map.MapOverlay#onMouseMoved(java.awt.event.MouseEvent)
    */
   @Override
   public void onMouseMoved(MouseEvent event) {
     if (_started) {
       switch (getDrawingTool()) {
         case POINT:
           break;
         case LINE:
           doPathToolMove(event);
           break;
         case POLYGON:
           doPathToolMove(event);
           break;
         case AUTO_COMPLETE_POLYGON:
           break;
         case FREEHAND:
           doFreehandToolMove(event);
           break;
         case CIRCLE:
           break;
         case ELLIPSE:
           break;
         case RECTANGLE:
           doRectangleToolMove(event);
           break;
         case NONE:
           break;
         default:
           break;
       }
     } else {
       super.onMouseMoved(event);
     }
   }

   boolean _contextMenuShowing = false;

   /* (non-Javadoc)
    * @see com.esri.map.MapOverlay#onMouseClicked(java.awt.event.MouseEvent)
    */
   @Override
   public void onMouseClicked(MouseEvent event) {
     if (SwingUtilities.isLeftMouseButton(event)) {
       if (!_contextMenuShowing) {
         switch (getDrawingTool()) {
           case POINT:
             doPointToolClick(event);
             break;
           case LINE:
             doPathToolClick(event);
             break;
           case POLYGON:
             doPathToolClick(event);
             break;
           case FREEHAND:
             doFreehandToolClick(event);
             break;
           case CIRCLE:
             break;
           case ELLIPSE:
             break;
           case RECTANGLE:
             doRectangleToolClick(event);
             break;
           case NONE:
           default:
             super.onMouseClicked(event);
             break;
         }
       } else {
         // this click closed the context menu
         _contextMenuShowing = false;
         // note the click point so feedback line is correct
         _mouseMapPoint = getMap().toMapPoint(event.getX(), event.getY());
         // repaint the overlay to show feedback
         repaint();
       }
     } else if (SwingUtilities.isRightMouseButton(event)) {
       if (_started) {
         switch (_rightClickFunction) {
           case CONTEXT_MENU:
             doContextMenu(event);
             break;
           case FINISH_FEATURE:
             _started = false;
             endDrawing();
             break;
           case CANCEL_FEATURE:
             cancelCurrentDrawing();
             break;
           case START_NEW_PATH:
             _startNewPath = true;
             break;
           case NONE:
             break;
         }
       }
     }
   }

   /**
    * Adds a listener for property changed events.
    *
    * @param listener
    */
   @Override
   public void addPropertyChangeListener(PropertyChangeListener listener) {
     _propertyChangeSupport.addPropertyChangeListener(listener);
   }

   /**
    * Removes a listener for property changed events.
    *
    * @param listener
    */
   @Override
   public void removePropertyChangeListener(PropertyChangeListener listener) {
     _propertyChangeSupport.removePropertyChangeListener(listener);
   }

   /**
    * Creates a context menu based on the tool in use
    *
    * @param event
    */
   private void doContextMenu(MouseEvent event) {
     JPopupMenu contextMenu = new JPopupMenu();
     DRAWING_TOOL drawingTool = getDrawingTool();
     if (drawingTool != DRAWING_TOOL.RECTANGLE && drawingTool != DRAWING_TOOL.POINT) {
       JMenuItem creatPathMenuItem = new JMenuItem("Create new path");
       creatPathMenuItem.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
           _startNewPath = true;
           _contextMenuShowing = false;
           removeLastPointIfRequired(true);
         }
       });
       contextMenu.add(creatPathMenuItem);
     }
     JMenuItem endFeatureMenuItem = new JMenuItem("Finish Feature");
     endFeatureMenuItem.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e) {
         _started = false;
         _contextMenuShowing = false;
         endDrawing();
       }
     });
     contextMenu.add(endFeatureMenuItem);

     JMenuItem cancelMenuItem = new JMenuItem("Cancel");
     cancelMenuItem.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent arg0) {
         _contextMenuShowing = false;
         cancelCurrentDrawing();
       }
     });
     contextMenu.add(cancelMenuItem);
     contextMenu.show(this, event.getX(), event.getY());
     _contextMenuShowing = true;
   }

   /* (non-Javadoc)
    * @see com.esri.map.MapOverlay#onPaint(java.awt.Graphics)
    */
   @Override
   public void onPaint(Graphics graphics) {
     if (_started && !_contextMenuShowing) {
       switch (getDrawingTool()) {
         case RECTANGLE:
           if (_completedBaseLine) {
             removeLastPoint = false;
             break;
           }
         case LINE:
         case POLYGON:
           if (!_startNewPath) {
             removeLastPointIfRequired(false);
             // add a temporary point to provide live feedback of the drawing
             _path.lineTo(_mouseMapPoint);
             removeLastPoint = true;
             _graphicsLayer.updateGraphic(id, _path);
           }
           break;
         default:
           break;
       }
     }
   }

   @Override public void onRemoved() {
     cancelCurrentDrawing();
     resetPanningTolerance();
     super.onRemoved();
   }

   @Override
   public void setActive(boolean active) {
     super.setActive(active);
     if (active) {
       setPanningTolerance();
     } else {
       resetPanningTolerance();
     }
   }
   
   @Override
   protected void setMap(JMap map) {
     // remove graphics layer if attached to existing map
     if (getMap() != null && _graphicsLayer != null) {
       getMap().getLayers().remove(_graphicsLayer);
       _graphicsLayer = null;
     }
     
     // set the new map
     super.setMap(map);
     
     // attach a new graphics layer to the new map
     updateGraphicsLayer();
   }

   private void setPanningTolerance() {
     if (getMap() != null) {
       // record current values so that panning can be reset after drawing
       _mapPanToleranceEnabled = getMap().isPanToleranceEnabled();
       _mapPanTolerance = getMap().getPanTolerance();
       getMap().setPanToleranceEnabled(true);
       _mapPanToleranceChanged = true;
     }
   }

   private void resetPanningTolerance() {
     if (getMap() != null && _mapPanToleranceChanged) {
       getMap().setPanToleranceEnabled(_mapPanToleranceEnabled);
       getMap().setPanTolerance(_mapPanTolerance);
       _mapPanToleranceChanged = false;
     }
   }

   /**
    * @param event
    */
   private void doPointToolClick(MouseEvent event) {
     if (SwingUtilities.isLeftMouseButton(event)) {
       if (_drawingProperties.getDrawingMode().getGeometryType() != Type.MULTIPOINT) {
         Point point = getMap().toMapPoint(event.getX(), event.getY());
         Graphic g = new Graphic(point, null, _drawingProperties.getAttributes());
         id = _graphicsLayer.addGraphic(g);
         endDrawing();
       } else {
         if (!_started) {
           _points = new MultiPoint();
           _points.add(getMap().toMapPoint(event.getX(), event.getY()));

           Graphic g = new Graphic(_points, null, _drawingProperties.getAttributes());
           id = _graphicsLayer.addGraphic(g);

           _started = true;
         } else {
           _points.add(getMap().toMapPoint(event.getX(), event.getY()));
           _graphicsLayer.updateGraphic(id, _points);
           if (event.getClickCount() > 1) {
             _started = false;
             endDrawing();
           }
         }
       }
     }
   }

   boolean _startNewPath = false;

   /**
    * @param event
    */
   private void doPathToolClick(MouseEvent event) {
     _clickedMapPoint = getMap().toMapPoint(event.getX(), event.getY());
     if (SwingUtilities.isLeftMouseButton(event)) {
       if (!_started) {
         startPath(_clickedMapPoint);
         _mouseMapPoint = _clickedMapPoint;
         _started = true;
       } else {
         if (event.getClickCount() > 1) {
           _started = false;
           endDrawing();
         }
         if (!_startNewPath) {
           removeLastPointIfRequired(false);
           _path.lineTo(_clickedMapPoint);
           _graphicsLayer.updateGraphic(id, _path);
         } else {
           _startNewPath = false;
           _path.startPath(_clickedMapPoint);
           _graphicsLayer.updateGraphic(id, _path);
         }
       }
     }
   }

   /**
    * @param event
    */
   private void doPathToolMove(MouseEvent event) {
     if (_started) {
       _mouseMapPoint = getMap().toMapPoint(event.getX(), event.getY());
     }
     repaint();
   }

   /**
    * @param event
    */
   private void doFreehandToolClick(MouseEvent event) {
     if (SwingUtilities.isLeftMouseButton(event)) {
       _clickedMapPoint = getMap().toMapPoint(event.getX(), event.getY());
       if (!_started) {
         startPath(_clickedMapPoint);
         _started = true;
       } else if (_startNewPath) {
         _startNewPath = false;
         _path.startPath(_clickedMapPoint);
       } else {
         _started = false;
         endDrawing();
       }
     }
   }

   /**
    * @param event
    */
   private void doFreehandToolMove(MouseEvent event) {
     if (_started) {
       if (!_startNewPath) {
         _path.lineTo(getMap().toMapPoint(event.getX(), event.getY()));
         _graphicsLayer.updateGraphic(id, _path);
       }
     }
   }

   private boolean _completedBaseLine = false;
   private double angle = 0.0;

   /**
    * @param event
    *     Mouse event
    */
   private void doRectangleToolClick(MouseEvent event) {
     if (SwingUtilities.isLeftMouseButton(event)) {
       if (!_started) {
         _completedBaseLine = false;
         _clickedMapPoint = getMap().toMapPoint(event.getX(), event.getY());
         _mouseMapPoint = _clickedMapPoint;
         startPath(_clickedMapPoint);
         _started = true;
       } else {
         _mouseMapPoint = getMap().toMapPoint(event.getX(), event.getY());

         if (!_completedBaseLine) {
           _completedBaseLine = true;
           angle = Math.atan2(_clickedMapPoint.getY() - _mouseMapPoint.getY(), _clickedMapPoint.getX() - _mouseMapPoint.getX());
         } else {
           _started = false;
           endDrawing();
         }
       }
     }
   }

   /**
    * @param event
    *     Mouse event
    */
   private void doRectangleToolMove(MouseEvent event) {
     if (_started) {
       _mouseMapPoint = getMap().toMapPoint(event.getX(), event.getY());
       if (_completedBaseLine) {
         createRectangle(_clickedMapPoint, _mouseMapPoint, angle, _path);
         _graphicsLayer.updateGraphic(id, _path);
       }
       repaint();
     }
   }

   /**
    * Creates a rectangle based on a diagonal and a rotation angle
    *
    * @param start
    *     start point of base line (map coordinates)
    * @param end
    *     end point of diagonal (map coordinates)
    * @param angle
    *     angle of base line (radians)
    * @param path
    *     path to put the rectangle geometry into
    */
   private void createRectangle(Point start, Point end, double angle, MultiPath path) {
     double x1 = start.getX();
     double y1 = start.getY();
     double x2 = end.getX();
     double y2 = end.getY();

     path.setEmpty();

     Path2D.Double diagonal = new Path2D.Double();
     diagonal.moveTo(x1, y1);
     diagonal.lineTo(x2, y2);

     diagonal.transform(AffineTransform.getRotateInstance(-angle, x1, y1));
     Rectangle2D rect = diagonal.getBounds2D();

     double[] points = new double[6];
     PathIterator iter = rect.getPathIterator(AffineTransform.getRotateInstance(angle, x1, y1));
     while (!iter.isDone()) {
       iter.currentSegment(points);
       int segType = iter.currentSegment(points);
       if (segType == PathIterator.SEG_MOVETO) {
         path.startPath(points[0], points[1]);
       } else if (segType == PathIterator.SEG_LINETO) {
         path.lineTo(points[0], points[1]);
       }

       iter.next();
     }
   }

   private void updateGraphicsLayer() {
     cancelCurrentDrawing();
     if (_graphicsLayer == null && getMap() != null) {
       _graphicsLayer = new GraphicsLayer();
       getMap().getLayers().add(_graphicsLayer);
     }
     if (_graphicsLayer != null) {
       _graphicsLayer.setRenderer(_drawingProperties.getRenderer());
     }
   }

   int id = -1;

   /**
    * Creates a polyline or polygon path and assigns it to a graphic
    *
    * @param startPoint
    *     first point in the path (map coordinates)
    */
   private void startPath(Point startPoint) {
     if (_drawingProperties.getDrawingMode().getGeometryType() == Type.POLYLINE) {
       _path = new Polyline();
       _path.startPath(startPoint);

     } else if (_drawingProperties.getDrawingMode().getGeometryType() == Type.POLYGON) {
       _path = new Polygon();
       _path.startPath(startPoint);
       _path.lineTo(startPoint);
       _path.closeAllPaths();
     }
     _graphic = new Graphic(_path, null, _drawingProperties.getAttributes());
     id = _graphicsLayer.addGraphic(_graphic);
   }

   /**
    * Removes, if necessary, the last point added to the multipath.
    */
   private void removeLastPointIfRequired(boolean updateGraphic) {
     if (removeLastPoint) {
       int currPathIndex = _path.getPathCount() - 1;
       _path.removePoint(currPathIndex, _path.getPathSize(currPathIndex) - 1);
       if (updateGraphic) {
         _graphicsLayer.updateGraphic(id, _path);
       }
       removeLastPoint = false;
     }
   }
}
