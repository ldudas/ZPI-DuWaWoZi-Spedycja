/* Copyright 2014 Esri
 
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.editing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.esri.client.toolkit.overlays.FeatureEditOverlay;

/**
 * This class implements an action that is used in conjunction with a
 * {@link com.esri.client.toolkit.overlays.FeatureEditOverlay FeatureEditOverlay}
 * instance to set which tool will be used to edit features. This action can be
 * added to any Swing component that supports actions. An instance of this class
 * will have a
 * {@link com.esri.client.toolkit.overlays.FeatureEditOverlay.EDIT_TOOL
 * EDIT_TOOL} associated with it. When the action is performed, the EDIT_TOOL
 * will be set in the FeatureEditOverlay.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.editing.EditToolAction} instead.
 */
@Deprecated
public class EditToolAction extends AbstractAction {
  private static final long serialVersionUID = 1L;
  private FeatureEditOverlay _overlay = null;
  private FeatureEditOverlay.EDIT_TOOL _editTool;

  /**
   * Default constructor
   */
  public EditToolAction() {
  }

  /**
   * Create a new instance with the given tooltip text, feature creation
   * overlay and drawing tool.
   * 
   * @param tooltip
   *            Tooltip text to display when the mouse passes over the
   *            associated control.
   * @param overlay
   *            Feature edit overlay that this class will set the current tool
   *            in.
   * @param editTool
   *            Edit tool associated with this instance.
   */
  public EditToolAction(String tooltip, FeatureEditOverlay overlay,
      FeatureEditOverlay.EDIT_TOOL editTool) {
    init(overlay, editTool, tooltip);
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
   *            Feature edit overlay that this class will set the current tool
   *            in.
   * @param editTool
   *            Edit tool associated with this instance.
   */
  public EditToolAction(String name, String tooltip,
      FeatureEditOverlay overlay, FeatureEditOverlay.EDIT_TOOL editTool) {
    super(name);
    init(overlay, editTool, tooltip);
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
   *            Feature edit overlay that this class will set the current tool
   *            in.
   * @param editTool
   *            Edit tool associated with this instance.
   */
  public EditToolAction(String name, String tooltip, Icon icon,
      FeatureEditOverlay overlay, FeatureEditOverlay.EDIT_TOOL editTool) {
    super(name, icon);
    init(overlay, editTool, tooltip);
  }

  /**
   * Get the feature edit overlay we are setting the current tool in.
   * 
   * @return A feature edit overlay.
   */
  public FeatureEditOverlay getOverlay() {
    return _overlay;
  }

  /**
   * Set the feature edit overlay we will be setting the current tool in.
   * 
   * @param overlay
   *            A feature edit overlay.
   */
  public void setOverlay(FeatureEditOverlay overlay) {
    _overlay = overlay;
  }

  /**
   * Get the edit tool associated with this instance.
   * 
   * @return An edit tool
   */
  public FeatureEditOverlay.EDIT_TOOL getEditTool() {
    return _editTool;
  }

  /**
   * Set the edit tool associated with this instance.
   * 
   * @param editTool
   *            An edit tool
   */
  public void setEditTool(FeatureEditOverlay.EDIT_TOOL editTool) {
    _editTool = editTool;
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
   * This override is used to set the edit tool associated with this instance
   * in the associated feature edit overlay.
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
      if (_overlay != null && _editTool != null) {
        _overlay.setEditTool(_editTool);
      }
    } else {
      setChecked(false);
    }
  }

  /**
   * Set up this instance with the given feature edit overlay, edit tool and
   * tooltip text.
   * 
   * @param overlay
   *            Feature creation overlay that this class will set the current
   *            tool in.
   * @param editTool
   *            Edit tool associated with this instance.
   * @param tooltip
   *            Tooltip text to display when the mouse passes over the
   *            associated control.
   */
  protected void init(FeatureEditOverlay overlay,
      FeatureEditOverlay.EDIT_TOOL editTool, String tooltip) {
    _overlay = overlay;
    _editTool = editTool;
    putValue(SHORT_DESCRIPTION, tooltip);
    boolean selected = false;
    if (_overlay != null && _overlay.getEditTool().equals(editTool)) {
      selected = true;
    }
    putValue(SELECTED_KEY, selected);
  }

}
