package com.goldlable.hapeefoods.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.goldlable.hapeefoods.model.ContactNumber;
import com.goldlable.hapeefoods.model.MenuItem;
import com.goldlable.hapeefoods.model.MenuItemRating;
import com.goldlable.hapeefoods.model.OperationTiming;
import com.goldlable.hapeefoods.model.RatingRequest;
import com.goldlable.hapeefoods.model.Restaurant;
import com.goldlable.hapeefoods.model.RestaurantRating;
import com.goldlable.hapeefoods.model.RestaurantSearchRequest;
import com.goldlable.hapeefoods.repository.MenuItemRatingRepository;
import com.goldlable.hapeefoods.repository.MenuItemRepository;
import com.goldlable.hapeefoods.repository.RestaurantRatingRepository;
import com.goldlable.hapeefoods.repository.RestaurantRepository;
import com.goldlable.hapeefoods.repository.RestaurantSpecification;

@Service
@Transactional
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Autowired
	private RestaurantRatingRepository restaurantRatingRepository;
	
	@Autowired
	private MenuItemRatingRepository menuItemRatingRepository;
	
	@Autowired
	private RestaurantSpecification restaurantSpecification;
	
	public void saveRestaurant(Restaurant restaurant) {
		if (restaurant.getContactNumbers() != null) {
			for (ContactNumber contactNumber : restaurant.getContactNumbers()) {
				contactNumber.setRestaurant(restaurant);
			}
		}
		
		if (restaurant.getOperationTimings() != null) {
			for (OperationTiming operationTiming : restaurant.getOperationTimings()) {
				operationTiming.setRestaurant(restaurant);
				operationTiming.setDay(operationTiming.getDay().toUpperCase());
			}
		}
		
		restaurantRepository.save(restaurant);
	}
	
	public List<Restaurant> search(RestaurantSearchRequest request) {
		return restaurantRepository.findAll(restaurantSpecification.searchRestaurants(request));
	}
	
	public void publishMenu(int restaurantId, List<MenuItem> menuItems) {
		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
		
		if (restaurant.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
		}
		
		for (MenuItem menuItem : menuItems) {
			menuItem.setRestaurant(restaurant.get());
		}
		
		menuItemRepository.saveAll(menuItems);
	}
	
	public void addRating(RestaurantRating restaurantRating, Set<MenuItemRating> menuItemRatings) {
		
		if (restaurantRating == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant is required");
		}

		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantRating.getRestaurant().getId());
		
		if (restaurant.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
		}
		
		restaurantRating.setRestaurant(restaurant.get());
		restaurantRatingRepository.save(restaurantRating);
		
		for (MenuItemRating menuItemRating : menuItemRatings) {
			Optional<MenuItem> menuItem = menuItemRepository.findById(menuItemRating.getMenuItem().getId());
			
			if (menuItem.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found");
			}
			
			menuItemRating.setMenuItem(menuItem.get());
			menuItemRatingRepository.save(menuItemRating);
		}
	}
}
