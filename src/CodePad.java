import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;

/*
 * Ivo Eijgenraam, GeeksforGeeks.
 */
@SuppressWarnings("serial")
public class CodePad extends JFrame implements ActionListener, WindowListener {
	JTextArea jta = new JTextArea();
	File fnameContainer;
	String cpd = " - CodePad";

	public CodePad() {
		Font fnt = new Font("Courier New", Font.PLAIN, 13);
		Container con = getContentPane();

		// Set look and feel.
		try { 
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 
		} catch(Exception ignored){}

		// Setting up the menu.
		JMenuBar jmb = new JMenuBar();
		JMenu jmfile = new JMenu("File");
		JMenu jmedit = new JMenu("Edit");
		JMenu jmhelp = new JMenu("Help");

		// Setting up window.
		con.setLayout(new BorderLayout());
		JScrollPane sbrText = new JScrollPane(jta);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Setting up text-area.
		jta.setFont(fnt);
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);

		con.add(sbrText);

		// Add all the items to the drop-down menu.
		createMenuItem(jmfile, "New");
		createMenuItem(jmfile, "Open");
		createMenuItem(jmfile, "Save As");
		createMenuItem(jmfile, "Save with Name for Calco");
		jmfile.addSeparator(); // Add separator in menu.
		createMenuItem(jmfile, "Exit");
		createMenuItem(jmedit, "Find");
		createMenuItem(jmedit, "Cut");
		createMenuItem(jmedit, "Copy");
		createMenuItem(jmedit, "Paste");
		createMenuItem(jmedit, "Add Comment..");
		createMenuItem(jmhelp, "About CodePad");

		// Adding menu's to JMenuBar.
		jmb.add(jmfile);
		jmb.add(jmedit);
		jmb.add(jmhelp);

		// Setting up window.
		setJMenuBar(jmb);
		setIconImage(Toolkit.getDefaultToolkit().getImage("calco.png")); // Set app icon.
		addWindowListener(this);
		setSize(640,480);
		setTitle("Untitled.txt - CodePad");
		setVisible(true);
	}

	public void createMenuItem(JMenu jm, String txt) {
		// Method for creating menu items used in constructor.
		JMenuItem jmi = new JMenuItem(txt);
		jmi.addActionListener(this);
		jm.add(jmi);
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();

		// New file == reset Title, reset JTextArea, reset filename container.
		if(e.getActionCommand().equals("New")) {
			this.setTitle("Untitled.txt - Notepad");
			jta.setText("");
			fnameContainer = null;
			
		// Open file.
		} else if(e.getActionCommand().equals("Open")) {

			int ret = jfc.showDialog(null,"Open");
			if(ret == JFileChooser.APPROVE_OPTION) { // If both are true.
				try {
					File fyl = jfc.getSelectedFile();
					OpenFile(fyl.getAbsolutePath());
					this.setTitle(fyl.getName()+cpd); // Set title to Filename - CodePad.
					fnameContainer = fyl;
				} catch(IOException eo) {}
			}

		// Save file as.
		} else if(e.getActionCommand().equals("Save As")) {
			if (fnameContainer!=null) {
				jfc.setCurrentDirectory(fnameContainer);
				jfc.setSelectedFile(fnameContainer);
			} else {
				jfc.setSelectedFile(new File("Untitled.txt"));
			}

			int ret = jfc.showSaveDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION) {
				try {
					File fyl = jfc.getSelectedFile();
					SaveFile(fyl.getAbsolutePath());
					this.setTitle(fyl.getName()+cpd);
					fnameContainer = fyl;
				} catch(Exception es) {}
			}
		
		// Save file with name for Calco, to hand in exercises the right way.
		} else if(e.getActionCommand().equals("Save with Name for Calco")) {
			if (fnameContainer!=null) {
				jfc.setCurrentDirectory(fnameContainer);
				jfc.setSelectedFile(fnameContainer);
			} else {
				jfc.setSelectedFile(new File("Exercise_Ivo_Eijgenraam.txt"));
			}
			
			int ret = jfc.showSaveDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION) {
				try {
					File fyl = jfc.getSelectedFile();
					SaveFile(fyl.getAbsolutePath());
					this.setTitle(fyl.getName()+cpd);
					fnameContainer = fyl;
				} catch(Exception es) {}
			}
		} else if(e.getActionCommand().equals("Exit")) {
			Exiting();
		} else if(e.getActionCommand().equals("Cut")) {
			jta.cut();
		}else if(e.getActionCommand().equals("Copy")) {
			jta.copy();
		} else if(e.getActionCommand().equals("Paste")) {
			jta.paste();
		} else if(e.getActionCommand().equals("Find")) {
			findText();
		} else if(e.getActionCommand().equals("Add Comment..")) {
			addComment();
		} else if(e.getActionCommand().equals("About CodePad")) {
			JOptionPane.showMessageDialog(this, "Created for last minute editing.\nNever again forget to add comments!\n\nBy Ivo with Geeks", "CodePad", JOptionPane.INFORMATION_MESSAGE);
		} 
	}

	public void OpenFile(String fname) throws IOException {
		// BufferedReader reads text from a character-input stream, our file.
		BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(fname)));
		String l;
		jta.setText("");

		// Read all the data.
		while((l=d.readLine())!= null) {
			jta.setText(jta.getText()+l+"\r\n");
		}

		d.close();
	}

	public void SaveFile(String fname) throws IOException {
		DataOutputStream o = new DataOutputStream(new FileOutputStream(fname));
		o.writeBytes(jta.getText());
		o.close();
	}
	
	public void findText() {
		// Show input dialog and find first word that matches.
		final String inputValue = JOptionPane.showInputDialog("What word?");
		final int valueIndex = jta.getText().indexOf(inputValue);
		final int valueLength = inputValue.length();

		if (valueIndex == -1) {
		    JOptionPane.showMessageDialog(null, "Can't find it."); // If nothing was found.
		} else {
		    jta.select(valueIndex, valueLength+valueIndex); // Selection start and selection end.
		}
	}
	
	public void addComment() {
		JPanel pnlComment = new JPanel(new GridLayout());
		JTextField txtComment = new JTextField(48);
		pnlComment.add(txtComment);
        setVisible(true);
        
        // Give options small and large. Depending on choice add different comments.
		Object[] options = {"Small", "Large"};
        Object optionComment = JOptionPane.showInputDialog(null, pnlComment, 
                "What comment do you want to add?", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        if (optionComment == "Small") {
        	try {
        		// Add small comment at cursor.
				jta.getDocument().insertString(jta.getCaretPosition(), "// " + txtComment.getText(), null);
			} catch (BadLocationException e) {}
        } else if (optionComment == "Large") {
        	try {
        		// Add large comment at cursor.
				jta.getDocument().insertString(jta.getCaretPosition(), "\n/* \n * " + txtComment.getText() + " \n */ \n", null);
			} catch (BadLocationException e) {}
        } 		
	}
	
	public void windowDeactivated(WindowEvent e) {}

	public void windowActivated(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowClosed(WindowEvent e) {}

	public void windowClosing(WindowEvent e) {
		Exiting();
	}

	public void windowOpened(WindowEvent e) {}

	public void Exiting() {
		System.exit(0);
	}

}


