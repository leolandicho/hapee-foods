package com.goldlable.hapeefoods.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "restaurants")
public class Restaurant {
	private int id;
	private String name;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private double geoLocationX;
	private double geoLocationY;
	private Set<ContactNumber> contactNumbers;
	private Set<OperationTiming> operationTimings;
	
	public Restaurant() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getGeoLocationX() {
		return geoLocationX;
	}

	public void setGeoLocationX(double geoLocationX) {
		this.geoLocationX = geoLocationX;
	}

	public double getGeoLocationY() {
		return geoLocationY;
	}

	public void setGeoLocationY(double geoLocationY) {
		this.geoLocationY = geoLocationY;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "restaurant")
	public Set<ContactNumber> getContactNumbers() {
		return contactNumbers;
	}

	public void setContactNumbers(Set<ContactNumber> contactNumbers) {
		this.contactNumbers = contactNumbers;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "restaurant")
	public Set<OperationTiming> getOperationTimings() {
		return operationTimings;
	}

	public void setOperationTimings(Set<OperationTiming> operationTimings) {
		this.operationTimings = operationTimings;
	}
	
}
