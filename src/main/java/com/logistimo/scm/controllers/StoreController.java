package com.logistimo.scm.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.logistimo.scm.exceptions.ResourceNotFoundException;
import com.logistimo.scm.models.Store;
import com.logistimo.scm.services.StoreService;

@RestController
@Validated
public class StoreController {
	
	@Autowired
	private StoreService service;
	
	@GetMapping(path = "logistimo/store/")
	List<Store> retriveAllStores() {
		return service.getAllStores();
	}
	
	@GetMapping(path = "logistimo/store/{Id}/")
	Store retriveStoreById(@PathVariable(value="Id") String id) {
		Optional<Store> store =service.getStoreById(id);
		if(!store.isPresent()) {
			throw new ResourceNotFoundException("Store with id "+id+" does not exist");
		}
		
		return store.get();
	}
	
	@PostMapping(path = "logistimo/store/")
	ResponseEntity<Store> createNewStore(@Valid @RequestBody(required=true) Store store){
		Store createdStore = service.addNewStore(store);
		URI lokation =ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdStore.getId()).toUri();
		return ResponseEntity.created(lokation).body(createdStore);
	}
	
	@DeleteMapping(path="logistimo/store/{Id}/delete")
	void deleteStoreById(@PathVariable(value="Id",required=true) String id) {
		service.deleteStoreById(id);
	}

}
