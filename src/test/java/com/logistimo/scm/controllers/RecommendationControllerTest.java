package com.logistimo.scm.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.logistimo.scm.models.Store;
import com.logistimo.scm.services.StoreService;

import net.bytebuddy.utility.RandomString;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers=RecommendationController.class)
public class RecommendationControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StoreService service;
	
	@Test
	public void testGetRecommendation() throws Exception {
		ArrayList<Store> mockList = new ArrayList<>();
		for(int i=0;i<3;i++) {
			Store mockStore = new Store(new Double(i),new Double(i),i);
			mockStore.setId(String.valueOf(i));
			mockStore.setDistance_from_store(Math.sqrt(2)*i);
			mockList.add(mockStore);
		}
		Store mockStore = new Store(10.0,20.0,165);
		String idOfCreatedResource =RandomString.make(10);
		mockStore.setId(idOfCreatedResource);
		when(service.getStoreById(Mockito.anyString())).thenReturn(Optional.of(mockStore));
		when(service.getStoreRecommendations(Mockito.anyString(),Mockito.anyDouble(),Mockito.anyDouble(),Mockito.anyDouble(),Mockito.anyInt())).thenReturn(mockList);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/logistimo/store/"+idOfCreatedResource+"/restock?quantity=12&distance=5").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String actual =result.getResponse().getContentAsString();
		
		//checking response for correct input parameters  
		String expected="{\"requested_stock_value\":12,\"maximum_distance\":5.0,\"recommended_stores\":[{\"id\":\"2\",\"x_coordinate\":2.0,\"y_coordinate\":2.0,\"current_inventory_level\":2,\"distance_from_store\":19.697715603592208},{\"id\":\"1\",\"x_coordinate\":1.0,\"y_coordinate\":1.0,\"current_inventory_level\":1,\"distance_from_store\":21.02379604162864},{\"id\":\"0\",\"x_coordinate\":0.0,\"y_coordinate\":0.0,\"current_inventory_level\":0,\"distance_from_store\":22.360679774997898}]}";
		JSONAssert.assertEquals(expected, actual, false);
		assertTrue(result.getResponse().getStatus()==200);
		
		//checking response for negative quantity
		requestBuilder=MockMvcRequestBuilders.get("/logistimo/store/"+idOfCreatedResource+"/restock?quantity=-12&distance=5").accept(MediaType.APPLICATION_JSON);
		result = mockMvc.perform(requestBuilder).andReturn();
		assertTrue(result.getResponse().getStatus()==500);
		
		//checking response for negative distance
		requestBuilder=MockMvcRequestBuilders.get("/logistimo/store/"+idOfCreatedResource+"/restock?quantity=12&distance=-5").accept(MediaType.APPLICATION_JSON);
		result = mockMvc.perform(requestBuilder).andReturn();
		assertTrue(result.getResponse().getStatus()==500);
		
		//checking response for invalid Id
		when(service.getStoreById(Mockito.anyString())).thenReturn(Optional.empty());
		requestBuilder=MockMvcRequestBuilders.get("/logistimo/store/id/restock?quantity=12&distance=5").accept(MediaType.APPLICATION_JSON);
		result = mockMvc.perform(requestBuilder).andReturn();
		assertTrue(result.getResponse().getStatus()==404);
	}
	
}
