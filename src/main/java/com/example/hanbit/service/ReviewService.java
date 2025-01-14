package com.example.hanbit.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.hanbit.domain.Review;
import com.example.hanbit.domain.User;
import com.example.hanbit.repository.ReviewRepository;
import com.example.hanbit.repository.UserRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    private final String UPLOAD_DIR = "uploads/";

    // 모든 리뷰 가져오기
    public List<Review> getReviewsByPlaceAndType(String placeid, String typeid) {
        // ReviewRepository에서 placeid와 typeid로 필터링하여 리뷰 목록을 가져옵니다.
        return reviewRepository.findByPlaceidAndTypeid(placeid, typeid);
    }

    // 특정 리뷰 가져오기
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }


    // 리뷰 삭제
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Review not found with ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    // 사진 저장
    public String savePhoto(MultipartFile photo) throws IOException {
        // 고유 파일명 생성
        String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);

        // 저장 디렉터리 생성 (존재하지 않을 경우)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 저장
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(photo.getInputStream(), filePath);

        // 저장된 사진의 상대 경로 반환
        return UPLOAD_DIR + fileName;
    }
}
