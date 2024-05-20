package GUI.plugins;

import GUI.ClipboardStack;
import GUI.TextEditorModel;
import GUI.UndoManager;

public interface Plugin {
	  String getName(); // ime plugina (za izbornicku stavku)
	  String getDescription(); // kratki opis
	  void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}
