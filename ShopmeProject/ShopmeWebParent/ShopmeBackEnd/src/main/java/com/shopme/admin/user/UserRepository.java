package com.shopme.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer i);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.firstName,' ',u.lastName,' ',u.email,' ') like %:keyword%") 
	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
	@Modifying
	public void updateUserEnabledStatus(@Param("id") Integer id, @Param("enabled") boolean enabled);
}
