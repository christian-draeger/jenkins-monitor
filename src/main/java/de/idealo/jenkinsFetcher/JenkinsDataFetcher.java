package de.idealo.jenkinsFetcher;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import de.idealo.webservice.JenkinsModel;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by christian.draeger on 15.01.16.
 */
@Slf4j
@Service
public class JenkinsDataFetcher {

    final String jenkinsTreeWithTestReport = "?passCount,skipCount,failCount,totalCount";
    final String jenkinsTreeWithoutTestReport = "?building,duration,fullDisplayName,id,number,result,timestamp,url,description,changeSet[items[author[fullName]]]";


    public JenkinsElement getJenkinsData(final String jobName, final String jenkinsUrl) throws IOException {

        String jenkinsApiUrlWithTestReports = jenkinsUrl + "/job/" + jobName + "/lastBuild/testReport/api/json" + jenkinsTreeWithTestReport;
        String jenkinsApiUrlWithoutTestReports = jenkinsUrl + "/job/" + jobName + "/lastBuild/api/json" + jenkinsTreeWithoutTestReport;

        JenkinsElement jenkinsElement = new JenkinsElement();
        ObjectMapper mapper = new ObjectMapper();
        JenkinsModel jenkinsModel;

        String responseLastBuildJsonAsString = getResponseAsString(jenkinsApiUrlWithoutTestReports);
        String responseLastBuildTestReportsJsonAsString = getResponseAsString(jenkinsApiUrlWithTestReports);


            jenkinsModel = mapper.readValue(responseLastBuildJsonAsString, JenkinsModel.class);

            jenkinsElement.setTimestamp(jenkinsModel.getTimestamp());
            jenkinsElement.setFullDisplayName(jenkinsModel.getFullDisplayName());
            jenkinsElement.setId(jenkinsModel.getId());
            jenkinsElement.setNumber(jenkinsModel.getNumber());
            jenkinsElement.setResult(jenkinsModel.getResult());
            jenkinsElement.setUrl(jenkinsModel.getUrl());
            jenkinsElement.setUrl(jenkinsModel.getDescription());
            // jenkinsElement.setFullName(jenkinsModel.getFullName());
            jenkinsElement.setBuilding(jenkinsModel.isBuilding());


            jenkinsModel = mapper.readValue(responseLastBuildTestReportsJsonAsString, JenkinsModel.class);

            jenkinsElement.setFailCount(jenkinsModel.getFailCount());
            jenkinsElement.setTotalCount(jenkinsModel.getTotalCount());
            jenkinsElement.setPassCount(jenkinsModel.getPassCount());
            jenkinsElement.setSkipCount(jenkinsModel.getSkipCount());


        return jenkinsElement;
    }

    private String getResponseAsString(final String url){

        try {
            HttpResponse<String> response = Unirest
                .get(url)
                .header("User-Agent", "IKDhPmJcdw")
                .asString();

            log.info("asking jenkins for " + url);

            if (response.getStatus() == HttpStatus.SC_OK) {
                return response.getBody();
            }

        } catch (Exception e) {
            log.error("UniRest Exception", e);
            return "{\"error\":\"jenkins not reachable\"}";
        }

        return "{}";
    }

}