package org.company.example.multipartupload.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();

//        ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
//        List<ClientHttpRequestInterceptor> ris = new ArrayList<>();
//        ris.add(ri);
//        restTemplate.setInterceptors(ris);
//        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        return restTemplate;
    }

    public static class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

        private static final Logger log = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

        @Override
        @NonNull
        public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, ClientHttpRequestExecution execution) throws IOException {

            ClientHttpResponse response = execution.execute(request, body);
            log(request,body);
            return response;
        }

        private void log(HttpRequest request, byte[] body) {
            log.info("Method: {}", request.getMethod().toString());
            log.info("URI: {}", request.getURI().toString());
            log.info("Request Body: " + new String(body));
        }
    }
}
