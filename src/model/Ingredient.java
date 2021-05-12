package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Ingredient implements Serializable {

	// ----------- Variablen -----------
	private int id;
	private int idJava;
	private String name;
	private String unit;

	// ----------- Konstruktoren -----------

	public Ingredient() {

	}

	public Ingredient(int idjava) {
		this.idJava = idjava;

	}

	public Ingredient(String name, String unit) {
		this.name = name;
		this.unit = unit;
	}

	public Ingredient(int idjava, String name, String unit) {
		this.idJava = idjava;
		this.name = name;
		this.unit = unit;
	}

	public Ingredient(int id, int idjava, String name, String unit) {

		this.id = id;
		this.idJava = idjava;
		this.name = name;
		this.unit = unit;

	}

	// ----------- Methoden -----------

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdJava() {
		return idJava;
	}

	public void setIdJava(int idjava) {
		this.idJava = idjava;
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
	public String toString() {
		return "Zutat_" + idJava + " Name: " + name + " Einheit: " + unit + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + idJava;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Ingredient))
			return false;
		Ingredient other = (Ingredient) obj;
		if (id != other.id)
			return false;
		if (idJava != other.idJava)
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

}
