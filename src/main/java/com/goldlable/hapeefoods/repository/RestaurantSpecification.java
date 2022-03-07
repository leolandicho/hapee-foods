package com.goldlable.hapeefoods.repository;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.goldlable.hapeefoods.model.MenuItem;
import com.goldlable.hapeefoods.model.MenuItemRating;
import com.goldlable.hapeefoods.model.OperationTiming;
import com.goldlable.hapeefoods.model.Restaurant;
import com.goldlable.hapeefoods.model.RestaurantRating;
import com.goldlable.hapeefoods.model.RestaurantSearchRequest;

@Component
public class RestaurantSpecification {
	public Specification<Restaurant> searchRestaurants(RestaurantSearchRequest request) {
		return (root, query, criteriaBuilder) -> {
			
			List<Predicate> predicates = new ArrayList<>();
			
			if (request != null) {
				if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
					String keyword = request.getKeyword();
					
					Subquery<Restaurant> subQuery = query.subquery(Restaurant.class);
					Root<MenuItem> rootMenuItem = subQuery.from(MenuItem.class);
					Join subQueryRestaurant = rootMenuItem.join("restaurant");
					subQuery.select(subQueryRestaurant).where(
						criteriaBuilder.like(
							criteriaBuilder.upper(rootMenuItem.get("name")),
							"%" + keyword.toUpperCase() + "%"
						)
					);
					
					predicates.add(
						criteriaBuilder.or(
							criteriaBuilder.in(root).value(subQuery),
							criteriaBuilder.like(
								criteriaBuilder.upper(root.get("name")),
								"%" + keyword.toUpperCase() + "%"
							),
							criteriaBuilder.like(
								criteriaBuilder.upper(root.get("addressLine1")),
								"%" + keyword.toUpperCase() + "%"
							),
							criteriaBuilder.like(
								criteriaBuilder.upper(root.get("addressLine2")),
								"%" + keyword.toUpperCase() + "%"
							),
							criteriaBuilder.like(
								criteriaBuilder.upper(root.get("city")),
								"%" + keyword.toUpperCase() + "%"
							),
							criteriaBuilder.like(
								criteriaBuilder.upper(root.get("state")),
								"%" + keyword.toUpperCase() + "%"
							),
							criteriaBuilder.like(
								criteriaBuilder.upper(root.get("country")),
								"%" + keyword.toUpperCase() + "%"
							)
						) // end of OR
					);
				}
				
				if (request.getOperationTiming() != null) {
					String day = request.getOperationTiming().getDay();
					Time time = request.getOperationTiming().getTime();
					Subquery<Restaurant> subQuery = query.subquery(Restaurant.class);
					Root<OperationTiming> rootOperationTiming = subQuery.from(OperationTiming.class);
					Join subQueryRestaurant = rootOperationTiming.join("restaurant");
					List<Predicate> sqPredicates = new ArrayList<>();
					
					if (day != null && !day.isEmpty()) {
						sqPredicates.add(
							criteriaBuilder.equal(rootOperationTiming.get("day"), day)
						);
					}
					
					if (time != null) {
						sqPredicates.add(criteriaBuilder.or(
							criteriaBuilder.isNull(rootOperationTiming.get("openingTime")),
							criteriaBuilder.and(
								criteriaBuilder.isNotNull(rootOperationTiming.get("openingTime")),
								criteriaBuilder.lessThanOrEqualTo(rootOperationTiming.get("openingTime"), time)
							)
						));
						
						sqPredicates.add(criteriaBuilder.or(
							criteriaBuilder.isNull(rootOperationTiming.get("closingTime")),
							criteriaBuilder.and(
								criteriaBuilder.isNotNull(rootOperationTiming.get("closingTime")),
								criteriaBuilder.greaterThanOrEqualTo(rootOperationTiming.get("closingTime"), time)
							)
						));
					}
					
					subQuery.select(subQueryRestaurant).where(sqPredicates.toArray(new Predicate[0]));
					predicates.add(criteriaBuilder.in(root).value(subQuery));
				} // end IF has operation timing
				
				if (request.getGeoLocation() != null) {
					predicates.add(
						criteriaBuilder.lessThanOrEqualTo(
							criteriaBuilder.prod(
								criteriaBuilder.literal(Double.valueOf(6371)),
								criteriaBuilder.function(
									"acos", 
									Double.class, 
									criteriaBuilder.sum(
										criteriaBuilder.prod(
											criteriaBuilder.prod(
												criteriaBuilder.function(
													"cos", 
													Double.class, 
													criteriaBuilder.function(
														"radians", 
														Double.class, 
														criteriaBuilder.literal(request.getGeoLocation().getY())
													)
												),
												criteriaBuilder.function(
													"cos", 
													Double.class, 
													criteriaBuilder.function(
														"radians", 
														Double.class, 
														root.get("geoLocationY")
													)
												)
											), // end of prod of 2 longitude cos'es
											criteriaBuilder.function(
												"cos", 
												Double.class, 
												criteriaBuilder.diff(
													criteriaBuilder.function(
														"radians", 
														Double.class, 
														root.get("geoLocationX")
													), 
													criteriaBuilder.function(
														"radians", 
														Double.class, 
														criteriaBuilder.literal(request.getGeoLocation().getX())
													)
												) // end of diff of radians longitude
											) // end of cos of radians diff
										), // end of prod of 2 cos and cos of diff
										criteriaBuilder.prod(
											criteriaBuilder.function(
												"sin", 
												Double.class, 
												criteriaBuilder.function(
													"radians", 
													Double.class, 
													criteriaBuilder.literal(request.getGeoLocation().getY())
												)
											),
											criteriaBuilder.function(
												"sin", 
												Double.class, 
												criteriaBuilder.function(
													"radians", 
													Double.class, 
													root.get("geoLocationY")
												)
											)
										) // end of prod of 2 sin functions
									) // end of sum
										
								) // end of acos
							), // end of prod of fixed km and acos
							criteriaBuilder.literal(Double.valueOf(5.00))
						) // end of withi or less than or equal to radius check
					);
				} // end IF has geolocation
				
				if (request.getMinimumRatingScore() > 0) {
					Subquery<Restaurant> subQuery = query.subquery(Restaurant.class);
					Root<RestaurantRating> rootRestaurantRating = subQuery.from(RestaurantRating.class);
					Join subQueryRestaurant = rootRestaurantRating.join("restaurant");
					List<Predicate> sqPredicates = new ArrayList<>();
					sqPredicates.add(
						criteriaBuilder.greaterThanOrEqualTo(rootRestaurantRating.get("score"), request.getMinimumRatingScore())
					);
					subQuery.select(subQueryRestaurant).where(sqPredicates.toArray(new Predicate[0]));
					
					Subquery<Restaurant> subQuery2 = query.subquery(Restaurant.class);
					Root<MenuItemRating> rootMenuItemRating = subQuery2.from(MenuItemRating.class);
					Join subQueryRestaurant2 = rootMenuItemRating.join("menuItem").join("restaurant");
					List<Predicate> sqPredicates2 = new ArrayList<>();
					sqPredicates2.add(
						criteriaBuilder.greaterThanOrEqualTo(rootMenuItemRating.get("score"), request.getMinimumRatingScore())
					);
					subQuery2.select(subQueryRestaurant2).where(sqPredicates2.toArray(new Predicate[0]));
					
					predicates.add(
						criteriaBuilder.or(
							criteriaBuilder.in(root).value(subQuery),
							criteriaBuilder.in(root).value(subQuery2)
						)
					);
				}
				
			} // end IF has search request
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
