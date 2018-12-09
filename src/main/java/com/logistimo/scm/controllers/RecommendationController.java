package com.logistimo.scm.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logistimo.scm.exceptions.ResourceNotFoundException;
import com.logistimo.scm.misc.InventorySorter;
import com.logistimo.scm.models.RecommendationResponse;
import com.logistimo.scm.models.Store;
import com.logistimo.scm.services.StoreService;

@RestController
@Validated
public class RecommendationController {
	
	@Autowired
	private StoreService storeService;
	
	@GetMapping(path="logistimo/store/{id}/restock")
	public RecommendationResponse getRecommendation(@PathVariable(value = "id", required = true) String storeId,
			@RequestParam(value = "quantity", required = true) @Positive(message="quantity must pe positive") Integer quantity,
			@RequestParam(value = "distance", required = true) @Positive(message="distance must pe positive") Double distance) {
		
		Optional<Store> fromStore = storeService.getStoreById(storeId);
		
		// requesting store not available in database
		if(!fromStore.isPresent()) {
			throw new ResourceNotFoundException("Store with id "+storeId+" does not exist");
		}
		
		Double x=fromStore.get().getX_coordinate();
		Double y=fromStore.get().getY_coordinate();
		
		//The list contains at most 10 recommendations.
		List<Store> recommendedList = storeService.getStoreRecommendations(storeId, x,y,distance, quantity);
		//calculating distance between requesting stores and recommended stores.
		for(int i=0;i<recommendedList.size();i++) {
			Double x1 = recommendedList.get(i).getX_coordinate();
			Double y1 = recommendedList.get(i).getY_coordinate();
			Double dist = Math.sqrt(Math.pow((x1-x), 2)+Math.pow((y1-y), 2));
			recommendedList.get(i).setDistance_from_store(dist);
		}
		
		// Stores are sorted based on their distance from requesting stores. if the
		// distance is same then the store with more inventory is given preference
		Collections.sort(recommendedList, new InventorySorter());
		
		// creating a response wrapper
		RecommendationResponse response = new RecommendationResponse();
		response.setStoreId(storeId);
		response.setMaxDistance(distance);
		response.setRequestedStockValue(quantity);
		response.setRecommendedStores(recommendedList);
		
		return response;
	}
}
