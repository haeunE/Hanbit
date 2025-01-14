package com.example.hanbit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hanbit.domain.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long>{

}
