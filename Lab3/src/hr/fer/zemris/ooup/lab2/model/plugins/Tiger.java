package hr.fer.zemris.ooup.lab2.model.plugins;
import hr.fer.zemris.ooup.lab2.model.Animal;

public class Tiger extends Animal{

	private String name;
	
	public Tiger(String name) {
		super();
		this.name = name;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String greet() {
		return "grrrr!";
	}

	@Override
	public String menu() {
		return "meat";
	}

}
