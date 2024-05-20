package hr.fer.zemris.ooup.lab2.model.plugins;
import hr.fer.zemris.ooup.lab2.model.Animal;

public class Cow extends Animal{

	private String name;
	
	public Cow(String name) {
		super();
		this.name = name;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String greet() {
		return "muuuuuuu!";
	}

	@Override
	public String menu() {
		return "grass";
	}

}
