package com.sodexo.junit.springjunittest.controller;

import java.util.List;
import static java.util.Collections.singletonList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import com.sodexo.junit.springjunittest.domain.Arrival;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static com.sodexo.junit.springjunittest.constant.Paths.ARRIVAL;
import static com.sodexo.junit.springjunittest.constant.Paths.VERSION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ArrivalController.class)
public class ArrivalControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ArrivalController arrivalController;

	Arrival arrival;
	List<Arrival> allArrivals;

	@Before
	public void setUp() throws Exception {
		// 1) Creamos la entidad de Arrival
		arrival = new Arrival();
		arrival.setId(1);
		arrival.setCity("Santiago");
		// 2) creamos una lista de Arrays de Arrival
		allArrivals = singletonList(arrival);
	}

	@Test
	public void getArrivals() throws Exception {

		//3) Aseguramos que si se llama al método getAllArrivals, retornará un listado de arrivals
		given(arrivalController.getAllArrivals()).willReturn(allArrivals);

		//4) Realizamos la petición get, y verificamos algunas asersiones
		mvc.perform(get(VERSION + ARRIVAL + "all").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				// El json resultante contiene 1 elemento
					.andExpect(jsonPath("$", hasSize(1)))   
					.andExpect(jsonPath("$[0].city", is(arrival.getCity()))); 
				//El json resultante, en su primer valor, contiene "Santiago" en el campo city
	}

	@Test
	public void getArrivalsById() throws Exception {

		given(arrivalController.getArrivalById(arrival.getId())).willReturn(arrival);

		mvc.perform(get(VERSION + ARRIVAL + arrival.getId()).contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("city", is(arrival.getCity())));
	}
}
