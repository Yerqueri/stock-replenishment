package com.logistimo.scm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.logistimo.scm.models.Store;
import com.logistimo.scm.repositories.StoreRepository;

@Service
public class StoreServiceImpl implements StoreService{
	
	@Autowired
	private StoreRepository repo;
	
	//method to get List of 10 recommended stores 
	public List<Store> getStoreRecommendations(String storeId, Double x, Double y, Double distance, int minInventory) {
		return repo.getRecommendedStores(storeId, x, y, distance, minInventory);
	}
	
	//method to fetch store by Id
	public Optional<Store> getStoreById(String storeId) {
		return repo.findById(storeId);
	}
	
	//method to fetch all stores
	public List<Store> getAllStores() {
		return repo.findAll();
	}
	
	//method to delete store by Id
	public void deleteStoreById(String storeId) {
		repo.deleteById(storeId);
	}
	
	//method to create a new Store.
	public Store addNewStore(Store store) {
		return repo.save(store);
	}
}
