package de.monitor.webservice;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.monitor.config.ConfigReader;
import de.monitor.jenkinsFetcher.Aggregator;
import de.monitor.jenkinsFetcher.JenkinsElement;
import de.monitor.jenkinsFetcher.SimpleFetcher;

/**
 * Created by christian.draeger on 15.01.16.
 */
@RestController
public class JenkinsController {

    @Autowired
    private Aggregator aggregator;
    @Autowired
    private ConfigReader configReader;
    @Autowired
    private SimpleFetcher simpleFetcher;

    @CrossOrigin
    @RequestMapping(value = "/jenkins", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JenkinsElement jenkinsResults(
            @RequestParam(value = "job") final String jobName,
            @RequestParam(value = "jenkinsUrl") final String jenkinsUrl) throws IOException {
        return aggregator.getJenkinsData(jobName, jenkinsUrl);
    }

    @CrossOrigin
    @RequestMapping(value = "/config", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String jenkinsResults() {
        return configReader.getConfig();
    }

    @CrossOrigin
    @RequestMapping(value = "/apiRaw", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String jenkinsResults(@RequestParam(value = "jenkinsUrl") final String jenkinsUrl) {
        return simpleFetcher.rawFetcher(jenkinsUrl);
    }
}
