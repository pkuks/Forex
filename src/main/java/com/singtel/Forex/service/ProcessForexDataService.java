package com.singtel.Forex.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.singtel.Forex.model.Forex;

@Service
public class ProcessForexDataService {
	private static final Logger log = 
			LoggerFactory.getLogger(ProcessForexDataService.class);
	
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	public ForexService forexService;
	
	@PostConstruct
	public void init() {
		
		try {
			Resource resource = resourceLoader.getResource("classpath:data/2017-01-01.txt");
			
			String sDate = "2017-01-01";
			DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
			Date date = df.parse(sDate);
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			log.debug("reading forex data file ");
			InputStream is = resource.getInputStream();
			log.info("fileStream : "  + is.toString());
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String line;
			String currency;
			double value;
			while ((line=br.readLine())!=null) {
				String[] splitStr = line.split("\\s+");
				currency = splitStr[1];
				value=Double.parseDouble(splitStr[4]);
				log.info(line + " Date : " + df.format(date) + " currency : " + currency + " value : " + value);
				forexService.create(date, currency, value);
			}
			br.close();
			
			Forex[] forexList = forexService.findByDate(date);
			for(Forex forex : forexList){
				log.info("Forex db : " + forex.toString());
				
			}
			
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}

