package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	
	@Autowired
	RoleRepository repository;
	
	@Test
	public void testCreateFirstRole() {
		
		Role role=new Role("Admin", "manage everything");
		Role saveRole= repository.save(role);
		assertThat(saveRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		
		Role roleSale=new Role("Salesperson", "manage poduct price,customer,"
				+ "shipping,orders and sale report");
		
		Role roleEditor=new Role("Editor", "manage categories,brands,"
				+ "products,articles and menu");
		
		Role roleShiper=new Role("Shipper", "view products,view orders,"
				+ "and update order status");
		
		Role roleAssistant=new Role("Assistant", "magane question and reviews"
				);
		List<Role>roles=Arrays.asList(roleSale,roleEditor,roleShiper,roleAssistant);
		 repository.saveAll(roles);
		
	}


}
