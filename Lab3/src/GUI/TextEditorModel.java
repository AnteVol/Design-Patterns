package GUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TextEditorModel{
    List<String> lines;  
    LocationRange selectionRange; 
    Location cursorLocation; 
    List<CursorObserver> cursorObservers;
    List<TextObserver> textObservers;

    public TextEditorModel(String text) {
        this.lines = new ArrayList<>(List.of(text.split("\n"))); 
        this.selectionRange = null;
        this.cursorLocation = setCoursorLocation(this.lines);
        this.cursorObservers = new LinkedList<CursorObserver>();
        this.textObservers = new LinkedList<TextObserver>();
    }

    public static Location setCoursorLocation(List<String> lines) {
        int max_row = lines.size();
        int current_column = lines.get(max_row - 1).length();
        Location help = new Location(max_row, current_column);
        return help;
    }
    
    public String setCursor(String line) {
    	StringBuilder sb = new StringBuilder(line);
    	sb.insert(cursorLocation.getColumn(), "|");
    	return sb.toString();
    }
    
    public String removeCursor(String line) {
    	StringBuilder sb = new StringBuilder(line);
    	int index = sb.indexOf("|");
        if (index != -1) {
            sb.deleteCharAt(index);
        }
    	return sb.toString();
    }
    
    public Iterator<String> allLines() {
        return lines.iterator();
    }

    public Iterator<String> linesRange(int index1, int index2) {
        return lines.subList(index1, Math.min(index2, lines.size())).iterator();
    }

    public void addCursorObserver(CursorObserver observer) {
        this.cursorObservers.add(observer);
    }

    public void removeCursorObservers() {
        this.cursorObservers.clear();
    }

    public void informCursorObservers() {
        for (CursorObserver o : cursorObservers) {
            o.updateCursorLocation();
        }
    }

    public void addTextObserver(TextObserver observer) {
        this.textObservers.add(observer);
    }

    public void removeTextObservers() {
        this.textObservers.clear();
    }

    public void informTextObservers() {
        for (TextObserver t : textObservers) {
            t.updateText();
        }
    }

    public void moveCursorLeft(boolean shiftPressed) {
        if (cursorLocation.getColumn() > 0) {
            Location previousLocation = new Location(cursorLocation.getRow(), cursorLocation.getColumn());
            cursorLocation.setColumn(cursorLocation.getColumn() - 1);
            updateSelection(shiftPressed, previousLocation);
            informCursorObservers();
        }
    }

    public void moveCursorRight(boolean shiftPressed) {
        if (cursorLocation.getColumn() < lines.get(cursorLocation.getRow() - 1).length()) {
            Location previousLocation = new Location(cursorLocation.getRow(), cursorLocation.getColumn());
            cursorLocation.setColumn(cursorLocation.getColumn() + 1);
            updateSelection(shiftPressed, previousLocation);
            informCursorObservers();
        }
    }

    public void moveCursorUp(boolean shiftPressed) {
        if (cursorLocation.getRow() > 1) {
            Location previousLocation = new Location(cursorLocation.getRow(), cursorLocation.getColumn());
            int targetRow = cursorLocation.getRow() - 1;
            int targetColumn = Math.min(cursorLocation.getColumn(), lines.get(targetRow - 1).length());
            cursorLocation.setRow(targetRow);
            cursorLocation.setColumn(targetColumn);
            updateSelection(shiftPressed, previousLocation);
            informCursorObservers();
        }
    }

    public void moveCursorDown(boolean shiftPressed) {
        if (cursorLocation.getRow() < lines.size()) {
            Location previousLocation = new Location(cursorLocation.getRow(), cursorLocation.getColumn());
            int targetRow = cursorLocation.getRow() + 1;
            int targetColumn = Math.min(cursorLocation.getColumn(), lines.get(targetRow - 1).length());
            cursorLocation.setRow(targetRow);
            cursorLocation.setColumn(targetColumn);
            updateSelection(shiftPressed, previousLocation);
            informCursorObservers();
        }
    }

    private void updateSelection(boolean shiftPressed, Location previousLocation) {
        if (shiftPressed) {
        	System.out.println("Radim sa shiftom");
            if (selectionRange == null) {
                selectionRange = new LocationRange(previousLocation, new Location(cursorLocation.getRow(), cursorLocation.getColumn()));
                System.out.println(selectionRange);
            } else {
            	Location newLocation = new Location(cursorLocation.getRow(), cursorLocation.getColumn());
            	if (newLocation.getRow() < selectionRange.getBegin().getRow() ||
            	    (newLocation.getRow() == selectionRange.getBegin().getRow() && newLocation.getColumn() < selectionRange.getBegin().getColumn())) {
            	    selectionRange.setBegin(newLocation);
            	} else if (newLocation.getRow() > selectionRange.getEnd().getRow() ||
            	    (newLocation.getRow() == selectionRange.getEnd().getRow() && newLocation.getColumn() > selectionRange.getEnd().getColumn())) {
            	    selectionRange.setEnd(newLocation);
            	}
            }
        } else {
            selectionRange = null;
        }
    }


    
    public void deleteBefore() {
    	if(selectionRange != null) {
    		deleteRange(selectionRange);
    		return;
    	}
        System.out.println("Deleting!");
        int rowIndex = cursorLocation.getRow() - 1;
        int columnIndex = cursorLocation.getColumn();

        if (columnIndex > 0) {
            StringBuilder newLine = new StringBuilder(lines.get(rowIndex));
            newLine.deleteCharAt(columnIndex - 1);
            lines.set(rowIndex, newLine.toString());
            moveCursorLeft(false);
            System.out.println("New line: " + lines.get(rowIndex));
            informTextObservers();
        } else {
            if (rowIndex > 0) {
                if (lines.get(rowIndex - 1).length() == 0) {
                    lines.remove(rowIndex - 1);
                    cursorLocation.setRow(rowIndex);
                    cursorLocation.setColumn(lines.get(rowIndex - 1).length());
                    informTextObservers();
                } else {
                    String currentLine = lines.get(rowIndex);
                    StringBuilder previousLine = new StringBuilder(lines.get(rowIndex - 1));
                    int newCursorColumn = previousLine.length();
                    previousLine.append(currentLine);
                    lines.set(rowIndex - 1, previousLine.toString());
                    lines.remove(rowIndex);
                    cursorLocation.setRow(rowIndex);
                    cursorLocation.setColumn(newCursorColumn);
                    informTextObservers();
                }
                System.out.println("Merged line: " + lines.get(rowIndex - 1));
            }
        }
        informCursorObservers();
    }

    
    public void deleteAfter() {
    	if(selectionRange != null) {
    		deleteRange(selectionRange);
    		return;
    	}
        int rowIndex = cursorLocation.getRow() - 1;
        int columnIndex = cursorLocation.getColumn();

        if (rowIndex < 0 || rowIndex >= lines.size()) return; 

        String currentLine = lines.get(rowIndex);
        boolean isLastCharacterInRow = columnIndex == currentLine.length();

        if (isLastCharacterInRow) {
            if (rowIndex < lines.size() - 1) {
                StringBuilder newLine = new StringBuilder(currentLine);
                newLine.append(lines.get(rowIndex + 1));
                lines.set(rowIndex, newLine.toString());
                lines.remove(rowIndex + 1);
                informTextObservers();
            }
        } else if (columnIndex < currentLine.length()) {
            StringBuilder newLine = new StringBuilder(currentLine);
            newLine.deleteCharAt(columnIndex);
            lines.set(rowIndex, newLine.toString());
            informTextObservers();
        }
        informCursorObservers();
    }

    
    public void deleteRange(LocationRange r) {
        if (r == null) return;

        Location start = r.getBegin();
        Location end = r.getEnd();

        if (start.getRow() > end.getRow() || (start.getRow() == end.getRow() && start.getColumn() > end.getColumn())) {
            Location temp = start;
            start = end;
            end = temp;
        }

        if (start.getRow() == end.getRow()) {
            StringBuilder line = new StringBuilder(lines.get(start.getRow() - 1));
            line.delete(start.getColumn(), end.getColumn());
            lines.set(start.getRow() - 1, line.toString());
        } else {
            StringBuilder firstLine = new StringBuilder(lines.get(start.getRow() - 1));
            StringBuilder lastLine = new StringBuilder(lines.get(end.getRow() - 1));

            firstLine.delete(start.getColumn(), firstLine.length());
            lastLine.delete(0, end.getColumn());

            firstLine.append(lastLine.toString());
            lines.set(start.getRow() - 1, firstLine.toString());

            for (int i = start.getRow(); i < end.getRow(); i++) {
                lines.remove(start.getRow());
            }
        }

        cursorLocation = new Location(start.getRow(), start.getColumn());
        selectionRange = null;
        informCursorObservers();
    }
    
    public LocationRange getSelectionRange() {
        if (selectionRange != null) {
            return selectionRange;
        }
        return null;
    }

    
    public void setSelectionRange(LocationRange range) {
        if (range == null) {
            selectionRange = null;
        } else {
            int startRow = Math.min(range.getBegin().getRow(), range.getEnd().getRow());
            int endRow = Math.max(range.getBegin().getRow(), range.getEnd().getRow());
            int startCol = range.getBegin().getColumn();
            int endCol = range.getEnd().getColumn();
            if (startRow == endRow) {
                startCol = Math.min(startCol, endCol);
                endCol = Math.max(startCol, endCol);
            }
            selectionRange = new LocationRange(new Location(startRow, startCol), new Location(endRow, endCol));
        }
        informTextObservers();
    }

    
    public void insert(char c) {
        if (selectionRange != null) {
            deleteRange(selectionRange);
            selectionRange = null; 
        }
        if (c == 10) { 
            String line = lines.get(cursorLocation.getRow() - 1);
            String first = line.substring(0, cursorLocation.getColumn());
            String second = line.substring(cursorLocation.getColumn());
            lines.set(cursorLocation.getRow() - 1, first);
            lines.add(cursorLocation.getRow(), second);
            cursorLocation.setRow(cursorLocation.getRow() + 1);
            cursorLocation.setColumn(0);
        } else {
            StringBuilder currentLine = new StringBuilder(lines.get(cursorLocation.getRow() - 1));
            currentLine.insert(cursorLocation.getColumn(), c);
            lines.set(cursorLocation.getRow() - 1, currentLine.toString());
            cursorLocation.setColumn(cursorLocation.getColumn() + 1);
        }
        informCursorObservers();
    }

    public void insert(String text) {
        if (selectionRange != null) {
            deleteRange(selectionRange);
            selectionRange = null; 
        }

        String[] parts = text.split("\n", -1);
        if (parts.length == 1) {
            StringBuilder currentLine = new StringBuilder(lines.get(cursorLocation.getRow() - 1));
            currentLine.insert(cursorLocation.getColumn(), parts[0]);
            lines.set(cursorLocation.getRow() - 1, currentLine.toString());
            cursorLocation.setColumn(cursorLocation.getColumn() + parts[0].length());
        } else {
            String firstPart = parts[0];
            StringBuilder currentLine = new StringBuilder(lines.get(cursorLocation.getRow() - 1));
            currentLine.insert(cursorLocation.getColumn(), firstPart);
            lines.set(cursorLocation.getRow() - 1, currentLine.toString());

            cursorLocation.setColumn(cursorLocation.getColumn() + firstPart.length());

            for (int i = 1; i < parts.length; i++) {
                String line = parts[i];
                lines.add(cursorLocation.getRow(), line);
                cursorLocation.setRow(cursorLocation.getRow() + 1);
                cursorLocation.setColumn(line.length());
            }
        }

        informCursorObservers();
        informTextObservers();
    }


    public String getTextFromRange(LocationRange range) {
        if (range == null) {
            return null; 
        }

        Location begin = range.getBegin();
        Location end = range.getEnd();

        if (begin.getRow() == end.getRow()) {
            String line = lines.get(begin.getRow() - 1);
            return line.substring(begin.getColumn(), end.getColumn());
        } else {
            StringBuilder text = new StringBuilder();
            text.append(lines.get(begin.getRow() - 1).substring(begin.getColumn()));

            for (int i = begin.getRow(); i < end.getRow() - 1; i++) {
                text.append("\n").append(lines.get(i));
            }

            text.append("\n").append(lines.get(end.getRow() - 1).substring(0, end.getColumn()));
            System.out.println();
            return text.toString();
        }
    }
    
    public void updateLine(int index, String newContent) {
        if (index >= 0 && index < lines.size()) {
            lines.set(index, newContent);
            informTextObservers();
        }
    }
}
