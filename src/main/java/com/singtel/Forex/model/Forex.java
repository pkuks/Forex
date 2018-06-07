package com.singtel.Forex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="forex")
public class Forex {

	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name="date")
	@NotNull
	@JsonFormat(pattern="yyyy-mm-dd", timezone="UTC")
	private Date date;
	
	@Column(name="currency")
	@NotNull
	@Size(max=3)
	private String currency;
	
	//@Column(name="value", precision=10, scale=2)
	@Column(name="value", columnDefinition="Decimal(10,2) default '1.00'")
	@NotNull
	private double value;
	
	public Forex() {
		super();
	}
	
	
	

	public Forex(Date date, String currency, double value) {
		super();
		this.date = date;
		this.currency = currency;
		this.value = value;
	}




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Forex [id=" + id + ", date=" + date + ", currency=" + currency + ", value=" + value + "]";
	}
	
	
	
	
	
}
