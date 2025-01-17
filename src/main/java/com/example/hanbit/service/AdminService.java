package com.example.hanbit.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.hanbit.domain.Place;
import com.example.hanbit.repository.PlaceRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class AdminService {
	@Autowired
	private PlaceRepository placeRepository;
	
	public void savePlacesFromCsv(MultipartFile file) throws IOException, CsvValidationException {
		 try (InputStream inputStream = file.getInputStream();
	            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8"); // 인코딩 설정
	            CSVReader csvReader = new CSVReader(reader)) {

	            List<Place> places = new ArrayList<>();
	            String[] nextLine;

	            // 첫 번째 줄(헤더)은 건너뛰기
	            csvReader.readNext();
	            
	            while ((nextLine = csvReader.readNext()) != null) {
	            	System.out.println(Arrays.toString(nextLine));
	                // CSV 데이터 매핑
	                Place place = new Place();
	                place.setServiceid(nextLine[0]);
	                place.setSevicecode(nextLine[1]);
	                place.setServicenum(nextLine[2]);
	                place.setOpen(nextLine[3].equals("1"));
	                place.setTel(nextLine[4]);
	                place.setAddnum(nextLine[5]);
	                place.setAddo(nextLine[6]);
	                place.setAddn(nextLine[7]);
	                place.setTitle(nextLine[8]);
	                place.setX(Double.parseDouble(nextLine[9]));
	                place.setY(Double.parseDouble(nextLine[10]));

	                places.add(place);
	            }

	            // 데이터베이스에 저장
	            placeRepository.saveAll(places);
	        }
    }
}
