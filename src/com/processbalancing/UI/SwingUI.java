package com.processbalancing.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.processbalancing.exceptions.NoValidThetaIDException;
import com.processbalancing.main.Main;

/**
 * This class creates the user interface. It handles all the actions like clicking a button in its inner class ActionHandler. It implements runnable to be able
 * to start all the calculations in a new thread. It also catches the possible exceptions in order to show them to the user via a message dialog popup.
 */
public class SwingUI extends javax.swing.JFrame implements Runnable {
	private static final long serialVersionUID = 454785486; // Auto-generated
	private static JLabel label_status;
	private static JTextField textfield_path;
	private JLabel label_header;
	private JTextArea textArea;
	private JButton button_choose;
	private JButton button_calculate;
	private JPanel container_top;
	private JPanel container_middle;
	private JPanel container_bottom;
	private Color backgroundColor = new Color(255, 255, 255);
	private File ExcelFile;
	// text used on UI-elements
	private final String TX_TOOLNAME = "Process Balancing Calculation Tool";
	private final String TX_SEARCH = "Durchsuchen";
	private final String TX_START_CALCULATION = "Starte Kalkulation";
	private final String TX_CALCULATING = "kalkuliere... ";
	private final String TX_CALCULATION_END = "Kalkulation beendet";
	private final String TX_WELCOME = "Bitte den Pfad der Excel-Datei angeben."
			+ "\n\nDas Ergebnis der Kalkulation wird dann innerhalb der angegebenen Excel-Datei in ein neues Tabellen-Blatt geschrieben."
			+ "\n\nHinweis: Bitte lassen Sie die Excel-Datei während der Berechnungen geschlossen.";

	/**
	 * creates all the elements on the UI
	 */
	public void createSwingUI() {
		// create frame and container
		JFrame frame = new JFrame(TX_TOOLNAME);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container mainContainer = frame.getContentPane();
		mainContainer.setBackground(backgroundColor);

		// create panels to group the UI-elements
		container_top = new JPanel();
		container_top.setBackground(backgroundColor);
		container_middle = new JPanel();
		container_middle.setBackground(backgroundColor);
		container_bottom = new JPanel();
		container_bottom.setBackground(backgroundColor);

		// create header
		label_header = new JLabel(TX_TOOLNAME);
		container_top.add(label_header);

		// create textArea
		textArea = new JTextArea(TX_WELCOME);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setBounds(10, 10, 400, 220);
		container_top.add(textArea);

		// create image
		Icon icon = new ImageIcon(getClass().getResource("fim_logo.jpg"));
		JLabel picture = new JLabel(icon);
		container_top.add(picture);

		// create textfield for path
		textfield_path = new JTextField("");
		textfield_path.setBounds(10, 10, 300, 200);
		textfield_path.setPreferredSize(new Dimension(400, 27));
		textfield_path.setEditable(false);
		container_middle.add(textfield_path);

		// create button choose and link it to the ActionListener
		button_choose = new JButton(TX_SEARCH);
		button_choose.setBounds(10, 10, 50, 40);
		container_middle.add(button_choose);
		button_choose.addActionListener(new ActionHandler());

		// add status information
		label_status = new JLabel("");
		label_status.setPreferredSize(new Dimension(400, 27));
		container_bottom.add(label_status);

		// create button calculate and link it to the ActionListener
		button_calculate = new JButton(TX_START_CALCULATION);
		container_bottom.add(button_calculate);
		button_calculate.addActionListener(new ActionHandler());

		// add container to mainContainer
		mainContainer.add(container_top, BorderLayout.NORTH);
		mainContainer.add(container_middle, BorderLayout.CENTER);
		mainContainer.add(container_bottom, BorderLayout.SOUTH);

		// set frame
		frame.setBounds(200, 100, 600, 325);
		frame.setVisible(true);
	}

	/**
	 * This inner class listens if the buttons are clicked. It checks which button it was and trigger the corresponding algorithm.
	 * 
	 */
	public class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == button_choose) {
				// create fileCooser popup
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(null);
				ExcelFile = fileChooser.getSelectedFile();
				if (ExcelFile != null) {
					// writes Filepath into the textfield
					textfield_path.setText(ExcelFile.getPath());
				}
			}
			if (ae.getSource() == button_calculate) {
				// start a new thread to trigger the calculation in Main Class
				SwingUI swUI = new SwingUI();
				Thread thread = new Thread(swUI);
				thread.start();
				label_status.setText(TX_CALCULATING);
			}
		}
	}

	/**
	 * Run-method for the second thread. It triggers the "processMainCalculation" in Main-class and catches the possible exceptions to be able to create popups
	 * on the UI to inform the user. The new thread is necessary to change the lable_status-textfield during the action-handling.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			// creates file out of the file-path and trigger processMainCalulation
			Main.processMainCalculation(new File(textfield_path.getText()));
		} catch (NoValidThetaIDException e) {
			e.printStackTrace();
			resetLabel();
			JOptionPane.showMessageDialog(null, e.toString() + " " + e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			resetLabel();
			JOptionPane.showMessageDialog(null, e.toString() + ". Möglicherweise ist das Excel-File noch geöffnet");
		} catch (IOException e) {
			e.printStackTrace();
			resetLabel();
			JOptionPane.showMessageDialog(null, e.toString() + ". Fehler tritt beim öffnen/schließen von Excel-Files bzw. beim lesen/schreiben in Excel-Files auf");
		} catch (NullPointerException e) {
			e.printStackTrace();
			resetLabel();
			JOptionPane.showMessageDialog(null, e.toString() + ". Möglicherweise wurde keine oder keine korrekte Datei ausgewählt");
		} catch (IllegalStateException e) {
			resetLabel();
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.toString()
					+ ". Fehler beim Excel-Import. Möglicherweise sind einige Felder nicht korrekt als Zahl, Text oder Standard formatiert.");
		} catch (Exception e) {
			resetLabel();
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.toString() + ". Unbekannte Ursache für diesen Fehler");
		}
		label_status.setText(TX_CALCULATION_END);
	}

	private void resetLabel() {
		label_status.setText("");
	}

}
