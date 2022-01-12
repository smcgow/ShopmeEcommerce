package com.shopme.admin.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer i);
	
	@Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
	@Modifying
	public void updateUserEnabledStatus(@Param("id") Integer id, @Param("enabled") boolean enabled);
}
