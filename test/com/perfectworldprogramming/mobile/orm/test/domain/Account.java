package com.perfectworldprogramming.mobile.orm.test.domain;

import com.perfectworldprogramming.mobile.orm.annotations.Column;
import com.perfectworldprogramming.mobile.orm.annotations.ColumnType;
import com.perfectworldprogramming.mobile.orm.annotations.Domain;
import com.perfectworldprogramming.mobile.orm.annotations.PrimaryKey;

@Domain(databaseName="ormtest", databaseVersion=3)
public class Account {
	public static final String PK_ACCOUNT_ID = "ACCOUNT_ID";
    public static final String COL_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String COL_AMOUNT = "AMOUNT";
    public static final String COL_YEAR_STARTED = "YEAR_STARTED";
    
	@PrimaryKey(PK_ACCOUNT_ID)
	private Long id;
	
	@Column(value=COL_ACCOUNT_TYPE, nullable=false, type=ColumnType.STRING)
	private String accountType;
	
	@Column(value=COL_AMOUNT, type=ColumnType.STRING)
	private double amount;
	
	@Column(value=COL_YEAR_STARTED, type=ColumnType.INTEGER)
	private int yearAccountOpened;
	
	public Long getId() {
		return id;
	}
	
	public String getAccountType() {
		return accountType;
	}
	
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public int getYearAccountOpened() {
		return yearAccountOpened;
	}
	
	public void setYearAccountOpened(int yearStarted) {
		this.yearAccountOpened = yearStarted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountType == null) ? 0 : accountType.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + yearAccountOpened;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (Double.doubleToLongBits(amount) != Double
				.doubleToLongBits(other.amount))
			return false;
		if (yearAccountOpened != other.yearAccountOpened)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountType=" + accountType
				+ ", amount=" + amount + ", yearStarted=" + yearAccountOpened + "]";
	}
	
	
}
