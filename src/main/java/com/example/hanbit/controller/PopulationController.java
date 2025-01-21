package com.example.hanbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class PopulationController {

   private final RestTemplate restTemplate;

   @Value("${map.api.client-id}")
   private String clientId;

   @Value("${map.api.client-secret}")
   private String clientSecret;

   @Value("${X-Naver-Client-Id}")
   private String searchClientID;

   @Value("${X-Naver-Client-Secret}")
   private String searchClientSecret;

   @Value("${seoul.opendata.api.key}")
   private String seoulOpendata;

   @Autowired
   public PopulationController(RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
   }

   // 공통으로 사용되는 HttpHeaders 생성 메서드
   private HttpHeaders createHeaders(String clientId, String clientSecret) {
      HttpHeaders headers = new HttpHeaders();
      headers.set("X-Naver-Client-Id", clientId);
      headers.set("X-Naver-Client-Secret", clientSecret);
      return headers;
   }

   @GetMapping("/searchmap")
   public ResponseEntity<?> searchMap(@RequestParam String query) {
      String searchMapUrl = "https://openapi.naver.com/v1/search/local.json?query=" + query;
      HttpHeaders headers = createHeaders(searchClientID, searchClientSecret);

      HttpEntity<String> entity = new HttpEntity<>(headers);
      ResponseEntity<String> response = restTemplate.exchange(searchMapUrl, HttpMethod.GET, entity, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
         String body = response.getBody();
         return ResponseEntity.ok(body);
      } else {
         return ResponseEntity.status(500).body("Error: " + response.getStatusCode());
      }
   }

   @GetMapping("/geocode")
   public ResponseEntity<?> getGeocode(@RequestParam String address) {
      String geocodeApiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;
      HttpHeaders headers = createHeaders(clientId, clientSecret);
      headers.set("Accept", "application/json");

      HttpEntity<String> entity = new HttpEntity<>(headers);
      ResponseEntity<String> response = restTemplate.exchange(geocodeApiUrl, HttpMethod.GET, entity, String.class);

      return response.getBody() != null ? ResponseEntity.ok(response.getBody())
            : ResponseEntity.status(500).body("Geocoding failed.");
   }
}

