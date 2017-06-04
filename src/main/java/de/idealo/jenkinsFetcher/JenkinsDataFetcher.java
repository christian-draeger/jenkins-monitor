package de.idealo.jenkinsFetcher;

import static com.jayway.restassured.RestAssured.given;
import static java.lang.System.setProperty;

import org.springframework.stereotype.Component;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.specification.RequestSpecification;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by christian.draeger on 15.01.16.
 */
@Slf4j
@Component
public class JenkinsDataFetcher {

    public String getResponseAsString(final String url) {

        setProperty("javax.net.ssl.trustStore", "/etc/ssl/certs/java/cacerts");

        RequestSpecification requestSpec = new RequestSpecBuilder().setConfig(
                RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                .build();
        String body = given(requestSpec)
                .get(url)
                .asString();
        if (body.contains("404")) {
            return "{\"error\":\"jenkins not reachable\"}";
        }
        if (body.contains("<html><")) {
            return "{}";
        }
        return body;
    }
}