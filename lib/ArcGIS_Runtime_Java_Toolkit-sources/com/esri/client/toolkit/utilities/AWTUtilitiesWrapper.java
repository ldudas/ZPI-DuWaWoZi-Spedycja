/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.utilities;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @deprecated From 10.2.3.
 */
@Deprecated
public class AWTUtilitiesWrapper {
  private static Class<?> _awtUtils;
  private static Class<?> _awtUtilsTranslucencyEnum;
  private static Method _setWindowShape;
  private static Method _setWindowOpaque;
  private static Method _setWindowOpacity;
  private static Method _isTranslucencySupported;
  private static Method _isTranslucencyCapable;
  
  public static Object PERPIXEL_TRANSPARENT; 
  public static Object TRANSLUCENT; 
  public static Object PERPIXEL_TRANSLUCENT;

  public static void init() {
    try {
      _awtUtils = Class.forName("com.sun.awt.AWTUtilities");
      _awtUtilsTranslucencyEnum = Class
          .forName("com.sun.awt.AWTUtilities$Translucency");
            if (_awtUtilsTranslucencyEnum.isEnum()) {
                Object[] kinds = _awtUtilsTranslucencyEnum.getEnumConstants();
                if (kinds != null) {
                    PERPIXEL_TRANSPARENT = kinds[0];
                    TRANSLUCENT = kinds[1];
                    PERPIXEL_TRANSLUCENT = kinds[2];
                }
            }
      _setWindowShape = _awtUtils.getMethod("setWindowShape",
          Window.class, Shape.class);
      _setWindowOpaque = _awtUtils.getMethod("setWindowOpaque",
          Window.class, boolean.class);
      _setWindowOpacity = _awtUtils.getMethod("setWindowOpacity",
          Window.class, float.class);
      _isTranslucencySupported = _awtUtils.getMethod(
          "isTranslucencySupported", _awtUtilsTranslucencyEnum);
      _isTranslucencyCapable = _awtUtils.getMethod(
          "isTranslucencyCapable", GraphicsConfiguration.class);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  static{
    init();
  }
  
  public static void setWindowShape(Window window, Shape shape){
    if(_awtUtils != null && _setWindowShape != null){
      try {
        _setWindowShape.invoke(_awtUtils, window, shape);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void setWindowOpaque(Window window, boolean isOpaque){
    if(_awtUtils != null && _setWindowOpaque != null){
      try {
        _setWindowOpaque.invoke(_awtUtils, window, isOpaque);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }			
  }
  
  public static void setWindowOpacity(Window window, float opacity){
    if(_awtUtils != null && _setWindowOpacity != null){
      try {
        _setWindowOpacity.invoke(_awtUtils, window, opacity);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }	
  }
  
  public static boolean isTranslucencySupported(Object translucencyKind){
    boolean retVal = false;
    
    if(_awtUtils != null && _isTranslucencySupported != null){
      try {
        retVal = (Boolean) _isTranslucencySupported.invoke(_awtUtils, translucencyKind);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    
    return retVal;
  }
  
  public static boolean isTranslucencyCapable(GraphicsConfiguration gc){
    boolean retVal = false;
    
    if(_awtUtils != null && _isTranslucencyCapable != null){
      try {
        retVal = (Boolean) _isTranslucencyCapable.invoke(_awtUtils, gc);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    
    return retVal;
  }
  
  public static GraphicsConfiguration findCompatableGraphicsConfiguration(){
    GraphicsConfiguration retVal = null;
    
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] devices = env.getScreenDevices();
    
    for(GraphicsDevice curDevice: devices){
      GraphicsConfiguration[] configs = curDevice.getConfigurations();
      
      for(GraphicsConfiguration curConfig: configs){
        if(isTranslucencyCapable(curConfig)){
          retVal = curConfig;
          break;
        }
      }
      
      if(retVal != null){
        break;
      }
    }
    
    return retVal;
  }
}
