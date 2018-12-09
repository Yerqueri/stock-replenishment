package com.logistimo.scm.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Random;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.logistimo.scm.StockReplenishmentRecommendationServiceApplication;
import com.logistimo.scm.models.Store;

import net.bytebuddy.utility.RandomString;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockReplenishmentRecommendationServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StoreControllerIT {
	
	@LocalServerPort
	private int port;
	
	/*
	 * TEST TYPE : INTERGRATION
	 * PASS CONDITION : Response status must be 201 Created for sending valid Post request;
	 * 
	 */
	@Test
	public void testCreateNewValidStore() {
		String url = "http://localhost:"+port+"/logistimo/store/";
		Random rand = new Random();
		Store store = new Store(Math.abs(rand.nextDouble()),Math.abs(rand.nextDouble()),Math.abs(rand.nextInt()));
		ResponseEntity<String> actualResponse=sendPostRequest(url, store);
		String createdLocation =actualResponse.getHeaders().get(HttpHeaders.LOCATION).get(0);
		assertTrue(createdLocation.contains("/logistimo/store/"));
		Assert.assertArrayEquals(new Object[] {HttpStatus.CREATED}, new Object[] {actualResponse.getStatusCode()});
	}
	
	/*
	 * TEST TYPE : INTERGRATION
	 * PASS CONDITION : Response status must be 400 Bad Request for sending invalid Post request;
	 * 
	 */
	@Test
	public void testCreateNewInvalidStore() {
		String url = "http://localhost:"+port+"/logistimo/store/";
		Random rand = new Random();
		
		//sending post request with negative inventory
		Store store = new Store(Math.abs(rand.nextDouble()),Math.abs(rand.nextDouble()),Math.abs(rand.nextInt())*-1);
		ResponseEntity<String> actualResponse=sendPostRequest(url, store);
		Assert.assertArrayEquals(new Object[] {HttpStatus.BAD_REQUEST}, new Object[] {actualResponse.getStatusCode()});
		
		//sending post requst with null x coordinate
		store = new Store(null,Math.abs(rand.nextDouble()),Math.abs(rand.nextInt()));
		actualResponse=sendPostRequest(url, store);
		Assert.assertArrayEquals(new Object[] {HttpStatus.BAD_REQUEST}, new Object[] {actualResponse.getStatusCode()});
		
		//sending post request with null y coordinate
		store = new Store(Math.abs(rand.nextDouble()),null,Math.abs(rand.nextInt()));
		actualResponse=sendPostRequest(url, store);
		Assert.assertArrayEquals(new Object[] {HttpStatus.BAD_REQUEST}, new Object[] {actualResponse.getStatusCode()});
		
		
		//sending post request with null inventory
		store = new Store(Math.abs(rand.nextDouble()),Math.abs(rand.nextDouble()),null);
		actualResponse=sendPostRequest(url, store);
		Assert.assertArrayEquals(new Object[] {HttpStatus.BAD_REQUEST}, new Object[] {actualResponse.getStatusCode()});
	}
	
	/*
	 * TEST TYPE : INTERGRATION
	 * PASS CONDITION : Response status must be 200 Ok for sending get request with valid storeid;
	 * 
	 */
	@Test
	public void testRetrieveStoreById() {
		// creating a dummy store to fetch later.
		String url = "http://localhost:"+port+"/logistimo/store/";
		Store store = new Store(10.0,20.0,165);
		ResponseEntity<String> actualResponse=sendPostRequest(url, store);
		String createdLocation =actualResponse.getHeaders().get(HttpHeaders.LOCATION).get(0);
		String[] location =createdLocation.split("/");
		String idOfCreatedResource=location[location.length-1];
		
		url = "http://localhost:"+port+"/logistimo/store/"+idOfCreatedResource+"/";
		String expectedResponse ="{\"id\":\""+idOfCreatedResource+"\",\"x_coordinate\":10.0,\"y_coordinate\":20.0,\"current_inventory_level\":165,\"distance_from_store\":null}";
		actualResponse=sendGetRequest(url);
		try {
			JSONAssert.assertEquals(expectedResponse, actualResponse.getBody(),true);
			Assert.assertArrayEquals(new Object[] {HttpStatus.OK}, new Object[] {actualResponse.getStatusCode()});
		} catch (JSONException e) {
			fail("Failed due to JSONException  while comparing expected responce with actual response");
			e.printStackTrace();
		}
	}
	
	
	/*
	 * TEST TYPE : INTERGRATION
	 * PASS CONDITION : Return status must be 404 Not Found for sending get request with invalid Store id;
	 * 
	 */
	@Test
	public void testRetrieveStoreByInvalidId() {
		String fakeId =RandomString.make(10);
		String url = "http://localhost:"+port+"/logistimo/store/"+fakeId+"/";
		String expectedResponse ="{\"message\":\"Store with id "+fakeId+" does not exist\",\"details\":\"uri=/logistimo/store/"+fakeId+"/\"}";
		ResponseEntity<String> actualResponse=sendGetRequest(url);
		try {
			JSONAssert.assertEquals(expectedResponse, actualResponse.getBody(),false);
			Assert.assertArrayEquals(new Object[] {HttpStatus.NOT_FOUND}, new Object[] {actualResponse.getStatusCode()});
		} catch (JSONException e) {
			fail("Failed due to JSONException  while comparing expected responce with actual response");
			e.printStackTrace();
		}
	}
	
	/*
	 * TEST TYPE : INTERGRATION
	 * PASS CONDITION : Return status must be 200 Ok for sending delete request with invalid Store id;
	 * 
	 */
	@Test
	public void testDeleteStoreByValidId() {
		//creating a dummy store to delete
		String url = "http://localhost:"+port+"/logistimo/store/";
		Store store = new Store(10.0,20.0,165);
		ResponseEntity<String> actualResponse=sendPostRequest(url, store);
		String createdLocation =actualResponse.getHeaders().get(HttpHeaders.LOCATION).get(0);
		String[] location =createdLocation.split("/");
		String idOfCreatedResource=location[location.length-1];
		
		//sending delete request to delete the dummy store
		url = "http://localhost:"+port+"/logistimo/store/"+idOfCreatedResource+"/delete";
		actualResponse=sendDeleteRequest(url);
		Assert.assertArrayEquals(new Object[] {HttpStatus.OK}, new Object[] {actualResponse.getStatusCode()});
		
	}
	
	/*
	 * TEST TYPE : INTERGRATION
	 * PASS CONDITION : Return status must be 500 Internal server error for sending delete request with invalid Store id;
	 * 
	 */
	@Test
	public void testDeleteStoreByInvalidId() {
		String fakeId =RandomString.make(10);
		String url = "http://localhost:"+port+"/logistimo/store/"+fakeId+"/delete";
		ResponseEntity<String> actualResponse=sendDeleteRequest(url);
		Assert.assertArrayEquals(new Object[] {HttpStatus.INTERNAL_SERVER_ERROR}, new Object[] {actualResponse.getStatusCode()});
		
	}
	
	// method to send get request at given url
	private ResponseEntity<String> sendGetRequest(String url){
		TestRestTemplate restTemplate= new TestRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> httpEntity=new HttpEntity<String>(headers);
		ResponseEntity<String> response =restTemplate.exchange(url,HttpMethod.GET,httpEntity,String.class);
		return response;
	}
	
	// method to send a post request at given url
	private ResponseEntity<String> sendPostRequest(String url,Object body){
		TestRestTemplate restTemplate= new TestRestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Object> httpEntity=new HttpEntity<Object>(body,headers);
		ResponseEntity<String> response =restTemplate.exchange(url,HttpMethod.POST,httpEntity,String.class);
		return response;
	}
	
	// method to send a delete request at given url
	private ResponseEntity<String> sendDeleteRequest(String url){
		TestRestTemplate restTemplate= new TestRestTemplate();
		ResponseEntity<String> response =restTemplate.exchange(url,HttpMethod.DELETE,null,String.class);
		return response;
	}
}
