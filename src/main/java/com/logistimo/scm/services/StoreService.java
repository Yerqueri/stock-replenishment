package com.logistimo.scm.services;

import java.util.List;
import java.util.Optional;

import com.logistimo.scm.models.Store;

public interface StoreService {
	public List<Store> getStoreRecommendations(String storeId, Double x, Double y, Double distance, int minInventory);
	public Optional<Store> getStoreById(String storeId);
	public List<Store> getAllStores();
	public void deleteStoreById(String storeId);
	public Store addNewStore(Store store);
}
