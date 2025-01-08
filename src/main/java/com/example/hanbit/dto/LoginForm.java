package com.example.hanbit.dto;

import javax.validation.constraints.NotBlank;

public class LoginForm {
    @NotBlank(message = "Username is required")
    private String login_username;

    @NotBlank(message = "Password is required")
    private String login_password;

    public String getLogin_username() {
        return login_username;
    }

    public void setLogin_username(String login_username) {
        this.login_username = login_username;
    }

    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
    }
}
