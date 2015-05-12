/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit;

import java.awt.geom.Point2D;

/**
 * Defines a single line segment making up a path. This can be used when
 * iterating a {@link java.awt.geom.Path2D Path2D} to place objects along
 * it.
 * This class allows us to get a point a given distance along the segment
 * and also get the segment's angle. The angle is a value from 0 to 2PI
 * radians counterclockwise.
 * <p/>
 * Instances of this class are created by
 * {@link com.esri.client.toolkit.PathSegmentIterator
 * PathSegmentIterator} whenever a new iterator is requested.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.utilities.PathSegment} instead.
 */
@Deprecated
public class LineSegment {
  private Point2D _startPoint;
  private Point2D _endPoint;
  private double _angleRad;
  private double _length;
  private double _sizeX;
  private double _sizeY;

  /**
   * Construct a new line segment with the given start and end
   * coordinates.
   * 
   * @param startPoint
   * @param endPoint
   */
  public LineSegment(Point2D startPoint, Point2D endPoint) {
    _startPoint = startPoint;
    _endPoint = endPoint;
    _sizeX = _endPoint.getX() - _startPoint.getX();
    _sizeY = _endPoint.getY() - _startPoint.getY();
    calculateAngle();
    calculateLength();
  }

  /**
   * Get the segment's start point.
   * 
   * @return Segment's start point.
   */
  public Point2D getStartPoint() {
    return _startPoint;
  }

  /**
   * Get the segment's end point.
   * 
   * @return Segment's end point.
   */
  public Point2D getEndPoint() {
    return _endPoint;
  }

  /**
   * Get the angle of the segment in radians. A segment with an angle of
   * zero degrees would be a horizontal line running left to right.
   * 
   * @return Angle in radians counterclockwise.
   */
  public double getAngle() {
    return _angleRad;
  }

  /**
   * Get the length of the segment in pixels.
   * 
   * @return The segment length in pixels.
   */
  public double getLength() {
    return _length;
  }

  /**
   * Return a point on the segment distanceFromStart pixels from the start
   * of the segment.
   * <p/>
   * The point's coordinates are calculate as follows:
   * <p/>
   * Calculate the ratio of distanceFromStart to segment length
   * <p/>
   * Apply the ratio to the x-offset from the start of the segment to the
   * end of the segment. This will give us an x-offset for the new point
   * <p/>
   * Apply the ratio to the y-offset from the start of the segment to the
   * end of the segment. This will give us a y-offset for the new point
   * <p/>
   * Apply the calculated x and y offset values to the start point to get
   * the new point on the segment.
   * <p/>
   * If distanceFromStart is zero, we simply return the start point. If
   * distanceFromStart is equal to or greater than the segment length, we
   * return the end point.
   * 
   * @param distanceFromStart
   *            Distance in pixels from the start of the segment.
   * @return A point on the segment.
   */
  public Point2D getPointOnLine(double distanceFromStart) {
    Point2D retVal = null;

    if (distanceFromStart == 0) {
      retVal = _startPoint;
    } else if (distanceFromStart >= _length) {
      retVal = _endPoint;
    } else {
      // Get distanceFromStart as a proportion of total length
      double ratio = distanceFromStart / _length;

      // Get end point x and y offsets from start point. Multiplying
      // these by the ratio calculated above will give us the correct
      // x and y offsets from the line start
      double pointDeltaX = (_endPoint.getX() - _startPoint.getX())
          * ratio;
      double pointDeltaY = (_endPoint.getY() - _startPoint.getY())
          * ratio;

      // Apply calculate offsets to the start point.
      retVal = new Point2D.Double(_startPoint.getX() + pointDeltaX,
          _startPoint.getY() + pointDeltaY);
    }
    return retVal;
  }

  /**
   * Called by the constructor to calculate the segment length.
   */
  private void calculateLength() {
    _length = Math.sqrt((_sizeX * _sizeX) + (_sizeY * _sizeY));
  }

  /**
   * Called by the constructor to calculate the angle of the segment.
   */
  private void calculateAngle() {
    _angleRad = Math.atan(_sizeY / _sizeX);

    // Math.atan returns values from -PI/2 to PI/2 so we want to correct
    // for this
    if (_angleRad < 0) {
      // As _angleRad is negative, this will be a subtraction.
      _angleRad = 2 * Math.PI + _angleRad;
    }
  }
}