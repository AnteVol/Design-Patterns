import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

interface Observer {
    void performAction(List<Integer> numbers);
}

interface IzvorBrojeva {
    int getNextNumber();
}

class FileObserver implements Observer {
    @Override
    public void performAction(List<Integer> numbers) {
        String filePath = "C:\\Users\\antev\\Documents\\Fakultet\\Fakultet 3. godina\\6. Semestar\\OOUP\\JAVA\\Labosi\\src\\outputFile.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int number : numbers) {
                writer.write(Integer.toString(number));
                writer.newLine();
            }
            writer.write("Datum i vrijeme: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss", new Locale("hr"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SumObserver implements Observer {
    @Override
    public void performAction(List<Integer> numbers) {
        System.out.println("Sum: " + numbers.stream().mapToInt(Integer::intValue).sum());
    }
}

class AverageObserver implements Observer {
    @Override
    public void performAction(List<Integer> numbers) {
        System.out.println("Average: " + (int)(numbers.stream().mapToDouble(Integer::intValue).average().getAsDouble()));
    }
}

class MedianObserver implements Observer {
    @Override
    public void performAction(List<Integer> numbers) {
        Collections.sort(numbers);
        double median;
        int size = numbers.size();
        if (size % 2 == 0) {
            median = (numbers.get(size / 2 - 1) + numbers.get(size / 2)) / 2.0;
        } else {
            median = numbers.get(size / 2);
        }
        System.out.println("Median: " + median);
        System.out.println("==================");
    }
}

interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notify(List<Integer> numbers);
}

class TipkovnickiIzvor implements IzvorBrojeva {
    private Scanner scanner;

    public TipkovnickiIzvor(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getNextNumber() {
        return scanner.nextInt();
    }
}

class DatotecniIzvor implements IzvorBrojeva {
    private Scanner scanner;

    public DatotecniIzvor(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getNextNumber() {
        if (scanner.hasNextInt()) {
        	int NumberToPrint = scanner.nextInt();
        	System.out.println(NumberToPrint);
            return NumberToPrint;
        }
        return -1; 
    }
}

class SlijedBrojeva implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private List<Integer> numbers = new ArrayList<>();
    private boolean running = false;
    private IzvorBrojeva izvor;

    public SlijedBrojeva(IzvorBrojeva izvor) {
        this.izvor = izvor;
    }

    public void start() {
        running = true;
        while (running) {
            int nextNumber = izvor.getNextNumber();
            if (nextNumber == -1) {
                running = false;
            } else {
                numbers.add(nextNumber);
                notify(numbers);
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(List<Integer> numbers) {
        for (Observer observer : observers) {
            observer.performAction(numbers);
        }
    }
}

public class Fifth {
    public static void main(String[] args) throws FileNotFoundException {
        //Scanner scanner = new Scanner(System.in);
        //IzvorBrojeva izvor = new TipkovnickiIzvor(scanner);
        
        String filePath = "C:\\Users\\antev\\Documents\\Fakultet\\Fakultet 3. godina\\6. Semestar\\OOUP\\JAVA\\Labosi\\src\\inputFile.txt";
        Scanner scanner = new Scanner(new File(filePath));
        IzvorBrojeva izvor = new DatotecniIzvor(scanner);

        SlijedBrojeva sb = new SlijedBrojeva(izvor);

        sb.addObserver(new FileObserver());
        sb.addObserver(new SumObserver());
        sb.addObserver(new AverageObserver());
        sb.addObserver(new MedianObserver());

        System.out.println("Unesite brojeve (-1 za zaustavljanje):");
        sb.start();

        scanner.close();
    }
}
