package com.logistimo.scm.misc;

import java.util.Comparator;

import com.logistimo.scm.models.Store;

public class InventorySorter implements Comparator<Store> {

	@Override
	public int compare(Store store1, Store store2) {
		if(store1.getDistance_from_store().compareTo(store2.getDistance_from_store())==0) {
			return store1.getCurrent_inventory_level().compareTo(store2.getCurrent_inventory_level());
		}else {
			return store1.getDistance_from_store().compareTo(store2.getDistance_from_store());
		}
	}

}
