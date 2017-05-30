package de.idealo.jenkinsFetcher;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by christian.draeger on 26.04.16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JenkinsElement {
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
    private String date;
    private boolean building;
    private boolean hasNumbers;
}