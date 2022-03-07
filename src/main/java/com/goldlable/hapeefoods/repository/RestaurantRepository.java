package com.goldlable.hapeefoods.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goldlable.hapeefoods.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	/*@Query("SELECT r FROM Restaurant r "
			+ "WHERE UPPER(r.name) LIKE CONCAT('%', UPPER(:keyword), '%')"
			+ "OR UPPER(r.branchName) LIKE CONCAT('%', UPPER(:keyword), '%')"
			+ "OR UPPER(r.addressLine1) LIKE CONCAT('%', UPPER(:keyword), '%')"
			+ "OR UPPER(r.addressLine2) LIKE CONCAT('%', UPPER(:keyword), '%')"
			+ "OR UPPER(r.city) LIKE CONCAT('%', UPPER(:keyword), '%')"
			+ "OR UPPER(r.state) LIKE CONCAT('%', UPPER(:keyword), '%')"
			+ "OR UPPER(r.country) LIKE CONCAT('%', UPPER(:keyword), '%')"
			+ "OR r.id IN (SELECT m.restaurant.id FROM MenuItem m WHERE UPPER(m.name) LIKE CONCAT('%', UPPER(:keyword), '%'))")
	List<Restaurant> search(String keyword);*/
	public List<Restaurant> findAll(Specification<Restaurant> spec);
}
