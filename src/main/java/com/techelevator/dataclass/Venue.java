package com.techelevator.dataclass;

import java.util.ArrayList;
import java.util.List;


public class Venue {
	
	private int cityId;
	private String cityName;
	private int venueId;
	private String venueName;
	private String description;
	private String stateCode;
	private List<String> categories = new ArrayList<String>();
	private int categoryId;

	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getVenueId() {
		return venueId;
	}
	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}
	public String getVenueName() {
		return venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Venue other = (Venue) obj;
		if (venueName == null) {
			if (other.venueName == null) {
				return false;
			}
		} else if (!venueName.equals(other.venueName)) {
			return false;
		}
		if (description == null) {
			if (other.description == null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (venueId != other.venueId) {
			return false;
		}
		return true;
	}
		
	
	

}
