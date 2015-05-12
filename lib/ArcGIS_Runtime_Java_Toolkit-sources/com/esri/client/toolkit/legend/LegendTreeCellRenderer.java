/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.legend;

import java.awt.Component;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import com.esri.core.geometry.Geometry.Type;
import com.esri.core.map.LayerLegendInfo;
import com.esri.core.map.LegendItemInfo;
import com.esri.map.ArcGISFeatureLayer;
import com.esri.map.GroupLayer;
import com.esri.map.Layer;
import com.esri.map.LayerInfo;
import com.esri.map.WmsLayerInfo;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.apache.james.mime4j.codec.Base64InputStream;

/**
 * This class is used by {@link JLegend} to render each node of its tree. Each
 * node will have an icon and a label. Each node that represents a layer or
 * sublayer than can have its visibility changed will also have a checkbox.
 * 
 * @see TreeCellRenderer
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.legend.LegendTreeCellRenderer} instead.
 */
@Deprecated
public class LegendTreeCellRenderer extends JPanel implements TreeCellRenderer {
  private static final long serialVersionUID = 1L;
  private JCheckBox _visibilityCheckBox;
  private JLabel _nodeLabel;

  /**
   * Instantiates a new legend tree cell renderer.
   */
  public LegendTreeCellRenderer() {
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    _visibilityCheckBox = new JCheckBox("");
    add(_visibilityCheckBox);

    _nodeLabel = new JLabel("New label");
    add(_nodeLabel);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.
   * swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
   */
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean selected, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {
    setBackground(selected ? SystemColor.textHighlight : tree
        .getBackground());
    _nodeLabel.setForeground(selected ? SystemColor.textHighlightText
        : SystemColor.textText);
    _visibilityCheckBox.setBackground(tree.getBackground());
    _visibilityCheckBox.setVisible(true);
    DefaultMutableTreeNode nodeToRender = (DefaultMutableTreeNode) value;
    Object userObject = nodeToRender.getUserObject();

    if (userObject instanceof Layer) {
      renderLayer((Layer) userObject);
    } else if (userObject instanceof LayerInfo) {
      renderSubLayer((LayerInfo) userObject, nodeToRender);
    } else if (userObject instanceof LayerLegendInfo) {
      renderLegendInfo((LayerLegendInfo) userObject);
    } else if (userObject instanceof LegendItemInfo) {
      renderLegendItem((LegendItemInfo) userObject);
    }

    return this;
  }

  /**
   * This method is used to render a tree node representing the legend for a
   * single sublayer or feature layer. The node will have an icon to represent
   * a sublayer and label set to the sublayer's name.
   * 
   * @param legendInfo
   *            the legend info represented by the node being rendered
   */
  private void renderLegendInfo(LayerLegendInfo legendInfo) {
    _visibilityCheckBox.setVisible(false);
    _nodeLabel.setText(legendInfo.getLayerName());
    _nodeLabel.setIcon(new ImageIcon(this.getClass().getResource(
        "/com/esri/client/toolkit/images/LayerServiceMap16.png")));
  }

  /**
   * This method renderer a single legend item entry by drawing an icon
   * created from the legend item's image and labelled with the item's label,
   * if any. Note that for WMS layers, the legend item will consist of one
   * image for all items in the legend with labels.
   * 
   * @param legendItem
   *            the legend item
   */
  private void renderLegendItem(LegendItemInfo legendItem) {
    ImageIcon imageIcon = null;
    _visibilityCheckBox.setVisible(false);
    _nodeLabel.setText(legendItem.getLabel());
    byte[] imageBytes = legendItem.getImageBytes();
    if (imageBytes != null) {
      ByteArrayInputStream bin = new ByteArrayInputStream(imageBytes);
      Base64InputStream b64in = new Base64InputStream(bin);
      try {
        BufferedImage decodedImage = ImageIO.read(b64in);
        if (decodedImage != null) {
          imageIcon = new ImageIcon(decodedImage);
        }
      } catch (IOException e) {
      }
    }
    _nodeLabel.setIcon(imageIcon);
  }

  /**
   * This method renders a node representing a layer with sublayers. If the
   * node represents a group layer, the node will be rendered with a group
   * layer icon. If it is not a group layer, the node will be rendered with a
   * sublayer icon. In both cases, the node will be labelled with the layer's
   * name. The node will also have a check box, the state of which is
   * determined by the current visibility of the associated layer or sublayer.
   * 
   * @param layerInfo
   *            the layer info
   * @param nodeToRender
   *            the node to render
   */
  private void renderSubLayer(LayerInfo layerInfo,
      DefaultMutableTreeNode nodeToRender) {
    DefaultMutableTreeNode firstChild = null;
    ImageIcon icon = null;

    if (nodeToRender.getChildCount() > 0) {
      firstChild = (DefaultMutableTreeNode) nodeToRender.getFirstChild();
    }

    if (firstChild != null
        && firstChild.getUserObject() instanceof LayerInfo) {
      // Node we are rendering must be a group layer as it
      // has child nodes that also contain LayerInfo instances
      icon = new ImageIcon(this.getClass().getResource(
          "/com/esri/client/toolkit/images/LayerGroup16.png"));
    } else {
      icon = new ImageIcon(this.getClass().getResource(
          "/com/esri/client/toolkit/images/LayerServiceMap16.png"));
    }
    _visibilityCheckBox.setSelected(layerInfo.isVisible());
    if (layerInfo instanceof WmsLayerInfo) {
      _nodeLabel.setText(((WmsLayerInfo) layerInfo).getDisplayName());
    } else {
      _nodeLabel.setText(layerInfo.getName());
    }
    _nodeLabel.setIcon(icon);
  }

  /**
   * This method renders the node for a top level layer. The icon used is
   * determined by the layer type and the label is the layer name. The node
   * will also have a check box, the state of which is determined by the
   * current visibility of the associated layer or sublayer.
   * 
   * @param layer
   *            the layer
   */
  private void renderLayer(Layer layer) {
    if (layer != null) {
      _visibilityCheckBox.setSelected(layer.isVisible());
      _nodeLabel.setText(layer.getName());
      ImageIcon icon = null;

      if (layer instanceof ArcGISFeatureLayer) {
        Type geometryType = ((ArcGISFeatureLayer) layer)
            .getGeometryType();
        if (geometryType != null) {
          switch (geometryType) {
          case POINT:
          case MULTIPOINT:
            icon = new ImageIcon(
                this.getClass()
                    .getResource(
                        "/com/esri/client/toolkit/images/LayerPoint16.png"));
            break;
          case LINE:
          case POLYLINE:
            icon = new ImageIcon(
                this.getClass()
                    .getResource(
                        "/com/esri/client/toolkit/images/LayerLine16.png"));
            break;
          case POLYGON:
            icon = new ImageIcon(
                this.getClass()
                    .getResource(
                        "/com/esri/client/toolkit/images/LayerPolygon16.png"));
            break;
          default:
            break;
          }
        } else {
          icon = new ImageIcon(
              this.getClass()
                  .getResource(
                      "/com/esri/client/toolkit/images/LayerGeneric16.png"));
        }
      } else if (layer instanceof GroupLayer) {
        icon = new ImageIcon(this.getClass().getResource(
            "/com/esri/client/toolkit/images/LayerGroup16.png"));
      } else {
        icon = new ImageIcon(
            this.getClass()
                .getResource(
                    "/com/esri/client/toolkit/images/LayerServiceMap16.png"));
      }
      _nodeLabel.setIcon(icon);
    }
  }

}
