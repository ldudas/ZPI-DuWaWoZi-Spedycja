package com.esri.toolkit.utilities;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


/**
 * A centralized utility to handle any errors from different toolkit components.
 * @since 10.2.4
 */
public class ExceptionHandler {

  /**
   * Handles exception. Shows the error message as a dialog if input component is
   * not null; prints to console otherwise.
   * @param component this determines the frame to which the dialog will be associated.
   * @param ex exception to be handled
   */
  public static void handleException(JComponent component, Throwable ex) {
    if (ex == null) {
     return; 
    }
    handleException(component, ex.getMessage());
  }
  
  /**
   * Handles exception. Shows the error message as a dialog if input component is
   * not null; prints to console otherwise.
   * @param component this determines the frame to which the dialog will be associated.
   * @param errMsg exception message to be handled
   */
  public static void handleException(final JComponent component, final String errMsg) {
    if (errMsg == null) {
      return;
    }
    if (component != null) {
      if (SwingUtilities.isEventDispatchThread()) {
        JOptionPane.showMessageDialog(component, wrap(errMsg), "", JOptionPane.ERROR_MESSAGE);  
      } else {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            JOptionPane.showMessageDialog(component, wrap(errMsg), "", JOptionPane.ERROR_MESSAGE);
          }
        });
      }
    } else {
      System.out.println(errMsg);
    }
  }
  
  private static String wrap(String str) {
    // create a HTML string that wraps text when longer
    return "<html><p style='width:200px;'>" + str + "</html>";
  }
}
