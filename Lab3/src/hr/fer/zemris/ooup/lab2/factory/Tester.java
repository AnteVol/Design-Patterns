package hr.fer.zemris.ooup.lab2.factory;
import hr.fer.zemris.ooup.lab2.model.*;

public class Tester {
	public static void main(String[] args) {
		
		String[] animals = {"Cow", "Cat", "Parrot", "Tiger", "Cow"};
		String[] names = {"kravica", "maca", "dosadna", "velika maca", "kravica2"};
		
		Animal newAnimal;
		
		for(int i = 0; i < animals.length; i++) {
			
			newAnimal = AnimalFactory.newInstance(animals[i], names[i]);
			
			System.out.println(newAnimal.name());
			System.out.println(newAnimal.menu());
			System.out.println(newAnimal.greet());
			System.out.println();
		}
		
		
	}
}
