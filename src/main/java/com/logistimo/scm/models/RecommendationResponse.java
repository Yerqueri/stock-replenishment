package com.logistimo.scm.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(description = "Respose model")
public class RecommendationResponse {
	
	@JsonProperty("store_id")
	@NotNull
	private String storeId;
	
	@JsonProperty("requested_stock_value")
	@ApiModelProperty(notes = "stock value requested for replenishment")
	@NotNull
	private Integer requestedStockValue;
	
	@JsonProperty("maximum_distance")
	@ApiModelProperty(notes = "distance in which to look for stores")
	@NotNull
	private Double maxDistance;
	
	@JsonProperty("recommended_stores")
	@ApiModelProperty(notes = "list of store recommendations")
	private List<Store> recommendedStores;
	
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public Integer getRequestedStockValue() {
		return requestedStockValue;
	}
	public void setRequestedStockValue(Integer requestedStockValue) {
		this.requestedStockValue = requestedStockValue;
	}
	public Double getMaxDistance() {
		return maxDistance;
	}
	public void setMaxDistance(Double maxDistance) {
		this.maxDistance = maxDistance;
	}
	public List<Store> getRecommendedStores() {
		return recommendedStores;
	}
	public void setRecommendedStores(List<Store> recommendedStores) {
		this.recommendedStores = recommendedStores;
	}
	
	
}
