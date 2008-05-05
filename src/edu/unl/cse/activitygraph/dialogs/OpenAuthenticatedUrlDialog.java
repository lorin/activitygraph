package edu.unl.cse.activitygraph.dialogs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;

import edu.unl.cse.activitygraph.interfaces.ISubversionInfo;

@SuppressWarnings("serial")
/**
 * A modal dialog for getting information about connecting to an URL when a username and 
 * password may be required.
 */
public class OpenAuthenticatedUrlDialog extends JDialog implements ActionListener, ISubversionInfo {
	private boolean okWasClicked;
	
	private JTextField loginField;
	private JPasswordField passwordField;
	private JTextField urlField;
	
	public OpenAuthenticatedUrlDialog(Frame owner, String title, String url,String login,String password) {
		super(owner,title, true);
		this.setResizable(false);
		
		JLabel urlLabel = new JLabel("url:");
		urlField = new JTextField(url,30);

		JLabel loginLabel = new JLabel("login:");
		loginField = new JTextField(login,20);

		JLabel passwordLabel = new JLabel("password:");
		passwordField = new JPasswordField(password, 20);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		
		JPanel topLevelPanel = new JPanel();
		
		BoxLayout topLevelLayout = new BoxLayout(topLevelPanel,BoxLayout.Y_AXIS);
		topLevelPanel.setLayout(topLevelLayout);
		this.setContentPane(topLevelPanel);
		
		JPanel loginPanel = new JPanel();
		//JPanel datePanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		
		loginPanel.setBorder(BorderFactory.createTitledBorder("Login"));
		
		
		/**
		 * Organizing the layout of the login fields
		 */
		GroupLayout loginLayout = new GroupLayout(loginPanel);
		loginPanel.setLayout(loginLayout);
		loginLayout.setAutocreateGaps(true);
		loginLayout.setAutocreateContainerGaps(true);

		
		loginLayout.setHorizontalGroup(loginLayout.createSequentialGroup()
				.add(loginLayout.createParallelGroup(GroupLayout.LEADING)
						.add(urlLabel)
						.add(loginLabel)
						.add(passwordLabel)
					)
				.add(loginLayout.createParallelGroup(GroupLayout.LEADING)
						.add(urlField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.add(loginField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.add(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					)
				);
		
		loginLayout.setVerticalGroup(loginLayout.createSequentialGroup()
				.add(loginLayout.createParallelGroup(GroupLayout.LEADING)
						.add(urlLabel)
						.add(urlField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					)
				.add(loginLayout.createParallelGroup(GroupLayout.LEADING)
						.add(loginLabel)
						.add(loginField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					)
				.add(loginLayout.createParallelGroup(GroupLayout.LEADING)
						.add(passwordLabel)
						.add(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					)
				);
		
		/**
		 * Organizing the layout of the date panel
		 */
		
		/**
				
		
		GroupLayout dateLayout = new GroupLayout(datePanel);
		datePanel.setLayout(dateLayout);
		dateLayout.setAutocreateGaps(true);

		
		dateLayout.setHorizontalGroup(dateLayout.createSequentialGroup()
				.add(dateLayout.createParallelGroup(GroupLayout.CENTER)
						.add(startField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					              GroupLayout.PREFERRED_SIZE)
						.add(startLabel)
					)
				.add(dateLayout.createParallelGroup(GroupLayout.CENTER)
						.add(endField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					              GroupLayout.PREFERRED_SIZE)
						.add(endLabel)
					)
			);
		
		dateLayout.setVerticalGroup(dateLayout.createSequentialGroup()
				.add(dateLayout.createParallelGroup(GroupLayout.LEADING)
						.add(startField,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					              GroupLayout.PREFERRED_SIZE)
						.add(endField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					              GroupLayout.PREFERRED_SIZE)
					)
					.add(dateLayout.createParallelGroup(GroupLayout.LEADING)
							.add(startLabel)
							.add(endLabel)
						)
			);
		
		datePanel.setBorder(BorderFactory.createTitledBorder("Dates"));
		
		**/
		
		
		/**
		 * Organizing the layout of the button panel
		 */
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		
		// Top level layout
		topLevelPanel.add(loginPanel);
		//topLevelPanel.add(datePanel);
		topLevelPanel.add(buttonPanel);
		
		this.getRootPane().setDefaultButton(okButton);
		this.pack();
		
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command=="OK") {
			this.okWasClicked = true;
			this.setVisible(false);
			
		} else if(command=="Cancel") {
			this.okWasClicked = false;
			this.setVisible(false);
			
		} else {
			// Should never happen here!
			assert false; 
		}
	
	}
	
	public boolean userClickedOk() {
		return this.okWasClicked;
		
	}
	
	public String getLogin() {
		return loginField.getText();
	}

	public String getPassword() {
		return new String(passwordField.getPassword());
	}

	public String getUrl() {
		return urlField.getText();
	}
	
	
	

}
