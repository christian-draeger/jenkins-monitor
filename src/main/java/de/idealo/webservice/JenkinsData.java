package de.idealo.webservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by christian.draeger on 15.01.16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsData {

    private long failCount;
    private long totalCount;
    private long passCount;
    private long skipCount;
    private long timestamp;
    private long number;
    private String fullDisplayName;
    private String id;
    private String result;
    private String url;
    private String fullName;
    private String description;
    private boolean building;
}