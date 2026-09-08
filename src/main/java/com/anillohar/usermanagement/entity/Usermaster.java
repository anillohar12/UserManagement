package com.anillohar.usermanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "USER_MASTER")
@Data
public class Usermaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String fullname;
    private String email;
    private Long mobile;
    private String gender;
    private LocalDate dob;
    private Long ssn;
    private String password;
    private String accStatus;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private LocalDate createBy;
    private LocalDate updateBy;
}