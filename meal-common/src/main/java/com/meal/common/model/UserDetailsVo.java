package com.meal.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class UserDetailsVo {
    private  String nickName;

    private  Byte gender;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate birthday;


    public String getNickName() {
        return nickName;
    }

    public UserDetailsVo setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public Byte getGender() {
        return gender;
    }

    public UserDetailsVo setGender(Byte gender) {
        this.gender = gender;
        return this;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public UserDetailsVo setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }


}
