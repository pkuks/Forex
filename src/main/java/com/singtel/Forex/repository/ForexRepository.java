package com.singtel.Forex.repository;

import java.util.Date;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.singtel.Forex.model.Forex;

@Repository
public interface ForexRepository extends JpaRepository<Forex, Integer>{
	
	@Cacheable("rates")
	Forex[] findByDate(Date date);
	
	Forex findByDateAndCurrency(Date date, String currency);
	
	@Query(value="select * from forex f where f.date >= :fromDate and f.date <= :toDate and f.currency = :currency",
			nativeQuery=true)
	Forex[] findForexByDateRange(
			@Param("fromDate") Date fromDate, 
			@Param("toDate") Date toDate, 
			@Param("currency") String currency);

}
