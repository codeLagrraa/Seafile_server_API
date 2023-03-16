package com.example.seafile_medicine.Controller;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class FileHistory {
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

    //Get File History
    @GetMapping(value = "/get_history")
    @CrossOrigin("*")
    public String getFileHistory(@RequestParam(value = "file_name") String file_name) {
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api/v2.1/repos/"+repoId+"/file/history/?path=/" + file_name;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }

    //get history if the value of next_start_commit is NOT false, it means there is more file history
    @GetMapping(value = "/get_history_more")
    public String getFileHistoryMore(@RequestParam(value = "file_name") String file_name, @RequestParam(value = "next_start_commit") String next_start_commit) {
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api/v2.1/repos/"+repoId+"/file/history/?path=/"+file_name+"&commit_id="+next_start_commit;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }

    //Restore File From History
    @PostMapping(value = "/restore_file_from_revision")
    public ResponseEntity<String> restoreFromHistory(@RequestParam(value = "file_name") String file_name, @RequestParam(value = "commit_id") String commit_id) {
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api/v2.1/repos/"+repoId+"/file/?p=/"+file_name;
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

    //Download File From Revision
    @GetMapping(value = "/download_file_from_revision")
    public String restoreFile(@RequestParam(value = "file_name") String file_name, @RequestParam(value = "commit_id") String commit_id) {
        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/file/revision/?p=/"+file_name+"&commit_id="+commit_id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url,HttpMethod.GET, entity, String.class).getBody();
    }


    //all versions using unique id
    @GetMapping(value = "/versions")
    public String searchByUniqueId(@RequestParam(value = "file_name") String file_name) {
        String url1="http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/file/detail/?p=/" + file_name;
        String url2="http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/dir/?p=/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String enteredFileDetails = restTemplate.exchange(url1, HttpMethod.GET, entity, String.class).getBody();
        String allFileDetails = restTemplate.exchange(url2, HttpMethod.GET, entity, String.class).getBody();

        //get the entered file's file id (String)
        JSONObject jsnobject = new JSONObject(enteredFileDetails);
        String fUniqId = jsnobject.getString("id");

        JSONArray jsonArray = new JSONArray(allFileDetails);
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

    //latest version using unique id
    @GetMapping(value = "/latest_version")
    public String searchForLatestFile(@RequestParam(value = "file_name") String file_name) {
        String url1="http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/file/detail/?p=/" + file_name;
        String url2="http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/dir/?p=/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", seafiletoken);
        headers.set("Accept", "application/json; indent=4");
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String enteredFileDetails = restTemplate.exchange(url1, HttpMethod.GET, entity, String.class).getBody();
        String allFileDetails = restTemplate.exchange(url2, HttpMethod.GET, entity, String.class).getBody();

        //get the entered file's file id (String)
        JSONObject jsnobject = new JSONObject(enteredFileDetails);
        String fUniqId = jsnobject.getString("id");

        JSONArray jsonArray = new JSONArray(allFileDetails);
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



