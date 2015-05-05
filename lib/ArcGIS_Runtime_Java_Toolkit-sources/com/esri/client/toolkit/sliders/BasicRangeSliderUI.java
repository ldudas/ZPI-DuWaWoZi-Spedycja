/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.sliders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSliderUI;

import com.esri.client.toolkit.sliders.RangeSlider.RangeMode;

/**
 * This class provides a basic UI for a {@link JSlider} that has two thumbs on
 * it. This class takes advantage of the fact that the default model for the
 * <code>JSlider</code> has a value and an extent property. The first thumb will
 * be drawn at the value location and the second will be offset from the first
 * by the extent value.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.sliders.BasicRangeSliderUI} instead.
 */
@Deprecated
public class BasicRangeSliderUI extends BasicSliderUI {

  /**
   * The listener interface for receiving rangeTrack events. The class that is
   * interested in processing a rangeTrack event implements this interface,
   * and the object created with that class is registered with a component
   * using the component's <code>addRangeTrackListener<code> method. When
   * the rangeTrack event occurs, that object's appropriate
   * method is invoked.
   * 
   * @see RangeTrackEvent
   */
  public class RangeTrackListener extends TrackListener {

    private boolean _isDragging;
    private boolean _isDraggingThumb;
    private boolean _isDraggingRange;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicSliderUI.TrackListener#mousePressed(java
     * .awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
      if (!slider.isEnabled()) {
        return;
      }

      currentMouseX = e.getX();
      currentMouseY = e.getY();

      if (slider.isRequestFocusEnabled()) {
        slider.requestFocus();
      }

      // Clicked in the Thumb area?
      if (_rangeMode != RangeMode.CumulativeFromStart
          && thumbRect.contains(currentMouseX, currentMouseY)) {
        switch (slider.getOrientation()) {
        case JSlider.VERTICAL:
          offset = currentMouseY - thumbRect.y;
          break;
        case JSlider.HORIZONTAL:
          offset = currentMouseX - thumbRect.x;
          break;
        }
        _isDragging = true;
        _isDraggingThumb = true;
        _isDraggingRange = false;
        return;
      } else if (_rangeMode != RangeMode.SingleValue
          && _upperThumbRect.contains(currentMouseX, currentMouseY)) {
        switch (slider.getOrientation()) {
        case JSlider.VERTICAL:
          offset = currentMouseY - _upperThumbRect.y;
          break;
        case JSlider.HORIZONTAL:
          offset = currentMouseX - _upperThumbRect.x;
          break;
        }
        _isDragging = true;
        _isDraggingThumb = false;
        _isDraggingRange = false;
        return;
      } else if (_rangeMode != RangeMode.CumulativeFromStart
          && _rangeDragRect.contains(currentMouseX, currentMouseY)) {
        switch (slider.getOrientation()) {
        case JSlider.VERTICAL:
          offset = currentMouseY - thumbRect.y;
          break;
        case JSlider.HORIZONTAL:
          offset = currentMouseX - thumbRect.x;
          break;
        }
        _isDragging = true;
        _isDraggingRange = true;
        return;
      }
      _isDragging = false;
      slider.setValueIsAdjusting(true);

      Dimension sbSize = slider.getSize();
      int direction = POSITIVE_SCROLL;

      switch (slider.getOrientation()) {
      case JSlider.VERTICAL:
        if (thumbRect.isEmpty()) {
          int scrollbarCenter = sbSize.height / 2;
          if (!drawInverted()) {
            direction = (currentMouseY < scrollbarCenter) ? POSITIVE_SCROLL
                : NEGATIVE_SCROLL;
          } else {
            direction = (currentMouseY < scrollbarCenter) ? NEGATIVE_SCROLL
                : POSITIVE_SCROLL;
          }
        } else {
          int thumbY = thumbRect.y;
          if (!drawInverted()) {
            direction = (currentMouseY < thumbY) ? POSITIVE_SCROLL
                : NEGATIVE_SCROLL;
          } else {
            direction = (currentMouseY < thumbY) ? NEGATIVE_SCROLL
                : POSITIVE_SCROLL;
          }
        }
        break;
      case JSlider.HORIZONTAL:
        if (thumbRect.isEmpty()) {
          int scrollbarCenter = sbSize.width / 2;
          if (!drawInverted()) {
            direction = (currentMouseX < scrollbarCenter) ? NEGATIVE_SCROLL
                : POSITIVE_SCROLL;
          } else {
            direction = (currentMouseX < scrollbarCenter) ? POSITIVE_SCROLL
                : NEGATIVE_SCROLL;
          }
        } else {
          int thumbX = thumbRect.x;
          if (!drawInverted()) {
            direction = (currentMouseX < thumbX) ? NEGATIVE_SCROLL
                : POSITIVE_SCROLL;
          } else {
            direction = (currentMouseX < thumbX) ? POSITIVE_SCROLL
                : NEGATIVE_SCROLL;
          }
        }
        break;
      }

      if (shouldScroll(direction)) {
        scrollDueToClickInTrack(direction);
      }
      if (shouldScroll(direction)) {
        scrollTimer.stop();
        scrollListener.setDirection(direction);
        scrollTimer.start();
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.plaf.basic.BasicSliderUI.TrackListener#mouseDragged(java
     * .awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(MouseEvent e) {
      int thumbMiddle = 0;

      if (!slider.isEnabled()) {
        return;
      }

      currentMouseX = e.getX();
      currentMouseY = e.getY();

      if (!_isDragging) {
        return;
      }

      slider.setValueIsAdjusting(true);

      switch (slider.getOrientation()) {
      case JSlider.VERTICAL:
        int halfThumbHeight = thumbRect.height / 2;
        int thumbTop = e.getY() - offset;
        int trackTop = trackRect.y;
        int trackBottom = trackRect.y + (trackRect.height - 1);
        int vMax = yPositionForValue(slider.getMaximum());

        if (drawInverted()) {
          trackBottom = vMax;
        } else {
          trackTop = vMax;
        }
        thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight);
        thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight);

        if (_isDraggingRange) {
          setThumbLocation(thumbRect.x, thumbTop);
          setUpperThumbLocation(_upperThumbRect.x, thumbTop
              - _upperThumbRect.height - _rangeDragRect.height);
        } else if (_isDraggingThumb) {
          setThumbLocation(thumbRect.x, thumbTop);
        } else {
          setUpperThumbLocation(_upperThumbRect.x, thumbTop);
        }

        thumbMiddle = thumbTop + halfThumbHeight;

        if (_isDraggingRange) {
          slider.setValue(valueForYPosition(thumbMiddle
              + _rangeDragRect.height));
        } else if (_isDraggingThumb) {
          int upperThumbValue = valueForYPosition((int) _upperThumbRect
              .getCenterY());
          slider.setValue(valueForYPosition(thumbMiddle));
          slider.setExtent(upperThumbValue - slider.getValue());
        } else {
          slider.setExtent(valueForYPosition(thumbMiddle)
              - valueForYPosition((int) thumbRect.getCenterY()));
        }
        break;
      case JSlider.HORIZONTAL:
        int halfThumbWidth = thumbRect.width / 2;
        int thumbLeft = e.getX() - offset;
        int trackLeft = trackRect.x;
        int trackRight = trackRect.x + (trackRect.width - 1);
        int hMax = xPositionForValue(slider.getMaximum());

        if (drawInverted()) {
          trackLeft = hMax;
        } else {
          trackRight = hMax;
        }

        thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth);
        thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth);

        if (_isDraggingRange) {
          setThumbLocation(thumbLeft, thumbRect.y);
          setUpperThumbLocation(thumbLeft + _rangeDragRect.width
              + thumbRect.width, _upperThumbRect.y);
        } else if (_isDraggingThumb) {
          setThumbLocation(thumbLeft, thumbRect.y);
        } else {
          setUpperThumbLocation(thumbLeft, _upperThumbRect.y);
        }

        thumbMiddle = thumbLeft + halfThumbWidth;

        if (_isDraggingRange) {
          slider.setValue(valueForXPosition(thumbMiddle));
        } else if (_isDraggingThumb) {
          int upperThumbValue = valueForXPosition((int) _upperThumbRect
              .getCenterX());
          slider.setValue(valueForXPosition(thumbMiddle));
          slider.setExtent(upperThumbValue
              - valueForXPosition(thumbMiddle));
        } else {
          slider.setExtent(valueForXPosition(thumbMiddle)
              - valueForXPosition((int) thumbRect.getCenterX()));
        }
        break;
      default:
        return;
      }
    }

  }

  private Rectangle _upperThumbRect;
  private Rectangle _rangeDragRect;
  private RangeMode _rangeMode = RangeMode.SingleValue;

  /**
   * Instantiates a new basic range slider UI.
   * 
   * @param b
   *            the control this UI will be applied to
   */
  public BasicRangeSliderUI(JSlider b) {
    super(b);
    if (b instanceof RangeSlider) {
      _rangeMode = ((RangeSlider) b).getRangeMode();
    }
  }

  public RangeMode getRangeMode() {
    return _rangeMode;
  }

  public void setRangeMode(RangeMode rangeMode) {
    _rangeMode = rangeMode;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.plaf.basic.BasicSliderUI#installUI(javax.swing.JComponent)
   */
  @Override
  public void installUI(JComponent c) {
    _upperThumbRect = new Rectangle();
    _rangeDragRect = new Rectangle();
    super.installUI(c);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.plaf.basic.BasicSliderUI#uninstallUI(javax.swing.JComponent)
   */
  @Override
  public void uninstallUI(JComponent c) {
    super.uninstallUI(c);
    _upperThumbRect = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.plaf.basic.BasicSliderUI#paint(java.awt.Graphics,
   * javax.swing.JComponent)
   */
  @Override
  public void paint(Graphics g, JComponent c) {
    if (_rangeMode != RangeMode.SingleValue) {
      paintRangeDrag(g);
    }
    super.paint(g, c);

    if (_rangeMode != RangeMode.SingleValue
        && g.getClipBounds().intersects(_upperThumbRect)) {
      paintUpperThumb(g);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.plaf.basic.BasicSliderUI#paintThumb(java.awt.Graphics)
   */
  @Override
  public void paintThumb(Graphics g) {
    if (_rangeMode != RangeMode.CumulativeFromStart) {
      super.paintThumb(g);
    }
  }

  /**
   * Paint upper thumb.
   * 
   * @param g
   *            the graphics object used to draw the thumb on the control
   */
  public void paintUpperThumb(Graphics g) {
    Rectangle knobBounds = _upperThumbRect;
    int w = knobBounds.width;
    int h = knobBounds.height;

    g.translate(knobBounds.x, knobBounds.y);

    if (slider.isEnabled()) {
      g.setColor(slider.getBackground());
    } else {
      g.setColor(slider.getBackground().darker());
    }

    Boolean paintThumbArrowShape = (Boolean) slider
        .getClientProperty("Slider.paintThumbArrowShape");

    if ((!slider.getPaintTicks() && paintThumbArrowShape == null)
        || paintThumbArrowShape == Boolean.FALSE) {

      // "plain" version
      g.fillRect(0, 0, w, h);

      g.setColor(Color.black);
      g.drawLine(0, h - 1, w - 1, h - 1);
      g.drawLine(w - 1, 0, w - 1, h - 1);

      g.setColor(getHighlightColor());
      g.drawLine(0, 0, 0, h - 2);
      g.drawLine(1, 0, w - 2, 0);

      g.setColor(getShadowColor());
      g.drawLine(1, h - 2, w - 2, h - 2);
      g.drawLine(w - 2, 1, w - 2, h - 3);
    } else if (slider.getOrientation() == JSlider.HORIZONTAL) {
      int cw = w / 2;
      g.fillRect(1, 1, w - 3, h - 1 - cw);
      Polygon p = new Polygon();
      p.addPoint(1, h - cw);
      p.addPoint(cw - 1, h - 1);
      p.addPoint(w - 2, h - 1 - cw);
      g.fillPolygon(p);

      g.setColor(getHighlightColor());
      g.drawLine(0, 0, w - 2, 0);
      g.drawLine(0, 1, 0, h - 1 - cw);
      g.drawLine(0, h - cw, cw - 1, h - 1);

      g.setColor(Color.black);
      g.drawLine(w - 1, 0, w - 1, h - 2 - cw);
      g.drawLine(w - 1, h - 1 - cw, w - 1 - cw, h - 1);

      g.setColor(getShadowColor());
      g.drawLine(w - 2, 1, w - 2, h - 2 - cw);
      g.drawLine(w - 2, h - 1 - cw, w - 1 - cw, h - 2);
    } else { // vertical
      int cw = h / 2;
      if (slider.getComponentOrientation().isLeftToRight()) {
        g.fillRect(1, 1, w - 1 - cw, h - 3);
        Polygon p = new Polygon();
        p.addPoint(w - cw - 1, 0);
        p.addPoint(w - 1, cw);
        p.addPoint(w - 1 - cw, h - 2);
        g.fillPolygon(p);

        g.setColor(getHighlightColor());
        g.drawLine(0, 0, 0, h - 2); // left
        g.drawLine(1, 0, w - 1 - cw, 0); // top
        g.drawLine(w - cw - 1, 0, w - 1, cw); // top slant

        g.setColor(Color.black);
        g.drawLine(0, h - 1, w - 2 - cw, h - 1); // bottom
        g.drawLine(w - 1 - cw, h - 1, w - 1, h - 1 - cw); // bottom
                                  // slant

        g.setColor(getShadowColor());
        g.drawLine(1, h - 2, w - 2 - cw, h - 2); // bottom
        g.drawLine(w - 1 - cw, h - 2, w - 2, h - cw - 1); // bottom
                                  // slant
      } else {
        g.fillRect(5, 1, w - 1 - cw, h - 3);
        Polygon p = new Polygon();
        p.addPoint(cw, 0);
        p.addPoint(0, cw);
        p.addPoint(cw, h - 2);
        g.fillPolygon(p);

        g.setColor(getHighlightColor());
        g.drawLine(cw - 1, 0, w - 2, 0); // top
        g.drawLine(0, cw, cw, 0); // top slant

        g.setColor(Color.black);
        g.drawLine(0, h - 1 - cw, cw, h - 1); // bottom slant
        g.drawLine(cw, h - 1, w - 1, h - 1); // bottom

        g.setColor(getShadowColor());
        g.drawLine(cw, h - 2, w - 2, h - 2); // bottom
        g.drawLine(w - 1, 1, w - 1, h - 2); // right
      }
    }

    g.translate(-knobBounds.x, -knobBounds.y);
  }

  /**
   * @param g
   */
  public void paintRangeDrag(Graphics g) {
    Color highlightColor = getShadowColor();
    Color rangeDragColor = new Color(highlightColor.getRed(),
        highlightColor.getGreen(), highlightColor.getBlue(), 125);
    g.setColor(rangeDragColor);
    g.fillRect(_rangeDragRect.x, _rangeDragRect.y, _rangeDragRect.width,
        _rangeDragRect.height);
  }

  // Used exclusively by setThumbLocation()
  private static Rectangle unionRect = new Rectangle();

  /**
   * Sets the upper thumb location.
   * 
   * @param x
   *            the x location
   * @param y
   *            the y location
   */
  public void setUpperThumbLocation(int x, int y) {
    unionRect.setBounds(_upperThumbRect);

    _upperThumbRect.setLocation(x, y);

    SwingUtilities.computeUnion(_upperThumbRect.x, _upperThumbRect.y,
        _upperThumbRect.width, _upperThumbRect.height, unionRect);
    slider.repaint(unionRect.x, unionRect.y, unionRect.width,
        unionRect.height);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.plaf.basic.BasicSliderUI#createTrackListener(javax.swing.
   * JSlider)
   */
  @Override
  protected TrackListener createTrackListener(JSlider slider) {
    return new RangeTrackListener();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.plaf.basic.BasicSliderUI#calculateThumbLocation()
   */
  @Override
  protected void calculateThumbLocation() {
    super.calculateThumbLocation();
    calculateUpperThumbLocation();
    calculateRangeDragLocation();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.plaf.basic.BasicSliderUI#calculateThumbSize()
   */
  @Override
  protected void calculateThumbSize() {
    super.calculateThumbSize();
    _upperThumbRect.setSize(thumbRect.getSize());
  }

  /**
   * Calculate upper thumb location.
   */
  protected void calculateUpperThumbLocation() {
    if (slider.getSnapToTicks()) {
      int sliderExtent = slider.getExtent();
      int snappedExtent = sliderExtent;
      int majorTickSpacing = slider.getMajorTickSpacing();
      int minorTickSpacing = slider.getMinorTickSpacing();
      int tickSpacing = 0;

      if (minorTickSpacing > 0) {
        tickSpacing = minorTickSpacing;
      } else if (majorTickSpacing > 0) {
        tickSpacing = majorTickSpacing;
      }

      if (tickSpacing != 0) {
        // If it's not on a tick, change the value
        if ((sliderExtent - slider.getMinimum()) % tickSpacing != 0) {
          float temp = (float) (sliderExtent - slider.getMinimum())
              / (float) tickSpacing;
          int whichTick = Math.round(temp);
          snappedExtent = slider.getMinimum()
              + (whichTick * tickSpacing);
        }

        if (snappedExtent != sliderExtent) {
          slider.setExtent(snappedExtent);
        }
      }
    }

    if (slider.getOrientation() == JSlider.HORIZONTAL) {
      int valuePosition = xPositionForValue(slider.getValue()
          + slider.getExtent());

      _upperThumbRect.x = valuePosition - (_upperThumbRect.width / 2);
      _upperThumbRect.y = trackRect.y;
    } else {
      int valuePosition = yPositionForValue(slider.getValue()
          + slider.getExtent());

      _upperThumbRect.x = _upperThumbRect.x;
      _upperThumbRect.y = valuePosition - (_upperThumbRect.height / 2);
    }
  }

  private void calculateRangeDragLocation() {
    if (slider.getOrientation() == JSlider.HORIZONTAL) {
      int rangeStart = _rangeMode == RangeMode.CumulativeFromStart ? 0
          : (int) thumbRect.getMaxX();
      _rangeDragRect.setLocation(rangeStart, thumbRect.y);
      _rangeDragRect.setSize(_upperThumbRect.x - rangeStart,
          thumbRect.height);
    } else {
      int rangeStart = (int)(_rangeMode == RangeMode.CumulativeFromStart ? trackRect
          .getMaxY() :  _upperThumbRect.getMaxY());
      _rangeDragRect.setLocation(thumbRect.x, rangeStart);
      _rangeDragRect.setSize(thumbRect.width,
          (int) (thumbRect.getMinY() - _upperThumbRect.getMaxY()));
    }
  }

}
