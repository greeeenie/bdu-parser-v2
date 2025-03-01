package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class ScanReportBdu {

    private String bduId;
    private String fullBduIds;
    private String description;
}
