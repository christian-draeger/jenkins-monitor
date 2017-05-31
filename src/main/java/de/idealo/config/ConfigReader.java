package de.idealo.config;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

@Component
public class ConfigReader {

    @Value(value = "classpath:static/config.json")
    private Resource configJson;

    @SneakyThrows
    public String getConfig() {
        InputStream inputStream = configJson.getInputStream();
        return IOUtils.toString(inputStream, "UTF-8");
    }
}
