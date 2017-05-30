package de.idealo.jenkinsFetcher;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.idealo.webservice.JenkinsData;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by christian.draeger on 15.01.16.
 */
@Slf4j
@Service
public class Aggregator {

    @Autowired
    JenkinsDataFetcher jenkinsDataFetcher;

    private static final String jenkinsTreeWithTestReport = "?passCount,skipCount,failCount,totalCount";
    private static final String jenkinsTreeWithoutTestReport = "?building,duration,fullDisplayName,id,number,result,timestamp,url,description,changeSet[items[author[fullName]]]";

    private ObjectMapper mapper = new ObjectMapper();

    public JenkinsElement getJenkinsData(final String jobName, final String jenkinsUrl) throws IOException {

        String jenkinsApiUrlWithTestReports = jenkinsUrl + "/job/" + jobName + "/lastBuild/testReport/api/json" + jenkinsTreeWithTestReport;
        String jenkinsApiUrlWithoutTestReports = jenkinsUrl + "/job/" + jobName + "/lastBuild/api/json" + jenkinsTreeWithoutTestReport;

        JenkinsElement jenkinsElement = new JenkinsElement();
        JenkinsData jenkinsData;

        String responseLastBuildJsonAsString = jenkinsDataFetcher.getResponseAsString(jenkinsApiUrlWithoutTestReports);
        String responseLastBuildTestReportsJsonAsString = jenkinsDataFetcher.getResponseAsString(jenkinsApiUrlWithTestReports);

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

        jenkinsElement.setHasNumbers(hasNumbers(jenkinsData));
        jenkinsElement.setFailCount(jenkinsData.getFailCount());
        jenkinsElement.setTotalCount(jenkinsData.getTotalCount());
        jenkinsElement.setPassCount(jenkinsData.getPassCount());
        jenkinsElement.setSkipCount(jenkinsData.getSkipCount());


        return jenkinsElement;
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

    private boolean hasNumbers(JenkinsData data) {
        long count = data.getPassCount() + data.getFailCount() + data.getSkipCount() + data.getTotalCount();

        if (count > 0) {
            return true;
        }
        return false;
    }

    private String formatTimestampToDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return sdf.format(date);
    }

}
