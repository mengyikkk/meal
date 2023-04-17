package com.meal.common.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class UserInfo {
    private String nickName;
    private String avatarUrl;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String mobile;

    private Byte  gender;

    public String getNickName() {
        return nickName;
    }

    public UserInfo setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserInfo setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public UserInfo setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public Byte getGender() {
        return gender;
    }

    public UserInfo setGender(Byte gender) {
        this.gender = gender;
        return this;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public UserInfo setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }
}
