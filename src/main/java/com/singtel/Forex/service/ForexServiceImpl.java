package com.singtel.Forex.service;

import java.util.Date;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.singtel.Forex.model.Forex;
import com.singtel.Forex.repository.ForexRepository;

@Component
public class ForexServiceImpl implements ForexService{

	private static final Logger logger = LoggerFactory.getLogger(ForexServiceImpl.class);

	@Autowired
	public ForexRepository forexRepository;
	
	@Override
	public Forex create(Date date, String currency, double value) {
		Forex forex = new Forex(date, currency, value);
		forexRepository.save(forex);
		return forex;
	}

	@Override
	public Forex[] findByDate(Date date) {
		Forex[] forexList = forexRepository.findByDate(date);
		return forexList;
	}

	@Override
	public double findByDateAndCurrency(Date date, String currency) {
		Forex forex = forexRepository.findByDateAndCurrency(date, currency);
		double value=0.0;
		if (forex!=null) {
			value =  forex.getValue();
		}
		return value;
	}
	
	@Override
	public double getForex(Date date, String fromCcy, String toCcy) {
		double fromValue=findByDateAndCurrency(date,fromCcy);
		double toValue=findByDateAndCurrency(date,toCcy);
		double rate=0.0;
		
		if (toValue!=0.0) {
			rate=fromValue/toValue;
		}
		return rate;
	}

	@Override
	public Forex[] findForexByDateRange(Date fromDate, Date toDate, String currency) {
		Forex[] forexList = forexRepository.findForexByDateRange(fromDate, toDate, currency);
		return forexList;
	}
	
	@Async("threadPoolTaskExecutor")
	public Future<Forex[]> findByDateAsync(Date date) {
		logger.info("execute asynch method..." + Thread.currentThread().getName());
		try {
			Thread.sleep(5000);
			Forex[] forexList = forexRepository.findByDate(date);
			return new AsyncResult<Forex[]>(forexList);
		} catch (InterruptedException e) {
			
		}
		return null;
	}

}
