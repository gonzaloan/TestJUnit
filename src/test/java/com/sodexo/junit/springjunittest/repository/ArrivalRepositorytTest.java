package com.sodexo.junit.springjunittest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.sodexo.junit.springjunittest.SpringJunitTestApplication;
import com.sodexo.junit.springjunittest.domain.Arrival;


@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class ArrivalRepositorytTest{

	
	@Autowired
	private ArrivalRepository arrivalRepository;
	
	Arrival firstArrival, secondArrival;
	
	@Before
	public void setUp() {
		//Seteamos los valores que probaremos
		firstArrival = new Arrival();
		secondArrival = new Arrival();
		
		firstArrival.setId(1);
		firstArrival.setCity("Santiago");
		
		secondArrival.setId(2);
		secondArrival.setCity("Buenos Aires");

	}
	
	@Test
	public void whenFindAll() {
		//when
		List<Arrival> arrivals = arrivalRepository.findAll();
		
		arrivals.forEach(System.out::println);
		//then
		assertThat(arrivals.size()).isEqualTo(3);
		assertThat(arrivals.get(0)).isEqualToComparingFieldByField(firstArrival);
		assertThat(arrivals.get(1)).isEqualToComparingFieldByField(secondArrival);
	}
	
	@Ignore
	@Test
	public void whenFindAllByID() {
		//when
		Arrival testArrival = arrivalRepository.findAllById(firstArrival.getId());
		
		//then
		assertThat(testArrival.getCity()).isEqualTo(firstArrival.getCity());
	}
}
