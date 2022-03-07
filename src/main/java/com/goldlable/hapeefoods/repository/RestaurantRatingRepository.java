package com.goldlable.hapeefoods.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goldlable.hapeefoods.model.RestaurantRating;

public interface RestaurantRatingRepository extends JpaRepository<RestaurantRating, Integer> {

}
