package org.example.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.time.Duration;

import static com.theokanning.openai.service.OpenAiService.defaultClient;

@Slf4j
@Configuration
public class OpenAiConfig {

    @Value("${app.api-key}")
    private String apiKey;

    private static class CustomInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl modifiedUrl = request.url().newBuilder()
                    .encodedPath("/openai" + request.url().encodedPath())
                    .build();
            Request modifiedRequest = request.newBuilder()
                    .url(modifiedUrl)
                    .build();
            return chain.proceed(modifiedRequest);
        }
    }

    @Bean
    public OpenAiService openAiService() {
        OkHttpClient client = defaultClient(apiKey, Duration.ofMillis(5000))
                .newBuilder()
                .addInterceptor(new CustomInterceptor())
                .build();
        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.proxyapi.ru/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        return new OpenAiService(api);
    }
}
