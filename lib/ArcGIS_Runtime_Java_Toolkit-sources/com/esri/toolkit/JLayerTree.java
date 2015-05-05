/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.esri.map.ArcGISDynamicMapServiceLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.Layer.LayerStatus;
import com.esri.map.LayerEvent;
import com.esri.map.LayerInfo;
import com.esri.map.LayerList;
import com.esri.map.LayerListEventListenerAdapter;
import com.esri.toolkit.utilities.ExceptionHandler;

/**
 * A UI component to display all layers of a map as a tree.
 * @usage
 * <code>
 * <pre>
 * JLayerTree jLayerTree = new JLayerTree(jMap);
 * contentPane.add(jLayerTree, BorderLayout.WEST); // adds to a component with border layout
 * </pre>
 * </code>
 * @since 10.2.3
 */
public class JLayerTree extends JPanel {
  private static final long serialVersionUID = 1L;

  private JMap map;
  private JTree layerTree;
  private LayerTreeModel treeModel;
  public JLayerTree(JMap map) {
    this.map = map;
    initGUI();

    map.getLayers().addLayerListEventListener(new LayerListEventListenerAdapter(){

      @Override
      public void layerAdded(LayerEvent event) {
        refresh();
      }

      @Override
      public void layerRemoved(LayerEvent event) {
        refresh();
      }
      
    });
  }

  public void initGUI() {
    layerTree = new JTree();
    treeModel = new LayerTreeModel(map);

    layerTree.addMouseListener(new LayerTreeMouseListener());
    layerTree.setModel(treeModel);
    layerTree.setCellRenderer(new LayerCellRenderer());
    layerTree.setRootVisible(false);// the root represents the JMap
    layerTree.setShowsRootHandles(true);

    add(new JScrollPane(layerTree));
  }

  public void refresh() {
    treeModel.refresh();
  }

  public static String getNodeLayerName(Object arg0) {
    if (arg0 instanceof JMap) {
      // the root node
      return "Map";
    } else if (arg0 instanceof LayerInfo) {
      return ((LayerInfo) arg0).getName();
    } else if (arg0 instanceof Layer
        && ((Layer) arg0).getStatus() == LayerStatus.INITIALIZED) {
      return ((Layer) arg0).getName();
    } else // unknown object type or uninitialized layer
    {
      return "";
    }
  }

  public static boolean getNodeLayerVisible(Object arg0) {
    if (arg0 instanceof JMap) {
      // the root node
      return false;
    } else if (arg0 instanceof LayerInfo) {
      return ((LayerInfo) arg0).isVisible();
    } else if (arg0 instanceof Layer
        && ((Layer) arg0).getStatus() == LayerStatus.INITIALIZED) {
      return ((Layer) arg0).isVisible();
    } else // unknown object type or uninitialized layer
    {
      return true;
    }
  }

  public static void setNodeLayerVisible(Object arg0, boolean isVisible) {
    if (arg0 instanceof JMap) {
      // do nothing
    } else if (arg0 instanceof LayerInfo) {
      ((LayerInfo) arg0).setVisible(isVisible);
    } else if (arg0 instanceof Layer
        && ((Layer) arg0).getStatus() == LayerStatus.INITIALIZED) {
      ((Layer) arg0).setVisible(isVisible);
    }
    // else, unknown object type or uninitialized layer
    // do nothing
  }

  public static String getNodeLayerToolTip(Object arg0) {
    if (arg0 instanceof JMap) {
      // the root node
      return "JMap";
    } else if (arg0 instanceof LayerInfo) {
      return "";
    } else if (arg0 instanceof GraphicsLayer) {
      return ("Graphics layer");
    } else if (arg0 instanceof Layer
        && ((Layer) arg0).getStatus() == LayerStatus.INITIALIZED) {
      return ((Layer) arg0).getUrl();
    } else // unknown object type or uninitialized layer
    {
      return "";
    }
  }
  
  class LayerTreeMouseListener extends MouseAdapter {
    int _hotspot = new JCheckBox().getPreferredSize().width;
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
      TreePath path = layerTree.getPathForLocation(e.getX(), e.getY());
      
      if(path != null && e.getX() < layerTree.getPathBounds(path).x + _hotspot){
        Object node = path.getLastPathComponent();
        
        if(node instanceof Layer){
          Layer layer = (Layer) node;
          layer.setVisible(!layer.isVisible());
        }else if(node instanceof LayerInfo){
          LayerInfo info = (LayerInfo) node;
          info.setVisible(!info.isVisible());
        }
        
        layerTree.repaint();
      }
    }

  }

  class LayerTreeModel implements TreeModel {
    JMap map;
    List<TreeModelListener> treeListeners = new ArrayList<TreeModelListener>();

    LayerTreeModel(JMap map) {
      this.map = map;
    }

    public void refresh() {
      for (TreeModelListener listener : treeListeners) {
        listener.treeStructureChanged(new TreeModelEvent(this,
            new TreePath(getRoot())));
      }
    }

    @Override
    public void addTreeModelListener(TreeModelListener arg0) {
      treeListeners.add(arg0);
    }

    @Override
    public Object getRoot() {
      return map;
    }

    @Override
    public Object getChild(Object arg0, int arg1) {
      if (arg0 instanceof JMap) {
        // the root node
        LayerList layers = map.getLayers();
        return layers.get(layers.size() - 1 - arg1);
      } else if (arg0 instanceof ArcGISDynamicMapServiceLayer
          && ((Layer) arg0).getStatus() == LayerStatus.INITIALIZED) {

        return ((ArcGISDynamicMapServiceLayer) arg0).getSubLayer(arg1);
      } else // unknown object type, layer type with no sub layers or
          // uninitialized layer
      {
        return null;
      }
    }

    @Override
    public int getChildCount(Object arg0) {
      if (arg0 instanceof JMap) {
        // the root node
        return map.getLayers().size();
      } else if (arg0 instanceof ArcGISDynamicMapServiceLayer
          && ((Layer) arg0).getStatus() == LayerStatus.INITIALIZED) {
        return ((ArcGISDynamicMapServiceLayer) arg0).getLayersList()
            .size();
      } else // unknown object type, layer type with no sub layers or
          // uninitialized layer
      {
        return 0;
      }
    }

    @Override
    public int getIndexOfChild(Object arg0, Object arg1) {
      if (arg0 instanceof JMap && arg1 instanceof Layer) {
        // the root node
        return (map.getLayers().lastIndexOf(arg1));
      } else if (arg0 instanceof ArcGISDynamicMapServiceLayer
          && ((Layer) arg0).getStatus() == LayerStatus.INITIALIZED
          && arg1 instanceof LayerInfo) {
        List<LayerInfo> infos = (List<LayerInfo>) (((ArcGISDynamicMapServiceLayer) arg0)
            .getLayersList());
        return infos.lastIndexOf(arg1);
      } else // unknown object type, layer type with no sub layers or
          // uninitialized layer
      {
        return -1;
      }
    }

    @Override
    public boolean isLeaf(Object arg0) {
      if (arg0 instanceof JMap) {
        // the root node
        return false;
      } else if (arg0 instanceof ArcGISDynamicMapServiceLayer
          && ((Layer) arg0).getStatus() == LayerStatus.INITIALIZED) {
        return ((ArcGISDynamicMapServiceLayer) arg0).getLayersList()
            .size() == 0;
      } else if (arg0 instanceof LayerInfo) {
        return ((LayerInfo) arg0).getSubLayerInfos().size() == 0;
      } else // unknown object type, layer type with no sub layers or
          // uninitialized layer
      {
        return true;
      }
    }

    @Override
    public void removeTreeModelListener(TreeModelListener arg0) {
      treeListeners.remove(arg0);
    }

    @Override
    public void valueForPathChanged(TreePath arg0, Object arg1) {
      // do nothing, these events aren't fired in this scenario
    }
  }

  private class LayerCellRenderer extends JCheckBox implements
      TreeCellRenderer {
    private static final long serialVersionUID = 1L;
    private Object nodeLayer;// member variable to allow event handler's to
                  // access

    public LayerCellRenderer() {
      setOpaque(true);
      initGui();
    }

    private void initGui() {
      setSelected(true);
      setPreferredSize(new Dimension(250, 20));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean cellIsSelected, boolean expanded, boolean isLeaf,
        int row, boolean hasFocus) {

      if (value == null)
        return this;

      // the value is probably a Layer or LayerInfo
      nodeLayer = value;
      try {
        setText(getNodeLayerName(nodeLayer));
        setSelected(getNodeLayerVisible(value));
        setToolTipText(getNodeLayerToolTip(nodeLayer));
      } catch (Exception e) {
        ExceptionHandler.handleException(JLayerTree.this, e);
      }

      if (cellIsSelected) {
        setColours(SystemColor.textHighlight,
            SystemColor.textHighlightText);
      } else {
        setColours(SystemColor.text, SystemColor.textText);
      }

      return this;
    }

    private void setColours(Color background, Color foreground) {
      setBackground(background);
      setForeground(foreground);
    }
  }
}
