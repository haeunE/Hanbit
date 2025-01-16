package com.example.hanbit.controller;


import java.io.IOException;
import java.lang.foreign.Linker.Option;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.hanbit.domain.Photo;
import com.example.hanbit.domain.Review;
import com.example.hanbit.domain.User;
import com.example.hanbit.dto.ReviewEditForm;
import com.example.hanbit.dto.ReviewForm;
import com.example.hanbit.repository.ReviewRepository;
import com.example.hanbit.repository.UserRepository;
import com.example.hanbit.service.ReviewService;
import com.example.hanbit.service.UserService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@GetMapping("/find")
    public ResponseEntity<List<Review>> getReviewsByUserId(@RequestParam Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
	
	@GetMapping
	public ResponseEntity<?> getReviewsByPlaceAndType(@RequestParam String placeid, @RequestParam String typeid) {
		System.out.println(placeid);
		System.out.println(typeid);
	    try {
	        List<Review> reviews = reviewService.getReviewsByPlaceAndType(placeid, typeid);
	        if (reviews.isEmpty()) {
	            return new ResponseEntity<>("해당 조건에 맞는 리뷰가 없습니다.", HttpStatus.NOT_FOUND);
	        }
	        return ResponseEntity.ok(reviews);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
    
 // 리뷰 생성
    @PostMapping("/upload")
    public ResponseEntity<?> createReview(ReviewForm reviewForm, @RequestParam("photos") MultipartFile[] photos) {
        System.out.println(reviewForm);
        try {
            Optional<User> optionalUser = userRepository.findById(reviewForm.getUserId());
            User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

            // Review 엔터티 생성 및 데이터 매핑
            Review review = new Review();
            review.setTitle(reviewForm.getTitle());
            review.setContent(reviewForm.getContent());
            review.setPlaceid(reviewForm.getPlaceid());
            review.setTypeid(reviewForm.getTypeid());
            review.setPlacetitle(reviewForm.getPlacetitle());
            review.setUser(user);

            // 사진 업로드 및 Photo 엔티티 생성 (사진이 있을 때만 처리)
            if (photos != null && photos.length > 0) {
                for (MultipartFile photo : photos) {
                    if (!photo.isEmpty()) {
                        // 고유 파일 이름 생성
                        String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();

                        // 파일 저장 경로 설정
                        Path filePath = Paths.get("upload/" + fileName);
                        Files.createDirectories(filePath.getParent());
                        Files.write(filePath, photo.getBytes());

                        // Photo 엔티티 생성 및 Review에 추가
                        Photo photoEntity = new Photo();
                        photoEntity.setUrl(fileName);
                        photoEntity.setReview(review); // review와 photo 연결
                        review.addPhoto(photoEntity); // review의 photos 리스트에 photo 추가
                    }
                }
            }

            // Review 저장
            reviewRepository.save(review);

            return new ResponseEntity<>("리뷰 생성 성공", HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("파일 업로드 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("리뷰 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

 // 리뷰 수정 및 이미지 삭제
    @PutMapping("/edit")
    public ResponseEntity<String> updateReview(@RequestParam Long reviewId, @RequestBody ReviewEditForm request) {
    	System.err.println(reviewId);
        try {
            reviewService.editReview(reviewId, request);
            return ResponseEntity.ok("리뷰 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 수정 실패");
        }
    }

    // 리뷰 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReview(@RequestParam Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
	
}
