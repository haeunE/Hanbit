package com.example.hanbit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanbit.domain.Place;

public interface PlaceRepository extends JpaRepository<Place, Long>{
	List<Place> findByTitleContainingIgnoreCase(String title);
	List<Place> findByTypeid(int typeid);
}
