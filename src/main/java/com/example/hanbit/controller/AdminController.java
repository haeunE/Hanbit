package com.example.hanbit.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.hanbit.domain.Place;
import com.example.hanbit.domain.User;
import com.example.hanbit.repository.PlaceRepository;
import com.example.hanbit.service.AdminService;
import com.example.hanbit.service.JwtService;
import com.example.util.InvalidFileException;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')") // ADMIN Role만 접근 가능
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PlaceRepository placeRepository;

    // 파일 업로드 시 검증과 처리를 담당하는 메서드
    @PostMapping("/csv/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        try {
            validateCsvFile(file);  // 파일 검증 로직을 별도로 분리
            adminService.savePlacesFromCsv(file);  // 파일 처리 로직
            return ResponseEntity.ok("CSV 파일 업로드 성공!");
        } catch (InvalidFileException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CSV 처리 중 오류 발생: " + e.getMessage());
        }
    }

    // CSV 파일 검증 메서드
    private void validateCsvFile(MultipartFile file) throws InvalidFileException {
        if (file.isEmpty()) {
            throw new InvalidFileException("파일이 비어 있습니다.");
        }
        if (!file.getContentType().equals("text/csv")) {
            throw new InvalidFileException("CSV 파일만 업로드 가능합니다.");
        }

        // 파일 크기 제한 (CSV 파일은 최대 50MB)
        long maxCsvSize = 50 * 1024 * 1024; // 50MB
        if (file.getSize() > maxCsvSize) {
            throw new InvalidFileException("CSV 파일 크기는 50MB를 초과할 수 없습니다.");
        }
    }
    
    // 장소 검색
    @GetMapping("/places/serch")
    public List<Place> getPlaces(@RequestParam(value = "query", defaultValue = "") String query) {
        if (query.isEmpty()) {
            return Collections.emptyList(); // 검색어가 없으면 빈 목록 반환
        } else {
            return placeRepository.findByTitleContainingIgnoreCase(query); // title이 검색어를 포함하는 장소만 반환
        }
    }

    @PutMapping("/places/{id}/update")
    public ResponseEntity<String> updatePlaceType(@PathVariable("id") Long id, @RequestBody Map<String, String> request) {
        Optional<Place> placeOpt = placeRepository.findById(id);
        
        if (!placeOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("장소를 찾을 수 없습니다.");
        }

        Place place = placeOpt.get();
        String typeId = request.get("typeId");

        // 장소 타입 업데이트
        place.setTypeid(Integer.parseInt(typeId));
        placeRepository.save(place);

        return ResponseEntity.ok("장소 타입이 업데이트되었습니다.");
    }
}

