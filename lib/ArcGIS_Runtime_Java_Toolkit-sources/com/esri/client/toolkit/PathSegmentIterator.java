/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to move along the given path and determine the points
 * along it that objects can be placed. Each iteration is determined by a total
 * distance from the start of the line. At each call to next(), we can get a
 * reference to the current {@link com.esri.client.toolkit.LineSegment
 * LineSegment} and a value indicating how far along it we are.
 * <p/>
 * We can reset the iterator back to the start of the first segment by calling
 * reset().
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.utilities.PathDivider} instead.
 */
@Deprecated
public class PathSegmentIterator {
  private int _currentSegmentIndex = 0;
  private double _segmentLengthSoFar = 0;
  private LineSegment _curSegment;
  private double _pathLength;
  private ArrayList<LineSegment> _segments;
  private double _distanceAlongCurrentSegment;

  /**
   * Construct a new iterator from the given path. The iterator will be
   * positioned at the start of the first segment in the collection so we
   * do not need to call reset.
   * 
   * @param path
   *            Path to construct iterator from
   */
  public PathSegmentIterator(Path2D path) {
    _pathLength = getLineSegments(path);

    if (!_segments.isEmpty()) {
      _curSegment = _segments.get(0);
    }
  }

  /**
   * Get the index of the current segment in _segments.
   * 
   * @return Index of current segment
   */
  public int getCurrentSegmentIndex() {
    return _currentSegmentIndex;
  }

  /**
   * This is the total length of all segments before our current segment.
   * 
   * @return Total length of segments iterated so far.
   */
  public double getSegmentLengthSoFar() {
    return _segmentLengthSoFar;
  }

  /**
   * The current segment that the iterator has reached.
   * 
   * @return Current segment.
   */
  public LineSegment getCurSegment() {
    return _curSegment;
  }

  /**
   * Return the total length, in pixels, of all of the segments in the
   * path used to construct this iterator.
   * 
   * @return Total length of all segments.
   */
  public double getPathLength() {
    return _pathLength;
  }

  /**
   * Get a copy of the list of segments in this iterator instance.
   * 
   * @return Copy of list of segments.
   */
  public List<LineSegment> getSegments() {
    return Collections.unmodifiableList(_segments);
  }

  /**
   * Get the distance along the current segment after our last call to
   * next.
   * 
   * @return Distance along current segment.
   */
  public double getDistanceAlongCurrentSegment() {
    return _distanceAlongCurrentSegment;
  }

  /**
   * Move lengthAlongPath pixels along our segment collection.
   * <p/>
   * After calling this method, getCurrentSegment will return the current
   * segment we are now on and getDistanceAlongCurrentSegment will return
   * how far along that segment we are. As long as the return value from
   * this method is true, getCurrentSegment and
   * getDistanceAlongCurrentSegment will return valid values.
   * 
   * @param lengthAlongPath
   *            Total distance moved along path from start of first
   *            segment.
   * 
   * @return True if we have not reached the end of the last segment,
   *         false otherwise.
   */
  public boolean next(double lengthAlongPath) {
    // Start by assuming that we have more segments left. Once we
    // reach the end of the last segment, this will be set false.
    boolean hasMore = true;
    if (!_segments.isEmpty()) {
      // _segmentLengthSoFar is the total length of all segments
      // before
      // our current segment. To determine how far along the current
      // segment
      // we are, we simply subtract _segmentLengthSoFar from
      // lengthAlongPath.

      _distanceAlongCurrentSegment = lengthAlongPath - _segmentLengthSoFar;

      // Check to see if we have moved beyond the end of the
      // current segment
      while (_distanceAlongCurrentSegment >= _curSegment.getLength()) {
        // Get length of all previous segments
        _segmentLengthSoFar += _curSegment.getLength();

        // Calculate how far along the next segment we will be
        _distanceAlongCurrentSegment = lengthAlongPath - _segmentLengthSoFar;

        // Move to next segment position
        _currentSegmentIndex++;

        // Check that we still have segments left
        if (_currentSegmentIndex >= _segments.size()) {
          // Reached the end of the last segment so our
          // distance along it
          // will simply be the segment length
          _distanceAlongCurrentSegment = _curSegment.getLength();

          // If we have reached the last index, we still want
          // to
          // return true so that calling code will know that
          // the
          // distance and current segment are valid. If the
          // current
          // index has exceeded the number of segments,
          // indicate that
          // we've finished.
          hasMore = _currentSegmentIndex == _segments.size();
          break;
        } else {
          // Get next segment
          _curSegment = _segments.get(_currentSegmentIndex);
        }
      }
    } else {
      // No segments
      hasMore = false;
    }

    return hasMore;
  }

  /**
   * Reset the iterator back to the start of the first segment.
   */
  public void reset() {
    _currentSegmentIndex = 0;
    if(!_segments.isEmpty()){
      _curSegment = _segments.get(0);
    }else{
      _curSegment = null;
    }
    _distanceAlongCurrentSegment = 0;
  }

  /**
   * Called by the constructor to get all the segments from the given path
   * and construct {@link com.esri.client.toolkit.LineSegment
   * LineSegment} instances for each one. This method also calculates the
   * total length of the given path.
   * 
   * @param path
   *            Path to break into segments
   * @return Total length of given path
   */
  private double getLineSegments(Path2D path) {
    double totalLength = 0;
    _segments = new ArrayList<LineSegment>();
    PathIterator pPath = path.getPathIterator(null, 1);
    double[] points = new double[6];
    Point2D startPoint = null;

    // Loop through segments in path
    while (!pPath.isDone()) {
      int segType = pPath.currentSegment(points);
      if (segType == PathIterator.SEG_MOVETO) {
        // Start of new disjoint path
        startPoint = new Point2D.Double(points[0], points[1]);
      } else if (segType == PathIterator.SEG_LINETO) {
        // Continuation of current disjoint path
        LineSegment lineSegment = new LineSegment(startPoint,
            new Point2D.Double(points[0], points[1]));
        _segments.add(lineSegment);
        totalLength += lineSegment.getLength();
        startPoint = new Point2D.Double(points[0], points[1]);
      }

      pPath.next();
    }
    return totalLength;
  }
}