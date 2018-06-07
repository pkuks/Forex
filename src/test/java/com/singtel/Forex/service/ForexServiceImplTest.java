package com.singtel.Forex.service;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.singtel.Forex.model.Forex;
import com.singtel.Forex.repository.ForexRepository;

@RunWith(SpringRunner.class)
public class ForexServiceImplTest {

	@TestConfiguration
	static class ForexServiceImplTestContextConfiguration {
		@Bean
		public ForexService forexService() {
			return new ForexServiceImpl();
		}
	}
	
	@Autowired
	private ForexService forexService;
	
	@MockBean
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
			
		Mockito.when(forexRepository.findByDate(date)).thenReturn(new Forex[] {chf,sgd});
		
		Mockito.when(forexRepository.findByDateAndCurrency(date, "CHF")).thenReturn(chf);
		Mockito.when(forexRepository.findByDateAndCurrency(date, "SGD")).thenReturn(sgd);
		
		Mockito.when(forexRepository.findForexByDateRange(date, date1, "CHF")).thenReturn(new Forex[] {chf,chf1});
	}

	@Test
	public void whenValidDate_thenForexShouldBeFound() {
		//when
		Forex[] forexList = forexService.findByDate(date);
		//then
		assertEquals(2,forexList.length);
	}
	
	@Test
	public void getForexTest() {
		//when
		String fromCcy="CHF";
		String toCcy="SGD";
		//then
		assertEquals(1.32,forexService.getForex(date, fromCcy, toCcy),0.01);
	}
	
	@Test
	public void whenValidDateRangeAndCurrency_thenfindForexByDateRangeShouldBeFound() {
		//when
		String currency="CHF";
		//then
		assertEquals(2,forexService.findForexByDateRange(date, date1, currency).length);
	}
	
}
