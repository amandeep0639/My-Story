package com.example.mystory.util;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Author {

    public String author_name;
    public String email;
    public String phone;
    public String age;
    public String gender;
    Author() {
    }

    public Author(String author_name, String email, String phone, String age,String gender) {

        this.author_name = author_name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.gender=gender;
    }
}
