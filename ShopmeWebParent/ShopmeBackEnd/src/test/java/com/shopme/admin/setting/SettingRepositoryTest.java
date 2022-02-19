package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {

	@Autowired 
	private SettingRepository repo;
	
	@Test
	public void testCeateGenaralSettigns() {
		//Setting s=new Setting("site_name","shopme",SettingCategory.GENERAL);
		Setting l=new Setting("site_logo","shopme.png",SettingCategory.GENERAL);
		Setting c=new Setting("copyright","copyright (c) 2023 shopme ltd",SettingCategory.GENERAL);
		
		repo.saveAll(List.of(l,c));
	}
}
