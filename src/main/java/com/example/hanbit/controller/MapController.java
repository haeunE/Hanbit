package com.example.hanbit.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MapController {
    
    @Value("${map.api.client-id}")
    private String clientId;

    @Value("${map.api.client-secret}")
    private String clientSecret;

    @GetMapping("/location")
    public String getLocation(@RequestParam double latitude, @RequestParam double longitude) {
        String URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&coords=" 
                     + longitude + "," + latitude 
                     + "&sourcecrs=epsg:4326&orders=admcode&output=json";

        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-api-key-id", clientId);
        headers.set("x-ncp-apigw-api-key", clientSecret);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
                               URL, HttpMethod.GET, entity, String.class);
        
        System.out.println(response);
        
        return response.getBody();  // API 응답을 클라이언트로 반환
    }
}
