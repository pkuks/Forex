package com.singtel.Forex.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.singtel.Forex.ForexController;
import com.singtel.Forex.model.Forex;
import com.singtel.Forex.service.ForexService;


@RunWith(SpringRunner.class)
@WebMvcTest(ForexController.class)
public class ForexControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ForexControllerTest.class);

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ForexService forexService;
	
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

	}
	
	@Test
	public void givenDate_whenGetAllForex_thenReturnJsonArray() throws Exception {
		String sDate = "2017-01-10";
		Forex chf = new Forex(date, "CHF", 1.24);
		Forex sgd = new Forex(date, "SGD", 0.94);
		Forex[] forexList = {chf,sgd};
		given(forexService.findByDate(date)).willReturn(forexList);
		
		mvc.perform(get("/forex")
				.param("sDate", sDate)
				.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].currency", is("CHF")))
				.andExpect(jsonPath("$[0].value", is(1.24)))
				.andExpect(jsonPath("$[1].currency", is("SGD")))
				.andExpect(jsonPath("$[1].value", is(0.94)))
				;

	}

	@Test
	public void givenDateAndCurrencies_whenGetForex_thenReturnJsonArray() throws Exception {
		String sDate = "2017-01-10";
		String fromCurrency="CHF";
		String toCurrency="SGD";
		Double rate=1.24/0.94;
		
		given(forexService.getForex(date, fromCurrency, toCurrency)).willReturn(rate);
		
		MvcResult result = mvc.perform(get("/getForex")
				.param("sDate", sDate)
				.param("fromCcy", fromCurrency)
				.param("toCcy", toCurrency)
				.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andReturn();
		String content = result.getResponse().getContentAsString();
		logger.info("result : " + result.toString());
		logger.info("response : " + result.getResponse().toString());
		logger.info("content : " + content);
		assertEquals("1.32",content);

	}
	
	@Test
	public void givenDateRangeAndCurrency_whenGetForexForDateRange_thenReturnJsonArray() throws Exception {
		
		String sDate = "2017-01-10";
		String sDate1 = "2017-01-11";
		Forex chf = new Forex(date, "CHF", 1.24);
		Forex chf1 = new Forex(date1, "CHF", 1.34);
		Forex[] forexList = {chf,chf1};
		String currency="CHF";
		
		given(forexService.findForexByDateRange(date, date1, currency)).willReturn(forexList);
		
		mvc.perform(get("/getForexForDateRange")
				.param("sFromDate", sDate)
				.param("sToDate", sDate1)
				.param("ccy", currency)
				.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].currency", is("CHF")))
				.andExpect(jsonPath("$[0].value", is(1.24)))
				.andExpect(jsonPath("$[1].currency", is("CHF")))
				.andExpect(jsonPath("$[1].value", is(1.34)))
				;
	}
	
}
