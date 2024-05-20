package GUI.plugins;

import java.util.Iterator;

import javax.swing.JOptionPane;

import GUI.ClipboardStack;
import GUI.TextEditorModel;
import GUI.UndoManager;

public class VelikoSlovo implements Plugin{

	@Override
	public String getName() {
		return "Veliko Slovo";
	}

	@Override
	public String getDescription() {
		return "Plugin koji broji koliko ima redaka, riječi i slova u dokumentu i to prikazuje korisniku u dijalogu";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		 Iterator<String> iterator = model.allLines();
	        StringBuilder result = new StringBuilder();
	        int lineIndex = 0;

	        while (iterator.hasNext()) {
	            String line = iterator.next();
	            String[] words = line.split("\\s+");
	            StringBuilder transformedLine = new StringBuilder();

	            for (String word : words) {
	                if (!word.isEmpty()) {
	                    String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
	                    transformedLine.append(capitalizedWord).append(" ");
	                }
	            }

	            result.append(transformedLine.toString().trim());
	            if (iterator.hasNext()) {
	                result.append("\n");
	            }
	            
	            model.updateLine(lineIndex, transformedLine.toString().trim());
	            lineIndex++;
	        }

	        JOptionPane.showMessageDialog(null, result.toString(), "Transformed Text", JOptionPane.INFORMATION_MESSAGE);
	}

}
