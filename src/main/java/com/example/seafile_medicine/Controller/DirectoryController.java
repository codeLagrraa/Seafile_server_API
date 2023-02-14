package com.example.seafile_medicine.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
public class DirectoryController {
    @Value("${app.auth}")
    String seafiletoken;
    @Autowired
    RestTemplate restTemplate;

    //Create New Directory
    @PostMapping(value = "/createDir")
    public ResponseEntity<String> createDirectory(@RequestParam(value = "fileName") String fileName) {

        String url = "http://localhost:8000/api/v2.1/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/dir/?p=/foo/"+fileName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4;");



        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("operation", "mkdir");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response;

    }
    //list items in a directory
    //all file details
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @GetMapping(value = "/allfilesdetails")
    public String allFilesDetails(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("http://localhost:8000/api2/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/dir/?p=/foo", HttpMethod.GET, entity, String.class).getBody();
    }


}
