/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.legend;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.esri.client.local.ArcGISLocalTiledLayer;
import com.esri.core.map.LayerLegendInfo;
import com.esri.core.map.LayerLegendInfoCollection;
import com.esri.core.map.LegendItemInfo;
import com.esri.map.ArcGISDynamicMapServiceLayer;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GroupLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.Layer.LayerStatus;
import com.esri.map.LayerEvent;
import com.esri.map.LayerInfo;
import com.esri.map.LayerInitializeCompleteEvent;
import com.esri.map.LayerInitializeCompleteListener;
import com.esri.map.LayerListEventListenerAdapter;
import com.esri.map.WmsDynamicMapServiceLayer;

/**
 * This class implements a legend control that displays layers, sublayers and legend items for layers in a {@link JMap}.
 * Layer visibility can be changed by checking or unchecking the check boxes next to each layer in the control. If a
 * particular layer supports sublayer visibility changes, then the sublayers will also have check boxes.
 * <p/>
 * When a JLegend is created, any layers in the associated map will be added to the control. Any layers subsequently
 * added to the map will also be added to the JLegend. Layers are added with the base layer at the bottom of the list in
 * the JLegend and the topmost layer at the top.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.legend.JLegend} instead.
 */
@Deprecated
public class JLegend extends JPanel {

  /**
   * This class is used to handle mouse click events in the JLegend control. The mouse click event handler looks to see
   * if the mouse click is over the check box of a tree node and updates the visibility of the corresponding layer as
   * appropriate. Note that the actual drawing of the check box and displaying of its state is handled by
   * {@link LegendTreeCellRenderer}.
   */
  protected class LegendMouseListener extends MouseAdapter {
    int _hotspot = new JCheckBox().getPreferredSize().width;

    /*
     * (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
      TreePath path = _tree.getPathForLocation(e.getX(), e.getY());

      if (path != null && e.getX() < _tree.getPathBounds(path).x + _hotspot) {
        Object userObject = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();

        if (userObject instanceof Layer) {
          Layer layer = (Layer) userObject;
          layer.setVisible(!layer.isVisible());
        } else if (userObject instanceof LayerInfo) {
          LayerInfo info = (LayerInfo) userObject;
          info.setVisible(!info.isVisible());
        }

        _tree.repaint();
      }
    }

  }

  /**
   * This class listens for tree items in the JLegend control being expanded. This is used to create sub nodes on the
   * tree on demand. The general pattern for this is to add a dummy node to each tree node that is expected to have
   * children at some point. When the node is expanded, this event handler will remove the dummy node and replace it
   * with the correct child nodes.
   */
  protected class LegendTreeWillExpandListener implements TreeWillExpandListener {

    /*
     * (non-Javadoc)
     * @see javax.swing.event.TreeWillExpandListener#treeWillExpand(javax.swing .event.TreeExpansionEvent)
     */
    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
      handleExpandingNode(event);
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.event.TreeWillExpandListener#treeWillCollapse(javax.swing .event.TreeExpansionEvent)
     */
    @Override
    public void treeWillCollapse(TreeExpansionEvent event) {
    }

  }

  /**
   * This class listens for layers being added to or removed from the {@link JMap} associated with the JLegend. This is
   * used to update the list of layers in the control.
   */
  protected class LegendLayerListEventListener extends LayerListEventListenerAdapter {

    /*
     * (non-Javadoc)
     * @see com.esri.map.LayerListEventListenerAdapter#layerAdded(com.esri.map .LayerEvent)
     */
    @Override
    public void layerAdded(LayerEvent event) {
      addLayerToLegend(event.getChangedLayer(), _rootNode, event.getLayerIndex());

      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          _tree.expandPath(new TreePath(_rootNode));
          _tree.validate();
          _tree.repaint();
        }
      });
    }

    /*
     * (non-Javadoc)
     * @see com.esri.map.LayerListEventListenerAdapter#multipleLayersAdded(com .esri.map.LayerEvent)
     */
    @Override
    public void multipleLayersAdded(LayerEvent event) {
      for (Map.Entry<Integer, Layer> curEntry : event.getChangedLayers().entrySet()) {
        addLayerToLegend(curEntry.getValue(), _rootNode, curEntry.getKey());
      }

      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          _tree.expandPath(new TreePath(_rootNode));
          _tree.validate();
          _tree.repaint();
        }
      });
    }

    @Override
    public void layerRemoved(LayerEvent event) {
      removeLayerFromLegend(event.getChangedLayer());
    }

    @Override
    public void multipleLayersRemoved(LayerEvent event) {
      for (Map.Entry<Integer, Layer> curEntry : event.getChangedLayers().entrySet()) {
        removeLayerFromLegend(curEntry.getValue());
      }
    }

  }

  private static final long serialVersionUID = 1L;

  private JTree _tree;

  private JMap _map;

  private DefaultMutableTreeNode _rootNode;

  private LegendLayerListEventListener _layerListEventListener = new LegendLayerListEventListener();

  private LegendTreeWillExpandListener _treeWillExpandListener = new LegendTreeWillExpandListener();

  private DefaultTreeModel _model;

  private LegendMouseListener _legendMouseListener = new LegendMouseListener();

  /**
   * Create a JLegend instance to display the layers in the given {@link JMap} .
   * 
   * @param map the {@link JMap} containing the layers that will be displayed in this control
   */
  public JLegend(JMap map) {
    _map = map;
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    JScrollPane scrollPane = new JScrollPane();
    add(scrollPane);

    _tree = new JTree();
    scrollPane.setViewportView(_tree);

    initTreeFromMap();
  }

  /**
   * Called by the constructor to add tree nodes for each layer in the given {@link JMap}. This will also setup the
   * various event listeners used to keep the tree in sync. with the map.
   */
  private void initTreeFromMap() {
    _rootNode = new DefaultMutableTreeNode(null);
    _model = new DefaultTreeModel(_rootNode);
    _tree.setRootVisible(false);
    _tree.setShowsRootHandles(true);
    _tree.setModel(_model);
    if (_map != null) {
      _tree.setCellRenderer(new LegendTreeCellRenderer());
      _tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

      for (Layer curLayer : _map.getLayers()) {
        addLayerToLegend(curLayer, _rootNode, -1);
      }

      _tree.expandPath(new TreePath(_rootNode));

      _map.getLayers().addLayerListEventListener(_layerListEventListener);
      _tree.addTreeWillExpandListener(_treeWillExpandListener);
      _tree.addMouseListener(_legendMouseListener);
    }
  }

  /**
   * Adds the given layer to the legend. The tree node created for the given layer has a dummy node added to allow the
   * node to be expanded. The first time the layer's node is expanded, the dummy node will be replaced with either the
   * layer's sublayers or its legend items, depending on the layer type.
   * <p/>
   * The given layer is added to its node as a user object so that it can be used later to get the list of sublayers or
   * legend items.
   * 
   * @param layerToAdd the layer to add
   * @param parentNode the parent node, usually the tree's root node
   * @param addAtIndex the index to add the layer at. If this is -1, the layer will be added to the top of the tree
   */
  protected void addLayerToLegend(Layer layerToAdd, DefaultMutableTreeNode parentNode, int addAtIndex) {
    int layerIndex = addAtIndex == -1 ? 0 : parentNode.getChildCount() - 1 - addAtIndex;
    layerIndex = Math.max(0, layerIndex);
    DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(layerToAdd, true);
    _model.insertNodeInto(newChild, parentNode, layerIndex);

    // Insert dummy node to allow parent node to be expanded.
    // Children will be added later.
    _model.insertNodeInto(new DefaultMutableTreeNode(), newChild, 0);

    layerToAdd.addLayerInitializeCompleteListener(new LayerInitializeCompleteListener() {

      @Override
      public void layerInitializeComplete(LayerInitializeCompleteEvent e) {
        // Force redrawing of all layer nodes to make sure they're drawn
        // with enough room for the layer name.
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            _model.reload();
          }
        });
      }
    });
  }
  
  protected void removeLayerFromLegend(Layer layerToRemove){
    for(int count = 0; count < _rootNode.getChildCount(); ++count){
      DefaultMutableTreeNode curNode = (DefaultMutableTreeNode) _rootNode.getChildAt(count);
      if(curNode.getUserObject().equals(layerToRemove)){
        _model.removeNodeFromParent(curNode);
        break;
      }
    }
  }

  /**
   * Called by {@link LegendTreeWillExpandListener} to handle the expansion of a tree node. This method first looks to
   * see if the user object of the expanding node is a layer. If it is a layer that has not yet been initialised, the
   * expansion is cancelled.
   * <p/>
   * The method then gets the user object of the expanding node's dummy node. The dummy node will be deleted.
   * <p/>
   * If user object is null, then the expanding node represents either a layer or a sublayer. For layers, sublayer
   * information will be added if there is any otherwise legend information is added. For sublayers either more
   * sublayers will be added or the sublayer's legend information.
   * <p/>
   * If the user object is not null it is probably a collection of layer legend information so each item in the
   * collection will be added to the parent node as a child node containing the legend image and its label.
   * 
   * @param event the event
   * @throws ExpandVetoException the expand veto exception thrown if the node should not be expanded
   */
  protected void handleExpandingNode(TreeExpansionEvent event) throws ExpandVetoException {
    TreePath path = event.getPath();
    // Get last node in path as this is the one about to expand
    DefaultMutableTreeNode expandingNode = (DefaultMutableTreeNode) path.getLastPathComponent();

    if (expandingNode != null) {
      // Get node's user object to determine what to do next
      Object userObject = expandingNode.getUserObject();

      if (userObject != null && userObject instanceof Layer) {
        if (((Layer) userObject).getStatus() != LayerStatus.INITIALIZED) {
          // This is the node for a layer that isn't initialised
          // yet, stop it expanding.
          throw new ExpandVetoException(event);
        }
      }
      if (!expandingNode.isLeaf()) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) expandingNode.getChildAt(0);
        Object firstChildUserObject = node.getUserObject();
        if (firstChildUserObject == null) {
          // Dummy node, remove it and add the correct ones
          expandingNode.remove(0);
          if (userObject instanceof LayerInfo) {
            LayerInfo curLayerInfo = (LayerInfo) userObject;
            addLayerInfoToLegend(expandingNode, new ArrayList<LayerInfo>(curLayerInfo.getSubLayerInfos().values()));
          } else if (userObject instanceof ArcGISDynamicMapServiceLayer) {
            ArcGISDynamicMapServiceLayer dynamicLayer = (ArcGISDynamicMapServiceLayer) userObject;
            addLayerInfoToLegend(expandingNode, new ArrayList<LayerInfo>(dynamicLayer.getLayers().values()));
          } else if (userObject instanceof WmsDynamicMapServiceLayer) {
            WmsDynamicMapServiceLayer wmsLayer = (WmsDynamicMapServiceLayer) userObject;
            ArrayList<LayerInfo> layers = new ArrayList<LayerInfo>();
            for (LayerInfo curInfo : wmsLayer.getLayers()) {
              layers.add(curInfo);
            }
            addLayerInfoToLegend(expandingNode, layers);
          } else if (userObject instanceof ArcGISTiledMapServiceLayer) {
            ArcGISTiledMapServiceLayer tiledLayer = (ArcGISTiledMapServiceLayer) userObject;
            addLayerLegendInfoToLegend(expandingNode, tiledLayer.getLegend());
          } else if (userObject instanceof ArcGISLocalTiledLayer) {
            ArcGISLocalTiledLayer tiledLayer = (ArcGISLocalTiledLayer) userObject;
            addLayerLegendInfoToLegend(expandingNode, tiledLayer.getLegend());
          } else if (userObject instanceof ArcGISFeatureLayer) {
            // This is a feature layer so add its legend info to
            // its node.
            ArcGISFeatureLayer featureLayer = (ArcGISFeatureLayer) userObject;
            
            boolean allowExpand = false;
            LayerLegendInfoCollection legendInfos = featureLayer.getLegend();
            if (legendInfos != null) {
              LayerLegendInfo legendInfo = legendInfos.getLayerLegendInfo(-1);
              if (legendInfo != null) {
                allowExpand = true;
                for (LegendItemInfo curInfo : legendInfo.getLegendItemInfos()) {
                  _model.insertNodeInto(new DefaultMutableTreeNode(curInfo), expandingNode,
                      expandingNode.getChildCount());
                }
              }
            }
            
            if(!allowExpand){
              // Need to re-add dummy node we removed otherwise the expand icon will be removed
              expandingNode.add(new DefaultMutableTreeNode());
              throw new ExpandVetoException(event);
            }
          } else if (userObject instanceof GroupLayer) {
            for (Layer curLayer : ((GroupLayer) userObject).getLayers()) {
              addLayerToLegend(curLayer, expandingNode, -1);
            }
          }
        } else if (firstChildUserObject instanceof LayerLegendInfo && node.getChildCount() == 0) {
          // Dummy node, remove it and add the correct ones from
          // the legend info
          expandingNode.remove(0);
          LayerLegendInfo legendInfo = (LayerLegendInfo) firstChildUserObject;
          for (LegendItemInfo curItem : legendInfo.getLegendItemInfos()) {
            _model.insertNodeInto(new DefaultMutableTreeNode(curItem), expandingNode, expandingNode.getChildCount());
          }
        }
      }
      _tree.repaint();
    }
  }

  /**
   * Adds the layer legend info to the legend for layers with no control over sublayer visibility. There will be one
   * node added to the expanding node for each group of legend items in the given collection. Each item in the given
   * collection is equivalent to the sublayers of a layer that can have its sublayer visibility changed. Each item added
   * will have a dummy child node added that has the actual legend items collection as a user object. This will allow
   * the legend items to be added when the new node is expanded.
   * 
   * @param expandingNode the expanding node
   * @param legend the collection of {@link LayerLegendInfo} items
   */
  private void addLayerLegendInfoToLegend(DefaultMutableTreeNode expandingNode, LayerLegendInfoCollection legend) {
    if (legend != null) {
      for (LayerLegendInfo curInfo : legend.getLayerLegendInfos()) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(curInfo);
        _model.insertNodeInto(newNode, expandingNode, expandingNode.getChildCount());

        if (!curInfo.getLegendItemInfos().isEmpty()) {
          // We have legend items, create a dummy node to hold the
          // collection so we can add them later when this node
          // is expanded.
          DefaultMutableTreeNode dummyNode = new DefaultMutableTreeNode(curInfo);
          _model.insertNodeInto(dummyNode, newNode, 0);
        }
      }
    }
  }

  /**
   * Adds a collection of {@link LayerInfo} items to the legend. For each layer info item in the collection, a new node
   * will be added. If a layer info item has further sublayers, a dummy node will be added. This will have the correct
   * layer info items added to it when it is expanded.
   * <p/>
   * If a layer info item has no sublayers then we have reached the bottom of the tree of sublayers. In this case, the
   * method will find the parent node that represents the top level layer. This should be the second item in the current
   * node's path. The associated layer will be used to get the legend information for the layer ID of the current layer
   * info. This legend information will be added to a dummy node to be added to this node as it is expanded later.
   * 
   * @param parentNode the parent node to add the sublayer information to
   * @param layerInfo the collection of layer info items to add nodes for
   */
  protected void addLayerInfoToLegend(DefaultMutableTreeNode parentNode, List<LayerInfo> layerInfo) {
    for (LayerInfo curLayerInfo : layerInfo) {
      DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(curLayerInfo);
      _model.insertNodeInto(newChild, parentNode, parentNode.getChildCount());

      if (curLayerInfo.getSubLayerInfos().size() > 0) {
        // Insert dummy node to allow parent node to be expanded.
        // Children will be added later.
        _model.insertNodeInto(new DefaultMutableTreeNode(), newChild, 0);
      } else {
        // This sublayer represents an actual feature layer
        // in the service so should have a legend, see if we
        // can get it.
        // First get parent layer, this will be the second node
        // in the path.
        TreeNode[] path = parentNode.getPath();
        if (path.length >= 2) {
          DefaultMutableTreeNode layerNode = (DefaultMutableTreeNode) path[1];

          // Get the user object from the node, this is hopefully
          // a layer.
          Object userObject = layerNode.getUserObject();
          if (userObject instanceof Layer) {
            // Get the legend info for this sublayer id
            LayerLegendInfoCollection legend = ((Layer) userObject).getLegend();
            LayerLegendInfo legendInfo = legend.getLayerLegendInfo(curLayerInfo.getId());

            if (legendInfo != null) {
              // Add a dummy node containing the legend info.
              // When the parent of this dummy node is expanded
              // we can remove the dummy node and use the legend
              // info to add the correct legend nodes.
              _model.insertNodeInto(new DefaultMutableTreeNode(legendInfo), newChild, 0);
            }
          }
        }
      }
    }
  }

}
