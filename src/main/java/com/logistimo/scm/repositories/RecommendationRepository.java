package com.logistimo.scm.repositories;

import java.util.List;

import com.logistimo.scm.models.Store;

public interface RecommendationRepository {
	List<Store> getRecommendedStores(String storeId,Double x,Double y,Double distance,int mininventory);
}
