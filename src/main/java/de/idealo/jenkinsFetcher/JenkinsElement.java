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
    private long failCount, totalCount, passCount, skipCount, timestamp, number;
    private String fullDisplayName, id, result, url, fullName, description;
    private boolean building;
}