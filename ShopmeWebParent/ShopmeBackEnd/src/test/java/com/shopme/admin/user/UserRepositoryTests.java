package com.shopme.admin.user;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.intThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private	UserRepository repository;
	
	@Autowired
	private TestEntityManager manager;

	@Test
	public void testCreateUser() {
	Role role=manager.find(Role.class, 1);
		User user=new User("duan1@gmail.com","name2020","nam","ha minh");
		user.addRole(role);
		//User save=repository.save(user);
		//assertThat(save.getId()).isGreaterThan(0);

	}
	
	@Test
	public void testCreateUserTwo() {
		
		User user=new User("john@gmail.com","mario123","mario","john");
		Role role=new Role(3);
		Role role2=new Role(5);
		user.addRole(role);
		user.addRole(role2);
		//User saveUser=repository.save(user);
	//	assertThat(saveUser.getId()).isGreaterThan(0);

	}
	
	@Test
	public void testListAllUser() {
		List<User>users=repository.findAll();
		users.forEach(x->System.out.println(x));
		
		
	}
	
	@Test
	public void testUserGetById() {
	
		User user=repository.findById(1).get();
		System.out.println(user);
		
	}
	
	@Test
	public void testUpdateUser() {
		
		User user=repository.findById(1).get();
		user.setEmail("ko@gmail.com");
		repository.save(user);
	}
	
	@Test
	public void testUpdateUserRole() {
		
		User user=repository.findById(1).get();
		Role role=new Role(1);
		Role role2=new Role(5);
		user.getRoles().remove(role);
		user.getRoles().add(role2);
		repository.save(user);
	}
	
	@Test
	public void testDeleteUser() {
		
	
		repository.deleteById(8);
	}
	
	@Test
	public void findByEmail() {
		String email="tuan123@gmail.com";
		User user=repository.getByEmail(email);
		assertThat(user).isNotNull();
		
	}
	
	@Test
	public void testCountById() {
		
		Integer idInteger=1;
		Long coutLong=repository.countById(idInteger);
		System.out.println(coutLong);
	}
	
	
	@Test
	public void testDisableUser() {
		
		Integer idInteger=10;
		repository.updateEnabledSatus(idInteger, true);
	}
	
	@Test
	public void testListFirstPage() {
		
		int pageNumber=0;
		int pageSize=4;
		Pageable pageable=PageRequest.of(pageNumber, pageSize);
		
		Page<User>page=repository.findAll(pageable);
		
		List<User>lUsers=page.getContent();
		lUsers.forEach(x->System.out.println(x));
		
		assertThat(lUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUser() {
		String keywordString="bruce";
		int pageNumber=0;
		int pageSize=4;
		Pageable pageable=PageRequest.of(pageNumber, pageSize);
		
		Page<User>page=repository.findAll(keywordString,pageable);
		
		List<User>lUsers=page.getContent();
		lUsers.forEach(x->System.out.println(x));
		
		assertThat(lUsers.size()).isGreaterThan(0);
	}


}
