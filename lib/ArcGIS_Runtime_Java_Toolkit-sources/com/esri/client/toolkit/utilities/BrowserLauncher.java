/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.utilities;

import java.awt.Desktop;
import java.net.URI;
import java.util.Arrays;

/**
 * Utility to open a URL in a web browser.
 * Note: This currently does not handle proxy server.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.utilities.BrowserLauncher} instead.
 */
@Deprecated
public class BrowserLauncher {

    /**
     * Opens the given URL in a web browser.
     * @param url URL to be opened in the web browser. It is required that the URL be prefixed with
     * corresponding scheme to work on different operating systems.
     */
    public static void openURL(String url) {
        if (url != null && !url.trim().isEmpty()) {
            if (Desktop.isDesktopSupported()) {
                // supports Windows, Linux that has GNOME libraries
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        // open the link in the default browser
                        desktop.browse(new URI(url));
                    } catch(Exception e) {
                        throw new RuntimeException("Error in launching browser. " +
                            e.getLocalizedMessage());
                    }
                }
            } else {
                // for Linux that does not have GNOME libraries
                String[] browserCmds = new String[] {"x-www-browser", "xdg-open"};
                for (String browserCmd : browserCmds) {
                    try {
                        ProcessBuilder pb = new ProcessBuilder(browserCmd, url);
                        pb.start();
                        return;
                    } catch (Exception ex) {
                        // expected based on command and OS
                        // ignore and continue with next command.
                    }
                }

                throw new RuntimeException("Cannot launch browser in this operating system. " +
                    "One of the reasons could be the system does not have any of these commands: " +
                    Arrays.toString(browserCmds));
            }
        }
    }

}
