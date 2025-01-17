package com.example.hanbit.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "places")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Place {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	// 장소 제목
	private String title;
    // X 좌표 (위치 좌표)
	private Double x; 
    // Y 좌표 (위치 좌표)
	private Double y;
	// 우편번호
	private String addnum;
	// 번지
	private String addo;
	// 도로명
	private String addn;
	// 전화번호
	private String tel;
	// (술집:101, 메이드카페:102, 클럽:103, 헌팅포차:104, 헌팅핫플:105, 카지노:106)
	private Integer typeid;
	// 개방서비스아이디
	private String serviceid;
	// 개방자치단체코드
	private String sevicecode;
	// 관리번호
	private String servicenum ;
	// 영업상태구분코드
	private boolean open;
	
	private String image;
}
