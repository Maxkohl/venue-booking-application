package com.techelevator.dataclass;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Space extends Venue {

	private int spaceId;
	private String spaceName;
	private int openMonth;
	private int closeMonth;
	private int maxOccupancy;
	private boolean isAccessible;
	private BigDecimal dailyRate;
	private static final String[] months = { "", "Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun.", "Jul.", "Aug", "Sep.",
			"Oct.", "Nov.", "Dec." };

	public int getOpenMonth() {
		return openMonth;
	}

	public void setOpenMonth(int openMonth) {
		this.openMonth = openMonth;
	}

	public int getCloseMonth() {
		return closeMonth;
	}

	public void setCloseMonth(int closeMonth) {
		this.closeMonth = closeMonth;
	}

	public int getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public boolean isAccessible() {
		return isAccessible;
	}

	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}

	public BigDecimal getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(BigDecimal dailyRate) {
		this.dailyRate = dailyRate;
	}

	public static String[] getMonths() {
		return months;
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
		Space other = (Space) obj;
		if (spaceName == null) {
			if (other.spaceName == null) {
				return false;
			}
		} else if (!spaceName.equals(other.spaceName)) {
			return false;
		}
		if (openMonth != other.openMonth) {
			return false;
		}
		if (closeMonth != other.closeMonth) {
			return false;
		}
		if (spaceId != other.spaceId) {
			return false;
		}
		if (maxOccupancy != other.maxOccupancy) {
			return false;
		}
		if (isAccessible != other.isAccessible) {
			return false;
		}
		if (dailyRate.compareTo(other.dailyRate) != 0) {
			return false;
		}

		return true;
	}

}
