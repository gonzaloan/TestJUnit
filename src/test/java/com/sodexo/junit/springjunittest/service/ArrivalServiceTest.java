package com.sodexo.junit.springjunittest.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sodexo.junit.springjunittest.domain.Arrival;
import com.sodexo.junit.springjunittest.repository.ArrivalRepository;
import com.sodexo.junit.springjunittest.service.impl.ArrivalServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
@RunWith(SpringJUnit4ClassRunner.class)
public class ArrivalServiceTest {
	
	@Mock
	private ArrivalRepository arrivalRepository;
	
	@InjectMocks
	private ArrivalServiceImpl arrivalService;

	Arrival firstArrival, secondArrival; 
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		firstArrival = new Arrival();
		secondArrival = new Arrival();
		
		firstArrival.setId(1);
		firstArrival.setCity("Santiago");
		
		secondArrival.setId(2);
		secondArrival.setCity("Buenos Aires");
		
		
	}
	
	@Test
	public void testGetAllArrivals() {
		List<Arrival> arrivalList = new ArrayList<Arrival>();
		
		arrivalList.add(firstArrival);
		arrivalList.add(secondArrival);
		
		when(arrivalRepository.findAll()).thenReturn(arrivalList);
		
		List<Arrival> result = arrivalService.getAllArrivals();
		assertThat(result.size()).isEqualTo(arrivalList.size());
		assertThat(result.get(0)).isEqualToComparingFieldByField(firstArrival);
		assertThat(result.get(1)).isEqualToComparingFieldByField(secondArrival);
	}
	
	@Test
	public void testGetArrivalById() {
		when(arrivalRepository.findById(1)).thenReturn(Optional.of(firstArrival));
		Arrival result = arrivalService.getArrivalById(1);
		assertThat(result.getCity()).isEqualTo(firstArrival.getCity());
	}
	

}
