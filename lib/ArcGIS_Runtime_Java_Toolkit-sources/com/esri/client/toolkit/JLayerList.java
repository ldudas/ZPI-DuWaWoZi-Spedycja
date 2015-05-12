/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.LayerEvent;
import com.esri.map.LayerListEventListenerAdapter;

/**
 * @deprecated From 10.2.3, use {@link JLayerList} instead.
 */
@Deprecated
public class JLayerList extends JPanel {

  private static final long serialVersionUID = 1L;

  private JMap jMap;
  private JList jList;
  private JScrollPane scrollPane;
  private DefaultListModel listModel;
  private JPanel editListItemPanel;

  private int _currentItemIndex = -1;

  public JLayerList(JMap map) {
    this.jMap = map;
    initGUI();
    
    jMap.getLayers().addLayerListEventListener(new LayerListEventListenerAdapter(){

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

  private void initGUI() {
    listModel = new DefaultListModel();
    jList = new JList(listModel);
    jList.setCellRenderer(new LayerCellRenderer());
    scrollPane = new JScrollPane(jList);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setMinimumSize(new Dimension(270, 50));
    add(scrollPane);
    initGuiListeners();
  }

  public void refresh() {
    listModel = new DefaultListModel();

    for (Layer curLayer : jMap.getLayers()) {
      listModel.insertElementAt(curLayer, 0);
    }
    jList.setModel(listModel);
    jList.validate();
  }

  private void initGuiListeners() {
    jList.addMouseMotionListener(new MouseMotionListener() {
      
      @Override
      public void mouseMoved(MouseEvent e) {
        getCellRendererForMouseOverItem(e.getPoint());
      }
      
      @Override
      public void mouseDragged(MouseEvent e) {}
    });
    jList.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent arg0) {
        // This will draw a functioning instance of the
        // cell renderer into the selected cell, allowing
        // the controls to be used.
        getCellRendererForSelectedItem();
      }
    });
  }

  private void getCellRendererForSelectedItem() {
    // Remove old item editor from list control
    if (editListItemPanel != null) {
      jList.remove(editListItemPanel);
      editListItemPanel.setVerifyInputWhenFocusTarget(false);
    }

    int selectedIndex = jList.getSelectedIndex();
    getCellRendererForGivenIndex(selectedIndex, jList.getSelectedValue());
    jList.validate();
    jList.repaint();
  }
  
  private void getCellRendererForMouseOverItem(Point mousePoint){
    int itemIndex = jList.locationToIndex(mousePoint);
    if(_currentItemIndex != itemIndex){
      _currentItemIndex = itemIndex;
      
      // Remove old item editor from list control
      if (editListItemPanel != null) {
        jList.remove(editListItemPanel);
      }
      
      getCellRendererForGivenIndex(itemIndex, jList.getModel().getElementAt(itemIndex));
      jList.validate();
      jList.repaint();
    }
  }

  /**
   * @param itemIndex
   */
  protected void getCellRendererForGivenIndex(int itemIndex, Object value) {
    if (itemIndex > -1) {
      // Get our cell renderer control
      LayerCellRenderer renderer = new LayerCellRenderer();
      editListItemPanel = (JPanel) renderer.getListCellRendererComponent(
          jList, value, itemIndex, jList.getSelectedIndex() == itemIndex, true);

      // Add it as a child of the list control
      jList.add(editListItemPanel);
      editListItemPanel.setBounds(this.jList.getCellBounds(itemIndex,
          itemIndex));
      editListItemPanel.setVisible(true);
    }
  }

  private class LayerCellRenderer extends JPanel implements ListCellRenderer {
    private static final long serialVersionUID = 1L;
    private JSlider jSlider;
    private JCheckBox jCheckBox;
    private boolean cellInitialised = false;
    private Layer layer;// member variable allow event handler's to access
              // the layer

    public LayerCellRenderer() {
      setOpaque(true);
      initGui();
    }

    private void initGui() {
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      jCheckBox = new JCheckBox();
      jCheckBox.setAlignmentX(LEFT_ALIGNMENT);
      jCheckBox.setSelected(true);

      jSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
      jSlider.setAlignmentX(LEFT_ALIGNMENT);
      jSlider.setPreferredSize(new Dimension(250, 20));
    }

    @Override
    public Component getListCellRendererComponent(JList jList,
        Object value, int index, boolean cellIsSelected,
        boolean cellHasFocus) {
      if (!cellInitialised) {
        add(jCheckBox);
        add(jSlider);
        jCheckBox.addItemListener(new ItemListener() {
          @Override
          public void itemStateChanged(ItemEvent event) {
            layer.setVisible(event.getStateChange() == ItemEvent.SELECTED);
          }

        });

        jSlider.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent event) {
            JSlider source = (JSlider) event.getSource();

            if (!source.getValueIsAdjusting()) {
              float opacity = ((float) source.getValue()) / 100;
              layer.setOpacity(opacity);
            }
          }
        });

        cellInitialised = true;
      }

      if (value == null)
        return this;

      layer = (Layer) value;
      jCheckBox.setText(layer.getName());
      jCheckBox.setSelected(layer.isVisible());
      jSlider.setValue((int) (layer.getOpacity() * 100));

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
      jCheckBox.setBackground(background);
      jCheckBox.setForeground(foreground);
      jSlider.setBackground(background);
      jSlider.setForeground(foreground);
    }
  }
}
