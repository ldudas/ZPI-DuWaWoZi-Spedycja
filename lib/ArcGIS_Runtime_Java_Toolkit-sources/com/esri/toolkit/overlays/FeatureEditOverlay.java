/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.event.EventListenerList;

import com.esri.toolkit.editing.JEditToolsPicker;
import com.esri.toolkit.utilities.ExceptionHandler;
import com.esri.toolkit.utilities.PathBasedToolBar;
import com.esri.core.geodatabase.GeodatabaseEditError;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureServiceTable;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Proximity2DResult;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.esri.map.FeatureLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListenerAdapter;
import com.esri.map.MapOverlay;

/**
* An overlay used to edit features in a <code>FeatureLayer</code>. Includes tools/actions 
* to select/unselect features using the mouse, edit the vertices of existing features, 
* and delete existing/selected features. Used by the {@link JEditToolsPicker} editing toolbar.
*
* @since 10.2.3
*/
public class FeatureEditOverlay extends MapOverlay {
    public class SaveChangesAction extends AbstractAction {
      private static final long serialVersionUID = 1L;
      /**
       * Action to save the changes that are being made to a feature.
       */
        public SaveChangesAction() {
            super("", new ImageIcon(FeatureEditOverlay.class.getResource("/com/esri/toolkit/images/GenericSaveTiny.png")));
            putValue(SHORT_DESCRIPTION, "Save any changes in the current feature");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            saveChanges();
        }
    }
    
    /**
     * Action to delete the currently selected vertex.
     */
    public class DeleteVertexAction extends AbstractAction {
      private static final long serialVersionUID = 1L;
        public DeleteVertexAction() {
            super("", new ImageIcon(FeatureEditOverlay.class.getResource("/com/esri/toolkit/images/GenericDeleteRedTiny.png")));
            putValue(SHORT_DESCRIPTION, "Delete the currently selected vertex");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
              deleteCurrentVertex();
            } catch (TableException e1) {
              ExceptionHandler.handleException(FeatureEditOverlay.this, e1);
            }
        }
    }

    /**
     * Action to cancel the changes that are being made to a feature.
     */
    public class CancelChangesAction extends AbstractAction {
      private static final long serialVersionUID = 1L;
        public CancelChangesAction() {
            super("", new ImageIcon(FeatureEditOverlay.class.getResource("/com/esri/toolkit/images/GenericCancelSaveTiny.png")));
            putValue(SHORT_DESCRIPTION, "Cancel all changes");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            cancelEdits();
        }
    }

    /**
     * Action to delete the features that are currently selected.
     */
    public class DeleteSelectedFeaturesAction extends AbstractAction {
      private static final long serialVersionUID = 1L;
        public DeleteSelectedFeaturesAction() {
            super("", new ImageIcon(FeatureEditOverlay.class.getResource("/com/esri/toolkit/images/GenericEraser16.png")));
            putValue(SHORT_DESCRIPTION, "Delete selected features");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            cancelEdits();
            try {
              deleteSelectedFeatures();
            } catch (TableException e1) {
              ExceptionHandler.handleException(FeatureEditOverlay.this, e1);
            }
        }
    }

    /**
     * Action to clear the selection: any selected features in the active layers
     * will no longer be selected.
     */
    public class ClearSelectionAction extends AbstractAction {
      private static final long serialVersionUID = 1L;
        public ClearSelectionAction() {
            super("", new ImageIcon(FeatureEditOverlay.class.getResource("/com/esri/toolkit/images/SelectionClearSelected16.png")));
            putValue(SHORT_DESCRIPTION, "Clear selection");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            clearSelectedFeatures();
        }
    }

    class VertexEditNode {
        public static final int NODE_SIZE = 10;
        public static final int HALF_NODE_SIZE = NODE_SIZE / 2;
        protected Point _centrePoint;
        protected Rectangle2D _node;
        protected FeatureLayer _parentFeatureLayer;
        protected Geometry _geometry;
        protected JMap _parentMap;
        protected int _centerPointIndex;
        protected boolean _currentNode;
        protected long _featureId;
        protected FeatureTable _featureTable;

        public VertexEditNode(int pointIndex, Point point, FeatureLayer parentFeatureLayer, long featureId, JMap map) {
            _centrePoint = point;
            _centerPointIndex = pointIndex;
            _parentFeatureLayer = parentFeatureLayer;
            _parentMap = map;
            _featureId = featureId;
            try {
              _featureTable = _parentFeatureLayer.getFeatureTable();
              _geometry = _featureTable.getFeature(_featureId).getGeometry();
            } catch (TableException e) {
              ExceptionHandler.handleException(FeatureEditOverlay.this, e);
            }

            setNodeRect();
        }

        public void move(double dx, double dy, boolean updateGeometry) throws TableException {
            if (_node != null) {
                // Update our location
                _node.setRect(_node.getX() + dx, _node.getY() + dy,
                        _node.getWidth(), _node.getHeight());

                if (updateGeometry) {
                    // Get the map location of our new center point
                    Point newMapPoint = _parentMap.toMapPoint(
                            (int) _node.getCenterX(), (int) _node.getCenterY());

                    // Update our geometry and refresh
                    _centrePoint.setXY(newMapPoint.getX(), newMapPoint.getY());
                    
                    Feature featureToUpdate = _featureTable.getFeature(_featureId);

                    if (_geometry instanceof MultiPath) {
                        MultiPath path = (MultiPath) _geometry;
                        path.setPoint(_centerPointIndex, _centrePoint);

                        _featureTable.updateFeature(_featureId, new GeodatabaseFeature(featureToUpdate.getAttributes(), _geometry, (GeodatabaseFeatureTable) _featureTable));
                    } else if (_geometry instanceof MultiPoint) {
                        MultiPoint points = (MultiPoint) _geometry;
                        points.setPoint(_centerPointIndex, _centrePoint);

                        _featureTable.updateFeature(_featureId, new GeodatabaseFeature(featureToUpdate.getAttributes(), _geometry, (GeodatabaseFeatureTable) _featureTable));
                    } else if (_geometry instanceof Point) {
                      _featureTable.updateFeature(_featureId, new GeodatabaseFeature(featureToUpdate.getAttributes(), _centrePoint, (GeodatabaseFeatureTable) _featureTable));
                    }
                    repaint();
                }
            }
        }

        public void draw(Graphics2D graphics){
            if(getNode() != null){
                // Save old state
                Paint oldPaint = graphics.getPaint();

                graphics.setPaint(_currentNode ? Color.red : Color.cyan);
                graphics.draw(getNode());
                graphics.fill(getNode());

                // Restore old state
                graphics.setPaint(oldPaint);
            }
        }

        public void setNodeRect() {
            if(_parentMap != null){
                Point screenPoint = getMap().toScreenPoint(_centrePoint);
                Point2D point = new Point2D.Double(screenPoint.getX(), screenPoint.getY());

                _node = new Rectangle2D.Double(point.getX() - HALF_NODE_SIZE, point.getY() - HALF_NODE_SIZE, NODE_SIZE, NODE_SIZE);
            }
        }

        public int getPointIndex(){
            return _centerPointIndex;
        }

        public Point getCentrePoint() {
            return _centrePoint;
        }

        public boolean isCurrentNode() {
            return _currentNode;
        }

        public void setCurrentNode(boolean currentNode) throws TableException {
            _currentNode = currentNode;
            if(currentNode){
                // Update geometry to match changes that may have been
                // made by another node since this one was created
                _geometry = _featureTable.getFeature(_featureId).getGeometry();
            }
        }

        public Feature getParentFeature() throws TableException{
            return _featureTable.getFeature(_featureId);
        }

        public FeatureInfo getFeatureInfo(){
            return new FeatureInfo(_parentFeatureLayer, _featureId);
        }

        public boolean hitTest(Point2D hitPoint){
            return getNode().contains(hitPoint);
        }

        public Rectangle2D getNode() {
            return _node;
        }

        public void delete() throws TableException {

            if(_geometry instanceof MultiPath){
                MultiPath path = (MultiPath) _geometry;
                path.removePoint(_centerPointIndex);
            }else if(_geometry instanceof MultiPoint){
                MultiPoint points = (MultiPoint) _geometry;
                points.removePoint(_centerPointIndex);
            }
            
            Feature featureToUpdate = _featureTable.getFeature(_featureId);
            _featureTable.updateFeature(_featureId, new GeodatabaseFeature(featureToUpdate.getAttributes(), _geometry, (GeodatabaseFeatureTable) _featureTable));
        }

        public int getOutcode(java.awt.Point point) {
            return _node.getBounds().outcode(point);
        }
    }

    class FakeVertexEditNode extends VertexEditNode{
        private int _nearestVertexIndex = -1;

        public FakeVertexEditNode(Point point,
                FeatureLayer parentFeatureLayer, long featureId, JMap map, int nearestVertexIndex) {
            super(-1, point, parentFeatureLayer, featureId, map);
            _nearestVertexIndex = nearestVertexIndex;
        }

        public int getNearestVertexIndex() {
            return _nearestVertexIndex;
        }

        public void makeReal() throws TableException {
            if (_geometry instanceof MultiPoint) {
                MultiPoint multiPoint = (MultiPoint) _geometry;
                multiPoint.add(_centrePoint);
                _centerPointIndex = multiPoint.getPointCount() - 1;
            } else if (_geometry instanceof MultiPath) {
                MultiPath multiPath = (MultiPath) _geometry;

                // Find out which path the point is on
                int pathIndex = multiPath
                        .getPathIndexFromPointIndex(_nearestVertexIndex);

                // Get the insert index relative to the path
                int beforePointIndex = _nearestVertexIndex
                        - multiPath.getPathStart(pathIndex) + 1;

                // Make sure we haven't exceeded the number of points in the path:
                // if we do, set the insert index to -1 to add at the end of
                // the path.
                int insertIndex = beforePointIndex < multiPath
                        .getPathSize(pathIndex) ? beforePointIndex : -1;

                // Insert the point. The insert index is relative to the
                // path rather than the geometry's vertex collection.
                multiPath.insertPoint(pathIndex, insertIndex,
                        _centrePoint);

                // Store the actual point index in the geometry's vertex
                // collection.
                _centerPointIndex = beforePointIndex;

                Feature featureToUpdate = _featureTable.getFeature(_featureId);
                _featureTable.updateFeature(_featureId, new GeodatabaseFeature(featureToUpdate.getAttributes(), _geometry, (GeodatabaseFeatureTable) _featureTable));
            }
        }

        @Override
        public void draw(Graphics2D graphics) {
            if (_centerPointIndex > -1) {
                super.draw(graphics);
            } else {
                if (_node != null) {
                    // Save old state
                    Paint oldPaint = graphics.getPaint();

                    graphics.setPaint(new Color(255, 0, 0, 125));
                    graphics.draw(_node);
                    graphics.fill(_node);

                    // Restore old state
                    graphics.setPaint(oldPaint);
                }
            }
        }

    }

    public enum EDIT_TOOL{
        /**
         * Represents that no tool is selected.
         */
        None,
        /**
         * Tool to add to the currently selected features.
         */
        SelectionAdd,
        /**
         * Tool to remove from the selected features.
         */
        SelectionRemove,
        /**
         * Tool to clear the selection/selected features.
         */
        SelectionClear,
        /**
         * Tool to edit the vertices of a feature.
         */
        VertexEdit,
        /**
         * Tool to delete selected features.
         */
        Delete
    }

    public class FeatureInfo{
        FeatureLayer featureLayer;
        long featureId;
        Geometry _originalGeometry;

        public FeatureInfo(FeatureLayer activeLayer, long curFeatureId) {
            this.featureLayer = activeLayer;
            this.featureId = curFeatureId;
            try {
              _originalGeometry = featureLayer.getFeatureTable().getFeature(featureId).getGeometry();
            } catch (TableException e) {
              ExceptionHandler.handleException(FeatureEditOverlay.this, e);
            }
        }

        public FeatureLayer getFeatureLayer() {
            return featureLayer;
        }

        public long getFeatureId() {
            return featureId;
        }
        
        public Geometry getOriginalGeometry(){
          return _originalGeometry;
        }
    }

    private EDIT_TOOL _editTool = EDIT_TOOL.None;
    private EventListenerList _featureEditCompleteListenerList = new EventListenerList();
    private List<FeatureInfo> _vertexEditFeatureInfos = null;
    private java.awt.Point _currentMousePoint;
    private FakeVertexEditNode _fakeNode = null;
    private PathBasedToolBar _vertexEditTools;

    private static final long serialVersionUID = 1L;
    private List<FeatureLayer> _activeLayers;
    private ArrayList<VertexEditNode> _editVertices;
    private ArrayList<FeatureLayer> _lockedLayers;
    private VertexEditNode _hitNode;
    public static final int BUTTON_SIZE = 14;
    public static final int HALF_BUTTON_SIZE = BUTTON_SIZE / 2;

    public EDIT_TOOL getEditTool() {
        return _editTool;
    }

    public void setEditTool(EDIT_TOOL drawingTool) {
        _editTool = drawingTool;

        setActive(drawingTool != EDIT_TOOL.None);
    }

    public List<FeatureLayer> getActiveLayers() {
        return _activeLayers;
    }
    
    public void setActiveLayers(List<Layer> activeLayers) {
      List<FeatureLayer> layers = new ArrayList<FeatureLayer>();
      if (activeLayers != null) {
        // only add feature layers that can be edited and allow geometry updates
        for (Layer layer : activeLayers) {
          if (layer instanceof FeatureLayer) {
            FeatureLayer featureLayer = (FeatureLayer)layer;
            FeatureTable table = featureLayer.getFeatureTable();
            if (table.isEditable() && table instanceof GeodatabaseFeatureTable && ((GeodatabaseFeatureTable) table).canUpdateGeometry()) {
              layers.add((FeatureLayer) layer);
            }
          }
        }
        _activeLayers = layers;
      } else {
        _activeLayers = null;
      }
    }

    public boolean isEditingVertices(){
        return (_vertexEditFeatureInfos != null && !_vertexEditFeatureInfos.isEmpty());
    }

    private void cancelEdits() {
      restoreGeometries();
        endVertexEdit();
        //repaint();
    }

  private void restoreGeometries() {
    if (_vertexEditFeatureInfos != null) {
      for (FeatureInfo curInfo : _vertexEditFeatureInfos) {
        long featureId = curInfo.getFeatureId();
        FeatureTable featureTable = curInfo.getFeatureLayer().getFeatureTable();
        Feature featureToRestore;
        try {
          featureToRestore = featureTable.getFeature(featureId);
          featureTable.updateFeature(featureId,
              new GeodatabaseFeature(featureToRestore.getAttributes(), curInfo.getOriginalGeometry(),
                  (GeodatabaseFeatureTable) featureTable));
        } catch (TableException e) {
          ExceptionHandler.handleException(FeatureEditOverlay.this, e);
        }
      }
    }
  }

    private void deleteCurrentVertex() throws TableException {
        if(_hitNode != null){
            _hitNode.delete();
            PathBasedToolBar tools = getVertexEditTools();
            if(tools != null){
                tools.setVisible(false);
            }
            updateEditVertices();
        }
    }

  private void deleteSelectedFeatures() throws TableException {
    if (getActiveLayers() == null) {
      setActiveLayers(new ArrayList<Layer>(getMap().getLayers()));
    }

    for (FeatureLayer activeLayer : getActiveLayers()) {
      GeodatabaseFeatureTable table = (GeodatabaseFeatureTable) activeLayer.getFeatureTable();

      // Get selected graphics
      List<Feature> selectedGraphics = activeLayer.getSelectedFeatures();

      if (selectedGraphics != null) {
        FeatureInfo[] selectedFeatureInfo = new FeatureInfo[selectedGraphics.size()];
        long[] featureIds = new long[selectedGraphics.size()];

        for (int count = 0; count < selectedGraphics.size(); ++count) {
          long curGraphicId = selectedGraphics.get(count).getId();
          selectedFeatureInfo[count] = new FeatureInfo(activeLayer, curGraphicId);
          featureIds[count] = curGraphicId;
        }

        if (selectedGraphics.size() != 0) {
          table.deleteFeatures(featureIds);
          if (table instanceof GeodatabaseFeatureServiceTable)
          {
            applyEditsToService((GeodatabaseFeatureServiceTable)table);
          }
          fireFeatureDeleteComplete(selectedFeatureInfo);
        }

        activeLayer.clearSelection();
        // repaint();
      }
    }
  }

    private void saveChanges() {
        if(_hitNode != null){
          if (_hitNode._featureTable instanceof GeodatabaseFeatureServiceTable)
          {
            applyEditsToService((GeodatabaseFeatureServiceTable)_hitNode._featureTable);
          }
            fireFeatureEditComplete(new FeatureInfo[]{_hitNode.getFeatureInfo()});
            endVertexEdit();
        }
    }

    private void clearSelectedFeatures() {
        List<FeatureLayer> activeLayers = getActiveLayers();
        if(activeLayers == null){
            activeLayers = new ArrayList<FeatureLayer>();
            
            for(Layer curLayer: getMap().getLayers()){
              if(curLayer instanceof FeatureLayer){
                activeLayers.add((FeatureLayer) curLayer);
              }
            }
        }

        for(FeatureLayer curLayer: activeLayers){
            curLayer.clearSelection();
        }
    }

    /**
     * Adds a listener for the event fired when a feature has been edited.
     *
     * @param l
     * Listener to remove
     */
    public void addFeatureEditCompleteListener(FeatureEditCompleteListener l) {
        _featureEditCompleteListenerList.add(FeatureEditCompleteListener.class, l);
    }

    /**
     * Removes a listener for the event fired when a feature has been edited.
     *
     * @param l
     * Listener to remove
     */
    public void removeFeatureEditCompleteListener(FeatureEditCompleteListener l) {
        _featureEditCompleteListenerList.remove(FeatureEditCompleteListener.class, l);
    }

    @Override
    protected void setMap(JMap map) {
        super.setMap(map);
        map.addMapEventListener(new MapEventListenerAdapter() {
            @Override
            public void mapExtentChanged(MapEvent event) {
                updateVertexLocations();
                if (_hitNode != null && _vertexEditTools != null) {
                    Point sp = getMap().toScreenPoint(_hitNode.getCentrePoint());
                    _vertexEditTools.setLocation(
                        (int) (sp.getX() - BUTTON_SIZE),
                        (int) (sp.getY() - BUTTON_SIZE));
                }
            }
        });
    }

    @Override
    public void onMouseClicked(MouseEvent event) {
        _currentMousePoint = event.getPoint();
        if (event.getClickCount() > 1) {
            super.onMouseClicked(event);
            updateVertexLocations();
        } else {
            switch (_editTool) {
            case SelectionAdd:
                handleAddSelectionMouseClick(event);
                break;
            case SelectionRemove:
                handleRemoveSelectionMouseClick(event);
                break;
            case VertexEdit:
                handleVertexEditMouseClick(event);
                break;
            default:
                super.onMouseClicked(event);
                break;
            }
        }
    }

    @Override
    public void onMousePressed(MouseEvent event) {
        _currentMousePoint = event.getPoint();
        boolean passThroughEvent = true;

        switch (_editTool) {
        case VertexEdit:
            try {
              passThroughEvent = !hitTestVertices(event);
            } catch (TableException e) {
              ExceptionHandler.handleException(FeatureEditOverlay.this, e);
            }
            break;
          default:
            break;
        }

        if (passThroughEvent) {
            super.onMousePressed(event);
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        boolean passThroughEvent = true;
        int dx = (int)(event.getX() - _currentMousePoint.getX());
        int dy = (int)(event.getY() - _currentMousePoint.getY());
        _currentMousePoint = event.getPoint();

        switch (_editTool) {
        case VertexEdit:
            if(_hitNode != null){
                try {
                  moveHitVertex(dx, dy);
                } catch (TableException e) {
                  ExceptionHandler.handleException(FeatureEditOverlay.this, e);
                }
                passThroughEvent = false;
            }else{
                // Move vertices to track movement of dragged map
                try {
                  moveCurrentVertices(dx, dy);
                } catch (TableException e) {
                  ExceptionHandler.handleException(FeatureEditOverlay.this, e);
                }
            }
            break;
          default:
            break;
        }

        if(passThroughEvent){
            if (_vertexEditTools != null) {
                _vertexEditTools.setVisible(false);
            }
            super.onMouseDragged(event);
        }
    }

    @Override
    public void onMouseMoved(MouseEvent event) {
        super.onMouseMoved(event);
        switch (_editTool) {
        case VertexEdit:
            try {
              handleFakeVertex(event);
            } catch (TableException e) {
              ExceptionHandler.handleException(FeatureEditOverlay.this, e);
            }
            break;
          default:
            break;
        }
    }

    @Override
    public void onPaint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;

        switch (_editTool) {
        case VertexEdit:
            if (_editVertices != null/* && !_moving*/) {
                for (VertexEditNode curNode : _editVertices) {
                    curNode.draw(g2d);
                }
            }

            if(_fakeNode != null){
                _fakeNode.draw(g2d);
            }
            break;
          default:
            break;
        }
    }

    /**
     * Fires a feature edit complete event
     */
    protected void fireFeatureEditComplete(FeatureInfo[] featureInfos) {
        Object[] listeners = _featureEditCompleteListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FeatureEditCompleteListener.class) {
                ((FeatureEditCompleteListener) listeners[i + 1])
                        .featureEditComplete(new FeatureEditCompleteEvent(
                                this,
                                featureInfos,
                                FeatureEditCompleteEvent.FEATUREEDITCOMPLETE_EDITED));
            }
        }
    }

    /**
     * Fires a feature delete complete event
     */
    protected void fireFeatureDeleteComplete(FeatureInfo[] deletedFeatures) {
        Object[] listeners = _featureEditCompleteListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FeatureEditCompleteListener.class) {
                ((FeatureEditCompleteListener) listeners[i + 1])
                        .featureDeleteComplete(new FeatureEditCompleteEvent(
                                this,
                                deletedFeatures,
                                FeatureEditCompleteEvent.FEATUREEDITCOMPLETE_DELETED));
            }
        }
    }

    protected void setSelectedAtClickedPoint(MouseEvent event, boolean select) {
        if(getActiveLayers() == null){
            setActiveLayers(new ArrayList<Layer>(getMap().getLayers()));
        }

        for(FeatureLayer activeLayer: getActiveLayers()){
            long[] ids = activeLayer.getFeatureIDs(event.getX(), event.getY(), 0);
            for(long i : ids) {
                // Set selected state
                if(select){
                  activeLayer.selectFeature(i);
                }else{
                  activeLayer.unselectFeature(i);
                }
            }
        }
    }

    private void handleAddSelectionMouseClick(MouseEvent event) {
        setSelectedAtClickedPoint(event, true);
    }

    private void handleRemoveSelectionMouseClick(MouseEvent event) {
        setSelectedAtClickedPoint(event, false);
    }

    private void handleVertexEditMouseClick(MouseEvent event) {
        if(isEditingVertices()){
            // Allows us to add a new vertex by clicking
            try {
              if(!hitTestVertices(event)){
                  cancelEdits();
              }
            } catch (TableException e) {
              ExceptionHandler.handleException(FeatureEditOverlay.this, e);
            }
        }else{
            beginVertexEditing(event);
        }
    }

    private void endVertexEdit() {
        if(_vertexEditFeatureInfos != null){
            _vertexEditFeatureInfos.clear();
        }

        if(_editVertices != null){
            _editVertices.clear();
        }

        getVertexEditTools().setVisible(false);
    }

    private void beginVertexEditing(MouseEvent event) {
        if(getActiveLayers() == null){
            setActiveLayers(new ArrayList<Layer>(getMap().getLayers()));
        }

        if(_lockedLayers == null){
            _lockedLayers = new ArrayList<FeatureLayer>();
        }

        _vertexEditFeatureInfos = new ArrayList<FeatureInfo>();

        for(FeatureLayer activeLayer: getActiveLayers()){
                long[] ids = activeLayer.getFeatureIDs(event.getX(), event.getY(), 0);

                if(ids.length > 0){
                    _lockedLayers.add(activeLayer);

                    for (long curFeatureId : ids) {
                        _vertexEditFeatureInfos.add(new FeatureInfo(
                            activeLayer, curFeatureId));
                    }
                }
        }

        setupVertexEditing();
    }

    private void setupVertexEditing() {
        if(_vertexEditFeatureInfos != null){
            updateEditVertices();
        }
    }

    private void updateEditVertices() {
        JMap map = getMap();
        _editVertices = new ArrayList<VertexEditNode>();
        for (FeatureInfo curVertexInfo : _vertexEditFeatureInfos) {
            Geometry geometry;
            try {
              geometry = curVertexInfo.featureLayer.getFeatureTable().getFeature(
                      curVertexInfo.featureId).getGeometry();

              if (geometry instanceof MultiPath) {
                  MultiPath path = (MultiPath) geometry;
                  for (int pointCount = 0; pointCount < path.getPointCount(); ++pointCount) {
                      _editVertices.add(new VertexEditNode(pointCount, path
                              .getPoint(pointCount), curVertexInfo.featureLayer,
                              curVertexInfo.featureId, map));
                  }
              } else if (geometry instanceof MultiPoint) {
                  MultiPoint points = (MultiPoint) geometry;
                  for (int pointCount = 0; pointCount < points.getPointCount(); ++pointCount) {
                      _editVertices.add(new VertexEditNode(pointCount, points
                              .getPoint(pointCount), curVertexInfo.featureLayer,
                              curVertexInfo.featureId, map));
                  }
              } else if (geometry instanceof Point) {
                  _editVertices.add(new VertexEditNode(0, (Point) geometry,
                          curVertexInfo.featureLayer, curVertexInfo.featureId,
                          map));
              }
            } catch (TableException e) {
              ExceptionHandler.handleException(FeatureEditOverlay.this, e);
            }
        }
        repaint();
    }

    private void moveCurrentVertices(int dx, int dy) throws TableException {
        if(_editVertices != null){
            for(VertexEditNode curNode: _editVertices){
                curNode.move(dx, dy, false);
            }
        }

        if(_fakeNode != null){
            _fakeNode.move(dx, dy, false);
        }
    }

    private void updateVertexLocations() {
        if(_editVertices != null){
            for(VertexEditNode curNode: _editVertices){
                curNode.setNodeRect();
            }
        }

    }

    private boolean hitTestVertices(MouseEvent event) throws TableException {
        if(_hitNode != null){
            _hitNode.setCurrentNode(false);
        }
        _hitNode = null;

        if (_editVertices != null) {
            Point2D.Double hitPoint = new Point2D.Double(event.getX(),
                    event.getY());
            for (VertexEditNode curNode : _editVertices) {
                if (curNode.hitTest(hitPoint)) {
                    _fakeNode = null;
                    _hitNode = curNode;
                    _hitNode.setCurrentNode(true);
                }
            }

            if (_hitNode == null && _fakeNode != null && _fakeNode.hitTest(hitPoint)) {
                _fakeNode.makeReal();
                updateEditVertices();
                _hitNode = _editVertices.get(_fakeNode.getPointIndex());
                _hitNode.setCurrentNode(true);
                _fakeNode = null;
            }

            if(_hitNode != null){
                Point pt = new Point(_hitNode.getCentrePoint().getX(), _hitNode.getCentrePoint().getY());
                Point screenPoint = getMap().toScreenPoint(pt);
                attachToNode(getVertexEditTools(), new java.awt.Point((int)screenPoint.getX(), (int)screenPoint.getY()));
            }
        }
        return _hitNode != null;
    }

    private void moveHitVertex(int dx, int dy) throws TableException {
        if(_hitNode != null){
            _hitNode.move(dx, dy, true);
            PathBasedToolBar vertexEditTools = getVertexEditTools();
            vertexEditTools.move(dx, dy);
            vertexEditTools.setVisible(true);
        }
    }

    private void handleFakeVertex(MouseEvent event) throws TableException {
        boolean repaint = false;

        if(_fakeNode != null){
            // We want to do a repaint to remove an existing
            // fake node if we don't draw a new one.
            repaint = true;
        }

        _fakeNode = null;

        if (_vertexEditFeatureInfos != null) {
            // Convert mouse point to map coordinates
            JMap map = getMap();
            SpatialReference spatialReference = map.getSpatialReference();
            Point mapPoint = map.toMapPoint(event.getX(), event.getY());

            // Get nearest graphic.
            double distance = map.getResolution() * 6;
            FeatureInfo closestVertexInfo = null;
            for (FeatureInfo curVertexInfo : _vertexEditFeatureInfos) {
                Feature curFeature = curVertexInfo.featureLayer.getFeatureTable()
                        .getFeature(curVertexInfo.featureId);
                double curDistance = GeometryEngine.distance(mapPoint,
                        curFeature.getGeometry(), spatialReference);
                if (curDistance < distance) {
                    distance = curDistance;
                    closestVertexInfo = curVertexInfo;
                }
            }

            if (closestVertexInfo != null) {
                Proximity2DResult result = GeometryEngine.getNearestCoordinate(
                        closestVertexInfo.featureLayer.getFeatureTable().getFeature(
                                closestVertexInfo.featureId).getGeometry(),
                        mapPoint, false);
                if (!result.isEmpty()) {
                    _fakeNode = new FakeVertexEditNode(result.getCoordinate(),
                            closestVertexInfo.featureLayer,
                            closestVertexInfo.featureId, map,
                            result.getVertexIndex());
                    repaint = true;
                }
            }

            if (repaint) {
                repaint();
            }
        }
    }

    private void attachToNode(PathBasedToolBar tools, java.awt.Point screenPoint) {
        // The path the buttons lie on is an arc created as half of a circle
        // that fits inside a rectangle four times bigger than a vertex edit
        // node. We want to position the buttons so that the baseline joining
        // the ends of the arc lies on the bottom edge of the node and the node
        // is located in the middle of the baseline.
        screenPoint.translate(-BUTTON_SIZE, -BUTTON_SIZE);
        tools.setLocation(screenPoint);
        tools.setVisible(true);
    }

    private PathBasedToolBar getVertexEditTools() {
        if (_vertexEditTools == null) {
            double ellipseSize = BUTTON_SIZE * 2;
            Rectangle2D ellipseBounds = new Rectangle2D.Double(0, 0,
                    ellipseSize, ellipseSize);
            Arc2D arc = new Arc2D.Double(ellipseBounds, 150, -120, Arc2D.OPEN);
            _vertexEditTools = new PathBasedToolBar(this, new Path2D.Double(arc),
                    new Dimension(BUTTON_SIZE, BUTTON_SIZE));
            _vertexEditTools.add(new DeleteVertexAction());
            _vertexEditTools.add(new SaveChangesAction());
            _vertexEditTools.add(new CancelChangesAction());
            _vertexEditTools.arrangeButtons();
        }

        return _vertexEditTools;
    }

  private void applyEditsToService(final GeodatabaseFeatureServiceTable featureServiceTable) {
    featureServiceTable.applyEdits(new CallbackListener<List<GeodatabaseEditError>>() {

      @Override
      public void onError(Throwable e) {
        String errMsg = "Failed to apply edits to server " + featureServiceTable.getTableName();
        errMsg += ". " + e.getMessage();
        ExceptionHandler.handleException(getMap(), errMsg);
      }

      @Override
      public void onCallback(List<GeodatabaseEditError> errors) {
        if (!errors.isEmpty()) {
          String errMsg = "Failed to apply edits to server " + featureServiceTable.getTableName();
          errMsg += ". " + errors;
          ExceptionHandler.handleException(getMap(), errMsg);
        }
      }
    });
  }
}
