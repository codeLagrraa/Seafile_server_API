package com.example.seafile_medicine.Controller;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

@RestController
@CrossOrigin("*")
public class UploadController {
    @Value("${app.auth}")
    String seafiletoken;
    @Value("${app.serverIP}")
    String serverIP;
    @Value("${app.SeaWebInterfacePort}")
    String SeaWebInterfacePort;
    @Value("${app.repoId}")
    String repoId;
    @Value("${app.storageFolderPath}")
    String storageFolderPath;
    @Autowired
    RestTemplate restTemplate;

    //file uploading to medicine repo in seafile server
    HttpHeaders httpHeaders = new HttpHeaders();
    public String UploadLink(){
        httpHeaders.set("Authorization", seafiletoken);
        httpHeaders.set("Accept", "application/json; indent=4");
        httpHeaders.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        String url = "http://"+serverIP+":"+SeaWebInterfacePort+"/api2/repos/"+repoId+"/upload-link/?p=/";
        //http://localhost:8000/api2/repos/f3316573-01d0-4ee8-bf76-04c84e0dac4b/upload-link/?p=/foo"
        try {
            String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    @PostMapping(value = "/fileUpload")
    public String fileUpload(@RequestParam("file")MultipartFile file){
        httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", seafiletoken);
        httpHeaders.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);
        httpHeaders.add("Content-Type", "multipart/form-data; boundary=---boundaryzx");
        //query para
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("parent_dir", "/");
        body.add("file", new FileSystemResource(convert(file,storageFolderPath)));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);

        //upload link
        StringBuffer requested_url= new StringBuffer(UploadLink());
        requested_url.deleteCharAt(requested_url.length()-1);
        requested_url.deleteCharAt(0);
        String url = requested_url.toString();

        try {
            restTemplate.postForObject(url,requestEntity,String.class);
//            clean file
            cleanLocal(file);
            return "Successfully uploaded";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private void cleanLocal(MultipartFile file) {
        String path=storageFolderPath;
        File filex = new File(path);
        File[] files = filex.listFiles();
        for (File f:files)
        {
            if (f.isFile() && f.exists() && f.getName().equals(file.getOriginalFilename())) {
                f.delete();
                System.out.println("Cleaned");
            }
        }
    }

    public static File convert(MultipartFile file, String storagePath){
        String pathname = storagePath+file.getOriginalFilename();
        File path = new File(pathname);
        try {
            FileOutputStream output = new FileOutputStream(path);
            output.write(file.getBytes());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
