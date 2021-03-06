/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.configuration;

import org.dspace.kernel.config.SpringLoader;
import org.dspace.services.ConfigurationService;

import java.io.File;
import java.net.MalformedURLException;

public class AACSpringLoader implements SpringLoader {

    @Override
    public String[] getResourcePaths(ConfigurationService configurationService) {
        StringBuffer filePath = new StringBuffer();
        filePath.append(configurationService.getProperty("dspace.dir"));
        filePath.append(File.separator);
        filePath.append("config");
        filePath.append(File.separator);
        filePath.append("spring");
        filePath.append(File.separator);
        filePath.append("aac");
        filePath.append(File.separator);


        try {
            return new String[]{new File(filePath.toString()).toURI().toURL().toString() + XML_SUFFIX};
        } catch (MalformedURLException e) {
            return new String[0];
        }
    }
}
