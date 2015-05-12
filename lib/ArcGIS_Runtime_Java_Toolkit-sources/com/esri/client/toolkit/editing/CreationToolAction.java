/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.editing;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.esri.client.toolkit.overlays.DrawingOverlay;
import com.esri.client.toolkit.overlays.DrawingOverlay.DrawingMode;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.renderer.Renderer;

/**
 * This class implements an action that is used in conjunction with a
 * {@link com.esri.client.toolkit.overlays.DrawingOverlay
 * FeatureCreationOverlay} instance to set which tool will be used to create
 * features. This action can be added to any Swing component that supports
 * actions. An instance of this class will have a
 * {@link com.esri.core.map.FeatureTemplate.DRAWING_TOOL DRAWING_TOOL}
 * associated with it. When the action is performed, the DRAWING_TOOL will be
 * set in the FeatureCreationOverlay.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.editing.CreationToolAction} instead.
 */
@Deprecated
public class CreationToolAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private DrawingOverlay _overlay = null;
    private FeatureTemplate.DRAWING_TOOL _drawingTool;
    private Geometry.Type _geometryType;
    private Renderer _renderer;
    private Map<String, Object> _attributes;

    /**
     * Default constructor
     */
    public CreationToolAction() {
    }

    /**
     * Create a new instance with the given tooltip text, feature creation
     * overlay and drawing tool.
     *
     * @param tooltip
     *            Tooltip text to display when the mouse passes over the
     *            associated control.
     * @param overlay
     *            Feature creation overlay that this class will set the current
     *            tool in.
     * @param drawingTool
     *            Drawing tool associated with this instance.
     */
    public CreationToolAction(String tooltip, DrawingOverlay overlay,
            FeatureTemplate.DRAWING_TOOL drawingTool) {
        init(overlay, drawingTool, tooltip);
    }

    /**
     * Create a new instance with the given name, tooltip text , feature
     * creation overlay and drawing tool.
     *
     * @param name
     *            Name of action.
     * @param tooltip
     *            Tooltip text to display when the mouse passes over the
     *            associated control.
     * @param overlay
     *            Feature creation overlay that this class will set the current
     *            tool in.
     * @param drawingTool
     *            Drawing tool associated with this instance.
     */
    public CreationToolAction(String name, String tooltip,
            DrawingOverlay overlay,
            FeatureTemplate.DRAWING_TOOL drawingTool) {
        super(name);
        init(overlay, drawingTool, tooltip);
    }

    /**
     * Create a new instance with the given name, tooltip text, icon, feature
     * creation overlay and drawing tool.
     *
     * @param name
     *            Name of action.
     * @param tooltip
     *            Tooltip text to display when the mouse passes over the
     *            associated control.
     * @param icon
     *            Icon to display in the associated control
     * @param overlay
     *            Feature creation overlay that this class will set the current
     *            tool in.
     * @param drawingTool
     *            Drawing tool associated with this instance.
     */
    public CreationToolAction(String name, String tooltip, Icon icon,
            DrawingOverlay overlay,
            FeatureTemplate.DRAWING_TOOL drawingTool) {
        super(name, icon);
        init(overlay, drawingTool, tooltip);
    }

    /**
     * Get the feature creation overlay we are setting the current tool in.
     *
     * @return A feature creation overlay.
     */
    public DrawingOverlay getOverlay() {
        return _overlay;
    }

    /**
     * Set the feature creation overlay we will be setting the current tool in.
     *
     * @param overlay
     *            A feature creation overlay.
     */
    public void setOverlay(DrawingOverlay overlay) {
        _overlay = overlay;
    }

    /**
     * Get the drawing tool associated with this instance.
     *
     * @return A drawing tool
     */
    public FeatureTemplate.DRAWING_TOOL getDrawingTool() {
        return _drawingTool;
    }

    /**
     * Set the drawing tool associated with this instance.
     *
     * @param drawingTool
     *            A drawing tool
     */
    public void setDrawingTool(FeatureTemplate.DRAWING_TOOL drawingTool) {
        _drawingTool = drawingTool;
    }

    /**
     * Indicates whether or not this action is checked.
     *
     * @return True if this instance is checked, false otherwise.
     */
    public boolean isChecked() {
        return (Boolean) getValue(SELECTED_KEY);
    }

    /**
     * Set whether or not this instance is checked. Setting this will also
     * update the checked state of the associated control if appropriate.
     *
     * @param checked
     *            True to set checked, false otherwise.
     */
    public void setChecked(boolean checked) {
        putValue(SELECTED_KEY, checked);

    }

    public void setDrawingProperties(
        Geometry.Type geometryType,
        Renderer renderer,
        Map<String, Object> attributes) {
        if (_overlay != null) {
            this._geometryType = geometryType;
            this._renderer = renderer;
            this._attributes = attributes;
        }
    }

    /*
     * This override allows us to catch calls from the control hosting this
     * action that are updating the selected state of the control. We want
     * to deactivate our overlay if this control is deselected.
     *
     * (non-Javadoc)
     * @see javax.swing.AbstractAction#putValue(java.lang.String, java.lang.Object)
     */
    @Override
    public void putValue(String key, Object newValue) {
        super.putValue(key, newValue);

        // Is our parent changing the selected state of its gui?
        if(key.equals(AbstractAction.SELECTED_KEY) && _overlay != null){
            if(!(Boolean)newValue){
                // Setting selected state to false so we want to deactivate
                // our overlay. It will be reactivated if another edit
                // tool is activated.
                _overlay.setActive(false);
            }
        }
    }

    /*
     * This override is used to set the drawing tool associated with this
     * instance in the associated feature creation overlay.
     *
     * (non-Javadoc)
     *
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean selected = isChecked();

        if (selected) {
            if (_overlay != null && _drawingTool != null && _geometryType != null) {
                DrawingMode drawingMode = DrawingMode.get(_drawingTool, _geometryType);
                _overlay.setUp(drawingMode, _renderer, _attributes);
            }
        } else {
            setChecked(false);
        }
    }

    /**
     * Set up this instance with the given feature creation overlay, drawing
     * tool and tooltip text.
     *
     * @param overlay
     *            Feature creation overlay that this class will set the current
     *            tool in.
     * @param drawingTool
     *            Drawing tool associated with this instance.
     * @param tooltip
     *            Tooltip text to display when the mouse passes over the
     *            associated control.
     */
    protected void init(DrawingOverlay overlay,
            FeatureTemplate.DRAWING_TOOL drawingTool, String tooltip) {
        _overlay = overlay;
        _drawingTool = drawingTool;
        putValue(SHORT_DESCRIPTION, tooltip);
        boolean selected = false;
        if (_overlay != null && _overlay.getDrawingMode().getDrawingTool().equals(drawingTool)) {
            selected = true;
        }
        putValue(SELECTED_KEY, selected);
    }

}
