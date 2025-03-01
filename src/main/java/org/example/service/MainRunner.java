package org.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MainRunner implements ApplicationRunner {

    private final List<Runner> runners;
    private final ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        runners.stream()
                .sorted(Comparator.comparingInt(Runner::order))
                .forEach(Runnable::run);
        SpringApplication.exit(applicationContext);
    }
}
