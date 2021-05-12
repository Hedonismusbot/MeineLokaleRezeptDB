package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.image.ImageView;

@SuppressWarnings("serial")
public class Recipe implements Serializable {

	// ----------- Variablen -----------

	private int id;
	private int idJava;
	private String name;
	private String description = "";
	private int duration = 0;
	private double portion;
	private ArrayList<Ingredient> ingriedients = new ArrayList<Ingredient>();
	private ArrayList<Double> amountIngriedient = new ArrayList<Double>();
	private ImageView imageView;
	private String imagePath = "";

	// ----------- Konstruktoren -----------

	public Recipe() {

	}

	public Recipe(int idjava, String title, double portion) {

		this.idJava = idjava;
		this.name = title;
		this.portion = portion;

	}

	public Recipe(String title, String description, int duration, double portion, String imagePath) {

		this.name = title;
		this.description = description;
		this.duration = duration;
		this.portion = portion;
		this.imagePath = imagePath;

	}

	public Recipe(int id, int idjava, String title, String description, double portion, String imagePath) {

		this.id = id;
		this.idJava = idjava;
		this.name = title;
		this.description = description;
		this.portion = portion;
		this.imagePath = imagePath;

	}

	public Recipe(int id, int idjava, String title, String description, int duration, double portion,
			String imagePath) {

		this.id = id;
		this.idJava = idjava;
		this.name = title;
		this.description = description;
		this.duration = duration;
		this.portion = portion;
		this.imagePath = imagePath;

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

	public void setName(String title) {
		this.name = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public double getPortion() {
		return portion;
	}

	public void setPortion(double portion) {
		this.portion = portion;
	}

	public ArrayList<Ingredient> getIngriedients() {
		return ingriedients;
	}

	public void setIngriedients(ArrayList<Ingredient> ingriedients) {
		this.ingriedients = ingriedients;
	}

	public ArrayList<Double> getAmountIngriedients() {
		return amountIngriedient;
	}

	public void setAmountIngriedients(ArrayList<Double> ratioingriedients) {
		this.amountIngriedient = ratioingriedients;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amountIngriedient == null) ? 0 : amountIngriedient.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + duration;
		result = prime * result + id;
		result = prime * result + idJava;
		result = prime * result + ((imagePath == null) ? 0 : imagePath.hashCode());
		result = prime * result + ((ingriedients == null) ? 0 : ingriedients.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(portion);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Recipe))
			return false;
		Recipe other = (Recipe) obj;
		if (amountIngriedient == null) {
			if (other.amountIngriedient != null)
				return false;
		} else if (!amountIngriedient.equals(other.amountIngriedient))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration != other.duration)
			return false;
		if (id != other.id)
			return false;
		if (idJava != other.idJava)
			return false;
		if (imagePath == null) {
			if (other.imagePath != null)
				return false;
		} else if (!imagePath.equals(other.imagePath))
			return false;
		if (ingriedients == null) {
			if (other.ingriedients != null)
				return false;
		} else if (!ingriedients.equals(other.ingriedients))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(portion) != Double.doubleToLongBits(other.portion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Recipe [id=" + id + ", idjava=" + idJava + ", title=" + name + ", description=" + description
				+ ", portion=" + portion + ", bildpfad=" + imagePath + ", ingriedients=" + ingriedients
				+ ", ratioingriedients=" + amountIngriedient + "]";
	}

}
