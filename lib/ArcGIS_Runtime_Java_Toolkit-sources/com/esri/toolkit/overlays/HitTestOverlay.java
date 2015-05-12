/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit.overlays;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.esri.core.map.Feature;
import com.esri.core.table.TableException;
import com.esri.map.FeatureLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.Layer;
import com.esri.map.MapOverlay;
import com.esri.toolkit.utilities.ExceptionHandler;

/**
 * This class implements an overlay that allows 'hit testing' of features 
 * on mouse clicks; that is, storing/retrieving features which were within 
 * a certain pixel tolerance of the clicked point. An event is fired when 
 * a hit test has been made; the features from the latest hit test/mouse 
 * click can be retrieved by calling {@link #getHitFeatures()}. The overlay 
 * works with {@link GraphicsLayer} and {@link FeatureLayer}.
 * @usage
 * <code>
 * <pre>
 * final HitTestOverlay overlay = new HitTestOverlay(graphics/feature layer);
 * overlay.addHitTestListener(new FeaturesHitListener());
 * 
 * class FeaturesHitListener implements HitTestListener {
 *   {@literal @}Override
 *   public void featureHit(HitTestEvent event) {
 *     List<Feature> featuresHit = event.getOverlay().getHitFeatures();
 *     // process features hit
 *   }
 * }
 * </pre>
 * </code>
 * @since 10.2.3
 */
public class HitTestOverlay extends MapOverlay {

  Layer layer;
  ArrayList<Feature> hitFeatures = null;

  /**
   * Instantiates a new hit testing overlay for a graphics layer.
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
   */
  public HitTestOverlay(GraphicsLayer graphicsLayer, HitTestListener listener) {
    layer = graphicsLayer;
    hitTestListenerList.add(HitTestListener.class, listener);
  }

  /**
   * Instantiates a new hit testing overlay for a feature layer.
   *
   * @param featureLayer the feature layer
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
   */
  public HitTestOverlay(FeatureLayer featureLayer, HitTestListener listener) {
    layer = featureLayer;
    hitTestListenerList.add(HitTestListener.class, listener);
  }

  EventListenerList hitTestListenerList = new EventListenerList();

  /**
   * Adds a listener for the event fired when one or more features has been hit.
   *
   * @param l
   * 		Listener to add
   */
  public void addHitTestListener(HitTestListener l) {
    hitTestListenerList.add(HitTestListener.class, l);
  }

  /**
   * Removes a listener for the event fired when one or more features has been hit.
   *
   * @param l 
   * 		Listener to remove
   */
  public void removeHitTestListener(HitTestListener l) {
    hitTestListenerList.remove(HitTestListener.class, l);
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
      if (layer instanceof GraphicsLayer) {
        GraphicsLayer hitLayer = (GraphicsLayer)layer;
        int[] _hitGraphicsIDs = hitLayer.getGraphicIDs(event.getX(), event.getY(), 0);
        for (int i : _hitGraphicsIDs) {
          hitFeatures.add(hitLayer.getGraphic(i));
        }
      } 
      if (layer instanceof FeatureLayer) {
        FeatureLayer hitLayer = (FeatureLayer)layer;
        long[] _hitGraphicsIDs = hitLayer.getFeatureIDs(event.getX(), event.getY(), 0);
        for (long i : _hitGraphicsIDs) {
          try {
            hitFeatures.add(((FeatureLayer)layer).getFeatureTable().getFeature(i));
          } catch (TableException e) {
            ExceptionHandler.handleException(HitTestOverlay.this, e);
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
   * Gets a list of hit {@link Feature}s.
   *
   * @return the hit features
   */
  public List<Feature> getHitFeatures() {
    return hitFeatures;
  }

  /**
   * Gets the layer associated with this overlay.
   *
   * @return the layer
   */
  public Layer getLayer() {
    return layer;
  }

  private static final long serialVersionUID = 1L;

}
