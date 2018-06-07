package com.singtel.Forex.service;

import java.util.Date;
import java.util.concurrent.Future;

import com.singtel.Forex.model.Forex;

public interface ForexService {
	
	Forex create(Date date, String currency, double value);
	
	Forex[] findByDate(Date date);
	
	double findByDateAndCurrency(Date date, String currency);
	
	double getForex(Date date, String fromCcy, String toCcy);
	
	Forex[] findForexByDateRange(Date fromDate, Date toDate, String currency);

	Future<Forex[]> findByDateAsync(Date date);

}
