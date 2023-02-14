package com.example.seafile_medicine.Controller;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
public class FileHistory {
    @Value("${app.auth}")
    String seafiletoken;
    @Autowired
    RestTemplate restTemplate;

    //Get File History
    @GetMapping(value = "/getHistory")
    public String getFileHistory(@RequestParam(value = "fileName") String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("http://localhost:8000/api/v2.1/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/file/history/?path=/foo/" + fileName, HttpMethod.GET, entity, String.class).getBody();
    }

    //get history If value of next_start_commit is NOT false, it means that there are more file history
    @GetMapping(value = "/getHistoryMore")
    public String getFileHistoryMore(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "next_start_commit") String next_start_commit) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("http://localhost:8000/api/v2.1/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/file/history/?path=/foo/"+fileName+"&commit_id="+next_start_commit, HttpMethod.GET, entity, String.class).getBody();
    }

    //Restore File From Historyb
    @PostMapping(value = "/restoreFile")
    public ResponseEntity<String> restoreFromHistory(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "commit_id") String commit_id) {

        String url = "http://localhost:8000/api/v2.1/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/file/?p=/foo/"+fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4;");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("operation", "revert");
        map.add("commit_id", commit_id);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response;
    }

    //Download File From a Revision
    @GetMapping(value = "/restoreFileFromRevision")
    public String restoreFile(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "commit_id") String commit_id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("http://localhost:8000/api2/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/file/revision/?p=/foo/"+fileName+"&commit_id="+commit_id,
                HttpMethod.GET, entity, String.class).getBody();
    }

    //search for a file - get latest - Name based api
    @GetMapping(value = "/searchForLatest")
    public String searchForLatest(@RequestParam(value = "fileName") String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String res = restTemplate.exchange("http://localhost:8000/api/v2.1/search-file/?repo_id=baeb1f6e-0981-4295-8234-aa93f2e6bc1a&q="+ fileName, HttpMethod.GET, entity, String.class).getBody();
        System.out.println("res->Xtype = "+ res.getClass().getName());
        System.out.println(res);

        //Converting jsonData(JSON object string) string into JSON object
        JSONObject jsnobject = new JSONObject(res);
        System.out.println("jsnobject->type = "+ jsnobject.getClass().getName());
        System.out.println("JSON Object");
        System.out.println(jsnobject);

        //Getting "data" JSON array from the JSON object
        JSONArray jsonArray = jsnobject.getJSONArray("data");
        System.out.println("jsonArray->type = "+ jsonArray.getClass().getName());
        System.out.println("JSON Array");
        System.out.println(jsonArray);

        //Creating an empty ArrayList of type Object
        List<Object> listdata = new ArrayList<Object>();

        //Checking whether the JSON array has some value or not
        if (jsonArray != null) {
            //Iterating JSON array
            for (int i=0;i<jsonArray.length();i++){
                //Adding each element of JSON array into ArrayList
                listdata.add(jsonArray.get(i));
            }
        }

        System.out.println("Each element of ArrayList");
        for(int i=0; i<listdata.size(); i++) {
            //Printing each element of ArrayList
            System.out.println(listdata.get(i));
        }

        return listdata.get(0).toString();
    }

    //search for a file by name - list all - name based api
    @GetMapping(value = "/searchFor")
    public String searchFor(@RequestParam(value = "fileName") String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String res = restTemplate.exchange("http://localhost:8000/api/v2.1/search-file/?repo_id=baeb1f6e-0981-4295-8234-aa93f2e6bc1a&q="+ fileName, HttpMethod.GET, entity, String.class).getBody();
        System.out.println("res->type = "+ res.getClass().getName());

        //Converting jsonData string into JSON object
        JSONObject jsnobject = new JSONObject(res);


        //Getting "data" JSON array from the JSON object
        JSONArray jsonArray = jsnobject.getJSONArray("data");

        //Creating an empty ArrayList of type Object
        List<Object> listdata = new ArrayList<Object>();

        //Checking whether the JSON array has some value or not
        if (jsonArray != null) {
            //Iterating JSON array
            for (int i=0;i<jsonArray.length();i++){
                //Adding each element of JSON array into ArrayList
                listdata.add(jsonArray.get(i));
            }
        }
//
//        System.out.println("Each element of ArrayList");
//        for(int i=0; i<listdata.size(); i++) {
//            //Printing each element of ArrayList
//            System.out.println(listdata.get(i));
//        }

        return listdata.toString();
    }

    //versions
    @GetMapping(value = "/searchForUniqueId")
    public String searchByUniqueId(@RequestParam(value = "fileName") String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String enteredFileDetails = restTemplate.exchange("http://localhost:8000/api2/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/file/detail/?p=/foo/" + fileName, HttpMethod.GET, entity, String.class).getBody();
        String res = restTemplate.exchange("http://localhost:8000/api2/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/dir/?p=/foo", HttpMethod.GET, entity, String.class).getBody();

        //get the entered file's file id (String)
        JSONObject jsnobject = new JSONObject(enteredFileDetails);
        String fUniqId = jsnobject.getString("id");

        JSONArray jsonArray = new JSONArray(res);
        List<String> list = new ArrayList<>();

        String fUniqIdR;
        String Element;

        if (jsonArray != null) {
            int len = jsonArray.length();
            System.out.println("Length = "+ len);
            for (int i=0;i<len;i++){
                Element = jsonArray.get(i).toString();
                JSONObject jsnobjectR = new JSONObject(Element);
                fUniqIdR = jsnobjectR.getString("id");
                String k = jsonArray.get(i).toString();
                if(fUniqIdR.equals(fUniqId))
                    { list.add(k);}
            }
        }
        return list.toString();
    }


    //latest version
    @GetMapping(value = "/searchForLatestFile")
    public String searchForLatestFile(@RequestParam(value = "fileName") String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String enteredFileDetails = restTemplate.exchange("http://localhost:8000/api2/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/file/detail/?p=/foo/" + fileName, HttpMethod.GET, entity, String.class).getBody();
        String res = restTemplate.exchange("http://localhost:8000/api2/repos/baeb1f6e-0981-4295-8234-aa93f2e6bc1a/dir/?p=/foo", HttpMethod.GET, entity, String.class).getBody();

        //get the entered file's file id (String)
        JSONObject jsnobject = new JSONObject(enteredFileDetails);
        String fUniqId = jsnobject.getString("id");

        JSONArray jsonArray = new JSONArray(res);
        List<Integer> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        String fUniqIdR;
        String Element;
        Integer mtime;

        if (jsonArray != null) {
            int len = jsonArray.length();
            System.out.println("Length = "+ len);
            for (int i=0;i<len;i++){
                Element = jsonArray.get(i).toString();
                JSONObject jsnobjectR = new JSONObject(Element);
                fUniqIdR = jsnobjectR.getString("id");
                if(fUniqIdR.equals(fUniqId))
                {
                     mtime = (Integer) jsnobjectR.getInt("mtime");
                     list.add(mtime);
                }
            }

            Collections.sort(list);
            Integer latestmtime = list.get(list.size()-1);
            Integer filemTime;

            for (int j=0;j<len;j++){
                Element = jsonArray.get(j).toString();
                JSONObject jsnobjectR = new JSONObject(Element);
                filemTime = jsnobjectR.getInt("mtime");
                String k = jsonArray.get(j).toString();
                if(filemTime.equals(latestmtime))
                {
                    list2.add(k);
                }
            }

        }

        return list2.toString();
    }
}



