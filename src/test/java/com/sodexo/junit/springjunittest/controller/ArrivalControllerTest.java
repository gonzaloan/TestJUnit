package com.sodexo.junit.springjunittest.controller;

import java.util.List;
import static java.util.Collections.singletonList;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.sodexo.junit.springjunittest.SpringJunitTestApplication;
import com.sodexo.junit.springjunittest.domain.Arrival;

import static com.sodexo.junit.springjunittest.constant.Paths.ARRIVAL;
import static com.sodexo.junit.springjunittest.constant.Paths.VERSION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringJunitTestApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrivalControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	Arrival arrival;
	

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		arrival = new Arrival();
		arrival.setId(1);
		arrival.setCity("Santiago");
	}

	@Test
	public void getAllArrivals() throws Exception {
		
		mockMvc.perform(MockMvcRequestBuilders.get(VERSION+ARRIVAL+"all")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].city", is(arrival.getCity())))
				.andDo(print());
		
		
	}
	
	
	@Test
	public void getArrivalsById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(VERSION + ARRIVAL + arrival.getId())
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("city", is(arrival.getCity())))
					.andDo(print());
		
		
	}

	@Test
	public void verifyInvalidArrivalPath() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(VERSION + ARRIVAL + "pepito/").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errorCode").value(400))
				.andExpect(jsonPath("$.message").value("The request could not be understood by the server"))
				.andDo(print());
	}

	@Test
	public void verifyInvalidadArrivalId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(VERSION + ARRIVAL + "4").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.errorCode").value(404))
				.andExpect(jsonPath("$.message").value("Arrival doesn't exist"))
				.andDo(print());
	}

}
