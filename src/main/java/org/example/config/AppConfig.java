package org.example.config;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties(AppConfig.AppProperties.class)
@Configuration
public class AppConfig {

    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(
                10,
                10,
                1000,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000)
        );
    }

    @Getter
    @ConfigurationProperties("app.reports")
    public static class AppProperties {

        private final Document bdusReport;
        private final Document cwesReport;
        private final Document capecsReport;
        private final Document scanReport;

        @SneakyThrows
        public AppProperties(
                File bdus,
                File cwes,
                File capecs,
                File scan
        ) {
            this.bdusReport = Jsoup.parse(bdus, "UTF-8","", Parser.xmlParser());
            this.cwesReport = Jsoup.parse(cwes, "UTF-8","", Parser.xmlParser());
            this.capecsReport = Jsoup.parse(capecs, "UTF-8","", Parser.xmlParser());
            this.scanReport = Jsoup.parse(scan, "UTF-8","", Parser.htmlParser());
        }
    }
}
