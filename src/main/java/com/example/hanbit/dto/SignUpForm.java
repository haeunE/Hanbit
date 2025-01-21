package com.example.hanbit.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
	// 회원 ID (8자 이상, 영문, 숫자만 가능, not null)
    @NotNull(message = "회원 ID는 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,}$", message = "회원 ID는 8자 이상, 영문과 숫자만 가능합니다.")
//    @Column(unique = true) // 아이디 중복 방지
    private String username;

    // 비밀번호 (영어, 숫자, 특수문자 3가지 조합, not null)
    @NotNull(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$", message = "비밀번호는 영어, 숫자, 특수문자가 모두 포함되어야 합니다.")
    private String password;

    // 이름 (영어 또는 한글만 가능, 섞어쓰기 불가, not null)
    @NotNull(message = "이름은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z]+$|^[가-힣]+$", message = "이름은 영어 또는 한글만 가능하며, 섞어쓸 수 없습니다.")
    private String name;

    // 이메일 (이메일 형식 준수, not null)
    @NotNull(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일을 입력해주세요.")
//    @Column(unique = true) // 이메일 중복 방지
    private String email;

    // 비상 연락처 (not null)
    @NotNull(message = "전화번호는 필수입니다.")
//    @Column(unique = true) // 전화번호 중복 방지
    private String tel;

    // 외국인 여부 (T/F, not null)
    @NotNull(message = "외국인 여부는 필수입니다.")
    private boolean foreignYN;
}