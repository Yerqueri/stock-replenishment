package com.logistimo.scm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistimo.scm.models.Store;

public interface StoreRepository extends JpaRepository<Store,String>,RecommendationRepository{

}
