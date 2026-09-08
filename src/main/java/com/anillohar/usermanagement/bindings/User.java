package com.anillohar.usermanagement.bindings;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private String fullname;
    private String email;
    private Long mobile;
    private String gender;
    private LocalDate dob;
    private Long ssn;
}