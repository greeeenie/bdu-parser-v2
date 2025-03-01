package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.model.ScanReportBdu;
import org.example.repository.MainRepository;
import org.example.service.extractor.ScanReportExtractor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.runners.enabled", name = "scan-report", havingValue = "true")
@RequiredArgsConstructor
public class ScanReportParserRunner implements Runner {

    private final AppConfig.AppProperties appProperties;
    private final ScanReportExtractor scanReportExtractor;
    private final MainRepository repository;

    @Override
    public void run() {
        log.info("Scan report parsing started");
        repository.truncateScanReport();
        scanReportExtractor.bdus(appProperties.getScanReport())
                .forEach(element -> repository.saveScanReportBdu(ScanReportBdu.builder()
                            .bduId(scanReportExtractor.bduId(element))
                            .fullBduIds(scanReportExtractor.fullBduIds(element))
                            .description(scanReportExtractor.description(element))
                            .build())
                );
        log.info("Scan report parsing completed");
    }

    @Override
    public int order() {
        return 3;
    }
}
