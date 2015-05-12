/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.esri.toolkit.sliders.ShapeSlider;
import com.esri.core.geometry.Envelope;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.LocationOnMap;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListenerAdapter;
import com.esri.map.MapOverlay;
import com.esri.map.ResolutionList;

/**
 * This class displays a navigation control over the top of its parent map
 * control. This consists of buttons for panning the map, a slider for
 * setting the zoom level, a circular control to rotate the map, and buttons 
 * to restore the map to its full extent and to restore the map so that 
 * the top corresponds to North.
 * <p>
 * The navigator's default position is at the bottom-left of the map.<br><br>
 * This overlay can be customized using methods such as:
 * <ul>
 * <li>{@link #setNavigatorSize(int)}
 * <li>{@link #setLocation(LocationOnMap)}
 * <li>{@link #setBackground(Color)}
 * </ul>
 * @usage
 * <code>
 * NavigatorOverlay navigatorOverlay = new NavigatorOverlay();<br>
 * jMap.addMapOverlay(navigatorOverlay);
 * </code>
 * @since 10.2.3
 */
public class NavigatorOverlay extends MapOverlay {

   /**
     * Spacing between buttons and other UI components.
     */
    private int _buttonSpacing = 4;

    private static final long serialVersionUID = 1L;
    private LocationOnMap locationOnMap = null;
    private Point topLeftAbsolute = null;
    private Color _background = new Color(100, 100, 100, 110);
    private Color _foreground = Color.WHITE;
    private Rectangle2D _panControlsBoundary;
    private int _size = 90;
    private java.awt.geom.Ellipse2D.Double _panButtonsBackground;
    private Stroke _panOutline = new BasicStroke(2);
    private Rectangle2D _panWestBounds;
    private Path2D _panWestShape;
    private Rectangle2D _panNorthBounds;
    private Path2D _panNorthShape;
    private Rectangle2D _panEastBounds;
    private Path2D _panEastShape;
    private Rectangle2D _panSouthBounds;
    private Path2D _panSouthShape;
    private Ellipse2D.Double _panShapeBackground;
    private JButton _buttonZoomIn;
    private JButton _buttonZoomOut;
    private RoundRectangle2D _resetNorthBackground;
    private Path2D _resetNorthNShape;
    private RoundRectangle2D _fullExtentBackground;
    private Path2D _fullExtentShape;
    private double _panShapeSize;
    private boolean _sliderCreated;
    private JPanel _zoomSliderBackground;
    private ShapeSlider _zoomSlider;
    private ArrayList<Double> _zoomValues;
    private boolean _settingZoom = false;
    private int _zoomSliderLength;
    private int _zoomSliderWidth;
    private int _mapDegrees = 0;
    private int _controlDegrees = 0;
    private Point prevPoint;

    // useful for rotation calculations
    private double _panButtonRotation;
    private int _pivotX;
    private int _pivotY;
    private int[] _posX = new int[4]; // in order, east, south, west, north
    private int[] _posY = new int[4]; // in order, east, south, west, north
    
    /**
     * Instantiates a new navigator overlay.
     */
    public NavigatorOverlay() {
      setLocation(LocationOnMap.BOTTOM_LEFT);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.esri.map.MapOverlay#onPaint(java.awt.Graphics)
     */
    @Override
    public void onPaint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform transform = g2d.getTransform();
        
        // Draw the control
        g2d.setPaint(_background);
        g2d.setTransform(transform);
        g2d.fill(_resetNorthBackground);
        g2d.fill(_fullExtentBackground);
        g2d.fill(_panButtonsBackground);

        g2d.setPaint(_foreground);
        g2d.draw(_resetNorthNShape);
        g2d.draw(_fullExtentShape);

        Stroke currStroke = g2d.getStroke();
        g2d.setStroke(_panOutline);
        g2d.draw(_panButtonsBackground);

        g2d.setStroke(currStroke);

        g2d.setTransform(transform);
        g2d.translate(_posX[0], _posY[0]);
        g2d.draw(_panShapeBackground);
        g2d.rotate(_panButtonRotation, _pivotX, _pivotY);
        g2d.fill(_panEastShape);

        g2d.setTransform(transform);
        g2d.translate(_posX[1], _posY[1]);
        g2d.draw(_panShapeBackground);
        g2d.rotate(_panButtonRotation, _pivotX, _pivotY);
        g2d.fill(_panSouthShape);

        g2d.setTransform(transform);
        g2d.translate(_posX[2], _posY[2]);
        g2d.draw(_panShapeBackground);
        g2d.rotate(_panButtonRotation, _pivotX, _pivotY);
        g2d.fill(_panWestShape);

        g2d.setTransform(transform);
        g2d.translate(_posX[3], _posY[3]);
        g2d.draw(_panShapeBackground);
        g2d.draw(_panNorthShape);

        g2d.setTransform(transform);
        paintChildren(graphics);
    }

    @Override
    public void setBackground(Color color) {
        if (color == null) {
          return;
        }
        _background = color;
        updateColors();
    }

    @Override
    public void setForeground(Color color) {
        if (color == null) {
          return;
        }
        _foreground = color;
        updateColors();
    }

    @Override
    public Color getBackground() {
        return _background;
    }

    /**
     * Sets the location.
     * @param topLeft location to be set. Null value is ignored.
     */
    @Override
    public void setLocation(Point topLeft) {
        if (topLeft == null) {
          return;
        }
        LocationOnMap loc =  new LocationOnMap(topLeft);
        topLeftAbsolute = loc;
        setLocation(loc);
    }
    
    @Override
    public void setLocation(int x, int y) {
      setLocation(new Point(x, y));        
    }
    
    /**
     * Sets the location of the navigator relative to the map.
     * @param location location to be set. Null value is ignored.
     * @since 10.2.3
     */
    public void setLocation(LocationOnMap location) {
      if (location == null) {
        return;
      }
      locationOnMap = location;
      if (_buttonZoomIn == null) {
        setupNavigator();
      } else {
        createShapes();
      }
      repaint();
    }
   
    @Override
    public void onMouseClicked(MouseEvent event) {
        Point p = event.getPoint();
        if (_panButtonsBackground.contains(p)) {
            if (_panNorthBounds.contains(p)) {
                Envelope extent = getMap().getExtent();
                extent.setYMin(extent.getYMin() + getMap().getExtent().getHeight() * (0.25));
                getMap().panTo(extent);
            } else if (_panEastBounds.contains(p)) {
                Envelope extent = getMap().getExtent();
                extent.setXMin(extent.getXMin() + getMap().getExtent().getWidth() * (0.25));
                getMap().panTo(extent);
            } else if (_panSouthBounds.contains(p)) {
                Envelope extent = getMap().getExtent();
                extent.setYMax(extent.getYMax() - getMap().getExtent().getHeight() * (0.25));
                getMap().panTo(extent);
            } else if (_panWestBounds.contains(p)) {
                Envelope extent = getMap().getExtent();
                extent.setXMax(extent.getXMax() - getMap().getExtent().getWidth() * (0.25));
                getMap().panTo(extent);
            }
        } else if (_resetNorthBackground.contains(p)) {
            if (getMap() != null) {
                getMap().setRotation(0);
                _controlDegrees = 0;
                _mapDegrees = 0;
                updateShapeTransformation();
                repaint();
            }
        } else if (_fullExtentBackground.contains(p)) {
            if (getMap() != null) {
                getMap().setExtent(getMap().getFullExtent());
            }
        } else {
            super.onMouseClicked(event);
        }
    }

    @Override
    public void onMousePressed(MouseEvent event) {
        Point p = event.getPoint();
        if (_panButtonsBackground.contains(p)) {
            prevPoint = p;
        } else {
            super.onMousePressed(event);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event) {
        super.onMouseReleased(event);
        prevPoint = null;
    }

    private long lastRequestTime;

    @Override
    public void onMouseDragged(MouseEvent event) {
        if (prevPoint != null) {
            if (_panControlsBoundary.contains(prevPoint)) {
                double dx = _panControlsBoundary.getCenterX() - prevPoint.getX();
                double dy = _panControlsBoundary.getCenterY() - prevPoint.getY();
                double angle1 = Math.atan2(dy,  dx);

                dx = _panControlsBoundary.getCenterX() - event.getPoint().getX();
                dy = _panControlsBoundary.getCenterY() - event.getPoint().getY();
                double angle2 = Math.atan2(dy,  dx);

                // map rotation is measured anti-clockwise, control rotation is measured clockwise.
                if (angle1 > angle2) {
                    _mapDegrees = (_mapDegrees + 2) % 360;
                    _controlDegrees = (_controlDegrees - 2);
                    if (_controlDegrees < 0) {
                        _controlDegrees = 360 + _controlDegrees;
                    }
                } else {
                    _mapDegrees = (_mapDegrees - 2);
                    if (_mapDegrees < 0) {
                        _mapDegrees = 360 + _mapDegrees;
                    }
                    _controlDegrees = (_controlDegrees + 2) % 360;
                }

                long currRequestTime = System.currentTimeMillis();
                if ((currRequestTime - lastRequestTime) > 50) {
                    getMap().setRotation(_mapDegrees);
                    lastRequestTime = currRequestTime;
                }

                updateShapeTransformation();
                repaint();
            }
            prevPoint = event.getPoint();
        } else {
            super.onMouseDragged(event);
        }
    }

    private void updateShapeTransformation() {
        _panButtonRotation = Math.toRadians(_controlDegrees);
        double x1 = _panControlsBoundary.getCenterX();
        double y1 = _panControlsBoundary.getCenterY();
        double r = _zoomSliderLength / 2;

        double radians = _panButtonRotation;

        _pivotX = _panEastBounds.getBounds().width/2;
        _pivotY = _panEastBounds.getBounds().height/2;

        // east
        double x2 = x1 + r * Math.cos(radians);
        double y2 = y1 + r * Math.sin(radians);
        _posX[0] = (int) (x2 - _pivotX);
        _posY[0] = (int) (y2 - _pivotY);
        _panEastBounds.setRect(_posX[0], _posY[0],
            _panEastBounds.getWidth(), _panSouthBounds.getHeight());

        // south
        radians = Math.toRadians((_controlDegrees + 90) % 360);
        x2 = x1 + r * Math.cos(radians);
        y2 = y1 + r * Math.sin(radians);
        _posX[1] = (int) (x2 - _pivotX);
        _posY[1] = (int) (y2 - _pivotY);
        _panSouthBounds.setRect(_posX[1], _posY[1],
            _panSouthBounds.getWidth(), _panSouthBounds.getHeight());

        // west
        radians = Math.toRadians((_controlDegrees + 180) % 360);
        x2 = x1 + r * Math.cos(radians);
        y2 = y1 + r * Math.sin(radians);
        _posX[2] = (int) (x2 - _pivotX);
        _posY[2] = (int) (y2 - _pivotY);
        _panWestBounds.setRect(_posX[2], _posY[2],
            _panWestBounds.getWidth(), _panWestBounds.getHeight());

        // north
        radians = Math.toRadians((_controlDegrees + 270) % 360);
        x2 = x1 + r * Math.cos(radians);
        y2 = y1 + r * Math.sin(radians);
        _posX[3] = (int) (x2 - _pivotX);
        _posY[3] = (int) (y2 - _pivotY);
        _panNorthBounds.setRect(_posX[3], _posY[3],
            _panNorthBounds.getWidth(), _panNorthBounds.getHeight());
    }

    /*
     * Overridden to add map ready and extent changed event handlers. When the
     * map becomes ready, we can set the zoom levels on the zoom slider. When
     * the extent changes we can set the zoom slider's thumb control to the
     * current zoom level.
     *
     * (non-Javadoc)
     *
     * @see com.esri.map.MapOverlay#setMap(com.esri.map.JMap)
     */
    @Override
    protected void setMap(JMap map) {
        super.setMap(map);
        map.addMapEventListener(new MapEventListenerAdapter() {

            @Override
            public void mapReady(MapEvent event) {

                final JMap map = (JMap) event.getSource();

                // Get the base layer, this will define the zoom levels we can
                // use.
                Layer baseLayer = (Layer) map.getLayers().get(0);

                if (baseLayer != null) {
                    int initialLevel = setupZoomValues(map);
                    _zoomSlider.setValue(initialLevel);
                    _zoomSlider.setMaximum((_zoomValues.size() - 1));
                }
                
                setupNavigator();
            }

            /*
             * Update the position of the zoom slider's thumb in response to
             * extent changes that weren't triggered by the zoom slider.
             *
             * (non-Javadoc)
             *
             * @see
             * com.esri.map.MapEventListenerAdapter#mapExtentChanged(com.esri
             * .map.MapEvent)
             */
            @Override
            public void mapExtentChanged(MapEvent event) {
                if (!_settingZoom && _zoomValues != null) {
                    _zoomSlider.setValue(determineZoomLevel(((JMap) event
                            .getSource()).getResolution()));
                }
            }
        });
    }

    /**
     * Sets up the zoom values. Currently this simply takes the resolution of the
     * map's maximum extent and divides it up into eight zoom levels by
     * repeatedly dividing by two. These values are put in an array with each
     * array index being one zoom level. The method works out the nearest zoom
     * level to the current map extent's resolution and returns the resulting
     * value.
     *
     * @param map
     *            The map control used to get the full extent
     * @return The current zoom level of the map.
     */
    protected int setupZoomValues(JMap map) {
        int initialZoomLevel = 0;

        ResolutionList resolutionList = map.getResolutionList();
        _zoomValues = new ArrayList<Double>(resolutionList);

        if(_zoomValues.isEmpty()){
        // Create an array of resolution values based on the resolution of
        // the map at its full extent
            double maxResolution = map.getFullExtent().getWidth() / getWidth();
            _zoomValues.add(maxResolution);
            _zoomValues.add(maxResolution / 2);
            _zoomValues.add(maxResolution / 4);
            _zoomValues.add(maxResolution / 8);
            _zoomValues.add(maxResolution / 16);
            _zoomValues.add(maxResolution / 32);
            _zoomValues.add(maxResolution / 64);
            _zoomValues.add(maxResolution / 128);
            _zoomValues.add(maxResolution / 256);
        }

        // Get the map's current resolution and determine the corresponding
        // zoom level.
        double curResolution = map.getResolution();

        initialZoomLevel = determineZoomLevel(curResolution);

        return initialZoomLevel;
    }

    /**
     * Given a map resolution, finds the nearest zoom level in the array of zoom
     * levels created by {@link #setupZoomValues(JMap)}. We first check to see
     * if the given value falls outside the range of values we have and return
     * the first or last zoom level as appropriate. If the given value falls
     * within the range of values we have, loop through them all and find the
     * nearest one to the given value.
     *
     * @param resolutionToFind
     *            Find the nearest zoom level to this value.
     * @return The zoom level nearest to the given value.
     */
    protected int determineZoomLevel(double resolutionToFind) {
        int initialZoomLevel = 0;

        int endIndex = _zoomValues.size() - 1;
        if (resolutionToFind < _zoomValues.get(endIndex)) {
            // Higher resolution than last resolution in list so just return
            // the last zoom level
            initialZoomLevel = endIndex;
        } else if (resolutionToFind > _zoomValues.get(0)) {
            // Lower resolution than first resolution in list so just return
            // first zoom level.
            initialZoomLevel = 0;
        } else {
            // Value lies somewhere in our list of resolutions
            for (int count = 0; count < endIndex; ++count) {
                Double higherResolution = _zoomValues.get(count + 1);
                Double lowerResolution = _zoomValues.get(count);

                // Default to assume our value is closer to the higher
                // resolution
                initialZoomLevel = count + 1;
                if (resolutionToFind > higherResolution
                        && resolutionToFind <= lowerResolution) {
                    // We fall between two zoom levels, which value are we
                    // closest
                    // to?
                    double midValue = (higherResolution + lowerResolution) / 2;
                    if (resolutionToFind > midValue) {
                        // Closer to lower value
                        initialZoomLevel = count;
                    }
                    break;
                }
            }
        }
        return initialZoomLevel;
    }

    /*
     * Overridden to allow us to set the size of the navigator control and the
     * mouse over area and position them in the bottom left hand corner of the
     * overlay.
     *
     * (non-Javadoc)
     *
     * @see java.awt.Component#setBounds(int, int, int, int)
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        setupNavigator();
    }

    /*
     * Overridden to allow us to set the size of the navigator control and the
     * mouse over area and position them in the bottom left hand corner of the
     * overlay.
     *
     * (non-Javadoc)
     *
     * @see java.awt.Component#setBounds(java.awt.Rectangle)
     */
    @Override
    public void setBounds(Rectangle r) {
        super.setBounds(r);
        setupNavigator();
    }

    /**
     * Sets the size of the navigator.
     *
     * @param size
     *            the new navigator size. Values less than 1 are ignored.
     */
    public void setNavigatorSize(int size) {
        if (size < 1) {
          return;
        }
        _size = size;
        setupNavigator();
    }

    /**
     * Sets the size of the buttons and the curved part of the navigator based
     * on the size of the navigator control.
     */
    protected void updateSize() {
        // Set button sizes based on default navigator size
        _panShapeSize = _size / 8;

        _zoomSliderLength = (int) (_size * 0.6);
        _zoomSliderWidth = _zoomSliderLength / 3;
    }

    /**
     * Sets the button actions. This uses the map control's pan actions to set
     * the actions performed by the buttons on the navigator.
     *
     */
    private void setButtonActions() {
        JMap map = getMap();
        if (map != null) {
            ActionMap actionMap = map.getActionMap();
            _buttonZoomIn.setAction(actionMap.get(JMap.zoomInAction));
            _buttonZoomIn.setText("+");
            _buttonZoomOut.setAction(actionMap.get(JMap.zoomOutAction));
            _buttonZoomOut.setText("-");
        }
    }

    private void setupZoomSlider() {
        Shape thumbsShape = new Rectangle2D.Double(0, 0, _zoomSliderWidth/2, _zoomSliderWidth/2);

        if (!_sliderCreated) {
            _zoomSlider = new ShapeSlider(
                thumbsShape,
                SwingConstants.VERTICAL,
                0,
                100,
                0);
            _zoomSlider.setOpaque(false);
            _zoomSlider.addChangeListener(new ChangeListener() {
                int prevValue = -1;
                /*
                 * Overriden to zoom the map as the zoom level is
                 * changed.
                 *
                 * (non-Javadoc)
                 *
                 * @see
                 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
                 */
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider) e.getSource();
                    if (source.getValueIsAdjusting()) {
                        // Set a flag to stop the extent changed handler
                        // from trying to set the slider's thumb in
                        // response to our extent change.
                        _settingZoom = true;

                        // Zoom as appropriate
                        int currValue = source.getValue();
                        if (prevValue != currValue) {
                            getMap().zoomToResolution(_zoomValues.get(currValue),
                                getMap().getExtent().getCenter());
                            prevValue = currValue;
                        }

                    // Reset our flag.
                    _settingZoom = false;
                }
            }});
            _zoomSlider.setVisible(true);

            _zoomSliderBackground = new JPanel();
            _zoomSliderBackground.setLayout(new BorderLayout(5, 5));
            _zoomSliderBackground.setVisible(true);

            _buttonZoomIn = new JButton();
            _buttonZoomIn.setBorder(null);
            _buttonZoomIn.setFocusPainted(false);
            _buttonZoomIn.setRolloverEnabled(false);

            _buttonZoomOut = new JButton();
            _buttonZoomOut.setBorder(null);
            _buttonZoomOut.setFocusPainted(false);
            _buttonZoomOut.setRolloverEnabled(false);

            add(_zoomSlider);
            add(_zoomSliderBackground);
            add(_buttonZoomIn);
            add(_buttonZoomOut);
            _sliderCreated = true;
            
            setButtonActions();
        } else {
            _zoomSlider.setShape(thumbsShape);
        }
        _zoomSlider.setSize(_zoomSliderWidth, _zoomSliderLength);
        _zoomSliderBackground.setSize(_zoomSliderWidth, _zoomSliderLength);
        _buttonZoomIn.setSize(_zoomSliderWidth, _zoomSliderWidth);
        _buttonZoomOut.setSize(_zoomSliderWidth, _zoomSliderWidth);
    }

    /**
     * Sets up the buttons used for panning the map. This will create the 
     * navigator buttons when first called or change their shapes on subsequent 
     * calls. This method is called each time the overlay's bounds are changed 
     * and also on each animation timing event. It makes sure that the buttons 
     * are drawn at the correct size to match the current size of the navigator 
     * control.
     */
    private void setupPanButtons() {
        // Triangles for pan buttons
        Ellipse2D.Double triangleBounds =
            new Ellipse2D.Double(0, 0, _panShapeSize, _panShapeSize);
        _panShapeBackground =
            new Ellipse2D.Double(0, 0, _panShapeSize * 2, _panShapeSize * 2);

        double triangleOffsetX = _panShapeBackground.getWidth() / 4;
        double triangleOffsetY = _panShapeBackground.getHeight() / 4;

        AffineTransform moveTriangle =
            AffineTransform.getTranslateInstance(triangleOffsetX, triangleOffsetY);

        // Triangles
        _panWestShape = new Path2D.Double();
        _panWestShape.moveTo(_panShapeSize, 0);
        _panWestShape.lineTo(0, triangleBounds.getCenterY());
        _panWestShape.lineTo(_panShapeSize, _panShapeSize);
        _panWestShape.closePath();
        _panWestShape.transform(moveTriangle);
        _panWestBounds = _panShapeBackground.getBounds2D();

        _panEastShape = new Path2D.Double();
        _panEastShape.moveTo(0, 0);
        _panEastShape.lineTo(_panShapeSize, triangleBounds.getCenterY());
        _panEastShape.lineTo(0, _panShapeSize);
        _panEastShape.closePath();
        _panEastShape.transform(moveTriangle);
        _panEastBounds = _panShapeBackground.getBounds2D();

        Shape n = getShape("N");
        _panNorthShape = new Path2D.Double();
        _panNorthShape.append(n, false);
        _panNorthShape.transform(AffineTransform.getTranslateInstance(
            _panShapeBackground.getCenterX() - n.getBounds().getWidth()/2,
            _panShapeBackground.getCenterY() + n.getBounds().getHeight()/2));
        _panNorthBounds = _panShapeBackground.getBounds2D();

        _panSouthShape = new Path2D.Double();
        _panSouthShape.moveTo(0, 0);
        _panSouthShape.lineTo(triangleBounds.getCenterX(), _panShapeSize);
        _panSouthShape.lineTo(_panShapeSize, 0);
        _panSouthShape.closePath();
        _panSouthShape.transform(moveTriangle);
        _panSouthBounds = _panShapeBackground.getBounds2D();
    }
    
    private void createZoom(Point p) {
      setupZoomSlider();
      _buttonZoomIn.setLocation(p);
      _zoomSliderBackground.setLocation(new Point(
          (int) _buttonZoomIn.getX(), (int) (_buttonZoomIn.getY() + _buttonZoomIn.getHeight())));
      _zoomSlider.setLocation(new Point(
          (int) (_zoomSliderBackground.getX()),
          (int) (_zoomSliderBackground.getY())));
      _buttonZoomOut.setLocation(new Point(
          (int) p.getX(),
          (int) (_zoomSliderBackground.getY() + _zoomSliderBackground.getSize().getHeight())));
    }

    private void createResetNorth() {
        _resetNorthBackground = new RoundRectangle2D.Double(
          (int) (_zoomSlider.getX() + _zoomSliderWidth + _buttonSpacing),
          (int) (_buttonZoomOut.getY()),
          _zoomSliderWidth,
          _zoomSliderWidth,
          _zoomSliderWidth/2,
          _zoomSliderWidth/2);
        _resetNorthNShape = new Path2D.Double();
        _resetNorthNShape.append(getShape("N"), false);
        
        // reset-north 
        int x = (int) (_resetNorthBackground.getX() +
          (_resetNorthBackground.getWidth() - _resetNorthNShape.getBounds().getWidth())/2);
        int y = (int) (_resetNorthBackground.getY() +
          (_resetNorthBackground.getHeight() + _resetNorthNShape.getBounds().getHeight())/2);
        
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        _resetNorthNShape.transform(transform);
    }
    
    private void createPan(Point p) {
      setupPanButtons();
      // pan buttons and its boundary
      int boundXMin = (int) (_resetNorthBackground.getCenterX() + _buttonSpacing);
      int boundXMax = boundXMin + _size;
      int boundYMin = (int) p.getY();
      int boundYMax = (int) (_resetNorthBackground.getY() + _resetNorthBackground.getHeight());
      int height = boundYMax - boundYMin;
      int width = boundXMax - boundXMin;

      _panControlsBoundary = new Rectangle2D.Double(boundXMin, boundYMin, width, height);
      _panButtonsBackground = new Ellipse2D.Double(boundXMin, boundYMin, width, height);

      _panWestBounds.setRect(
          boundXMin,
          (int) (boundYMin + (height / 2) - _panWestBounds.getHeight()/2),
          _panWestBounds.getWidth(),
          _panWestBounds.getHeight());
      _panEastBounds.setRect(
          boundXMax - _panWestBounds.getWidth(),
          (int) _panWestBounds.getY(),
          _panEastBounds.getWidth(),
          _panEastBounds.getHeight());
      _panNorthBounds.setRect(
          boundXMin + (width / 2) - _panNorthBounds.getWidth()/2,
          boundYMin,
          _panNorthBounds.getWidth(),
          _panNorthBounds.getHeight());
      _panSouthBounds.setRect(
          _panNorthBounds.getX(),
          (int) (boundYMax - _panSouthBounds.getHeight()),
          _panSouthBounds.getWidth(),
          _panSouthBounds.getHeight());
    }
    
    private void createFullExtent() {
      _fullExtentBackground = new RoundRectangle2D.Double(
          (int) (_panControlsBoundary.getMaxX() - _zoomSliderWidth/2 + _buttonSpacing),
          (int) (_panControlsBoundary.getY() + _panControlsBoundary.getHeight() - _zoomSliderWidth),
          _zoomSliderWidth,
          _zoomSliderWidth,
          _zoomSliderWidth/2,
          _zoomSliderWidth/2);
      
      _fullExtentShape = new Path2D.Double();
      int diameter = (int) (_zoomSliderWidth * 0.8);
      Ellipse2D.Double ellipse1, ellipse2, ellipse3;
      ellipse1 = new Ellipse2D.Double(0, 0, diameter, diameter);
      ellipse2 = new Ellipse2D.Double(diameter/4, 0, diameter/2, diameter);
      ellipse3 = new Ellipse2D.Double(0, diameter/4, diameter, diameter/2);
      _fullExtentShape.append(ellipse1, false);
      _fullExtentShape.append(ellipse2, false);
      _fullExtentShape.append(ellipse3, false);
      AffineTransform transform = new AffineTransform();
      int shift = (int) (_zoomSliderWidth - diameter)/2;
      transform.translate(shift + _fullExtentBackground.getX(), shift + _fullExtentBackground.getY());
      _fullExtentShape.transform(transform);
    }

    /**
     * Sets up the navigator control. This method is used to calculate the positions of the
     * navigator, its buttons, and the zoom slider.
     *
     */
    private void setupNavigator() {
        if (getMap() == null) {
          return;
        }
        
        updateSize();
        createShapes();
        updateColors();
    }

    private void updateColors() {
        if (!_sliderCreated) {
          return;
        }
        // when same color is painted on a shape and a component, the component appears darker.
        // workaround: the background color on slider and button components is made lighter here
        // so that their color is same as the rotation control shape.
        Color lighterBackground = new Color(
            _background.getRed(),
            _background.getGreen(),
            _background.getBlue(),
            (int) (_background.getAlpha() * 0.70f));
        _zoomSliderBackground.setBackground(lighterBackground);
        _zoomSliderBackground.setForeground(_foreground);

        _zoomSlider.setForeground(_foreground);

        _buttonZoomIn.setBackground(lighterBackground);
        _buttonZoomIn.setForeground(_foreground);

        _buttonZoomOut.setBackground(lighterBackground);
        _buttonZoomOut.setForeground(_foreground);
        
        repaint();
    }
    
    private void createShapes() {
      // calculate at (0,0) first to get component dimensions
      createShapes(new Point(0, 0));

      // now update shapes based on component dimensions
      int componentWidth = (int) (_fullExtentBackground.getMaxX() - _buttonZoomOut.getX());
      int componentHeight = (int) (_buttonZoomOut.getY() + _buttonZoomOut.getHeight());
      topLeftAbsolute = LocationOnMap.getAbsoluteLocation(getMap().getWidth(),
        getMap().getHeight(), componentWidth, componentHeight, locationOnMap);
      createShapes(topLeftAbsolute);
      
      updateShapeTransformation();
  }
    
    private void createShapes(Point p) {
      createZoom(p);
      createResetNorth();
      createPan(p);
      createFullExtent();
    }

    private Shape getShape(String str) {
        Shape shape;
        Font font = getFont();
        if (font == null) {
            font = new Font(Font.SANS_SERIF, Font.PLAIN, _size/10);
        }
        GlyphVector n = font.createGlyphVector(getFontMetrics(font).getFontRenderContext(), str);
        shape = n.getOutline();
        return shape;
    }
}
