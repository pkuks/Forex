package com.singtel.Forex.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.singtel.Forex.model.Forex;
import com.singtel.Forex.repository.ForexRepository;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-integrationtest.properties")
public class ForexControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;
	
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
	}

	@Test
	public void givenDate_whenGetAllForex_thenStatus200() throws Exception {
		String sDate = "2017-01-10";
		Forex chf = new Forex(date, "CHF", 1.24);
		Forex sgd = new Forex(date, "SGD", 0.94);
		forexRepository.save(chf);
		forexRepository.save(sgd);
		
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

}
