package com.anillohar.usermanagement.Repo;

import com.anillohar.usermanagement.entity.Usermaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMasterRepo extends JpaRepository<Usermaster, Integer> {

    Usermaster findByEmail(String email);
}