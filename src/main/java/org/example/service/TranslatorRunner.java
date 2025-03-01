package org.example.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.repository.MainRepository;
import org.example.utils.ExecutorUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.runners.enabled", name = "translator", havingValue = "true")
@RequiredArgsConstructor
public class TranslatorRunner implements Runner {
    private final OpenAiService openAiService;
    private final MainRepository repository;
    private final ExecutorService executorService;

    @SneakyThrows
    @Override
    public void run() {
        log.info("Translator started");
        List<Callable<Void>> tasks = repository.getCapecs()
                .stream()
                .map(capec -> ExecutorUtils.nullCallable(log, () -> {
                            String nameRu = translateToRussian(capec.getName());
                            log.info(nameRu);
                            repository.updateCapecNameRu(capec.getId(), nameRu);
                        }))
                .toList();
        executorService.invokeAll(tasks);
        log.info("Translator completed");
    }

    private String translateToRussian(String message) {
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(),
                "Переведи название кибератаки на русский язык: " + message +
                        ". Ничего не добавляй от себя, оставь в ответе только перевод. Используй общепринятые термины, которыми называют кибератаки. Ставь точку в конце своего ответа.");
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-4o-mini")
                .temperature(1.0)
                .messages(List.of(chatMessage))
                .build();
        ChatCompletionResult result = openAiService.createChatCompletion(chatCompletionRequest);
        return result.getChoices().get(0).getMessage().getContent();
    }

    @Override
    public int order() {
        return 2;
    }
}
