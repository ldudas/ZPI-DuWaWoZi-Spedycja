/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.utilities;

import java.text.DecimalFormat;

/**
 * Utilities to help formatting.
 * @since 10.2
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.utilities.FormatUtils} instead.
 */
@Deprecated
public final class FormatUtils {

  /**
   * Private CTOR. Prevent instantiation.
   */
  private FormatUtils() {
  }

  /**
   * Format input value as memory.
   * @param value memory in bytes.
   * @return string representation of the input memory. Return value has maximum of
   * 2 digits in its fraction.
   * <br>
   * "n bytes", if value < 1 KB,<br>
   * "n KB", if value < 1 MB, <br>
   * "n MB", if value < 1 GB, <br>
   * "n GB", otherwise. 
   */
  public static String formatMemory(long value) {
    String[] units = new String[] {"bytes", "KB", "MB", "GB"};
    
    double KB = 1024;
    int unitIndex = 0;
    double canonicalValue = value;
    while (canonicalValue > KB && unitIndex < units.length) {
      canonicalValue = canonicalValue / KB;
      unitIndex++;
    }

    DecimalFormat df = new DecimalFormat("#.##");
    return df.format(canonicalValue) + " " + units[unitIndex];
  }
}
