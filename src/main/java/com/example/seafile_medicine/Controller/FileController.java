package com.example.seafile_medicine.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
@CrossOrigin("*")
@RestController
public class FileController{
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
    @GetMapping(value = "/file_search")
    public String fileSearch(@RequestParam(value = "file_name") String file_name){
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api/v2.1/search-file/?repo_id="+repoId+"&q="+file_name;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }

    //download specified file by filename in a user repository
    @GetMapping(value = "/download_file")
    public String getFile(@RequestParam(value = "file_name") String file_name){
        String res="";
        try {
            String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/file/?p=/"+file_name+"&reuse=1";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", seafiletoken);
            headers.set("Accept", "application/json; indent=4");
            headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            res= restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        }
        catch (Exception e){
            res="File not exists in the server";
            //System.out.println(res);
            e.printStackTrace();
        }
        return res;
    }

    //get specified file details in library
    @GetMapping(value = "/get_file_details")
    public String getFileDetail(@RequestParam (value = "file_name") String file_name) {
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/file/detail/?p=/" + file_name;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }

    //Rename file in the library
    @PostMapping(value = "/rename_file")
    public ResponseEntity<String> createDirectory(@RequestParam(value = "new_name") String new_name, @RequestParam(value = "existing_name") String existing_name) {

        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api/v2.1/repos/"+repoId+"/file/?p=/"+existing_name;
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

    //create share link function
    public ResponseEntity<String> createShareLink(String file_name){
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api/v2.1/share-links/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4;");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("repo_id", repoId);
        map.add("path", "/"+file_name);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response;
    }

    //get share link
    @GetMapping(value = "/view_file")
    public String shareLinkFile(@RequestParam (value = "file_name") String file_name){
        String res = "";
        try{
            getFileDetail(file_name);
            try{
                createShareLink(file_name);
            }
            catch (Exception e){
                e.getStackTrace();
            }
            String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api/v2.1/share-links/?repo_id="+repoId+"&path=/"+file_name;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", seafiletoken);
            headers.set("Accept", "application/json; indent=4");
            headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            res= restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        }
        catch (Exception e){
            res="File not exists in the server";
            //System.out.println(res);
            e.printStackTrace();
        }
        return res;
    }

    //remove specified file details in library
    @DeleteMapping (value = "/remove_file")
    public String remove_file(@RequestParam (value = "file_name") String file_name) {
        String res="";
       try {
           getFileDetail(file_name);
           String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/file/?p=/" + file_name;
           HttpHeaders headers = new HttpHeaders();
           headers.set("Authorization", seafiletoken);
           headers.set("Accept", "application/json; indent=4");
           headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
           HttpEntity<String> entity = new HttpEntity<>(headers);
           res=restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class).getBody();
       }
       catch (Exception e){
           res="File not exists in the server";
           //System.out.println(res);
           e.printStackTrace();
       }
       return res;
    }

}

//
//e.getStackTrace(); catch (Exception e){
//        res="File not exists in the server";
//        e.getStackTrace();
//        }
//        This code snippet is a Java catch block for an exception.
//
//        When an exception occurs in the try block, the catch block is executed. In this case, the catch block catches an exception of type Exception.
//
//        The first line of the catch block is assigning the string "File not exists in the server" to the variable 'res'. This line of code will only execute if an exception of type Exception is caught.
//
//        The second line in the catch block is calling the getStackTrace() method on the caught Exception object 'e'. This method returns an array of stack trace elements, each representing one stack frame. This information can be used for debugging purposes to determine where the exception occurred in the code.
//
//        However, in this case, the stack trace is not being used for any specific purpose, it's just being printed to the console. If you want to print the stack trace to the console, you can use the following code:
//
//        php
//        Copy code
//        catch (Exception e){
//        res="File not exists in the server";
//        e.printStackTrace();
//        }
//        The printStackTrace() method prints the stack trace to the console, along with the exception message and class name.