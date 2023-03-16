package com.example.seafile_medicine.Controller;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

@RestController
@CrossOrigin("*")
public class DirectoryController {
    @Value("${app.auth}")
    String seafiletoken;
    @Value("${app.serverIP}")
    String serverIP;
    @Value("${app.SeaWebInterfacePort}")
    String SeaWebInterfacePort;
    @Value("${app.repoId}")
    String repoId;
    @Autowired
    RestTemplate restTemplate;

    //Create Directory
    @PostMapping(value = "/create_dir")
    public ResponseEntity<String> createUserDir(@RequestParam(value = "repo_name") String repo_name) {

        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api/v2.1/repos/"+repoId+"/dir/?p=/"+repo_name;
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

    //List library
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @GetMapping(value = "/all_files_details")
    public String all_files_details(){
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/dir/?p=/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        JSONArray jsonArray = new JSONArray(response);
        if(jsonArray.length()==0)
            return "Currently library is empty";
        return response;
    }

    //list items in a specified directory
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @GetMapping(value = "/specified_dir_details")
    public String specified_dir_details(@RequestParam(value = "repo_name") String repo_name){
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/dir/?p=/"+repo_name;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        JSONArray jsonArray = new JSONArray(response);
        if(jsonArray.length()==0)
            return "Currently directory is empty";
        return response;
    }

    //Rename Directory
    @PostMapping(value = "/rename_dir")
    public ResponseEntity<String> renameUserDirectory(@RequestParam(value = "new_name") String new_name, @RequestParam(value = "existing_name") String existing_name) {

        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api/v2.1/repos/"+repoId+"/dir/?p=/"+existing_name;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4;");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("operation", "rename");
        map.add("newname", new_name);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response;

    }

}
