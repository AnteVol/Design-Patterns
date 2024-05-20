package GUI;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import GUI.plugins.*;

import javax.print.DocFlavor.URL;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;


public class TextEditor extends JFrame implements CursorObserver, TextObserver{
	
	TextEditorModel textModel;
	PanelToDisplay panel;
	ClipboardStack clipboard;
    JLabel statusBar;
    List<Plugin> allPlugins;
	
	public TextEditor(String text) {
		textModel = new TextEditorModel(text);
		textModel.addCursorObserver(this);
		panel = new PanelToDisplay(textModel);
		clipboard = new ClipboardStack();
		statusBar = new JLabel();
		allPlugins = new ArrayList<Plugin>();
        getContentPane().add(panel, BorderLayout.CENTER);
		setTitle("Text editor");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadPlugins();
        setCursorActions();
        setOtherActions();
        setTypingActions();
        setCopyPasteActions();
        setupStatusBar();
        addMenuBarToolBarAndItems();
	}
	
	public void setCursorActions() {
	    KeyStroke upKey = KeyStroke.getKeyStroke("UP");
	    KeyStroke shiftUpKey = KeyStroke.getKeyStroke("shift UP");
	    KeyStroke downKey = KeyStroke.getKeyStroke("DOWN");
	    KeyStroke shiftDownKey = KeyStroke.getKeyStroke("shift DOWN");
	    KeyStroke leftKey = KeyStroke.getKeyStroke("LEFT");
	    KeyStroke shiftLeftKey = KeyStroke.getKeyStroke("shift LEFT");
	    KeyStroke rightKey = KeyStroke.getKeyStroke("RIGHT");
	    KeyStroke shiftRightKey = KeyStroke.getKeyStroke("shift RIGHT");

	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(upKey, "moveCursorUP");
	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(shiftUpKey, "selectUp");
	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(downKey, "moveCursorDOWN");
	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(shiftDownKey, "selectDown");
	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(leftKey, "moveCursorLEFT");
	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(shiftLeftKey, "selectLeft");
	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(rightKey, "moveCursorRIGHT");
	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(shiftRightKey, "selectRight");

	    getRootPane().getActionMap().put("moveCursorUP", new AbstractAction() {
	        public void actionPerformed(ActionEvent e) {
	            textModel.moveCursorUp(false);
	        }
	    });
	    getRootPane().getActionMap().put("moveCursorDOWN", new AbstractAction() {
	        public void actionPerformed(ActionEvent e) {
	            textModel.moveCursorDown(false);
	        }
	    });
	    getRootPane().getActionMap().put("moveCursorLEFT", new AbstractAction() {
	        public void actionPerformed(ActionEvent e) {
	            textModel.moveCursorLeft(false);
	        }
	    });
	    getRootPane().getActionMap().put("moveCursorRIGHT", new AbstractAction() {
	        public void actionPerformed(ActionEvent e) {
	            textModel.moveCursorRight(false);
	        }
	    });

	    getRootPane().getActionMap().put("selectUp", new AbstractAction() {
	        public void actionPerformed(ActionEvent e) {
	            textModel.moveCursorUp(true);
	        }
	    });
	    getRootPane().getActionMap().put("selectDown", new AbstractAction() {
	        public void actionPerformed(ActionEvent e) {
	            textModel.moveCursorDown(true);
	        }
	    });
	    getRootPane().getActionMap().put("selectLeft", new AbstractAction() {
	        public void actionPerformed(ActionEvent e) {
	            textModel.moveCursorLeft(true);
	        }
	    });
	    getRootPane().getActionMap().put("selectRight", new AbstractAction() {
	        public void actionPerformed(ActionEvent e) {
	            textModel.moveCursorRight(true);
	        }
	    });
	}
	
	public void setCopyPasteActions() {
		 KeyStroke ctrlC = KeyStroke.getKeyStroke("control C");
	        getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrlC, "copy");
	        getRootPane().getActionMap().put("copy", new AbstractAction() {
	            public void actionPerformed(ActionEvent e) {
	                System.out.println("copy");
	                LocationRange range = textModel.getSelectionRange();
	                if (range != null) {
	                    clipboard.put(textModel.getTextFromRange(range));
	                }
	            }
	        });

	        // Paste
	        KeyStroke ctrlV = KeyStroke.getKeyStroke("control V");
	        getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrlV, "paste");
	        getRootPane().getActionMap().put("paste", new AbstractAction() {
	            public void actionPerformed(ActionEvent e) {
	                System.out.println("paste" + clipboard.peek());
	                if (clipboard.peek() != null) {
	                    textModel.insert(clipboard.peek());
	                }
	            }
	        });

	        // Cut
	        KeyStroke ctrlX = KeyStroke.getKeyStroke("control X");
	        getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrlX, "cut");
	        getRootPane().getActionMap().put("cut", new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                System.out.println("cut");
	                LocationRange range = textModel.getSelectionRange();
	                if (range != null) {
	                    clipboard.put(textModel.getTextFromRange(range));
	                    textModel.deleteRange(range);
	                }
	            }
	        });

	        // Paste and Remove
	        KeyStroke ctrlShiftV = KeyStroke.getKeyStroke("control shift V");
	        getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrlShiftV, "pasteAndRemove");
	        getRootPane().getActionMap().put("pasteAndRemove", new AbstractAction() {
	            public void actionPerformed(ActionEvent e) {
	                System.out.println("paste and remove");
	                if (clipboard.peek() != null) {
	                    textModel.insert(clipboard.pop());
	                }
	            }
	        });

	}

	
	public void setTypingActions() {
	    String allChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789?.! ";
	    for (char ch : allChars.toCharArray()) {
	        String keyStrokeName = "typed " + ch;
	        getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(ch), keyStrokeName);
	        getRootPane().getActionMap().put(keyStrokeName, new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	System.out.println(ch);
	                textModel.insert(ch);
	            }
	        });
	    }
	    
	    String tabKeyStrokeName = "typed Tab";
	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), tabKeyStrokeName);
	    getRootPane().getActionMap().put(tabKeyStrokeName, new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            char tab = '\t';
	            System.out.println(tab);
	            textModel.insert(tab);
	        }
	    });

	    String enterKeyStrokeName = "typed Enter";
	    getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), enterKeyStrokeName);
	    getRootPane().getActionMap().put(enterKeyStrokeName, new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            char enter = '\n';
	            System.out.println(enter);
	            textModel.insert(enter);
	        }
	    });
	}
	
	public void setOtherActions() {
		getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("BACK_SPACE"), "deleteTextBehind");
        getRootPane().getActionMap().put("deleteTextBehind", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textModel.deleteBefore();
            }
        });
        
        getRootPane().getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), "deleteTextInFront");
        getRootPane().getActionMap().put("deleteTextInFront", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textModel.deleteAfter();
            }
        });
	}
	
	
	public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	TextEditor t = new TextEditor("");
                t.setVisible(true);
                t.textModel.cursorLocation.printLoaction();
                
            }
        });
    }
	
	public void addMenuBarToolBarAndItems() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(open);
		open.addActionListener(e -> {
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); 
	        fileChooser.setDialogTitle("Select a Text File");
	        fileChooser.setAcceptAllFileFilterUsed(false);
	        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));

	        int result = fileChooser.showOpenDialog(this);

	        if (result == JFileChooser.APPROVE_OPTION) {
	            File selectedFile = fileChooser.getSelectedFile();
	            try {
	            	List<String> lines = Files.readAllLines(selectedFile.toPath(), StandardCharsets.UTF_8);
	            	for(String l : lines) {
	            		System.out.println(l);
	            	}
	            	textModel.lines = lines;
	            	updateCursorLocation();
	            	updateStatusBar();
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(new Frame(), "Error reading the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
		});
		fileMenu.add(save);
		save.addActionListener(e -> {
		    try {
		        Iterator<String> lines = textModel.allLines();
		        Path file = Paths.get("C:\\Users\\antev\\Documents\\Fakultet\\Fakultet 3. godina\\6. Semestar\\OOUP\\3LV JAVA\\src\\GUI\\TextEditorSaver.txt");
		        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
		            while (lines.hasNext()) {
		                writer.write(lines.next());
		                writer.newLine();
		            }
		            System.out.println("File written successfully.");
		        }
		        JOptionPane.showMessageDialog(new Frame(), "Data saved successfully!", "Save", JOptionPane.INFORMATION_MESSAGE);
		        
		        System.out.println("Exiting!");
		        System.exit(0);
		    } catch (IOException ex) {
		        JOptionPane.showMessageDialog(new Frame(), "Failed to save data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(new Frame(), "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    }
		});


		fileMenu.add(exit);
		exit.addActionListener(e -> {
            System.out.println("Closing...");
            System.exit(0);
		});
		
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		JMenuItem undo = new JMenuItem("Undo");
		JMenuItem undo1 = new JMenuItem("Undo");
		undo.setEnabled(false);
		JMenuItem redo = new JMenuItem("Redo");
		JMenuItem redo1 = new JMenuItem("Redo");
		redo.setEnabled(false);
		JMenuItem cut = new JMenuItem("Cut");
		JMenuItem cut1 = new JMenuItem("Cut");
		cut.addActionListener(e -> {
			LocationRange range = textModel.getSelectionRange();
            if (range != null) {
                clipboard.put(textModel.getTextFromRange(range));
                textModel.deleteRange(range);
            }
		});
		cut1.addActionListener(e -> {
			System.out.println("cut");
			LocationRange range = textModel.getSelectionRange();
            if (range != null) {
                clipboard.put(textModel.getTextFromRange(range));
                textModel.deleteRange(range);
            }
		});
		if(textModel.selectionRange == null) {
			cut.setEnabled(false);
			cut1.setEnabled(false);
		}else {
			cut.setEnabled(true);
			cut1.setEnabled(true);
		}
		JMenuItem copy = new JMenuItem("Copy");
		JMenuItem copy1 = new JMenuItem("Copy");
		copy.addActionListener(e -> {
                System.out.println("copy");
                LocationRange range = textModel.getSelectionRange();
                if (range != null) {
                    clipboard.put(textModel.getTextFromRange(range));
                }
                textModel.informCursorObservers();
		});
		copy1.addActionListener(e -> {
            System.out.println("copy");
            LocationRange range = textModel.getSelectionRange();
            if (range != null) {
                clipboard.put(textModel.getTextFromRange(range));
            }
            textModel.informCursorObservers();
		});
		if(textModel.selectionRange == null) {
			copy.setEnabled(false);
			copy1.setEnabled(false);
		}else {
			copy.setEnabled(true);
			copy1.setEnabled(true);
		}
		JMenuItem paste = new JMenuItem("Paste");
		JMenuItem paste1 = new JMenuItem("Paste");
		paste.addActionListener(e -> {
                System.out.println("paste" + clipboard.peek());
                if (clipboard.peek() != null) {
                    textModel.insert(clipboard.peek());
                }
                textModel.informCursorObservers();
		});
		paste1.addActionListener(e -> {
            System.out.println("paste" + clipboard.peek());
            if (clipboard.peek() != null) {
                textModel.insert(clipboard.peek());
            }
            textModel.informCursorObservers();
		});
		if(clipboard.texts.isEmpty()) {
			paste.setEnabled(false);
			paste1.setEnabled(false);
		}else {
			paste.setEnabled(true);
			paste1.setEnabled(true);
		}
		JMenuItem pasteAndTake = new JMenuItem("Paste and take");
		pasteAndTake.addActionListener(e -> {
                System.out.println("paste and remove");
                if (clipboard.peek() != null) {
                    textModel.insert(clipboard.pop());
                }
                textModel.informCursorObservers();
		});
		if(clipboard.texts.isEmpty()) {
			pasteAndTake.setEnabled(false);
		}else {
			pasteAndTake.setEnabled(true);
		}
		JMenuItem deleteSelection = new JMenuItem("Delete selection");
		deleteSelection.addActionListener(e -> {
            System.out.println("paste and remove");
            textModel.deleteRange(textModel.selectionRange);
            textModel.informCursorObservers();
		});
		if(textModel.selectionRange == null) {
			deleteSelection.setEnabled(false);
		}else {
			deleteSelection.setEnabled(true);
		}
		JMenuItem clearDocument = new JMenuItem("Clear document");
		clearDocument.addActionListener(e -> {
            textModel.lines.clear();
            textModel.lines.add("");
            textModel.cursorLocation = new Location(1, 0);
            textModel.informCursorObservers();
		});
		
		editMenu.add(undo);
		editMenu.add(redo);
		editMenu.add(cut1);
		editMenu.add(copy1);
		editMenu.add(paste1);
		editMenu.add(pasteAndTake);
		editMenu.add(deleteSelection);
		editMenu.add(clearDocument);
		
		JMenu moveMenu = new JMenu("Move");
		menuBar.add(moveMenu);
		JMenuItem cursorToStartDocument = new JMenuItem("Cursor to document start");
		cursorToStartDocument.addActionListener(e -> {
		    textModel.cursorLocation = new Location(1, 0);
		    textModel.informCursorObservers();
		});
		JMenuItem cursorToEndDocument = new JMenuItem("Cursor to document end");
		cursorToEndDocument.addActionListener(e -> {
		    int lastLineIndex = textModel.lines.size() - 1;
		    int lastLineLength = textModel.lines.get(lastLineIndex).length();
		    textModel.cursorLocation = new Location(lastLineIndex + 1, lastLineLength);
		    textModel.informCursorObservers();
		});
		moveMenu.add(cursorToStartDocument);
		moveMenu.add(cursorToEndDocument);
		
		JMenu plugins = new JMenu("Plugins");
		menuBar.add(plugins);
        for(Plugin p : allPlugins) {
        	JMenuItem plugin = new JMenuItem(p.getName());
        	plugin.addActionListener(e -> p.execute(textModel, null, clipboard));
        	plugins.add(plugin);
        	System.out.println(p.getName());
        }
		
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		toolBar.add(undo);
		toolBar.add(redo);
		toolBar.add(cut);
		toolBar.add(copy);
		toolBar.add(paste);
		
	}
	
	   private void setupStatusBar() {
	        statusBar = new JLabel();
	        statusBar.setBorder(BorderFactory.createEtchedBorder());
	        getContentPane().add(statusBar, BorderLayout.SOUTH);
	        updateStatusBar();
	    }

	    private void updateStatusBar() {
	        int lineCount = textModel.lines.size();
	        Location cursorLocation = textModel.cursorLocation;
	        statusBar.setText("Line: " + cursorLocation.getRow() + ", Column: " + cursorLocation.getColumn() + " | Total lines: " + lineCount);
	    }	
	    
	    private void loadPlugins() {
	    	String folderPath = "C:\\Users\\antev\\Documents\\Fakultet\\Fakultet 3. godina\\6. Semestar\\OOUP\\3LV JAVA\\src\\GUI\\plugins";
	        File folder = new File(folderPath);
	        File[] files = folder.listFiles((dir, name) -> name.endsWith(".java"));
	        System.out.println("Loadam");
	        if (files != null) {
	            for (File file : files) {
	                try {
	                    String className = file.getName().substring(0, file.getName().length() - 5);
	                    if ("Plugin".equals(className)) {
	                        continue;
	                    }
	                    String packageName = "GUI.plugins.";
	                    java.net.URL[] urls = {new File(folderPath).toURI().toURL()};
	                    try (URLClassLoader classLoader = new URLClassLoader(urls)) {
	                        Class<?> cls = Class.forName(packageName + className, true, classLoader);
	                        Constructor<?> constructor = cls.getConstructor();
	                        Plugin instance = (Plugin) constructor.newInstance();
	                        allPlugins.add(instance);
	                        System.out.println("Created instance of " + className + ": " + instance.getName());
	                    }
	                } catch (Exception e) {
	                    System.err.println("Could not instantiate " + file.getName() + ": " + e.getMessage());
	                }
	            }
	        }
	    }

	    
	@Override
	public void updateCursorLocation() {
		updateStatusBar();
		addMenuBarToolBarAndItems();
		panel.repaint();
	}

	@Override
	public void updateText() {
		updateStatusBar();
		panel.repaint();
	}

}
