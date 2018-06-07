package com.singtel.Forex.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.singtel.Forex.model.Forex;
import com.singtel.Forex.repository.ForexRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ForexRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ForexRepository forexRepository;
	
	public Date date = null;
	public Date date1 = null;
	
	@Before
	public void setUp() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		
		String sDate = "2017-01-10";
		String sDate1 = "2017-01-11";
		try {
			date = df.parse(sDate);
			date1 = df.parse(sDate1);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		//given
		Forex chf = new Forex(date, "CHF", 1.24);
		Forex sgd = new Forex(date, "SGD", 0.94);
		Forex chf1 = new Forex(date1, "CHF", 1.14);
		Forex sgd1 = new Forex(date1, "SGD", 0.84);
		entityManager.persist(chf);
		entityManager.persist(sgd);
		entityManager.persist(chf1);
		entityManager.persist(sgd1);
		entityManager.flush();
	}

	
	@Test
	public void findByDateTest() {
		//when
		Forex[] forexList = forexRepository.findByDate(date);
		//then
		assertEquals(2,forexList.length);
	}
	
	@Test
	public void findByDateAndCurrency() {
		//when
		String currency="CHF";
		Forex chf = forexRepository.findByDateAndCurrency(date, currency);
		//then
		assertTrue(date==chf.getDate() && currency==chf.getCurrency());
	}
	
	@Test
	public void findForexByDateRangeTest() {
		//when
		Forex[] forexList = forexRepository.findForexByDateRange(date, date1, "CHF");
		//then
				assertEquals(2,forexList.length);
		
	}
		
}
