package com.logistimo.scm.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
@WebMvcTest(controllers=StoreController.class)
public class StoreControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StoreService service;
	
	@Test
	public void testretriveStoreById() throws Exception {
		Store mockStore = new Store(10.0,20.0,165);
		String idOfCreatedResource =RandomString.make(10);
		mockStore.setId(idOfCreatedResource);
		when(service.getStoreById(Mockito.anyString())).thenReturn(Optional.of(mockStore));
		
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/logistimo/store/id/").accept(MediaType.APPLICATION_JSON);
		String expectedResponse ="{\"x_coordinate\":10.0,\"y_coordinate\":20.0,\"current_inventory_level\":165,\"distance_from_store\":null}";
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String actualresult =result.getResponse().getContentAsString();
		JSONAssert.assertEquals(expectedResponse,actualresult, false);
		assertTrue(result.getResponse().getStatus()==200);
		
		when(service.getStoreById(Mockito.anyString())).thenReturn(Optional.empty());
		requestBuilder=MockMvcRequestBuilders.get("/logistimo/store/id/").accept(MediaType.APPLICATION_JSON);
		result = mockMvc.perform(requestBuilder).andReturn();
		expectedResponse="{\"message\":\"Store with id id does not exist\",\"details\":\"uri=/logistimo/store/id/\"}";
		JSONAssert.assertEquals(expectedResponse,result.getResponse().getContentAsString(), false);
		assertTrue(result.getResponse().getStatus()==404);
	}
	
	
}
