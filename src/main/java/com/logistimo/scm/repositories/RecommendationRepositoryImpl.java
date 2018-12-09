package com.logistimo.scm.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import com.logistimo.scm.models.Store;

@Service
public class RecommendationRepositoryImpl implements RecommendationRepository{
	
	@PersistenceContext
    private EntityManager em;
	
	//method to get stores such that |x1-x|<d and |y1-y|<d and inventory > requested value 
	
	@Override
	public List<Store> getRecommendedStores(String storeId, Double x, Double y, Double distance ,int mininventory) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Store> query = cb.createQuery(Store.class);
		Root<Store> store =query.from(Store.class);
		
		List<Predicate> conditionsList = new ArrayList<Predicate>();
		conditionsList.add(cb.notEqual(store.<String>get("id"),storeId));
		conditionsList.add(cb.lessThan(store.<Double>get("x_coordinate"),x+distance));
		conditionsList.add(cb.greaterThan(store.<Double>get("x_coordinate"),x-distance));
		conditionsList.add(cb.lessThan(store.<Double>get("y_coordinate"),y+distance));
		conditionsList.add(cb.greaterThan(store.<Double>get("y_coordinate"),y-distance));
		conditionsList.add(cb.greaterThanOrEqualTo(store.<Integer>get("current_inventory_level"), mininventory));
		
		query.select(store).where(conditionsList.toArray(new Predicate[] {}));
		return em.createQuery(query).setMaxResults(10).getResultList();
	}
	
}
