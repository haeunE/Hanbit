package com.example.hanbit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hanbit.domain.Review;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByPlaceidAndTypeid(String placeid, String typeid);
}
