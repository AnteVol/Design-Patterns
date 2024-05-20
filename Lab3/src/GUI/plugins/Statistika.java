package GUI.plugins;

import java.util.Iterator;

import javax.swing.JOptionPane;

import GUI.ClipboardStack;
import GUI.TextEditorModel;
import GUI.UndoManager;

public class Statistika implements Plugin{

	@Override
	public String getName() {
		return "Statistika";
	}

	@Override
	public String getDescription() {
		return "Plugin koji broji koliko ima redaka, riječi i slova u dokumentu i to prikazuje korisniku u dijalogu";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
	       int numberOfChars = 0, numberOfWords = 0, numberOfRows = 0;
	        Iterator<String> iterator = model.allLines();
	        String line;

	        while (iterator.hasNext()) {
	            line = iterator.next();
	            numberOfRows++;

	            if (!line.isBlank()) {
	                String[] words = line.trim().split("\\s+");
	                numberOfWords += words.length;

	                numberOfChars += line.replace(" ", "").length(); 
	            }
	        }

	        String message = String.format("Number of lines: %d%nNumber of words: %d%nNumber of characters (excluding spaces): %d",
	                                       numberOfRows, numberOfWords, numberOfChars);
	        
	        JOptionPane.showMessageDialog(null, message, "Document Statistics", JOptionPane.INFORMATION_MESSAGE);

	}

}
