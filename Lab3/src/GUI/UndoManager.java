package GUI;

import java.util.Stack;

public class UndoManager {
	
	private Stack<EditAction> undoStack;
	private Stack<EditAction> redoStack;
	
	public UndoManager() {
		undoStack = new Stack<EditAction>();
		redoStack = new Stack<EditAction>();
	}
	
    public void undo() {
        if (!undoStack.isEmpty()) {
            EditAction action = undoStack.pop();
            redoStack.push(action);
            action.execute_undo();
            notifyObservers();
        }
    }
	
    public void redo() {
        if (!redoStack.isEmpty()) {
            EditAction action = redoStack.pop();
            undoStack.push(action);
            action.execute_do();
            notifyObservers();
        }
    }

    public void push(EditAction action) {
        undoStack.push(action);
        redoStack.clear();
        notifyObservers();
    }
	
    private void notifyObservers() {
        System.out.println("Undo is " + (undoStack.isEmpty() ? "not available" : "available"));
        System.out.println("Redo is " + (redoStack.isEmpty() ? "not available" : "available"));
    }
	
	public Stack<EditAction> getUndoStack() {
		return undoStack;
	}
	public void setUndoStack(Stack<EditAction> undoStack) {
		this.undoStack = undoStack;
	}
	public Stack<EditAction> getRedoStack() {
		return redoStack;
	}
	public void setRedoStack(Stack<EditAction> redoStack) {
		this.redoStack = redoStack;
	} 
	

}
