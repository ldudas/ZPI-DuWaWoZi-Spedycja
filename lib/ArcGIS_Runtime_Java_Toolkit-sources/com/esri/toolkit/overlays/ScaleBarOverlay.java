/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.UIManager;

import com.esri.core.geometry.AngularUnit;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.geometry.Unit.UnitType;
import com.esri.map.JMap;
import com.esri.map.LocationOnMap;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListenerAdapter;
import com.esri.map.MapOverlay;

/**
 * This class demonstrates a non-interactive map overlay. It overrides
 * the <code>onPaint</code> method to draw a scale bar on top of the map. 
 * The class listens for extent changed events from the map and updates 
 * the scale bar to match the current extent.
 * 
 * @usage
 * <code>
 * ScaleBarOverlay scaleBar = new ScaleBarOverlay();<br>
 * // optionally, set the location of the scale bar on the map<br>
 * scaleBar.setLocation(LocationOnMap.TOP_RIGHT);<br>
 * jMap.addMapOverlay(scaleBar);<br>
 * </code>
 * @since 10.2.3
 */
public class ScaleBarOverlay extends MapOverlay {
    /**
     * Handler for the extent changed event. Note that this handler extends
     * {@link com.esri.map.MapEventListenerAdapter MapEventListenerAdapter}
     * rather than implementing {@link com.esri.map.MapEventListener
     * MapEventListener} as we are only interested in one map event.
     *
     */
    public class MapEventHandler extends MapEventListenerAdapter {

        /*
         * Handle the map extent changed event by creating a new scalebar
         * to match the new map extent.
         *
         * (non-Javadoc)
         * @see com.esri.map.MapEventListenerAdapter#mapExtentChanged(com.esri.map.MapEvent)
         */
        @Override
        public void mapExtentChanged(MapEvent event) {
            if (getMap().isReady()) {
                setupScaleBar();
            }
        }

        @Override
        public void mapReady(MapEvent event) {
            mapUnits = ((JMap) event.getSource()).getSpatialReference().getUnit();
            setupScaleBar();
        }

    }

    private static final long serialVersionUID = 1L;
    private static Font font;
    static {
        font = UIManager.getDefaults().getFont("TextField.font");
        if (font == null) {
            String fontName = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getAvailableFontFamilyNames()[0];
            font = new Font(fontName, Font.PLAIN, 12);
        }
    }

    private Unit mapUnits = null;
    private MapEventHandler mapEventListener = new MapEventHandler();
    private double resolutionMeters;
    private com.esri.core.geometry.Point extentCenter;
    private double scaleBarGroundLength;
    private double scaleBarScreenLength;
    private ArrayList<Shape> scaleBarShapes = new ArrayList<Shape>();
    // 1st label is for 0, 2nd is for metric, 3rd is for imperial.
    private ArrayList<TickLabel> scaleBarLabels = new ArrayList<TickLabel>();
    private LocationOnMap locationOnMap = LocationOnMap.BOTTOM_LEFT;
    
    // Get conversion from metre to yard
    private static final double METER_TO_FEET = 3.2808;
    private static final SpatialReference SR_WGS84 = 
      SpatialReference.create(SpatialReference.WKID_WGS84);
    private static final double LENGTH_FACTOR = 0.2; // 20% of application width
    // a part is one division (black/white) of scale bar.
    private int numDivisions = 8;
    private static final int DIVISION_HEIGHT = 5; // in pixels
    private ArrayList<Shape> tempShapes = new ArrayList<Shape>();
    private int totalWidth;
    private int totalHeight;
    
    /**
     * Used to define and draw the labels on the scalebar.
     *
     */
    class TickLabel {
        private Point2D location;
        private String text;
        private Font font;
        private boolean offsetLabel;
        private TextLayout txtLayout;
        private Shape outline;

        /**
         * Construct a new TickLabel
         *
         * @param location
         *            Location to draw label at. This will be where the text
         *            origin is placed.
         * @param text
         *            Text to set on the label.
         * @param font
         *            Font to draw the text with.
         * @param offsetLabel
         *            True to offset the label to set location in the middle of
         *            the text, false otherwise.
         */
        public TickLabel(Point2D location, String text, Font font, boolean offsetLabel) {
            this.location = location;
            this.text = text;
            this.font = font;
            this.offsetLabel = offsetLabel;
            txtLayout = new TextLayout(this.text, this.font, ((Graphics2D) getGraphics()).getFontRenderContext());
            outline = txtLayout.getLogicalHighlightShape(0, this.text.length());
        }
        
        public TickLabel(Point2D location, int n, Font font, boolean offsetLabel) {
          this.location = location;
          this.text = NumberFormat.getNumberInstance().format(n);
          this.font = font;
          this.offsetLabel = offsetLabel;
          txtLayout = new TextLayout(this.text, this.font, ((Graphics2D) getGraphics()).getFontRenderContext());
          outline = txtLayout.getLogicalHighlightShape(0, this.text.length());
        }

        /**
         * Construct a new TickLabel with the text offset so that location is in the middle
         * of the text.
         *
         * @param location
         *            Location to draw label at. This will be where the text
         *            origin is placed.
         * @param text
         *            Text to set on the label.
         * @param font
         *            Font to draw the text with.
         */
        public TickLabel(Point2D location, String text, Font font) {
            this(location, text, font, true);
        }

        /**
         * Transform the current location of the label. Initially the scalebar is
         * drawn at the origin of the overlay. This method is then used to move
         * the label to the scalebar's final position.
         *
         * @param transform Transform used to move the label.
         */
        public void transformLocation(AffineTransform transform) {
            transform.transform(this.location, this.location);
        }

        /**
         * Draw the label with the given graphics object. This method draws the
         * text on the given graphics object with the configured font. The text
         * will be drawn in black on a white background.
         *
         * @param graphics Graphics to draw text on.
         */
        public void drawLabel(Graphics2D graphics) {
            outline = txtLayout.getLogicalHighlightShape(0, text.length());
            float labelOffsetX = this.offsetLabel ? txtLayout.getAdvance() / 2 : 0;
            outline = AffineTransform.getTranslateInstance(
               this.location.getX() - labelOffsetX, this.location.getY()).createTransformedShape(outline);
            graphics.setColor(Color.WHITE);
            graphics.fill(outline);
            graphics.draw(outline);
            graphics.setColor(Color.BLACK);
            txtLayout.draw(graphics, 
              (float) this.location.getX() - labelOffsetX, (float) this.location.getY());
        }
        
        public Rectangle2D getBounds() {
          return outline == null ? null : outline.getBounds();
        }
    }

    /*
     * Using this override to add a listener for extent changed events as we
     * need to recreate the scalebar each time the extent is changed.
     *
     * (non-Javadoc)
     *
     * @see com.esri.map.MapInteractionOverlay#setMap(com.esri.map.JMap)
     */
    @Override
    protected void setMap(JMap map) {
        super.setMap(map);
        map.addMapEventListener(mapEventListener);
    }
    
    @Override
    public void setLocation(int x, int y) {
      setLocation(new LocationOnMap(x, y));
    }
    
    @Override
    public void setLocation(Point p) {
      if (p == null) {
        return;
      }
      setLocation(new LocationOnMap(p));
    }
    
    /**
     * Sets the location of the scale bar relative to the map.
     * @param p location on map, null is ignored.
     * @since 10.2.3
     */
    public void setLocation(LocationOnMap p) {
      if (p == null) {
        return;
      }
      locationOnMap = p; 
      setupScaleBar();
    }
    
    /**
     * Sets the number of divisions per line of the scale bar.
     * @param numDivisions number of divisions per line. It can range from 1 to 10, both inclusive.
     * Default is 8.
     * @throws IllegalArgumentException if the input number of divisions is out of valid range.
     * @since 10.2.3
     * @see #getNumberOfDivisions()
     */
    public void setNumberOfDivisions(int numDivisions) {
      if (numDivisions < 1 || numDivisions > 10) {
        throw new IllegalArgumentException("Number of divisions should be between 1 and 10.");
      }
      this.numDivisions = numDivisions;
      setupScaleBar();
    }
    
    /**
     * Gets the number of divisions per line of the scale bar. Default is 8.
     * @return the number of divisions per line of the scale bar.
     * @since 10.2.3
     * @see #setNumberOfDivisions(int)
     */
    public int getNumberOfDivisions() {
      return numDivisions;
    }

    /**
     * Used to recreate the scalebar each time the map extent is changed.
     */
    private void setupScaleBar() {
        JMap map = getMap();
        if (map == null || !map.isReady()) {
          return;
        }
        calcScalebarLength(map);
        createScaleBar();
    }

    /**
     * Get the centre of the map extent. This is used by
     * {@link #getExtentCentreLatitude(SpatialReference)
     * getExtentCentreLatitude}. The latitude at this point is used in the
     * conversion from degrees or radians to metres if required when converting
     * the map resolution to metres.
     *
     * @param map
     *            Get the centre point of this map's extent.
     */
    private void initExtentCentre(JMap map) {
        Envelope extent = map.getExtent();
        extentCenter = new com.esri.core.geometry.Point(extent.getCenterX(), extent.getCenterY());
    }

    /**
     * Convert the current map resolution to a resolution in metres per pixel.
     * This is used when calculating the scalebar sizes.
     *
     * @param map
     *            Convert the current resolution of this map.
     */
    private void calcMapResolutionInMetres(JMap map) {
        initExtentCentre(map);
        if (mapUnits.getUnitType() != UnitType.LINEAR) {
            // We have angular units and want to convert to metres. We do
            // this by taking our resolution per pixel in degrees and
            // multiplying it by the number of metres in a degree at the
            // equator (111200). We can then use this number to convert our degrees
            // per pixel to a metres per pixel value.
            resolutionMeters = Unit.convertUnits(
                map.getResolution(), mapUnits, Unit.create(AngularUnit.Code.DEGREE));
            resolutionMeters = resolutionMeters * 111200;
        } else {
           resolutionMeters = map.getResolution(); 
        }
        
        // This is then multiplied by the cosine of latitude to get a more accurate number 
        // of metres per degree. 
        resolutionMeters *= Math.cos(getExtentCentreLatitude(map.getSpatialReference()));
    }
        

    /**
     * Get the latitude of the centre of our current extent. This is used in the
     * conversion from angular units to linear units when we are calculating our
     * map resolution in metres per pixel.
     *
     * @param spatialRef
     *            The map's spatial reference
     * @return The latitude of the centre of our extent in radians.
     */
    private double getExtentCentreLatitude(SpatialReference spatialRef) {
        double retVal = 0;

        // Project our point to WGS84 to get it in angular coordinates.
        com.esri.core.geometry.Point projectedPoint = 
          (com.esri.core.geometry.Point) GeometryEngine.project(extentCenter, spatialRef, SR_WGS84);

        // If we managed to project the point, use its latitude. If we get
        // null here, latitude will default to zero and our calculation will
        // be based on the number of meters per degree at the equator.
        if (projectedPoint != null) {
            retVal = projectedPoint.getY();
        }
        return Math.toRadians(retVal);
    }

    /**
     * @param map
     */
    private void calcScalebarLength(JMap map) {
        calcMapResolutionInMetres(map);
      
        scaleBarScreenLength = getWidth() * LENGTH_FACTOR;
        scaleBarGroundLength = scaleBarScreenLength * resolutionMeters;
        
        // Round to nearest power of ten
        double newScaleBarGroundLength = Math.pow(10,
          Math.floor(Math.log10(scaleBarGroundLength) - Math.log10(0.5)));

        double ratio = newScaleBarGroundLength / scaleBarGroundLength;

        if (ratio <= 0.3) {
            scaleBarGroundLength = newScaleBarGroundLength * 6;
        } else if (ratio <= 0.5) {
            scaleBarGroundLength = newScaleBarGroundLength * 4;
        } else {
            scaleBarGroundLength = newScaleBarGroundLength;
        }

        // Adjust the scalebar length to match the new ground length
        scaleBarScreenLength = scaleBarGroundLength / resolutionMeters;
    }

    /**
     * Creates pieces of scale bar UI, relative to location (0, 0).
     * The location (0,0) corresponds to the top-left part of the UI.
     * @param scaleBarScreenLength
     * @param scaleBarGroundLength
     * @param scaleBarParts
     * @param labels
     * @param xScalebar
     * @param yScaleBar
     * @param yLabel
     * @param isMetric
     */
    private void createScaleBarParts(
        double scaleBarScreenLength,
        double scaleBarGroundLength, 
        ArrayList<Shape> scaleBarParts,
        ArrayList<TickLabel> labels, 
        int xScalebar,
        int yScaleBar,
        int yLabel,
        boolean isMetric) {
        
        // Create subdivision rectangles
        double partLength = scaleBarScreenLength / numDivisions;
        for (int part = 0; part < numDivisions; part++) {
            double partXOffset = part * partLength;
            scaleBarParts.add(
              new Rectangle2D.Double(xScalebar + partXOffset, yScaleBar, partLength, DIVISION_HEIGHT));
        }
        
        // create outline
        scaleBarParts.add(new Rectangle2D.Double(xScalebar, yScaleBar, scaleBarScreenLength, DIVISION_HEIGHT));

        // Create label - centered at the end of the scale bar
        double xLabel = scaleBarScreenLength;
        String formattedValue = isMetric ? 
          formatMetric(scaleBarGroundLength) : formatImperial(scaleBarGroundLength * METER_TO_FEET);
        labels.add(new TickLabel(new Point2D.Double(xLabel, yLabel), formattedValue, font, true));
    }
    
    private void createScaleBar() {
        tempShapes.clear();
        scaleBarLabels.clear();
        totalHeight = 0;
        
        // create label for 0 
        TickLabel labelZero = new TickLabel(new Point2D.Double(0, 0), 0, font, true);
        // offset position so that top-left of the label is at (0,0)
        int textHeight = (int) labelZero.getBounds().getHeight();
        int textHeightHalf = textHeight/2; 
        labelZero.location.setLocation(
            labelZero.getBounds().getWidth()/2, 
            textHeightHalf);
        scaleBarLabels.add(labelZero);
        
        // Create scalebars right below label for 0
        totalHeight += textHeight;
        createScaleBarParts(
            scaleBarScreenLength, scaleBarGroundLength, tempShapes, scaleBarLabels, 
            (int) (labelZero.getBounds().getWidth()/2), totalHeight, 
            textHeightHalf, true);
        
        totalHeight += DIVISION_HEIGHT;
        createScaleBarParts(
            scaleBarScreenLength, scaleBarGroundLength, tempShapes, scaleBarLabels, 
            (int) (labelZero.getBounds().getWidth()/2), totalHeight, 
            totalHeight + DIVISION_HEIGHT + textHeight, false);
        
        // translate positions
        totalHeight += DIVISION_HEIGHT + textHeight;
        totalWidth =  
            (int) (scaleBarScreenLength +
            labelZero.getBounds().getWidth()/2 +
            scaleBarLabels.get(2).getBounds().getWidth()/2);
        updateLocation();
        
        repaint();
    }
    
    private void updateLocation() {
      double dx = getX(); 
      double dy = getY();
      if (getMap() != null && locationOnMap != null) {
        Point p = LocationOnMap.getAbsoluteLocation(
          getMap().getWidth(), getMap().getHeight(), 
          totalWidth, totalHeight, locationOnMap);
        dx = p.getX();
        dy = p.getY();
      }
      
      // Get transform to translate scale bar parts
      AffineTransform translate = AffineTransform.getTranslateInstance(dx, dy);

      // Transform scalebar shapes
      scaleBarShapes.clear();
      for (Shape curShape : tempShapes) {
         scaleBarShapes.add(translate.createTransformedShape(curShape));
      }

      for (TickLabel curLabel : scaleBarLabels) {
          curLabel.transformLocation(translate);
      }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.esri.map.MapInteractionOverlay#onPaint(java.awt.Graphics)
     */
    @Override
    public void onPaint(Graphics graphics) {
        if (scaleBarShapes.isEmpty()) {
          return;
        }
        
        Graphics2D g = (Graphics2D) graphics;
       
        // Draw scalebar shapes - parts and outlines
        // start 1st line with black, 2nd line with white.
        // alternate between black and white in a single line.
        Color c = Color.BLACK;
        for (int i = 0; i < numDivisions; i++) {
            g.setPaint(c);
            g.fill(scaleBarShapes.get(i));
            c = c == Color.WHITE ? Color.BLACK : Color.WHITE;
        }
        g.setPaint(Color.BLACK);
        g.draw(scaleBarShapes.get(numDivisions));
        
        c = Color.WHITE;
        for (int i = numDivisions + 1; i <= 2 * numDivisions; i++) {
          g.setPaint(c);
          g.fill(scaleBarShapes.get(i));
          c = c == Color.WHITE ? Color.BLACK : Color.WHITE;
        }
        g.setPaint(Color.BLACK);
        g.draw(scaleBarShapes.get(2 * numDivisions + 1));
        
        // Draw labels
        for (TickLabel curLabel : scaleBarLabels) {
          curLabel.drawLabel(g);
        }
    }
    
    private static String formatMetric(double value) {
      int unitChangeLimit = 1000;
      String formattedValue = null;
      double displayValue = 0;
      String displayUnits = null;
      if (value < unitChangeLimit) {
          displayUnits = "m";
          displayValue = value;
      } else {
          displayUnits = "km";
          displayValue = value / unitChangeLimit;
      }
      NumberFormat formatter = NumberFormat.getNumberInstance();
      formatter.setMinimumFractionDigits(0);
      formatter.setMaximumFractionDigits(0);
      formattedValue = formatter.format(displayValue) + " " + displayUnits;
      return formattedValue;
    }
    
    private static String formatImperial(double value) {
      int unitChangeLimit = 5280;
      String formattedValue = null;
      double displayValue = 0;
      String displayUnits = null;
      if (value < unitChangeLimit) {
          displayUnits = "feet";
          displayValue = value;
      } else {
          displayUnits = "miles";
          displayValue = value / unitChangeLimit;
      }
      NumberFormat formatter = NumberFormat.getNumberInstance();
      formatter.setMinimumFractionDigits(2);
      formatter.setMaximumFractionDigits(2);
      formattedValue = formatter.format(displayValue) + " " + displayUnits;
      return formattedValue;
    }
}
