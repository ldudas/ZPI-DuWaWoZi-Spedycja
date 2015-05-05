/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.utilities;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * This component is similar to {@link JToolBar}. It contains <code>Action</code>s placed 
 * along a path.
 * @usage
 * <pre>
 * <code>
 * double size = 14;
 * Rectangle2D shape = new Rectangle2D.Double(0, 0, size * 2, size * 2);
 * Path2D.Double path = new Path2D.Double(new Arc2D.Double(shape, 150, -120, Arc2D.OPEN));
 * 
 * PathBasedToolBar toolbar = new PathBasedToolBar(container, path, new Dimension(size, size));
 * 
 * toolbar.add({@literal <}Action{@literal >});
 * toolbar.add(...);
 * toolbar.arrangeButtons();
 * 
 * toolbar.setVisible(true);
 * toolbar.setLocation(10, 10);
 * </code>
 * </pre>
 * @since 10.2.3
 */
public class PathBasedToolBar {
  private Vector<AbstractButton> _buttons = new Vector<AbstractButton>();
  private PathDivider _pathDivider;
  private boolean _isVisible = false;
  private Dimension _buttonSize;
  private boolean _autoArrangeButtons = false;
  private Path2D _path;
  private Container _parentContainer;
  private Rectangle _bounds;
  
  public PathBasedToolBar(Container parent, Path2D path, Dimension buttonSize) {
    _parentContainer = parent;
    _buttonSize = buttonSize;
    setPath(path);
  }
  
  public Path2D getPath() {
    return _path;
  }

  public void setPath(Path2D path) {
    _path = path;
    
    // Get the path's bounds
    _bounds = _path.getBounds();
    
    // Inflate the bounds by an amount that should incorporate
    // the size of the buttons. The largest dimension of a button
    // is its diagonal.
    int buttonDiagonal = (int) Math.sqrt((_buttonSize.width ^ 2) + (_buttonSize.height ^ 2));
    _bounds.grow(buttonDiagonal, buttonDiagonal);
    
    _pathDivider = new PathDivider(path);
  }

  public boolean isVisible(){
    return _isVisible;
  }
  
  public void setVisible(boolean visible){
    _isVisible = visible;
    
    for (AbstractButton curButton: _buttons) {
      curButton.setVisible(visible);
    }
  }
  
  public boolean isAutoArrangeButtons() {
    return _autoArrangeButtons;
  }

  public void setAutoArrangeButtons(boolean autoArrangeButtons) {
    _autoArrangeButtons = autoArrangeButtons;
  }
  
  public void add(Action action){
    JButton btnTool = new JButton(action);
    btnTool.setOpaque(false);
    btnTool.setSize(_buttonSize);
    _parentContainer.add(btnTool);
    _buttons.add(btnTool);
    
    if(_autoArrangeButtons){
      arrangeButtons();
    }
  }
  
  public void remove(AbstractButton button){
    _buttons.remove(button);
    _parentContainer.remove(button);
    
    if(_autoArrangeButtons){
      arrangeButtons();
    }
  }
  
  public void remove(Action action){
    AbstractButton foundButton = null;
    
    for(AbstractButton curButton: _buttons){
      if(curButton.getAction().equals(action)){
        foundButton = curButton;
        break;
      }
    }
    
    if(foundButton != null){
      remove(foundButton);
    }
  }
  
  public void arrangeButtons() {
    int buttonCount = _buttons.size();
    double buttonSpacing = _pathDivider.getPathLength() / (buttonCount - 1);
    _pathDivider.reset();
    
    for(int counter = 0; counter < buttonCount; ++counter){
      if(_pathDivider.next(counter * buttonSpacing)){
        double lengthOnCurSegment = _pathDivider.getDistanceAlongCurrentSegment();
        PathSegment curSegment = _pathDivider.getCurSegment();
        Point2D buttonPoint = curSegment.getPointOnLine(lengthOnCurSegment);
        placeButton(buttonPoint, _buttons.get(counter));
      }
    }
  }
  
  public Point getLocation(){
    return _path.getBounds().getLocation();
  }
  
  public void setLocation(int x, int y){
    Point curLocation = getLocation();
    move(x - curLocation.x, y - curLocation.y);
  }

  public void setLocation(java.awt.Point location) {
    setLocation(location.x, location.y);
  }
  
  public void move(int dx, int dy){
    _bounds.translate(dx, dy);
    _path.transform(AffineTransform.getTranslateInstance(dx, dy));
    for(AbstractButton curButton: _buttons){
      moveButton(curButton, dx, dy);
    }
  }

  private void moveButton(AbstractButton curButton, int dx, int dy) {
    Point curLocation = curButton.getLocation();
    curLocation.translate(dx, dy);
    curButton.setLocation(curLocation);
  }

  private void placeButton(Point2D buttonPoint, AbstractButton button) {
    button.setLocation(
      (int)(buttonPoint.getX() - (_buttonSize.getWidth() / 2)), 
      (int)(buttonPoint.getY() - (_buttonSize.getHeight() / 2)));
  }

  public Rectangle getBounds() {
    return _bounds;
  }
}

/**
 * This class is used to move along the given path and determine the points
 * along it that objects can be placed. Each iteration is determined by a total
 * distance from the start of the line. At each call to next(), you can get a
 * reference to the current PathSegment and a 
 * value indicating how far along it you are.
 * <p/>
 * Reset the iterator back to the start of the first segment by calling reset().
 * @since 10.2.3
 */
class PathDivider {
  private int _currentSegmentIndex = 0;
  private double _segmentLengthSoFar = 0;
  private PathSegment _curSegment;
  private double _pathLength;
  private ArrayList<PathSegment> _segments;
  private double _distanceAlongCurrentSegment;

  /**
   * Construct a new iterator from the given path. The iterator will be
   * positioned at the start of the first segment in the collection so you
   * do not need to call reset.
   * 
   * @param path
   *            Path to construct iterator from
   */
  public PathDivider(Path2D path) {
    _pathLength = calculateLength(path);

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
  public PathSegment getCurSegment() {
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
  public List<PathSegment> getSegments() {
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
        }
        // Get next segment
        _curSegment = _segments.get(_currentSegmentIndex);
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
    _curSegment = _segments.isEmpty() ? null : _segments.get(0);
    _distanceAlongCurrentSegment = 0;
  }

  /**
   * Called by the constructor to get all the segments from the given path
   * and construct {@link com.esri.toolkit.utilities.PathSegment
   * LineSegment} instances for each one. This method also calculates the
   * total length of the given path.
   * 
   * @param path
   *            Path to break into segments
   * @return Total length of given path
   */
  private double calculateLength(Path2D path) {
    double totalLength = 0;
    _segments = new ArrayList<PathSegment>();
    PathIterator pathIter = path.getPathIterator(null, 1);
    double[] points = new double[6];
    Point2D startPoint = null;

    // Loop through segments in path
    while (!pathIter.isDone()) {
      int segType = pathIter.currentSegment(points);
      if (segType == PathIterator.SEG_MOVETO) {
        // Start of new disjoint path
        startPoint = new Point2D.Double(points[0], points[1]);
      } else if (segType == PathIterator.SEG_LINETO) {
        // Continuation of current disjoint path
        PathSegment lineSegment = new PathSegment(startPoint,
            new Point2D.Double(points[0], points[1]));
        _segments.add(lineSegment);
        totalLength += lineSegment.getLength();
        startPoint = new Point2D.Double(points[0], points[1]);
      }

      pathIter.next();
    }
    return totalLength;
  }
}

/**
 * Defines a single line segment making up a path. This can be used when
 * iterating a {@link java.awt.geom.Path2D Path2D} to place objects along
 * it.
 * This class allows us to get a point a given distance along the segment
 * and also get the segment's angle. The angle is a value from 0 to 2PI
 * radians counterclockwise.
 * <p/>
 * Instances of this class are created by PathDivider whenever a new iterator is requested.
 * @since 10.2.3
 */
class PathSegment {
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
  public PathSegment(Point2D startPoint, Point2D endPoint) {
    _startPoint = startPoint;
    _endPoint = endPoint;
    
    _sizeX = _endPoint.getX() - _startPoint.getX();
    _sizeY = _endPoint.getY() - _startPoint.getY();
    _angleRad = Math.atan(_sizeY / _sizeX);
    // Math.atan returns values from -PI/2 to PI/2 so we want to correct for this
    if (_angleRad < 0) {
      // As _angleRad is negative, this will be a subtraction.
      _angleRad = 2 * Math.PI + _angleRad;
    }
    _length = Math.sqrt((_sizeX * _sizeX) + (_sizeY * _sizeY));
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
   * The point's coordinates are calculated as follows:
   * <ul>
   * <li>Calculate the ratio of distanceFromStart to segment length.</li>
   * <li>Apply the ratio to the x, and y-offset from the start of the segment to the
   * end of the segment. This will give us an x, and y-offset for the new point.</li>
   * <li>Apply the calculated x and y offset values to the start point to get
   * the new point on the segment.</li>
   * <li>If distanceFromStart is zero, we simply return the start point. If
   * distanceFromStart is equal to or greater than the segment length, we
   * return the end point.</li>
   * </ul>
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
      double offsetX = (_endPoint.getX() - _startPoint.getX()) * ratio;
      double offsetY = (_endPoint.getY() - _startPoint.getY()) * ratio;

      // Apply calculate offsets to the start point.
      retVal = new Point2D.Double(_startPoint.getX() + offsetX, _startPoint.getY() + offsetY);
    }
    return retVal;
  }
}
