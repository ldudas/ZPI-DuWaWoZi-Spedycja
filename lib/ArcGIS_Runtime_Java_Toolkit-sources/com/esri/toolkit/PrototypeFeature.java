/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.toolkit;
import java.awt.Color;
import java.util.Map;

import javax.swing.ImageIcon;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MultiLayerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.map.FeatureLayer;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureTemplate.DRAWING_TOOL;
import com.esri.core.map.Graphic;

/**
 * This class represents the prototype for a feature in a <code>FeatureLayer</code>. 
 * Contains the necessary information for creating new features according to this 
 * prototype, including symbol, attributes, and which {@link DRAWING_TOOL} to use 
 * when drawing a new feature.
 * 
 * @since 10.2.3
 */
public class PrototypeFeature {

  Feature feature;
  String _name;
  String _description;
  Geometry.Type _geometryType;
  FeatureLayer _featureLayer;
  DRAWING_TOOL _drawingTool;

  /**
   * Instantiates a PrototypeFeature.
   * 
   * @param attributes The attributes for this prototype
   * @param symbol The symbol for this prototype
   * @param featureLayer The feature layer of the features; cannot be null
   * @param drawingTool the drawing tool used to draw this type of feature
   * @param name The name of the prototype
   * @param description The description of the prototype
   * @throws IllegalArgumentException if the feature layer is null
   */
  public PrototypeFeature(Map<String, Object> attributes, Symbol symbol, FeatureLayer featureLayer, DRAWING_TOOL drawingTool, String name, String description) {
    if (featureLayer == null) {
      throw new IllegalArgumentException("feature layer cannot be null");
    }
    _name = name;
    _description = description;
    feature = new Graphic(null, symbol, attributes);
    _featureLayer = featureLayer;
    _drawingTool = drawingTool;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return feature.getAttributes() + "  " + feature.getSymbol();
  }

  /**
   * Gets the feature layer this prototype is for.
   * 
   * @return the feature layer that this prototype is for
   */
  public FeatureLayer getFeatureLayer() {
    return _featureLayer;
  }

  /**
   * Gets the name.
   * 
   * @return the name of the prototype feature
   */
  public String getName() {
    return _name;
  }

  /**
   * Gets the description.
   * 
   * @return the description of the prototype feature
   */
  public String getDescription() {
    return _description;
  }

  /**
   * Gets the geometry type.
   * 
   * @return the geometry type of this prototype
   */
  public Geometry.Type getGeometryType() {
    return _featureLayer.getGeometryType();
  }

  /**
   * Gets the drawing tool.
   * 
   * @return a drawing tool that can be used to create features from this prototype
   */
  public DRAWING_TOOL getDrawingTool() {
    return _drawingTool;
  }

  /**
   * Creates a sample image icon for this prototype, of the specified width and height.
   * 
   * @param width the width of the icon
   * @param height the height of the icon
   * @return an icon demonstrating how a feature with this prototype would be symbolized 
   */
  public ImageIcon getIcon(int width, int height) {
    Symbol symbol = getSymbol();
    Geometry geom = null;

    if (symbol instanceof SimpleMarkerSymbol || symbol instanceof PictureMarkerSymbol) {
      geom = new Point(width / 2, height / 2);
    } else if (symbol instanceof SimpleFillSymbol) {
      LineSymbol outline = ((SimpleFillSymbol)symbol).getOutline();
      float outlineThickness = 0;
      if (outline != null) {
        outlineThickness = outline.getWidth();
      }
      Polygon p = new Polygon();
      p.startPath(new Point(0.0 + outlineThickness, 0.0 + outlineThickness));
      p.lineTo(0.0 + outlineThickness, height - outlineThickness);
      p.lineTo(width - outlineThickness, height - outlineThickness);
      p.lineTo(width - outlineThickness, 0.0 + outlineThickness);
      p.closeAllPaths();

      geom = p;
    } else if (symbol instanceof SimpleLineSymbol) {
      Polyline p = new Polyline();
      p.startPath(new Point(0.0, height / 2.0));
      p.lineTo(width, height / 2.0);
      p.closeAllPaths();

      geom = p;
    } else if (symbol instanceof MultiLayerSymbol) {
      if (this.getGeometryType() == Geometry.Type.POINT) {
        geom = new Point(width / 2, height / 2);
      } else if (this.getGeometryType() == Geometry.Type.POLYLINE) {
        Polyline p = new Polyline();
        p.startPath(new Point(0.0, height / 2.0));
        p.lineTo(width, height / 2.0);
        p.closeAllPaths();

        geom = p;
      } else if (this.getGeometryType() == Geometry.Type.POLYGON) {
        Polygon p = new Polygon();
        p.startPath(new Point(0.0, 0.0));
        p.lineTo(0.0, height);
        p.lineTo(width, height);
        p.lineTo(width, 0.0);
        p.closeAllPaths();

        geom = p;
      }
    }

    return new ImageIcon(_featureLayer.createSymbolImage(symbol, geom, width, height, new Color(0x00000000, true)));
  }

  /**
   * Gets the symbol used to render features of this prototype.
   *
   * @return the symbol
   */
  public Symbol getSymbol() {
    return feature.getSymbol();
  }

  /**
   * Gets the attributes to use for features created with this prototype.
   *
   * @return the map of attributes
   */
  public Map<String, Object> getAttributes() {
    return feature.getAttributes();
  }
}
