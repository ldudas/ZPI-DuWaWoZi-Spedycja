/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.editing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.esri.toolkit.PrototypeFeature;
import com.esri.toolkit.SelectLayersAction;
import com.esri.toolkit.overlays.DrawingOverlay;
import com.esri.toolkit.overlays.DrawingOverlay.DrawingMode;
import com.esri.toolkit.overlays.DrawingProperties;
import com.esri.toolkit.overlays.FeatureEditOverlay;
import com.esri.toolkit.overlays.FeatureEditOverlay.EDIT_TOOL;
import com.esri.toolkit.utilities.ExceptionHandler;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureTemplate.DRAWING_TOOL;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.map.FeatureLayer;
import com.esri.map.JMap;
import com.esri.map.Layer;
import com.esri.map.MapEvent;
import com.esri.map.MapEventListenerAdapter;

/**
 * An editing toolbar to help edit features in <code>FeatureLayer</code>s. Includes tools 
 * to select/unselect features, edit the vertices of a feature, and delete selected 
 * features. This toolbar can be used on its own, or alongside a {@link JTemplatePicker} 
 * for the ability to add features from feature templates.
 * 
 * @usage
 * <code><pre>
 * JEditToolsPicker editToolsPicker = new JEditToolsPicker(map);
 * // add the toolbar to your app, for example in a BorderLayout
 * jPanel.add(editToolsPicker, BorderLayout.NORTH);
 * 
 * // for use with a template picker:
 * JTemplatePicker jPicker = new JTemplatePicker(map);
 * jPicker.setWatchMap(true);
 * jPanel.add(jPicker, BorderLayout.WEST);
 * // set the template picker's overlay as the edit tools' overlay
 * editToolsPicker.setCreationOverlay(jPicker.getOverlay());
 * </pre></code>
 * 
 * @since 10.2.3
 */
public class JEditToolsPicker extends JToolBar {
    private class OverlayPropertyChangeListener implements PropertyChangeListener {

        /*
         * Overridden to allow us to set the drawing tools to match the current
         * template.
         *
         * (non-Javadoc)
         *
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
         * PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (event.getPropertyName().equals("drawingProperties")) {
                DrawingProperties drawingProperties = (DrawingProperties) event.getNewValue();
                updateDrawingTools(drawingProperties.getDrawingMode(),
                    drawingProperties.getRenderer(),
                    drawingProperties.getAttributes());
            }
        }
    }

    private static final long serialVersionUID = 1L;
    private DrawingOverlay _createOverlay;

    private HashMap<FeatureTemplate.DRAWING_TOOL, CreationToolAction> _creationToolsMap =
        new HashMap<FeatureTemplate.DRAWING_TOOL, CreationToolAction>();
    private int _fixedButtonsCount;
    private OverlayPropertyChangeListener _overlayPropertyChangeListener = new OverlayPropertyChangeListener();
    private ButtonGroup _createButtonGroup = new ButtonGroup();
    protected boolean _handlingEditToolAction;
    private final ButtonGroup _layersButtonGroup = new ButtonGroup();
    private JToggleButton _btnShowSelectedLayers;
    private SelectLayersAction _selectLayersAction;
    private FeatureEditOverlay _editOverlay;
    private Dimension _separatorSize;
    private JMap _map;
    private final Point tooltipLocation = new Point(0, -14);

    /**
     * Launch the application.
     * @param args input arguments.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final JDialog dialog = new JDialog();
                    dialog.setBounds(100, 100, 450, 123);
                    dialog.setResizable(true);
                    final JEditToolsPicker picker = new JEditToolsPicker();
                    dialog.getContentPane().setLayout(
                            new BoxLayout(dialog.getContentPane(), BoxLayout.X_AXIS));
                    dialog.getContentPane().add(picker);

                    picker.addComponentListener(new ComponentListener() {

                        @Override
                        public void componentShown(ComponentEvent arg0) {
                        }

                        @Override
                        public void componentResized(ComponentEvent arg0) {
                            dialog.setResizable(true);
                            Rectangle toolbarSize = picker.getBounds();
                            dialog.setSize(new Dimension(
                                    (int) (toolbarSize.getWidth()),
                                    (int) (toolbarSize.getHeight())));
                            dialog.setResizable(false);
                        }

                        @Override
                        public void componentMoved(ComponentEvent arg0) {
                        }

                        @Override
                        public void componentHidden(ComponentEvent arg0) {
                        }
                    });
                    picker.setFloatable(false);
                    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    picker.setUp(DrawingMode.POLYGON, new SimpleFillSymbol(Color.RED), null);
                    dialog.pack();
                    dialog.setVisible(true);
                } catch (Exception e) {
                    ExceptionHandler.handleException(null, e);
                }
            }
        });
    }

    /**
     * Creates an edit tools picker instance. Call {@link #setMap(JMap)} to 
     * associate it to a map.
     */
    public JEditToolsPicker() {

        ToolTipManager.sharedInstance().setInitialDelay(100);

        setMargin(new Insets(5, 3, 5, 3));
        _editOverlay = new FeatureEditOverlay();

        setupLayerSelectionMode();
        setupSelectionButtons();
        addSeparator(_separatorSize);
        setupDeleteButton();
        addSeparator(_separatorSize);
        setupEditButtons();
        _fixedButtonsCount = getComponentCount();

        initCreationToolsMap();

        addComponentListener(new ComponentListener() {

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentResized(ComponentEvent e) {}

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {
                if(_editOverlay != null){
                    _editOverlay.setActive(false);
                }
            }
        });
    }

    /**
     * Creates a new instance associated with the specified map.
     * 
     * @param map map to which this tools picker will be attached to.
     * @throws IllegalArgumentException if the map is null
     */
    public JEditToolsPicker(JMap map){
        this();
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        setMap(map);
    }

    /**
     * Sets the current drawing properties. The set of options in the toolbar will change depending
     * on the drawing tool. These changes are propagated to the underlying overlay.
     * @param mode drawing mode.
     * @param renderer renderer to be used.
     * @param attributes graphic attributes.
     */
    public void setUp(DrawingMode mode, Renderer renderer,
        Map<String, Object> attributes) {
        if (_createOverlay != null) {
            _createOverlay.setUp(mode, renderer, attributes);
        }
    }

    /**
     * Sets the current drawing properties. The set of options in the toolbar will change depending
     * on the drawing tool. These changes are propagated to the underlying overlay.
     * @param mode drawing mode.
     * @param symbol symbol to be used for drawing.
     * @param attributes graphic attributes.
     */
    public void setUp(DrawingMode mode, Symbol symbol,
        Map<String, Object> attributes) {
        if (_createOverlay != null) {
            _createOverlay.setUp(mode, new SimpleRenderer(symbol), attributes);
        }
    }

    /**
     * Sets the current drawing properties. The set of options in the toolbar will change depending
     * on the drawing tool. These changes are propagated to the underlying overlay.
     * @param prototypeFeature graphic from which drawing properties will be derived.
     */
    public void setUp(PrototypeFeature prototypeFeature) {
        if (_createOverlay != null) {
            _createOverlay.setUp(prototypeFeature);
        }
    }

    /**
     * Returns the overlay on which edits can be performed.
     * @return the edit overlay.
     */
    public FeatureEditOverlay getEditOverlay(){
        return _editOverlay;
    }

    /**
     * Sets the overlay on which drawing is performed.
     * @param overlay the drawing overlay.
     */
    public void setCreationOverlay(DrawingOverlay overlay) {
      if(overlay != null){
        if (_createOverlay != null) {
            // Remove property change listener
            _createOverlay.removePropertyChangeListener(_overlayPropertyChangeListener);
        }
        _createOverlay = overlay;

        // Add a property change listener so we can update our tools to match
        // the current template
        _createOverlay.addPropertyChangeListener(_overlayPropertyChangeListener);

        if(_map == null){
            setMap(_createOverlay.getMap());
        }

        // Set the overlay in all the tools
        for (CreationToolAction curAction : _creationToolsMap.values()) {
            curAction.setOverlay(_createOverlay);
        }
      }
    }

    private void updateDrawingTools(DrawingMode drawingMode,
            Renderer renderer,
            Map<String, Object> attributes) {
        if (!_handlingEditToolAction) {
            // We didn't change the drawing tool so update toolbar to
            // match the change triggered externally.
            CreationToolAction[] toolsToShow = getToolsToShow(drawingMode.getDrawingTool());
            if (toolsToShow != null) {
                for (CreationToolAction tool : toolsToShow) {
                    tool.setDrawingProperties(drawingMode.getGeometryType(), renderer, attributes);
                }
                setToolsInToolbar(toolsToShow);
            }
        } else {
            _handlingEditToolAction = false;
        }

        // change in drawing tool affects the UI.
        repaint();
    }

    private CreationToolAction[] getToolsToShow(DRAWING_TOOL drawingTool) {
        CreationToolAction[] toolsToShow = null;
        switch (drawingTool) {
        case POINT:
            toolsToShow = new CreationToolAction[] {
                _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.POINT)
                };
            break;
        case LINE:
            toolsToShow = new CreationToolAction[] {
                _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.LINE),
                _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.FREEHAND),
                _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.RECTANGLE)
                };
            break;
        case POLYGON:
            toolsToShow = new CreationToolAction[] {
                    _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.POLYGON),
                    // comment tools that don't have implementation yet
                    // _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.AUTO_COMPLETE_POLYGON),
                    _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.FREEHAND),
                    _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.RECTANGLE)
                    };
                break;
        case FREEHAND:
            toolsToShow = new CreationToolAction[] {
                _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.FREEHAND),
                };
            break;
        case AUTO_COMPLETE_POLYGON:
            throw new RuntimeException(drawingTool + " is not supported yet.");
            // comment tools with no implementation
            /*
            setToolsInToolbar(new CreationToolAction[] {
                _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.AUTO_COMPLETE_POLYGON)
                });
            break;*/
        case CIRCLE:
            toolsToShow = new CreationToolAction[] {
                _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.CIRCLE)
                };
            break;
        case ELLIPSE:
            toolsToShow = new CreationToolAction[] {
                _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.ELLIPSE)
                };
            break;
        case RECTANGLE:
            toolsToShow = new CreationToolAction[] {
                _creationToolsMap.get(FeatureTemplate.DRAWING_TOOL.RECTANGLE)
                };
            break;
        case NONE:
            toolsToShow = new CreationToolAction[] {};
            break;
          default:
            break;
        }
        return toolsToShow;
    }

    /**
     * Returns the map associated with this tools picker.
     * @return the map.
     */
    public JMap getMap() {
        return _map;
    }

    /**
     * Associates this tools picker to the map.
     * @param map map to which the tools picker will be attached to.
     */
    public void setMap(JMap map) {
      if (map != null) {
        _map = map;
        _map.addMapEventListener(new MapEventListenerAdapter() {

          @Override
          public void mapReady(MapEvent event) {
            _map.addMapOverlay(_editOverlay);
            _editOverlay.setActive(false);
          }

        });
        if (_selectLayersAction != null) {
          _selectLayersAction.setMap(_map);
        }
      }
    }

    protected void updateShowSelectedLayers(boolean selected) {
        _btnShowSelectedLayers.setVisible(selected);
    }

    /**
     *
     */
    protected void setupEditButtons() {
        // Add vertex edit button
        JToggleButton vertexEditButton = new JToggleButton(new EditToolAction("EditVertices",
                "Edit vertices", new ImageIcon(getClass().getResource(
                "/com/esri/client/toolkit/images/EditingEditVertices16.png")), _editOverlay,
        EDIT_TOOL.VertexEdit)) {
            /**
           * 
           */
          private static final long serialVersionUID = 1L;

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return tooltipLocation;
            }
        };
        vertexEditButton.setHideActionText(true);
        add(vertexEditButton);
        _createButtonGroup.add(vertexEditButton);
    }

    protected void setupDeleteButton() {
        // button to delete selected features
        JButton deleteButton = new JButton(_editOverlay.new DeleteSelectedFeaturesAction()) {
          private static final long serialVersionUID = 1L;

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return tooltipLocation;
            }
        };
        add(deleteButton);
    }

    protected void setupSelectionButtons() {

        // Get size of separators
        _separatorSize = new Dimension(_btnShowSelectedLayers.getSize());

        // Add select button
        JToggleButton selectButton = new JToggleButton(new EditToolAction("AddToSelection",
                "Add to selection", new ImageIcon(getClass().getResource(
                        "/com/esri/client/toolkit/images/SelectionSelectTool16.png")), _editOverlay,
                EDIT_TOOL.SelectionAdd)) {
          
            private static final long serialVersionUID = 1L;

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return tooltipLocation;
            }
        };
        selectButton.setHideActionText(true);
        add(selectButton);
        _createButtonGroup.add(selectButton);

        // Add deselect button
        JToggleButton deselectButton = new JToggleButton(new EditToolAction("RemoveFromSelection",
                "Remove from selection", new ImageIcon(getClass().getResource(
                "/com/esri/client/toolkit/images/SelectionSelectUnselect16.png")), _editOverlay,
        EDIT_TOOL.SelectionRemove)) {
          private static final long serialVersionUID = 1L;

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return tooltipLocation;
            }
        };
        deselectButton.setHideActionText(true);
        add(deselectButton);
        _createButtonGroup.add(deselectButton);

        // Add clear selection button
        JButton clearSelectionButton = new JButton(_editOverlay.new ClearSelectionAction()) {
          private static final long serialVersionUID = 1L;

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return tooltipLocation;
            }
        };
        add(clearSelectionButton);
    }

    protected void setupLayerSelectionMode() {
        // Add panel for layer selection mode radio buttons
        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add all layers button
        JRadioButton rdbtnAllLayers = new JRadioButton("All Layers");
        _layersButtonGroup.add(rdbtnAllLayers);
        rdbtnAllLayers.setSelected(true);
        rdbtnAllLayers.setMargin(new Insets(0, 2, 0, 2));
        panel.add(rdbtnAllLayers);

        // Add selected layers button
        JRadioButton rdbtnSelectedlayers = new JRadioButton("Selected Layers");
        _layersButtonGroup.add(rdbtnSelectedlayers);
        rdbtnSelectedlayers.setMargin(new Insets(0, 2, 0, 2));
        rdbtnSelectedlayers.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                boolean selected = ((JRadioButton) arg0.getSource()).isSelected();
                updateShowSelectedLayers(selected);

                List<Layer> activeLayers = null;

                if(selected){
                    activeLayers = _selectLayersAction.getSelectedLayers();
                }

                if(_editOverlay != null){
                    _editOverlay.setActiveLayers(activeLayers);
                }
            }
        });

        panel.add(rdbtnSelectedlayers);

        // Add control for selecting layers
        _btnShowSelectedLayers = new JToggleButton() {
          private static final long serialVersionUID = 1L;

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return tooltipLocation;
            }
        };
        _selectLayersAction = new SelectLayersAction("ShowSelectedLayers",
                new ImageIcon(getClass().getResource(
                        "/com/esri/client/toolkit/images/LayerSelect16.png")), "Show selected layers");
        _selectLayersAction.setFilterClass(FeatureLayer.class);
        _selectLayersAction.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(SelectLayersAction.SELECTED_LAYERS_CHANGED_KEY)){
                    handleLayerSelectionChanged((SelectLayersAction)evt.getSource());
                }
            }
        });
        _btnShowSelectedLayers.setAction(_selectLayersAction);
        _btnShowSelectedLayers.setHideActionText(true);
        _btnShowSelectedLayers.setVisible(false);
        add(_btnShowSelectedLayers);
    }

    protected void handleLayerSelectionChanged(SelectLayersAction source) {
        if(_editOverlay != null){
            _editOverlay.setActiveLayers(source.getSelectedLayers());
        }
    }

    private void initCreationToolsMap() {
        Class<? extends JEditToolsPicker> thisClass = getClass();

        // do not include tools that don't have implementation yet.
        // when included, uncomment corresponding getter in setDrawingTool()
        /*
        _creationToolsMap
                .put(DRAWING_TOOL.AUTO_COMPLETE_POLYGON,
                        new CreationToolAction(
                                "Autocomplete polygon",
                                "Create an autocomplete polygon",
                                new ImageIcon(
                                        thisClass
                                                .getResource("/com/esri/client/toolkit/images/EditingAutoCompletePolygonTool16.png")),
                                _createOverlay,
                                DRAWING_TOOL.AUTO_COMPLETE_POLYGON));*/
        _creationToolsMap
                .put(DRAWING_TOOL.CIRCLE,
                        new CreationToolAction(
                                "Create circle",
                                "Create a circle",
                                new ImageIcon(
                                        thisClass
                                                .getResource("/com/esri/client/toolkit/images/EditingCircleTool16.png")),
                                _createOverlay,
                                DRAWING_TOOL.CIRCLE));
        _creationToolsMap
                .put(DRAWING_TOOL.ELLIPSE,
                        new CreationToolAction(
                                "Create ellipse",
                                "Create an ellipse",
                                new ImageIcon(
                                        thisClass
                                                .getResource("/com/esri/client/toolkit/images/EditingEllipseTool16.png")),
                                _createOverlay,
                                DRAWING_TOOL.ELLIPSE));
        _creationToolsMap
                .put(DRAWING_TOOL.FREEHAND,
                        new CreationToolAction(
                                "Create freehand feature",
                                "Create a freehand feature",
                                new ImageIcon(
                                        thisClass
                                                .getResource("/com/esri/client/toolkit/images/EditingFreehandTool16.png")),
                                _createOverlay,
                                DRAWING_TOOL.FREEHAND));
        _creationToolsMap
                .put(DRAWING_TOOL.LINE,
                        new CreationToolAction(
                                "Create line",
                                "Create a line",
                                new ImageIcon(
                                        thisClass
                                                .getResource("/com/esri/client/toolkit/images/EditingLineTool16.png")),
                                _createOverlay,
                                DRAWING_TOOL.LINE));
        _creationToolsMap
                .put(DRAWING_TOOL.POINT,
                        new CreationToolAction(
                                "Create point",
                                "Create a point",
                                new ImageIcon(
                                        thisClass
                                                .getResource("/com/esri/client/toolkit/images/EditingPointTool16.png")),
                                _createOverlay,
                                DRAWING_TOOL.POINT));
        _creationToolsMap
                .put(DRAWING_TOOL.POLYGON,
                        new CreationToolAction(
                                "Create polygon",
                                "Create a polygon",
                                new ImageIcon(
                                        thisClass
                                                .getResource("/com/esri/client/toolkit/images/EditingPolygonTool16.png")),
                                _createOverlay,
                                DRAWING_TOOL.POLYGON));
        _creationToolsMap
                .put(DRAWING_TOOL.RECTANGLE,
                        new CreationToolAction(
                                "Create rectangle",
                                "Create a rectangle",
                                new ImageIcon(
                                        thisClass
                                                .getResource("/com/esri/client/toolkit/images/EditingRectangleTool16.png")),
                                _createOverlay,
                                DRAWING_TOOL.RECTANGLE));
    }

    private void setToolsInToolbar(CreationToolAction[] toolsToAdd) {
        int componentCount = getComponentCount();
        for (int count = _fixedButtonsCount; count < componentCount; ++count) {
            Component componentAtIndex = getComponentAtIndex(_fixedButtonsCount);
            if (componentAtIndex instanceof AbstractButton) {
                _createButtonGroup.remove((AbstractButton) componentAtIndex);
            }
            remove(_fixedButtonsCount);
        }

        addSeparator(_separatorSize);

        for (CreationToolAction curAction : toolsToAdd) {
            JToggleButton newButton = new JToggleButton(curAction) {

              private static final long serialVersionUID = 1L;

                @Override
                public Point getToolTipLocation(MouseEvent e) {
                    return tooltipLocation;
                }
            };
            newButton.setHideActionText(true);

            // Add listeners for each action so we can ignore tool change
            // events we've triggered ourselves
            newButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    _handlingEditToolAction = true;
                }
            });

            add(newButton);
            _createButtonGroup.add(newButton);
        }

        if (_createOverlay != null) {
            CreationToolAction editToolAction = _creationToolsMap
                    .get(_createOverlay.getDrawingMode().getDrawingTool());
            if (editToolAction != null) {
                editToolAction.setChecked(true);
            }
        }
    }
}