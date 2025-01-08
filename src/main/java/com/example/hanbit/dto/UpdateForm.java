package com.example.hanbit.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateForm {
    // 비밀번호 (영어, 숫자, 특수문자 3가지 조합, not null)
    @NotNull(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$", message = "비밀번호는 영어, 숫자, 특수문자가 모두 포함되어야 합니다.")
    private String password;

    // 비상 연락처 (not null)
    @NotNull(message = "전화번호는 필수입니다.")
    private String tel;

    // 외국인 여부 (T/F, not null)
    @NotNull(message = "외국인 여부는 필수입니다.")
    private boolean foreignYN;
}
