package de.idealo.jenkinsFetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleFetcher {

    @Autowired
    private JenkinsDataFetcher jenkinsDataFetcher;

    public String rawFetcher(String jenkinsUrl) {
        jenkinsUrl = jenkinsUrl + "/api/json";
        return jenkinsDataFetcher.getResponseAsString(jenkinsUrl);
    }
}
