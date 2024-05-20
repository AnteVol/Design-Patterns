package hr.fer.zemris.ooup.lab2.model.plugins;
import hr.fer.zemris.ooup.lab2.model.Animal;

public class Cat extends Animal{

	private String name;
	
	public Cat(String name) {
		super();
		this.name = name;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String greet() {
		return "mauuuuu!";
	}

	@Override
	public String menu() {
		return "milk";
	}

}
