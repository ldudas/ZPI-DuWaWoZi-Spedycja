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
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.LayerEvent;
import com.esri.map.LayerListEventListenerAdapter;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListenerAdapter;

/**
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.SelectLayersAction} instead. 
 */
@Deprecated
public class SelectLayersAction extends AbstractAction {
    class LayerListCellRenderer extends JLabel implements ListCellRenderer {
        private static final long serialVersionUID = 1L;

        public LayerListCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            String layerName = ((Layer) value).getName();

            if(layerName == null || layerName == ""){
                layerName = "Layer";
            }
            setText(layerName);

            Color background;
            Color foreground;

            if (isSelected) {
                background = SystemColor.textHighlight;
                foreground = SystemColor.textHighlightText;
            } else {
                background = SystemColor.text;
                foreground = SystemColor.textText;
            }

            setBackground(background);
            setForeground(foreground);

            return this;
        }

    }

    /**
     * This class extends JDialog to create a control that acts a bit
     * like the list control portion of a combo box. The list will show
     * the layers currently referenced by the parent action and allow
     * the user to select one or more of them. The control can be hidden
     * either by clicking (unchecking) the parent control or simply by
     * losing focus.
     *
     */
    public class JLayerListControl extends JDialog {
        private static final long serialVersionUID = 1L;
        private JList _layerListControl;
        private JComponent _parentComponent;

        public JLayerListControl(JComponent parentComponent) {
            super((Window) parentComponent.getTopLevelAncestor(),
                    Dialog.ModalityType.MODELESS);
            _parentComponent = parentComponent;

            // We don't want any border at all.
            setUndecorated(true);

            // Create a panel containing a scroll pane that will contain
            // the list of layers.
            Container contentPane = getContentPane();
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            JScrollPane scrollPanel = new JScrollPane();
            panel.add(scrollPanel);
            _layerListControl = new JList();
            _layerListControl.setCellRenderer(new LayerListCellRenderer());
            _layerListControl.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            scrollPanel.setViewportView(_layerListControl);
            contentPane.add(panel);

            // Set the control to be focusable so we can detect loss of
            // focus.
            setFocusable(true);

            // Add a window focus listener to allow us to hide the dialog
            // when it loses focus in the same way as the list control of
            // a combo box.
            addWindowFocusListener(new WindowFocusListener() {

                @Override
                public void windowLostFocus(WindowEvent e) {
                    setVisible(false);

                }

                @Override
                public void windowGainedFocus(WindowEvent e) {}
            });

            // Add a window listener to allow us to set the bounds of the
            // dialog to the current location of the control our action is
            // associated with.
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent e) {
                    positionRelativeToParent();
                }
            });
        }

        public void setLayers(ListModel model) {
            _layerListControl.setModel(model);
        }

        public List<Layer> getSelectedLayers(){
            ArrayList<Layer> retVal = new ArrayList<Layer>();

            for(Object curLayer: _layerListControl.getSelectedValues()){
                retVal.add((Layer) curLayer);
            }

            return Collections.unmodifiableList(retVal);
        }

        /**
         * @param parentComponent
         */
        protected void positionRelativeToParent() {
            Rectangle parentBounds = _parentComponent.getBounds();
            Point parentBottomLeft = new Point((int) (parentBounds.getX()),
                    (int) (parentBounds.getMaxY()));
            SwingUtilities.convertPointToScreen(parentBottomLeft,
                    _parentComponent.getParent());
            setBounds((int) (parentBottomLeft.getX()),
                    (int) (parentBottomLeft.getY()), 200, 100);
        }

    }

    private static final long serialVersionUID = 1L;
    public static final String SELECTED_LAYERS_CHANGED_KEY = "SelectedLayersChanged";
    private JMap _map;
    private DefaultListModel _layerList = new DefaultListModel();
    private Class<?> _filterClass = null;
    private boolean _clicked = false;
    private JLayerListControl _layerListControl;
    protected boolean _layerListLostFocus = false;

    public SelectLayersAction(String name) {
        super(name);
        putValue(SELECTED_KEY, _clicked);
    }

    public SelectLayersAction(String name, Icon icon) {
        super(name, icon);
        putValue(SELECTED_KEY, _clicked);
    }

    public SelectLayersAction(String name, Icon icon, String tooltip) {
        super(name, icon);
        putValue(SHORT_DESCRIPTION, tooltip);
        putValue(SELECTED_KEY, _clicked);
    }

    public JMap getMap() {
        return _map;
    }

    public void setMap(JMap map) {
        _map = map;
        _layerList.clear();
        _map.addMapEventListener(new MapEventListenerAdapter() {
            @Override
            public void mapReady(MapEvent event) {
                handleMapReady(event);
            }
        });

        _map.getLayers().addLayerListEventListener(new LayerListEventListenerAdapter(){

            @Override
            public void layerAdded(LayerEvent event) {
                handleLayerAdded(event);
            }

            @Override
            public void layerRemoved(LayerEvent event) {
                handleLayerRemoved(event);
            }

            @Override
            public void multipleLayersRemoved(LayerEvent event) {
                handleMultipleLayersRemoved(event);
            }

        });
    }

    public Class<?> getFilterClass() {
        return _filterClass;
    }

    public void setFilterClass(Class<?> filterClass) {
        _filterClass = filterClass;
    }

    public List<Layer> getSelectedLayers(){
        List<Layer> selectedLayers = null;

        if(_layerListControl != null){
            selectedLayers = _layerListControl.getSelectedLayers();
        }else{
            selectedLayers = Collections.unmodifiableList(new ArrayList<Layer>());
        }

        return selectedLayers;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (!_clicked && !_layerListLostFocus) {
            if (_layerListControl == null) {
                _layerListControl = new JLayerListControl(
                        (JComponent) event.getSource());
                _layerListControl
                        .setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                _layerListControl.setLayers(_layerList);

                _layerListControl.addWindowFocusListener(new WindowFocusListener() {

                    @Override
                    public void windowLostFocus(WindowEvent e) {
                        putValue(SELECTED_KEY, false);

                        // Indicate that the layer selection has changed.
                        // Property change notification will let any listeners
                        // know that this has changed so they can query the
                        // current layer selection.
                        putValue(SELECTED_LAYERS_CHANGED_KEY, getSelectedLayers().toString());
                        _clicked = false;
                        _layerListLostFocus = true;
                    }

                    @Override
                    public void windowGainedFocus(WindowEvent e) {}
                });
            }
            _layerListControl.setVisible(true);

            _clicked = true;
        } else {
            putValue(SELECTED_KEY, false);
            _layerListControl.setVisible(false);
            _layerListLostFocus = false;
            _clicked = false;
        }
    }

    protected void handleMapReady(MapEvent event) {
        for (Layer curLayer : _map.getLayers()) {
            if ((_filterClass == null)
                    || curLayer.getClass().equals(_filterClass)) {
                _layerList.addElement(curLayer);
            }
        }
    }

    protected void handleLayerRemoved(LayerEvent event) {
        _layerList.removeElement(event.getChangedLayer());
    }

    protected void handleLayerAdded(LayerEvent event) {
        Layer curLayer = event.getChangedLayer();
        if ((_filterClass == null) || curLayer.getClass().equals(_filterClass)) {
            _layerList.addElement(curLayer);
        }
    }

    protected void handleMultipleLayersRemoved(LayerEvent event) {
        for(Layer curLayer: event.getChangedLayers().values()){
            _layerList.removeElement(curLayer);
        }
    }

}
