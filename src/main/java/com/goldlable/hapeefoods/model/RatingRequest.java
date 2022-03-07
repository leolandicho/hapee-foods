package com.goldlable.hapeefoods.model;

import java.util.Set;

public class RatingRequest {
	private RestaurantRating restaurantRating;
	private Set<MenuItemRating> menuItemRatings;
	
	public RestaurantRating getRestaurantRating() {
		return restaurantRating;
	}
	
	public void setRestaurantRating(RestaurantRating restaurantRating) {
		this.restaurantRating = restaurantRating;
	}
	
	public Set<MenuItemRating> getMenuItemRatings() {
		return menuItemRatings;
	}
	
	public void setMenuItemRatings(Set<MenuItemRating> menuItemRatings) {
		this.menuItemRatings = menuItemRatings;
	}
}
