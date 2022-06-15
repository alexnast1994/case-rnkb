package com.cognive.projects.casernkb.bpm.common;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceHelper {
    public static String readString(String resourceName) {
        if(resourceName == null)
            throw new RuntimeException("resourceName cannot be null");

        try {
            URL resourceUrl = ResourceHelper.class.getResource(getResourcePath(resourceName));
            if(resourceUrl == null)
                throw new RuntimeException("Cannot find resource with name: " + resourceName);
            return Files.readString(Paths.get(resourceUrl.toURI()));
        }
        catch (URISyntaxException | IOException ex) {
            throw new RuntimeException("Cannot find resource with name: " + resourceName);
        }
    }

    private static String getResourcePath(String path) {
        if(path.startsWith("/"))
            return path;

        return "/" + path;
    }
}
