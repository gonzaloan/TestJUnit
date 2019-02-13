package com.sodexo.junit.springjunittest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sodexo.junit.springjunittest.domain.Arrival;
import com.sodexo.junit.springjunittest.repository.ArrivalRepository;

import static com.sodexo.junit.springjunittest.constant.Paths.VERSION;

import java.util.List;

import static com.sodexo.junit.springjunittest.constant.Paths.ARRIVAL;

@RestController
@RequestMapping(value= VERSION + ARRIVAL)
public class ArrivalController {

	@Autowired
	private ArrivalRepository arrivalRepository;
	
	@GetMapping(value="all")
	@ResponseBody
	public List<Arrival> getAllArrivals(){
		return arrivalRepository.findAll();
	}
	
	@GetMapping(value="{id}")
	@ResponseBody
	public Arrival getArrivalById(@PathVariable(value="id") Integer id) {
		return arrivalRepository.findAllById(id);
	}
}
