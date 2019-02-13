package com.sodexo.junit.springjunittest.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sodexo.junit.springjunittest.domain.Arrival;

public interface ArrivalRepository extends PagingAndSortingRepository<Arrival, Integer>{
	
	List<Arrival> findAll();
	
	Arrival findAllById(Integer id);

}
