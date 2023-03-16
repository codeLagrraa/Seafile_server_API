package com.example.seafile_medicine.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
@RestController
@CrossOrigin("*")
public class AccountController{
    @Value("${app.auth}")
    String seafiletoken;
    @Value("${app.serverIP}")
    String serverIP;
    @Value("${app.SeaWebInterfacePort}")
    String SeaWebInterfacePort;
    @Autowired
    RestTemplate restTemplate;
    //check account info
    @GetMapping(value="/account")
    public String getAccInfo(){
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api2/account/info/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }
}
