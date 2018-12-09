package com.logistimo.scm.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Model representing store information")
public class Store {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name="Store_ID")
	private String id;
	
	@Column(name="X_Coordinate")
	@NotNull(message="x coordinate can not be null")
	@ApiModelProperty(notes = "field to denote x coordinate of store")
	private Double x_coordinate;
	
	@Column(name="Y_Coordinate")
	@NotNull(message="y coordinate can not be null")
	@ApiModelProperty(notes = "field to denote y coordinate of store")
	private Double y_coordinate;
	
	@Column(name="Current_Inventory_Level")
	@PositiveOrZero(message="current inventory level can not be negative")
	@NotNull(message="current inventory level can not be null")
	@ApiModelProperty(notes = "field to denote current inventory level of store")
	private Integer current_inventory_level;
	
	@Transient
	private Double distance_from_store;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getX_coordinate() {
		return x_coordinate;
	}

	public void setX_coordinate(Double x_coordinate) {
		this.x_coordinate = x_coordinate;
	}

	public Double getY_coordinate() {
		return y_coordinate;
	}

	public void setY_coordinate(Double y_coordinate) {
		this.y_coordinate = y_coordinate;
	}

	public Integer getCurrent_inventory_level() {
		return current_inventory_level;
	}

	public void setCurrent_inventory_level(Integer current_inventory_level) {
		this.current_inventory_level = current_inventory_level;
	}

	public Double getDistance_from_store() {
		return distance_from_store;
	}

	public void setDistance_from_store(Double distance_from_store) {
		this.distance_from_store = distance_from_store;
	}

	public Store() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Store(@NotNull(message = "x coordinate can not be null") Double x_coordinate,
			@NotNull(message = "y coordinate can not be null") Double y_coordinate,
			@PositiveOrZero(message = "current inventory level can not be negative") @NotNull(message = "current inventory level can not be null") Integer current_inventory_level) {
		super();
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;
		this.current_inventory_level = current_inventory_level;
	}

	@Override
	public String toString() {
		return "Store [id=" + id + ", x_coordinate=" + x_coordinate + ", y_coordinate=" + y_coordinate
				+ ", current_inventory_level=" + current_inventory_level + ", distance_from_store="
				+ distance_from_store + "]";
	}


}

