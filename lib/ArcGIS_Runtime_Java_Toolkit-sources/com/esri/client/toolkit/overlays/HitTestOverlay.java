/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.overlays;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.table.TableException;
import com.esri.map.FeatureLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.Layer;
import com.esri.map.MapOverlay;
import com.esri.client.toolkit.overlays.HitTestEvent;
import com.esri.client.toolkit.overlays.HitTestListener;

/**
 * The Class HitTestOverlay. Implements an overlay that allows hit testing of
 * graphics with the mouse. An event is fired when a hit test has been made;
 * the graphics can be retrieved by calling getHitGraphics and the graphic IDs can be retrieved by calling getHitGraphicIDs.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.overlays.HitTestOverlay} instead.
 */
@Deprecated
public class HitTestOverlay extends MapOverlay {

  Layer layer;
  ArrayList<Graphic> _hitGraphics = null;
  ArrayList<Feature> hitFeatures = null;
  int[] _hitGraphicsIDs = null;
  
  /**
   * Instantiates a new graphic hit testing overlay.
   *
   * @param graphicsLayer the graphics layer
   */
  public HitTestOverlay(GraphicsLayer graphicsLayer) {
    layer = graphicsLayer;
  }
  
  /**
   * Instantiates a new hit testing overlay for a graphics layer, specifying a 
   * hit test listener.
   *
   * @param graphicsLayer the graphics layer
   * @param listener the event listener
   * @since 10.2.3
   */
  public HitTestOverlay(GraphicsLayer graphicsLayer, HitTestListener listener) {
    layer = graphicsLayer;
    hitTestListenerList.add(HitTestListener.class, listener);
  }
  
  /**
   * Instantiates a new hit testing overlay for a feature layer.
   *
   * @param featureLayer the feature layer
   * @since 10.2.3
   */
  public HitTestOverlay(FeatureLayer featureLayer) {
    layer = featureLayer;
  }

  /**
   * Instantiates a new hit testing overlay for a feature layer, specifying a 
   * hit test listener.
   *
   * @param featureLayer the feature layer
   * @param listener the event listener
   * @since 10.2.3
   */
  public HitTestOverlay(FeatureLayer featureLayer, HitTestListener listener) {
    layer = featureLayer;
    hitTestListenerList.add(HitTestListener.class, listener);
  }
  
  EventListenerList hitTestListenerList = new EventListenerList();

  /**
   * Add listeners for event fired when a graphic has been hit.
   *
   * @param l
   * 		Listener to add
   */
  public void addHitTestListener(HitTestListener l) {
    hitTestListenerList.add(HitTestListener.class, l);
  }

  /**
   * Remove listeners for event fired when a graphic has been hit.
   *
   * @param l 
   * 		Listener to remove
   */
  public void removeHitTestListener(HitTestListener l) {
    hitTestListenerList.remove(HitTestListener.class, l);
  }
  
  /**
   * Fires a graphic hit tested event.
   * @deprecated From 10.2.3, use {@link #fireFeatureHitTested()} instead.
   */
  protected void fireGraphicHitTested() {
       Object[] listeners = hitTestListenerList.getListenerList();
       for (int i = listeners.length - 2; i >= 0; i -= 2) {
           if (listeners[i] == HitTestListener.class) {
               ((HitTestListener)listeners[i+1]).featureHit(new HitTestEvent(this, HitTestEvent.FEATURE_HIT));
           }
       }
  }
  
  /**
   * Fires a feature hit tested event.
   */
  protected void fireFeatureHitTested() {
    Object[] listeners = hitTestListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == HitTestListener.class) {
        ((HitTestListener)listeners[i+1]).featureHit(new HitTestEvent(this, HitTestEvent.FEATURE_HIT));
      }
    }
  }
  
  /* (non-Javadoc)
   * @see com.esri.map.MapOverlay#onMouseClicked(java.awt.event.MouseEvent)
   */
  @Override
  public void onMouseClicked(MouseEvent event) {
    if (layer != null) {
      hitFeatures = new ArrayList<Feature>();
      _hitGraphics = new ArrayList<Graphic>();
      if (layer instanceof GraphicsLayer) {
        GraphicsLayer hitLayer = (GraphicsLayer)layer;
        _hitGraphicsIDs = hitLayer.getGraphicIDs(event.getX(), event.getY(), 0);
        for (int i : _hitGraphicsIDs) {
          hitFeatures.add(hitLayer.getGraphic(i));
          _hitGraphics.add(hitLayer.getGraphic(i));
        }
      } 
      if (layer instanceof FeatureLayer) {
        FeatureLayer hitLayer = (FeatureLayer)layer;
        long[] _hitGraphicsIDs = hitLayer.getFeatureIDs(event.getX(), event.getY(), 0);
        for (long i : _hitGraphicsIDs) {
          try {
            hitFeatures.add(((FeatureLayer)layer).getFeatureTable().getFeature(i));
          } catch (TableException e) {
            e.printStackTrace();
          }
        }
      }

      if (hitFeatures != null && !hitFeatures.isEmpty()) {
        fireFeatureHitTested();
      }
    }
    super.onMouseClicked(event);
  }
  
  /**
   * Gets the hit graphics.
   *
   * @return the hit graphics
   * @deprecated From 10.2.3, use {@link #getHitFeatures()} instead.
   */
  public List<Graphic> getHitGraphics() {
    return _hitGraphics;
  }

  /**
   * Gets the hit graphic IDs.
   *
   * @return the hit graphic IDs
   * @deprecated From 10.2.3, use {@link #getHitFeatures()} instead.
   */
  public int[] getHitGraphicIDs() {
    return _hitGraphicsIDs;
  }
  
  /**
   * Gets a list of hit {@link Feature}s.
   *
   * @return the hit features
   */
  public List<Feature> getHitFeatures() {
    return hitFeatures;
  }
  
  /**
   * Gets the layer associated with this overlay.
   * Prior to 10.2.3, this method used to return GraphicsLayer.
   *
   * @return the layer
   * @since 10.2.3
   */
  public Layer getLayer() {
    return layer;
  }
  
  private static final long serialVersionUID = 1L;

}
