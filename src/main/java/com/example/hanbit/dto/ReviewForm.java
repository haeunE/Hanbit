package com.example.hanbit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ReviewForm {
	private Long userId;
    private String title;
    private String content;
    private String placeid;
    private String typeid;
}
