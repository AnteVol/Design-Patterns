package hr.fer.zemris.ooup.lab2.model.plugins;
import hr.fer.zemris.ooup.lab2.model.Animal;

public class Parrot extends Animal{

	private String name;
	
	public Parrot(String name) {
		super();
		this.name = name;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String greet() {
		return "helooooooo!";
	}

	@Override
	public String menu() {
		return "seeds";
	}

}
