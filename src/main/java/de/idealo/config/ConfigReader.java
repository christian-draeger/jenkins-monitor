package de.idealo.config;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

@Component
public class ConfigReader implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getConfig() {
        return resourceToString(getExternalConfig());
    }

    @SneakyThrows
    private String resourceToString(Resource resource) {
        InputStream inputStream = resource.getInputStream();
        return IOUtils.toString(inputStream, "UTF-8");
    }

    private Resource getExternalConfig() {
        String pathToExternalConfig = "file:" + new File("jenkinsMonitorConfig.json").getAbsolutePath();
        return resourceLoader.getResource(pathToExternalConfig);
    }
}
