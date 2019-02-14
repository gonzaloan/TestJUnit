package com.sodexo.junit.springjunittest.service;

import java.util.List;

import com.sodexo.junit.springjunittest.domain.Arrival;

public interface ArrivalService {

	public List<Arrival> getAllArrivals();
	public Arrival getArrivalById(Integer id);
	
}
