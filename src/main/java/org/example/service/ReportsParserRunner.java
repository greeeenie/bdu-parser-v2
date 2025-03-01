package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;
import org.example.model.Bdu;
import org.example.model.Capec;
import org.example.model.Cwe;
import org.example.repository.MainRepository;
import org.example.service.extractor.BduExtractor;
import org.example.service.extractor.CapecExtractor;
import org.example.service.extractor.CweExtractor;
import org.example.utils.CustomTransactionTemplate;
import org.example.utils.ExecutorUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.runners.enabled", name = "reports-parser", havingValue = "true")
@RequiredArgsConstructor
public class ReportsParserRunner implements Runner {

    private final AppConfig.AppProperties appProperties;
    private final ExecutorService executorService;
    private final CapecExtractor capecExtractor;
    private final CweExtractor cweExtractor;
    private final BduExtractor bduExtractor;
    private final MainRepository repository;
    private final CustomTransactionTemplate customTransactionTemplate;

    @Override
    public void run() {
        parseCapecs(appProperties.getCapecsReport());
        parseCwes(appProperties.getCwesReport());
        parseBdus(appProperties.getBdusReport());
    }

    @Override
    public int order() {
        return 1;
    }

    @SneakyThrows
    private void parseCapecs(Document document) {
        log.info("Capecs parsing started");
        Element attackPatterns = capecExtractor.attackPatterns(document);
        int attackPatternsCount = attackPatterns.childrenSize();
        List<Callable<Void>> tasks = IntStream.range(0, attackPatternsCount)
                .mapToObj(index -> ExecutorUtils.nullCallable(log, () -> {
                            Element attackPattern = capecExtractor.attackPattern(attackPatterns, index);
                            repository.save(Capec.builder()
                                    .id(capecExtractor.id(attackPattern))
                                    .name(capecExtractor.name(attackPattern))
                                    .likelihood(capecExtractor.likelihood(attackPattern))
                                    .build());
                        }
                ))
                .toList();
        executorService.invokeAll(tasks);
        log.info("Capecs parsing completed");
    }

    private void parseCwes(Document document) {
        log.info("Cwes parsing started");
        parseCweWeaknesses(document);
        parseCweCategories(document);
        parseCweViews(document);
        log.info("Cwes parsing completed");
    }

    @SneakyThrows
    private void parseCweWeaknesses(Document document) {
        Element weaknesses = cweExtractor.weaknesses(document);
        int weaknessesCount = weaknesses.childrenSize();
        Set<Integer> nonExistingCapecs = new HashSet<>();
        List<Callable<Void>> tasks = IntStream.range(0, weaknessesCount)
                .mapToObj(index -> ExecutorUtils.nullCallable(log, () ->
                        customTransactionTemplate.runInTransaction(() -> {
                            Element weakness = cweExtractor.weakness(weaknesses, index);
                            Cwe cwe = Cwe.builder()
                                    .id(cweExtractor.id(weakness))
                                    .name(cweExtractor.name(weakness))
                                    .build();
                            repository.save(cwe);
                            cweExtractor.capecIds(weakness)
                                    .forEach(capecId -> {
                                        if (repository.existsCapec(capecId)) {
                                            repository.saveRelation(cwe.getId(), capecId);
                                        } else {
                                            nonExistingCapecs.add(capecId);
                                        }
                                    });
                        })
                ))
                .toList();
        executorService.invokeAll(tasks);
        if (!nonExistingCapecs.isEmpty()) {
            List<Integer> sortedCapecs = nonExistingCapecs.stream()
                    .sorted()
                    .toList();
            log.warn("Non-existing capecs found: {}", sortedCapecs);
        }
    }

    @SneakyThrows
    private void parseCweCategories(Document document) {
        Element categories = cweExtractor.categories(document);
        int categoriesCount = categories.childrenSize();
        List<Callable<Void>> tasks = IntStream.range(0, categoriesCount)
                .mapToObj(index -> ExecutorUtils.nullCallable(log, () ->
                        customTransactionTemplate.runInTransaction(() -> {
                            Element category = cweExtractor.category(categories, index);
                            Cwe cwe = Cwe.builder()
                                    .id(cweExtractor.id(category))
                                    .name(cweExtractor.name(category))
                                    .build();
                            repository.save(cwe);
                        })
                ))
                .toList();
        executorService.invokeAll(tasks);
    }

    @SneakyThrows
    private void parseCweViews(Document document) {
        Element views = cweExtractor.views(document);
        int viewsCount = views.childrenSize();
        List<Callable<Void>> tasks = IntStream.range(0, viewsCount)
                .mapToObj(index -> ExecutorUtils.nullCallable(log, () ->
                        customTransactionTemplate.runInTransaction(() -> {
                            Element view = cweExtractor.view(views, index);
                            Cwe cwe = Cwe.builder()
                                    .id(cweExtractor.id(view))
                                    .name(cweExtractor.name(view))
                                    .build();
                            repository.save(cwe);
                        })
                ))
                .toList();
        executorService.invokeAll(tasks);
    }

    @SneakyThrows
    private void parseBdus(Document document) {
        log.info("Bdus parsing started");
        Element vulnerabilities = bduExtractor.vulnerabilities(document);
        int vulnerabilitiesCount = vulnerabilities.childrenSize();
        Set<Integer> nonExistingCwes = new HashSet<>();
        List<Callable<Void>> tasks = IntStream.range(0, vulnerabilitiesCount)
                .mapToObj(index -> ExecutorUtils.nullCallable(log, () ->
                        customTransactionTemplate.runInTransaction(() -> {
                            Element vulnerability = bduExtractor.vulnerability(vulnerabilities, index);
                            Bdu bdu = Bdu.builder()
                                    .id(bduExtractor.id(vulnerability))
                                    .name(bduExtractor.name(vulnerability))
                                    .description(bduExtractor.description(vulnerability))
                                    .build();
                            repository.save(bdu);
                            bduExtractor.cweIds(vulnerability)
                                    .forEach(cweId -> {
                                        if (repository.existsCwe(cweId)) {
                                            repository.saveRelation(bdu.getId(), cweId);
                                        } else {
                                            nonExistingCwes.add(cweId);
                                        }
                                    });
                        })
                ))
                .toList();
        executorService.invokeAll(tasks);
        if (!nonExistingCwes.isEmpty()) {
            List<Integer> sortedCwes = nonExistingCwes.stream()
                    .sorted()
                    .toList();
            log.warn("Non-existing cwes found: {}", sortedCwes);
        }
        log.info("Bdus parsing completed");
    }
}
