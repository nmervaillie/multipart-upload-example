package org.company.example.multipartupload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@RestController
public class MultipartController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${remote.server.upload.url}")
    private String serverUrl;

    @PostMapping(value = "/multipartfile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> processMultipartFile(@RequestParam MultipartFile file) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("pdf", file.getResource());
        body.add("upload", "false");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(serverUrl, requestEntity, String.class);
    }
}
