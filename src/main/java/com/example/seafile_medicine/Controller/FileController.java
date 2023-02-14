package com.example.seafile_medicine.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
@CrossOrigin
@RestController
public class FileController{
    @Value("${app.auth}")
    String seafiletoken;
    @Autowired
    RestTemplate restTemplate;
    //all file details
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @GetMapping(value = "/files")
    public String getFiles(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("http://localhost:8000/api2/repos/f3316573-01d0-4ee8-bf76-04c84e0dac4b/dir/?p=/foo", HttpMethod.GET, entity, String.class).getBody();
    }

    //get specified file by filename (download)
    @GetMapping(value = "/getFile")
    public String getFile(@RequestParam(value = "fileName") String fileName){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("http://localhost:8000/api2/repos/f3316573-01d0-4ee8-bf76-04c84e0dac4b/file/?p=/foo/"+fileName, HttpMethod.GET, entity, String.class).getBody();
    }

    //get file details
    @GetMapping(value = "/getFileDetails")
    public String getFileDetail(@RequestParam (value = "reqname") String fileNamex) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("http://localhost:8000/api2/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/file/detail/?p=/foo/" + fileNamex, HttpMethod.GET, entity, String.class).getBody();
    }
}
//how to send request parameters with