package com.carspot.app.repository;

import com.carspot.app.entity.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAvatarRepository extends JpaRepository<UserAvatar, String> {
}
