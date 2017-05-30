package de.idealo.jenkinsFetcher;

import static com.jayway.restassured.RestAssured.given;
import static java.lang.System.setProperty;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.specification.RequestSpecification;

import de.idealo.webservice.JenkinsData;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by christian.draeger on 15.01.16.
 */
@Slf4j
@Service
public class JenkinsDataFetcher {

    private static final String jenkinsTreeWithTestReport = "?passCount,skipCount,failCount,totalCount";
    private static final String jenkinsTreeWithoutTestReport = "?building,duration,fullDisplayName,id,number,result,timestamp,url,description,changeSet[items[author[fullName]]]";

    private ObjectMapper mapper = new ObjectMapper();

    public JenkinsElement getJenkinsData(final String jobName, final String jenkinsUrl) throws IOException {

        String jenkinsApiUrlWithTestReports = jenkinsUrl + "/job/" + jobName + "/lastBuild/testReport/api/json" + jenkinsTreeWithTestReport;
        String jenkinsApiUrlWithoutTestReports = jenkinsUrl + "/job/" + jobName + "/lastBuild/api/json" + jenkinsTreeWithoutTestReport;

        JenkinsElement jenkinsElement = new JenkinsElement();
        JenkinsData jenkinsData;

        String responseLastBuildJsonAsString = getResponseAsString(jenkinsApiUrlWithoutTestReports);
        String responseLastBuildTestReportsJsonAsString = getResponseAsString(jenkinsApiUrlWithTestReports);


        jenkinsData = mapper.readValue(responseLastBuildJsonAsString, JenkinsData.class);

        jenkinsElement.setTimestamp(jenkinsData.getTimestamp());
        jenkinsElement.setDate(formatTimestampToDate(jenkinsData.getTimestamp()));

        jenkinsElement.setFullDisplayName(jenkinsData.getFullDisplayName());
        jenkinsElement.setId(jenkinsData.getId());
        jenkinsElement.setNumber(jenkinsData.getNumber());
        jenkinsElement.setResult(getResult(jenkinsData));
        jenkinsElement.setUrl(jenkinsData.getUrl());
        jenkinsElement.setUrl(jenkinsData.getDescription());
        jenkinsElement.setBuilding(jenkinsData.isBuilding());


        jenkinsData = mapper.readValue(responseLastBuildTestReportsJsonAsString, JenkinsData.class);

        jenkinsElement.setFailCount(jenkinsData.getFailCount());
        jenkinsElement.setTotalCount(jenkinsData.getTotalCount());
        jenkinsElement.setPassCount(jenkinsData.getPassCount());
        jenkinsElement.setSkipCount(jenkinsData.getSkipCount());


        return jenkinsElement;
    }

    private String getResponseAsString(final String url) {

        setProperty("javax.net.ssl.trustStore", "/etc/ssl/certs/java/cacerts");

        RequestSpecification requestSpec = new RequestSpecBuilder().setConfig(
                RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()))
                .build();
        String body = given(requestSpec)
                .get(url)
                .asString();
        if (body.contains("HTTP Status 404")) {
            return "{\"error\":\"jenkins not reachable\"}";
        }
        if (body.contains("<html><")) {
            return "{}";
        }
        return body;
    }

    private String getResult(JenkinsData jenkinsData) {

        String result = jenkinsData.getResult();

        if ("SUCCESS".equals(result) || "STABLE".equals(result)) {
            return "success";
        }
        if ("FAILURE".equals(result)) {
            return "failure";
        }
        if ("ABORTED".equals(result)) {
            return "abort";
        }
        if (jenkinsData.isBuilding()) {
            return "building";
        }
        return "error";
    }

    private String formatTimestampToDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return sdf.format(date);
    }

}