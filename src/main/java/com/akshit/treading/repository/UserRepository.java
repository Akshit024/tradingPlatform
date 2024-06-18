package com.akshit.treading.repository;

import com.akshit.treading.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public User findByEmail(String email);
    User findByJwt(String jwt);

}
