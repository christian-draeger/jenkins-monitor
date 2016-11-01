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
public class JenkinsModel {

    private long failCount, totalCount, passCount, skipCount, timestamp, number;
    private String fullDisplayName, id, result, url, fullName, description;
    private boolean building;
}