package com.carspot.app.repository;

import com.carspot.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmailAddress(String emailAddress);
    Boolean existsByEmailAddress(String emailAddress);
    @Query("SELECT u.emailVerified FROM User u WHERE u.emailAddress = ?1")
    Boolean findEmailVerifiedByEmailAddress(String emailAddress);




}
