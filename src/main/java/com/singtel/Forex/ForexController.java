package com.singtel.Forex;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.singtel.Forex.model.Forex;
import com.singtel.Forex.service.ForexService;

@RestController
public class ForexController {

	private static final Logger logger = LoggerFactory.getLogger(ForexController.class);
	
	@Autowired
	public ForexService forexService;
	
	@GetMapping(value="/forex")
	public Forex[] getAllForex(@RequestParam("sDate") String sDate) {
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		Date date = null;
		try {
			date = df.parse(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return forexService.findByDate(date);	
	}
	
	@GetMapping(value="/getForex")
	public String getForex(@RequestParam("sDate") String sDate,
			@RequestParam("fromCcy") String fromCcy,
			@RequestParam("toCcy") String toCcy) 
	{	
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		Date date = null;
		try {
			date = df.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		DecimalFormat df2 = new DecimalFormat(".##");
		double rate = forexService.getForex(date, fromCcy, toCcy);
		return df2.format(rate);
		
	}
	
	@GetMapping(value="/getForexForDateRange")
	public Forex[] getForexForDateRange(@RequestParam("sFromDate") String sFromDate,
			@RequestParam("sToDate") String sToDate,
			@RequestParam("ccy") String ccy) 
	{
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = df.parse(sFromDate);
			toDate=df.parse(sToDate);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return forexService.findForexByDateRange(fromDate, toDate, ccy);	
	}
	
	@GetMapping(value="/asyncForex")
	public Forex[] getAllAsyncForex(@RequestParam("sDate") String sDate) 
			throws InterruptedException, ExecutionException {
		
		logger.info("invoking async method.." + Thread.currentThread().getName());
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		Date date = null;
		try {
			date = df.parse(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Future<Forex[]> future = forexService.findByDateAsync(date);
		
		while (true) {
			if (future.isDone()) {
				logger.info("Result from async forex process - " + future.get());
				break;
			}
			logger.info("Continue...");
			Thread.sleep(1000);
		}
		
		return forexService.findByDate(date);	
	}
	
	
	
	
	
	

}
