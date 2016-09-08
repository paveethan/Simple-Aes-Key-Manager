import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.table.*;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.util.Scanner;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;


public class guiClient implements ActionListener {
	static private JFrame frame;
	static private  JPanel mainPanel, p1, p2,p3, buttonPanel, buttonPanel2, buttonPanel3;
	static private  CardLayout cdLayout = new CardLayout();
	static private  JLabel t1,t2, t3;
	static private  JButton newUser, loadUser, saveToText, goBackHome, goBackHome2, setDecryptPw, loadTable;
	static private  String inputFile = "", outputFile = "", encryptionPW = "", decryptionPW = "";
	static private  JFileChooser chooser;
	static private  int returnVal = 0;
	static private  boolean fileSave = false, fileOpen = false;
	static private  JTable table, table2;
	static private  Cipher cipher;
	static private  boolean passSet = false;

	public guiClient(){
		//mainPanel
		mainPanel = new JPanel();
		mainPanel.setLayout(cdLayout);
		//frame
		frame = new JFrame();
		//panels
		p1 = new JPanel(new BorderLayout());
		p2 = new JPanel(new BorderLayout()); 
		p3 = new JPanel(new BorderLayout()); 

		//panel 1
		t1 = new JLabel("<html><p>MTH-816: Bespoke Project 3 (Simple AES Password Manager) </p>"
				+ "</html>", SwingConstants.CENTER);
		
		buttonPanel = new JPanel(new GridLayout(2,1));

		newUser = new JButton ("New User Profile");
		newUser.setActionCommand("newUser");
		newUser.addActionListener(this);

		loadUser = new JButton ("Load User Profile");
		loadUser.setActionCommand("loadUser");
		loadUser.addActionListener(this);
		
		buttonPanel.add(newUser);
		buttonPanel.add(loadUser);
		
		p1.add(t1, BorderLayout.NORTH);
		p1.add(buttonPanel, BorderLayout.SOUTH);
		mainPanel.add(p1, "1");

		//panel 2 (new User)
		t2 = new JLabel("<html><p>New User! On left-side, enter site name, on right-side enter password!</p>"+ "<p><b><pre>Site Name!                                                Password!</pre></b></p>"+ "</html>", SwingConstants.CENTER);               
		
		table = new JTable(10, 2);
	        table.setPreferredScrollableViewportSize(table.getPreferredSize());
		JScrollPane scrollPane = new JScrollPane(table);
	        frame.getContentPane().add( scrollPane );

		buttonPanel2 = new JPanel(new GridLayout(2,1));

		saveToText = new JButton ("Save");
		saveToText.setActionCommand("save");
		saveToText.addActionListener(this);

		goBackHome = new JButton ("Go Back to Main Screen");
		goBackHome.setActionCommand("goBackHome");
		goBackHome.addActionListener(this);
		
		buttonPanel2.add(saveToText);
		buttonPanel2.add(goBackHome);

		p2.add(t2, BorderLayout.NORTH);
		p2.add(table, BorderLayout.CENTER);
		p2.add(buttonPanel2, BorderLayout.SOUTH);
		mainPanel.add(p2, "2");

		//P3 STUFF
		t3 = new JLabel("<html><p>Loading user passwords. Set decryption password first, then load table!</p>"+ "<p><b><pre>Site Name!                                                Password!</pre></b></p>"+ "</html>", SwingConstants.CENTER);  
		
		table2 = new JTable(10, 2);
	        table2.setPreferredScrollableViewportSize(table2.getPreferredSize());
		JScrollPane scrollPane2 = new JScrollPane(table2);
	        frame.getContentPane().add(scrollPane2);     
		buttonPanel3 = new JPanel(new GridLayout(3,1)); 

		setDecryptPw = new JButton ("Set Decryption Password");
		setDecryptPw.setActionCommand("setDecryptPw");
		setDecryptPw.addActionListener(this);

		loadTable = new JButton ("Load user passwords");
		loadTable.setActionCommand("loadTable");
		loadTable.addActionListener(this);

		goBackHome2 = new JButton ("Go Back to Main Screen");
		goBackHome2.setActionCommand("goBackHome");
		goBackHome2.addActionListener(this);
		

		buttonPanel3.add(setDecryptPw);
		buttonPanel3.add(loadTable);
		buttonPanel3.add(goBackHome2);
		
		p3.add(t3, BorderLayout.NORTH);
		p3.add(table2, BorderLayout.CENTER);
		p3.add(buttonPanel3, BorderLayout.SOUTH);
		mainPanel.add(p3, "3");

		//frame stuff
		frame.add(mainPanel);
		frame.setSize(700, 500); 
	        frame.addWindowListener(new WindowAdapter() {
	    	    public void windowClosing(WindowEvent evt) {
	    	        System.exit(0);
	    	    }
	    	});
	        frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("loadTable")){
			chooser = new JFileChooser();
			returnVal = chooser.showOpenDialog(frame);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
		       		inputFile = chooser.getSelectedFile().getAbsolutePath();
			       fileOpen = true;
	  	        }
			Scanner scanner;
			if (fileOpen){
			DefaultTableModel model = (DefaultTableModel)table2.getModel();
				try {
					scanner = new Scanner(new File(inputFile));
					while (scanner.hasNext()){
						for (int i = 0; i < 10; i++){
							for (int j = 0; j < 2; j++){
								try {
									String temp = scanner.nextLine();
									byte [] tempBYTE = Base64.getDecoder().decode(temp); 
									//DECRYPT HERE
									byte[] key = (decryptionPW).getBytes("UTF-8");
									MessageDigest sha = MessageDigest.getInstance("SHA-1");
									key = sha.digest(key);
									key = Arrays.copyOf(key, 16);
									SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
									cipher = Cipher.getInstance("AES");
									cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
									byte[] original = cipher.doFinal(tempBYTE);	
									String input = new String(original);
									input = input.substring(3);
									if (input.equals("null\n")){
										input = "";
									}
									model.setValueAt(input, i, j);
								} catch (Exception esz) {
									JOptionPane.showMessageDialog(frame, "Something went wrong while decrpyting. Program will terminate, please try again!");
								}
							}
						}
					}
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(frame, "File not found!");
				} 
			}
		}
		if (e.getActionCommand().equals("setDecryptPw")){
			decryptionPW = JOptionPane.showInputDialog("Please enter your decryption password!");
		}
		if (e.getActionCommand().equals("newUser")){
			cdLayout.show(mainPanel, "2");
		}
		if (e.getActionCommand().equals("goBackHome")){
			cdLayout.show(mainPanel, "1");
		}
		if (e.getActionCommand().equals("loadUser")){
			cdLayout.show(mainPanel, "3");
			JOptionPane.showMessageDialog(frame, "Please set the decryption password first, and then load the file!");
		}
		if (e.getActionCommand().equals("save")){
			encryptionPW = JOptionPane.showInputDialog("Please enter your encryption password, and make note of it since it will be needed to decrypt!");
			chooser = new JFileChooser();
			returnVal = chooser.showSaveDialog(frame);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
		       		outputFile = chooser.getSelectedFile().getAbsolutePath()+".txt";
			       fileSave = true;
	  	        }
			File outputfile = new File(outputFile);
		 	PrintStream fw = null;
			try {
				fw = new PrintStream(outputfile);
				fw.flush();
				if (fileSave) {
					for (int row = 0; row < table.getRowCount(); row++) {
					    for (int col = 0; col < table.getColumnCount(); col++) {
						String temp = (table.getColumnName(col));
						temp = temp +(": ");
						 temp = temp+table.getValueAt(row, col)+"\n";
						//ENCRYPT STRING HERE
						try{
							byte[] key = (encryptionPW).getBytes("UTF-8");
							MessageDigest sha = MessageDigest.getInstance("SHA-1");
							key = sha.digest(key);
							key = Arrays.copyOf(key, 16); 
							SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
							cipher = Cipher.getInstance("AES");
					  	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
							byte[] encrypted=cipher.doFinal(temp.getBytes());
							String encoded = Base64.getEncoder().encodeToString(encrypted);
							fw.println(encoded);
						} catch (Exception es) {
							JOptionPane.showMessageDialog(frame, "Something went wrong encrypting. Check file and password!");
						}
					    }
					}
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(frame, "File not found. Check file!");
			}
		}
	}

	public static void main (String args[]) {
	      new guiClient();
	}

}
