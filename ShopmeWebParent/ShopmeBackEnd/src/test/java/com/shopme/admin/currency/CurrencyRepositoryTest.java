package com.shopme.admin.currency;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTest {

	@Autowired
	private CurrencyRepository repo;
	
	@Test
	public void testCreateCurrencies() {
		
		List<Currency>currencies=Arrays.asList(
				new Currency("united states dollar", "$", "usd"),
				new Currency("british pound", "&", "gpb"),
				new Currency("japanese", "$", "usd"),
				new Currency("united states dollar", "$", "usd"),
				new Currency("united states dollar", "$", "usd")
				);
		
		repo.saveAll(currencies);
	}
}
