package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.JPanel;

public class PanelToDisplay extends JPanel{
	
    private TextEditorModel textModel;

    public PanelToDisplay(TextEditorModel model) {
        this.textModel = model;
    }
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int lineHeight = 2;
        int y = 12;
        Iterator<String> iterator = textModel.allLines();
        String line;
        int rowIndex = 0;

        // Set a larger font size
        Font font = new Font("SansSerif", Font.PLAIN, 18);  
        g.setFont(font);

        FontMetrics fm = g.getFontMetrics();
        lineHeight = fm.getHeight();

        while (iterator.hasNext()) {
            line = iterator.next();
            int x = 0;
            LocationRange range = textModel.getSelectionRange();

            if (textModel.cursorLocation.getRow() - 1 == rowIndex) {
                line = textModel.setCursor(line);
            } else {
                line = textModel.removeCursor(line);
            }

            if (range != null && rowIndex >= range.getBegin().getRow() - 1 && rowIndex <= range.getEnd().getRow() - 1) {
                int startColumn = 0;
                int endColumn = line.length();

                if (rowIndex == range.getBegin().getRow() - 1) {
                    startColumn = range.getBegin().getColumn();
                }
                if (rowIndex == range.getEnd().getRow() - 1) {
                    endColumn = range.getEnd().getColumn();
                }

                String beforeSelection = line.substring(0, startColumn);
                int prefixWidth = g.getFontMetrics().stringWidth(beforeSelection);
                int min = Math.min(startColumn, endColumn);
                int max = Math.max(startColumn, endColumn);
                String selectionText = line.substring(min, max);
                int selectionWidth = g.getFontMetrics().stringWidth(selectionText);

                Color veryBrightBlue = new Color(204, 229, 255);
                g.setColor(veryBrightBlue);
                g.fillRect(x + prefixWidth, y - lineHeight + 3, selectionWidth, lineHeight);

                g.setColor(Color.BLACK);
            }

            g.drawString(line, x, y);
            y += lineHeight;
            rowIndex++;
        }
    }


	
}
