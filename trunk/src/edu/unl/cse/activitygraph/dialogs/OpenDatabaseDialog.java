package edu.unl.cse.activitygraph.dialogs;


import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import edu.unl.cse.activitygraph.ActivityGraph;

public class OpenDatabaseDialog extends JDialog {
	
	private JPasswordField passwordField;

	/**
	 * 
	 */
	private static final long serialVersionUID = -158202926070572830L;

	/**
	 * Creates a modal dialog that allows the user to specify the connection
	 * parameters for an Experiment Manager database
	 * 
	 * @param graph
	 */
	public OpenDatabaseDialog(ActivityGraph graph) {
		super(graph,true);
		
	    passwordField = new JPasswordField(10);

	 
		
		JLabel label;
		label = new JLabel("PostgreSQL DB");
		label = new JLabel("Username");		
		label = new JLabel("Password");
		label.setLabelFor(passwordField);
		
	}
	

	public void actionPerformed(ActionEvent e) {
	 
	  }

}
