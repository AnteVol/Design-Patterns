package hr.fer.zemris.ooup.lab2.factory;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;

import hr.fer.zemris.ooup.lab2.model.*;

public class AnimalFactory {
    
    private static URLClassLoader classLoader = createClassLoader();

    private static URLClassLoader createClassLoader() {
        try {
            URL[] urls = new URL[]{
                new File("D:/java/plugins/").toURI().toURL(),
                new File("D:/java/plugins-jarovi/zivotinje.jar").toURI().toURL()
            };
            ClassLoader parent = AnimalFactory.class.getClassLoader();
            return new URLClassLoader(urls, parent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Animal newInstance(String animalKind, String name) {
        if (classLoader == null) {
            System.out.println("ClassLoader nije inicijaliziran.");
            return null;
        }
        
        try {
            String className = "hr.fer.zemris.ooup.lab2.model.plugins." + animalKind;
            Class<Animal> clazz = (Class<Animal>) Class.forName(className, true, classLoader);
            
            Constructor<?> ctr = clazz.getConstructor(String.class);
            Animal animal = (Animal) ctr.newInstance(name);
            
            return animal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
