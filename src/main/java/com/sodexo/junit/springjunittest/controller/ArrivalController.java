package com.sodexo.junit.springjunittest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sodexo.junit.springjunittest.domain.Arrival;
import com.sodexo.junit.springjunittest.exception.ArrivalException;
import com.sodexo.junit.springjunittest.service.ArrivalService;

import static com.sodexo.junit.springjunittest.constant.Paths.VERSION;

import java.util.List;

import static com.sodexo.junit.springjunittest.constant.Paths.ARRIVAL;

@RestController
@RequestMapping(value= VERSION + ARRIVAL)
public class ArrivalController {
	
	 @Autowired
	 private ArrivalService arrivalService;
	
	@GetMapping(value="all")
	@ResponseBody
	public List<Arrival> getAllArrivals(){
		return arrivalService.getAllArrivals();
	}
	
	@GetMapping(value="{id}")
	@ResponseBody
	public Arrival getArrivalById(@PathVariable(value="id") Integer id) throws ArrivalException {
		Arrival arrival = arrivalService.getArrivalById(id);
		if(arrival.getCity().isEmpty()) {
			throw new ArrivalException("Arrival doesn't exist");
		}
		return arrival;
	}
}
