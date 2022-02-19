package com.shopme.admin.setting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {

	
}
