package com.sodexo.junit.springjunittest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sodexo.junit.springjunittest.domain.Arrival;
import com.sodexo.junit.springjunittest.repository.ArrivalRepository;
import com.sodexo.junit.springjunittest.service.ArrivalService;

@Service
public class ArrivalServiceImpl implements ArrivalService{

	@Autowired
	private ArrivalRepository arrivalRepository;
	
	@Override
	public List<Arrival> getAllArrivals() {
		
		return arrivalRepository.findAll();
	}

	@Override
	public Arrival getArrivalById(Integer id) {
		return arrivalRepository.findById(id).orElse(new Arrival(""));
	}

}
