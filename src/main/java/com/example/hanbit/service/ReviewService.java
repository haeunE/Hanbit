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

import com.example.hanbit.domain.Photo;
import com.example.hanbit.domain.Review;
import com.example.hanbit.domain.User;
import com.example.hanbit.dto.ReviewEditForm;
import com.example.hanbit.repository.PhotoRepository;
import com.example.hanbit.repository.ReviewRepository;
import com.example.hanbit.repository.UserRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PhotoRepository photoRepository;

    private final String UPLOAD_DIR = "uploads/";

    // 모든 리뷰 가져오기
    public List<Review> getReviewsByPlaceAndType(String placeid, String typeid) {
        // ReviewRepository에서 placeid와 typeid로 필터링하여 리뷰 목록을 가져옵니다.
        return reviewRepository.findByPlaceidAndTypeid(placeid, typeid);
    }

    // 특정 리뷰 가져오기
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }


    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("Review not found with ID: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
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
    
    public void editReview(Long reviewId, ReviewEditForm request) {
        // 리뷰 수정
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        reviewRepository.save(review);

        // 삭제된 이미지 처리
        if (request.getDeletedImages() != null && !request.getDeletedImages().isEmpty()) {
            for (Long imageId : request.getDeletedImages()) {
                Photo image = photoRepository.findById(imageId)
                        .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다."));
                photoRepository.delete(image); // 이미지 삭제
            }
        }
    }
}
