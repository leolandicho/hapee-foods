package com.goldlable.hapeefoods.model;

public class RestaurantSearchRequest {
	private String keyword;
	
	private GeoLocation geoLocation;
	
	private OperationTimingRequest operationTiming;
	
	private short minimumRatingScore;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public OperationTimingRequest getOperationTiming() {
		return operationTiming;
	}

	public void setOperationTiming(OperationTimingRequest operationTiming) {
		this.operationTiming = operationTiming;
	}

	public short getMinimumRatingScore() {
		return minimumRatingScore;
	}

	public void setMinimumRatingScore(short minimumRatingScore) {
		this.minimumRatingScore = minimumRatingScore;
	}
}
