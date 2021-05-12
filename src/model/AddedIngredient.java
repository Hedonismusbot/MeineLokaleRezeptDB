package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AddedIngredient implements Serializable{

	// ----------- Variablen  ----------- 
	
	private String name;
	private double amount;
	private String unit;

	// ----------- Konstruktor  ----------- 
	
	public AddedIngredient(String name, String unit, double amount) {
		this.name = name;
		this.amount = amount;
		this.unit = unit;

	}

	// ----------- Methoden  ----------- 
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AddedIngredient))
			return false;
		AddedIngredient other = (AddedIngredient) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AddedIngredient [name=" + name + ", amount=" + amount + ", unit=" + unit + "]";
	}

}
