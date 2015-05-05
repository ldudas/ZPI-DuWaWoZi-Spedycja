/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListener;

/**
 * A {@link JMap} that can interact with a target map to focus that map's extent in the context
 * of this map's extent. A mouse click on this map causes a pan on the target
 * map to the corresponding extent. <br><br>
 * The spatial reference of the overview map can be different from that of the target map.<br><br>
 * The focus area is represented by a rectangle or a point based on its size, customizable
 * using  {@link #setMinimumFocusSize(int)}.<br><br>
 * To customize the UI, see <ul>
 * <li>{@link #setFocusAreaFillColor(Color)},
 * <li>{@link #setFocusAreaBorderColor(Color)},
 * <li>{@link #setFocusPointFillColor(Color)},
 * <li>{@link #setFocusPointBorderColor(Color)},
 * <li>{@link #setFocusPointSize(int)}.
 * </ul>
 * @usage
 * <code>
 * <pre>
 * final OverviewMap overviewMap = new OverviewMap(jMap, layer);
 * overviewMapPanel.add(overviewMapPanel); // adds to a component
 * 
 * // to set the overview map's extent.
 * //overviewMap.setExtent(new Envelope(-17517313, -1387361, -2546029, 8598576));
 * </pre>
 * </code>
 * @since 10.2.3
 */
@SuppressWarnings("serial")
public class OverviewMap extends JMap {

    private JMap targetMap;
    private Layer layer;
    
    // variables to avoid re-calculation/re-fetching
    private SpatialReference sr;
    private SpatialReference targetMapSR;
    private Rectangle2D focusRect = new Rectangle2D.Double();
    private Rectangle2D focusRect2 = new Rectangle2D.Double();
    private boolean wrapFocus = false;
    private boolean drawAsPoint = false;
    private Envelope fullExtent;
    
    // UI customization options
    private int minFocusSize = 4;
    private int focusPointSize = 8;
    private final Rectangle2D.Double polygonShape =
        new Rectangle2D.Double(0, 0, minFocusSize, minFocusSize);
    private final Ellipse2D.Double pointShape =
        new Ellipse2D.Double(0, 0, minFocusSize, minFocusSize);
    private Color focusAreaFillColor    = new Color(0, 0, 0, 40);
    private Color focusAreaBorderColor  = Color.RED;
    private Color focusPointFillColor   = Color.RED;
    private Color focusPointBorderColor = Color.WHITE;

// ------------------------------------------------------------------------
// Constructor
// ------------------------------------------------------------------------
    /**
     * Creates an overview map.
     * @param targetMap a map that this will interact with.
     * @param layer a layer to be displayed in this map.
     * @throws IllegalArgumentException in case of null arguments.
     */
    public OverviewMap(JMap targetMap, Layer layer) {
        super();
        if (targetMap == null || layer == null) {
            throw new NullPointerException("Either map or layer is null.");
        }
        this.targetMap = targetMap;
        this.targetMapSR = targetMap.getSpatialReference();
        this.layer = layer;
        init();
    }

// ------------------------------------------------------------------------
// Public methods
// ------------------------------------------------------------------------
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // no need to paint if focus area is non-existing.
        if (focusRect.getWidth() == 0 || focusRect.getHeight() == 0) {
            return;
        }

        // draw focus area as rectangle or a point based on size.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (drawAsPoint) {
            pointShape.setFrame(
                focusRect.getCenterX() - focusPointSize/2,
                focusRect.getCenterY() - focusPointSize/2,
                focusPointSize,
                focusPointSize);
            g2d.setColor(focusPointFillColor);
            g2d.fill(pointShape);
            g2d.setColor(focusPointBorderColor);
            g2d.draw(pointShape);
        } else {
          paintRect(focusRect, g2d);
          if (wrapFocus) {
            paintRect(focusRect2, g2d);
          }
        }
    }
    
    private void paintRect(Rectangle2D rect, Graphics2D g2d) {
      g2d.setColor(focusAreaFillColor);
      polygonShape.setRect(rect);
      g2d.fill(polygonShape);
      g2d.setColor(focusAreaBorderColor);
      g2d.draw(polygonShape);
    }

    /**
     * When the width or height of the focus area is less than this, then the focus
     * area is represented by a point.
     * @param minSize size in pixels.
     * @see #getMinimumFocusSize()
     */
    public void setMinimumFocusSize(int minSize) {
        this.minFocusSize = minSize;
    }

    /**
     * Returns the minimum focus size.
     * @return the size in pixels that is used to determine whether the focus area
     * should be a rectangle or a point.
     * @see #setMinimumFocusSize(int)
     */
    public int getMinimumFocusSize() {
        return minFocusSize;
    }

    /**
     * Gets the color to be used for the focus area when it is a rectangle.
     * @return focus area fill color.
     * @see #setFocusAreaFillColor(Color)
     * @see #setFocusAreaBorderColor(Color)
     */
    public Color getFocusAreaFillColor() {
        return focusAreaFillColor;
    }

    /**
     * Sets the fill color to be used when the focus area is a rectangle.
     * @param focusAreaFillColor focus area fill color.
     * @see #getFocusAreaFillColor()
     * @see #setFocusAreaBorderColor(Color)
     * @throws IllegalArgumentException if the input is null.
     */
    public void setFocusAreaFillColor(Color focusAreaFillColor) {
        if (focusAreaFillColor == null) {
            throw new IllegalArgumentException("Focus area fill color should not be null.");
        }
        this.focusAreaFillColor = focusAreaFillColor;
    }

    /**
     * Gets the focus area border color, used when it is a rectangle.
     * @return focus area border color.
     * @see #setFocusAreaBorderColor(Color)
     * @see #setFocusAreaFillColor(Color)
     */
    public Color getFocusAreaBorderColor() {
        return focusAreaBorderColor;
    }

    /**
     * Sets the focus area border color, used when it is a rectangle.
     * @param focusAreaBorderColor focus area border color.
     * @see #getFocusAreaBorderColor()
     * @see #setFocusAreaFillColor(Color)
     * @throws IllegalArgumentException if the input is null.
     */
    public void setFocusAreaBorderColor(Color focusAreaBorderColor) {
        if (focusAreaBorderColor == null) {
            throw new IllegalArgumentException("Focus area border color should not be null.");
        }
        this.focusAreaBorderColor = focusAreaBorderColor;
    }

    /**
     * Gets the diameter used when the focus area is a point.
     * @return focus point diameter, in pixels.
     */
    public int getFocusPointSize() {
        return focusPointSize;
    }

    /**
     * Sets the diameter to be used when the focus area is a point.
     * @param focusPointSize diameter to be used for the focus point, in pixels.
     */
    public void setFocusPointSize(int focusPointSize) {
        this.focusPointSize = focusPointSize;
    }

    /**
     * Gets the fill color used when the focus area is a point.
     * @return the focus point fill color.
     * @see #setFocusPointFillColor(Color)
     * @see #setFocusPointBorderColor(Color)
     */
    public Color getFocusPointFillColor() {
        return focusPointFillColor;
    }

    /**
     * Sets fill color to be used when the focus area is a point.
     * @param focusPointFillColor the focus point fill color.
     * @throws IllegalArgumentException if the input is null.
     * @see #getFocusPointFillColor()
     */
    public void setFocusPointFillColor(Color focusPointFillColor) {
        if (focusPointFillColor == null) {
            throw new IllegalArgumentException("Focus point fill color should not be null.");
        }
        this.focusPointFillColor = focusPointFillColor;
    }

    /**
     * Gets border color to be used when the focus area is a point.
     * @return the focus point border color.
     * @throws IllegalArgumentException if the input is null.
     * @see #setFocusAreaBorderColor(Color)
     */
    public Color getFocusPointBorderColor() {
        return focusPointBorderColor;
    }

    /**
     * Sets the border color to be used when the focus area is a point.
     * @param focusPointBorderColor the focus point border color.
     * @throws IllegalArgumentException if the input is null.
     * @see #getFocusPointBorderColor()
     */
    public void setFocusPointBorderColor(Color focusPointBorderColor) {
        if (focusPointBorderColor == null) {
            throw new IllegalArgumentException("Focus point border color should not be null.");
        }
        this.focusPointBorderColor = focusPointBorderColor;
    }

// ------------------------------------------------------------------------
// Private methods
// ------------------------------------------------------------------------
    private void init() {
        // customize
        setShowingEsriLogo(false);
        addMapEventListener(new MapEventListener() {
            @Override
            public void mapReady(MapEvent event) {
                sr = getSpatialReference();
                fullExtent = getFullExtent();
                updateFocus();
            }

            @Override
            public void mapExtentChanged(MapEvent event) {
              updateFocus();
            }

            @Override
            public void mapDispose(MapEvent event) {
            }
        });


        // change focus based on parent map's extent.
        addParentMapListener();

        // disable unwanted mouse actions on overview map.
        // change parent map's extent based on interaction with the overview map.
        configureMouseListeners();

        // add layer to map
        getLayers().add(layer);

    }

    private void addParentMapListener() {
        // listen to parent map
        targetMap.addMapEventListener(new MapEventListener() {

            @Override
            public void mapReady(MapEvent event) {
                targetMapSR = targetMap.getSpatialReference();
                updateFocus();
            }

            @Override
            public void mapExtentChanged(MapEvent event) {
              updateFocus();
            }

            @Override
            public void mapDispose(MapEvent event) {
                dispose();
            }
        });
    }

    private void configureMouseListeners() {
        // disable base mouse motion listeners
        MouseMotionListener[] mouseMotionListeners = getMouseMotionListeners();
        for (MouseMotionListener mouseMotionListener : mouseMotionListeners) {
            removeMouseMotionListener(mouseMotionListener);
        }
        MouseListener[] mouseListeners = getMouseListeners();
        for (MouseListener mouseListener : mouseListeners) {
            removeMouseListener(mouseListener);
        }
        MouseWheelListener[] mouseWheelListeners = getMouseWheelListeners();
        for (MouseWheelListener mouseWheelListener : mouseWheelListeners) {
            removeMouseWheelListener(mouseWheelListener);
        }

        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (sr == null || targetMapSR == null) {
                    return;
                }

                // avoid GeometryEngine.project() when the clicked point is outside
                // of its full extent. 
                Point p = toMapPoint(arg0.getX(), arg0.getY());
                if (!fullExtent.contains(p)) {
                    return;
                }

                // project if spatial references are different.
                if (sr.getID() != targetMapSR.getID()) {
                    p = (Point) GeometryEngine.project(p, sr, targetMapSR);
                    // For Point p = new Point(-179, 89.9);
                    // GeometryEngine.project(p, SpatialReference.create(4326), 
                    // SpatialReference.create(102100));
                    // returns empty point. It works for p = (-179, 89);
                    // Reason: The Projection Engine has a cutoff on y to +/-89 degrees, 
                    // because Web Mercator grows to infinity as y approaches 90.
                    if (p.isEmpty()) {
                        p = (Point) GeometryEngine.project(
                            new Point((int) p.getX(), (int) p.getY()), sr, targetMapSR);
                    }
                    if (p.isEmpty()) {
                        throw new RuntimeException("Error in projecting a point from " +
                            sr.getID() + " to " + targetMapSR.getID());
                    }
                }

                // focus on the clicked area within the parent map.
                // the consequent extent change in parent map will cause a
                // draw of the focused area within the overview map.
                targetMap.panTo(p);
            }
        };
        addMouseListener(mouseListener);
    }
    
    private void updateFocus() {
      if (sr == null || targetMapSR == null) {
          return;
      }
      calculateFocus();
      repaint();
    }
    
    private void calculateFocus() {
      // normalize current extent
      Envelope extent = getNormalizedExtent(targetMap);
      Envelope fullExtent = targetMap.getFullExtent();
      
      // decide whether the focus area has to be wrapped.
      // wrapping will create 2 rectangles - one each on either end of the x-axis.
      double leftWidth = 0;
      double rightWidth = 0;
      if (extent.getXMin() < fullExtent.getXMin()) {
        rightWidth = fullExtent.getXMin() - extent.getXMin();
        leftWidth = extent.getWidth() - rightWidth;
      } else if (extent.getXMax() > fullExtent.getXMax()) {
        leftWidth = extent.getXMax() - fullExtent.getXMax();
        rightWidth = extent.getWidth() - leftWidth;
      }
      wrapFocus = leftWidth > 0;
      
      // convert extent coordinates to screen coordinates
      if (wrapFocus) {
        Envelope leftExtent = new Envelope(
          fullExtent.getXMin(), extent.getYMin(), 
          fullExtent.getXMin() + leftWidth, extent.getYMax());
        Envelope rightExtent = new Envelope(
          fullExtent.getXMax() - rightWidth, extent.getYMin(), 
          fullExtent.getXMax(), extent.getYMax());
        calculateFocusFromExtent(focusRect, leftExtent);
        calculateFocusFromExtent(focusRect2, rightExtent);
        // draw as point or rectangle based on size 
        drawAsPoint = 
          (focusRect.getWidth() + focusRect2.getWidth()) < minFocusSize || 
          focusRect.getHeight() < minFocusSize;
      } else {
        calculateFocusFromExtent(focusRect, extent);
        drawAsPoint = focusRect.getWidth() < minFocusSize || focusRect.getHeight() < minFocusSize;
      }
      
      
    }
    
    /**
     * This method calculates the normalized value of the current extent of a map.
     * <p>
     * When the map is wrapped around the x-axis, its x-value is continuously increased/decreased
     * based on direction of panning. 
     * <p>
     * For example, consider a map with full extent/normalized range of 0 to 4,
     * if the map is wrapped around once towards east, then the current extent x-value could be 
     * greater than 4; if the map is wrapped around thrice towards east, then the current extent 
     * x-value could be greater than 3 * 4.
     * @param map map.
     * @return the normalized extent of the current extent of the input map.
     */
    private Envelope getNormalizedExtent(JMap map) {
      Envelope extent = map.getExtent();
      Envelope fullExtent = map.getFullExtent();
      if (extent.getXMin() > fullExtent.getXMax()) {
        double minX = extent.getXMin();
        // reduce by the number of wraparounds
        while (minX > fullExtent.getXMax()) {
          minX -= fullExtent.getWidth();
        }
        return new Envelope(minX, extent.getYMin(), minX + extent.getWidth(), extent.getYMax());
      } else if (extent.getXMax() < fullExtent.getXMin()) {
        double maxX = extent.getXMax();
        // increase by the number of wraparounds
        while (maxX < fullExtent.getXMin()) {
          maxX += fullExtent.getWidth();
        }
        return new Envelope(maxX - extent.getWidth(), extent.getYMin(), maxX, extent.getYMax());
      } else {
        return extent;
      }
    }
    
    private void calculateFocusFromExtent(Rectangle2D rect, Envelope targetMapExtent) {
      
      if (targetMapExtent == null) {
        return;
      }
      
      // map current extent to screen coordinates
      Point ul = targetMapExtent.getUpperLeft();
      Point lr = targetMapExtent.getLowerRight();
      if (sr.getID() != targetMapSR.getID()) {
          ul = project(ul, sr, targetMapSR);
          lr = project(lr, sr, targetMapSR);
      }
      ul = toScreenPoint(ul);
      lr = toScreenPoint(lr);
      
      rect.setFrameFromDiagonal(
          new Point2D.Double(ul.getX(), ul.getY()),
          new Point2D.Double(lr.getX(), lr.getY()));
    }
    
    private Point project(Point p, SpatialReference fromSR, SpatialReference toSR) {
      Point pp = (Point) GeometryEngine.project(p, toSR, fromSR);
      // For Point p = new Point(-179, 89.9);
      // GeometryEngine.project(p, SpatialReference.create(4326), SpatialReference.create(102100));
      // returns empty point. It works for p = (-179, 89);
      // Reason: The Projection Engine has cutoff on y to +/-89 degrees, because Web Mercator
      // grows to infinity as y approaches 90.
      // so the logic below rounds off the values in such cases.
      if (pp.isEmpty()) {
          pp = (Point) GeometryEngine.project(
              new Point((int) p.getX(), (int) p.getY()), toSR, fromSR);
      }
      if (pp.isEmpty()) {
          throw new RuntimeException("Error in projecting a point from " +
              fromSR.getID() + " to " + toSR.getID());
      }
      return pp;
    }

}
