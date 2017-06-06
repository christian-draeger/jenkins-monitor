package de.idealo.jenkinsFetcher;

import static com.jayway.restassured.RestAssured.given;
import static java.lang.System.setProperty;

import org.springframework.stereotype.Component;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.response.Response;
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
        Response response = given(requestSpec)
                .get(url);

        if (response.getStatusCode() == 404) {
            return "{\"error\":\"jenkins not reachable\"}";
        }

        String bodyAsString = response.asString();

        if (bodyAsString.contains("<html><")) {
            return "{}";
        }
        return bodyAsString;
    }
}