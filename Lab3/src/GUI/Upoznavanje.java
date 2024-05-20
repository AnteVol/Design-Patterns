package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;

public class Upoznavanje extends JFrame {
    public Upoznavanje() {
        setTitle("Custom Drawing and Interaction");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        add(new DrawingPanel());
        
        getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "closeTheWindow");
        getRootPane().getActionMap().put("closeTheWindow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            
            g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            
            g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
            
            g.drawString("Prvi red teksta", 20, 20);
            g.drawString("Drugi red teksta", 20, 40);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Upoznavanje().setVisible(true);
            }
        });
    }
}
