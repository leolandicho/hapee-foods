package com.goldlable.hapeefoods.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goldlable.hapeefoods.model.MenuItem;
import com.goldlable.hapeefoods.model.RatingRequest;
import com.goldlable.hapeefoods.model.Restaurant;
import com.goldlable.hapeefoods.model.RestaurantSearchRequest;
import com.goldlable.hapeefoods.service.RestaurantService;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
	@Autowired
	RestaurantService restaurantService;
	
	@GetMapping("/")
	public String get() {
		return "Restaurant";
	}
	
	@PostMapping("/")
	public void add(@RequestBody Restaurant restaurant) {
		restaurantService.saveRestaurant(restaurant);
	}
	
	@PostMapping("/search")
	public List<Restaurant> search(@RequestBody RestaurantSearchRequest request) {
		return restaurantService.search(request);
	}
	
	@PostMapping("/{id}/menu")
	public void publishMenu(@PathVariable("id") int restaurantId, @RequestBody List<MenuItem> menuItems) {
		restaurantService.publishMenu(restaurantId, menuItems);
	}
	
	@PostMapping("/ratings")
	public void addRating(@RequestBody RatingRequest ratingRequest) {
		restaurantService.addRating(ratingRequest.getRestaurantRating(), ratingRequest.getMenuItemRatings());
	}
}
