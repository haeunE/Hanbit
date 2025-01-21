package com.example.hanbit.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanbit.domain.Place;
import com.example.hanbit.repository.PlaceRepository;

@RestController
public class PlaceController {
	@Autowired 
	private PlaceRepository placeRepository;
	
	@GetMapping("/places/nightlist")
	public ResponseEntity<List<Place>> getPlacesByCategory(@RequestParam("contentId") int contentId) {
        // typeid로 장소 목록 조회
        List<Place> places = placeRepository.findByTypeid(contentId);
        
        if (places.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(places);
    }
	
}
