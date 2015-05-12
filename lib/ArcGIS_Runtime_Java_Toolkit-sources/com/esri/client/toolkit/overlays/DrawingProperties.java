/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.overlays;

import java.util.Map;

import com.esri.client.toolkit.overlays.DrawingOverlay.DrawingMode;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.Symbol;

/**
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.overlays.DrawingProperties} instead.
 */
@Deprecated
public class DrawingProperties {
    private DrawingMode drawingMode;
    private Renderer renderer;
    private Map<String, Object> attributes;

    public DrawingProperties(
        DrawingMode drawingMode,
        Symbol symbol,
        Map<String, Object> attributes) {
        this.drawingMode = drawingMode;
        this.renderer = new SimpleRenderer(symbol);
        this.attributes = attributes;
    }

    public DrawingProperties(
        DrawingMode drawingMode,
        Renderer renderer,
        Map<String, Object> attributes) {
        this.drawingMode = drawingMode;
        this.renderer = renderer;
        this.attributes = attributes;
    }

    public DrawingMode getDrawingMode() {
        return drawingMode;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
