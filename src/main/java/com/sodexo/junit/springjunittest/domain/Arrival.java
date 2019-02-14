package com.sodexo.junit.springjunittest.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="arrival")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Arrival implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column
//	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@Column
	private String city;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Arrival [id=" + id + ", city=" + city + "]";
	}

	public Arrival(String city) {
		super();
		this.city = city;
	}

	public Arrival() {
		super();
	}
	
	
	
}
