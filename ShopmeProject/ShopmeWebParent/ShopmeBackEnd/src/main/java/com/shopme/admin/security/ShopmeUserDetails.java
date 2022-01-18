package com.shopme.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entity.User;

public class ShopmeUserDetails implements UserDetails {

    private static final long serialVersionUID = 2608772314307592464L;
	
    private User user;
	
	public ShopmeUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	public String getFullName() {
		return user.getFullName();
	}
	
	public void setFirstName(String firstName) {
		user.setFirstName(firstName);
	}
	
	public void setLastName(String lastName) {
		user.setLastName(lastName);
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

}
