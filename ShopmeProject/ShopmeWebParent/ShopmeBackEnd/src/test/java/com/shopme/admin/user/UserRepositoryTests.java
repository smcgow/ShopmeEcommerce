package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUserWithOneRole() {
		Role adminRole = entityManager.find(Role.class, 1);
		User stephen = new User("stephen@email.com", "today1", "stephen", "mcgowan");
		stephen.addRole(adminRole);
		User savedUser = userRepository.save(stephen);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles() {
		Role assistantRole = new Role(5);
		Role editorRole = new Role(3);
		User aoife = new User("aoife@email.com", "today1", "aoife", "walsh");
		aoife.addRole(assistantRole);
		aoife.addRole(editorRole);
		User savedUser = userRepository.save(aoife);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		List<User> users = userRepository.findAll();
		users.forEach(System.out::println);
		assertThat(users.size()).isGreaterThan(0);
	}
	
	@Test
	public void testGetUserById() {
		Optional<User> stephen = userRepository.findById(1);
		System.out.println(stephen.get());
		assertThat(stephen.get()).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		Optional<User> stephenOpt = userRepository.findById(1);
		User stephen = stephenOpt.get();
		stephen.setEnabled(true);
		stephen.setEmail("updatestephen@email.com");
		userRepository.save(stephen);
	}
	
	@Test
	public void testUpdateUserRoles() {
		Optional<User> aoifeOpt = userRepository.findById(2);
		User aoife = aoifeOpt.get();
		Role editorRole = roleRepository.findById(3).get();
		aoife.getRoles().remove(editorRole);
		Role salesPerson = new Role(2);
		aoife.addRole(salesPerson);
		userRepository.save(aoife);
	}
	
	@Test
	public void testDeleteUser() {
		Optional<User> aoifeOpt = userRepository.findById(2);
		User aoife = aoifeOpt.get();
		userRepository.delete(aoife);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "stephen.mcgowan@live.ie";
		User user = userRepository.getUserByEmail(email);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Long countById = userRepository.countById(1);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 3;
		userRepository.updateUserEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 3;
		userRepository.updateUserEnabledStatus(id, true);
	}
}
