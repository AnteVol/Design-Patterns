package GUI;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ClipboardStack {
	
	Stack<String> texts;
	List<ClipboardObserver> clipboardObservers;
	

	public ClipboardStack() {
		super();
		this.texts = new Stack<String>();
		this.clipboardObservers = new LinkedList<ClipboardObserver>();
	}

	public Stack<String> getTexts() {
		return texts;
	}

	public void setTexts(Stack<String> texts) {
		this.texts = texts;
	}
	
	public void put(String line) {
		texts.push(line);
	}
	
	public String pop() {
		 return texts.pop();
	}
	
	public String peek() {
		if(texts.isEmpty()) {
			return null;
		}
		return texts.peek();
	}
	
    public void addClipboardObserver(ClipboardObserver observer) {
        this.clipboardObservers.add(observer);
    }

    public void removeClipboardObservers() {
        this.clipboardObservers.clear();
    }

    public void informClipboardObservers() {
        for (ClipboardObserver c : clipboardObservers) {
            c.updateClipboard();
        }
    }
	
}
