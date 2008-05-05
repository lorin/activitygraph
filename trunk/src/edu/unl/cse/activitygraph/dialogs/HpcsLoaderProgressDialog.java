package edu.unl.cse.activitygraph.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.swingworker.SwingWorker;

import edu.unl.cse.activitygraph.hpcs.AssignmentUpdaterServices;
import edu.unl.cse.activitygraph.interfaces.IStatusUpdater;

@SuppressWarnings("serial")
public class HpcsLoaderProgressDialog extends JDialog implements ActionListener {
	
	JTextField statusTextField;
	JProgressBar currentBar, totalBar;
	JButton cancelButton;
	private HpcsLoaderWorker worker;
	private int accountId;
	private int classId;
	
	class HpcsLoaderWorker extends SwingWorker<Void,StatusInfo> implements IStatusUpdater {

		private AssignmentUpdaterServices appServices;
		private HpcsLoaderProgressDialog dlg;
		
		@Override
		protected void process(List<StatusInfo> statusList) {
			for(StatusInfo status : statusList) {
				dlg.updateStatus(status);
			}
			
		}

		public HpcsLoaderWorker(HpcsLoaderProgressDialog dlg, AssignmentUpdaterServices appServices) {
			this.dlg = dlg;
			this.appServices = appServices;
		}


		@Override
		protected Void doInBackground() throws Exception {
			appServices.loadDataFromDB(accountId, classId, this);
			return null;
		}


		@Override
		protected void done() {
			try {
				get();
			} catch (CancellationException e) {
				JOptionPane.showMessageDialog(dlg, "Operation was cancelled");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dlg.setVisible(false);
			
		}


		public void updateStatus(String status, int shortProgress, int longProgress) {
			this.publish(new StatusInfo(status,shortProgress,longProgress));
		}

		public void updateCurrent(int current) {
			dlg.updateCurrent(current);
			
		}

		public void updateText(String text) {
			dlg.updateText(text);
			
		}

		public void updateTotal(int total) {
			dlg.updateTotal(total);
		}
		
	}
	
	class StatusInfo {
		public String status;
		public Integer shortProgress;
		public Integer longProgress;
		
		public StatusInfo(String status, Integer shortProgress, Integer longProgress) {
			this.status = status;
			this.shortProgress = shortProgress;
			this.longProgress = longProgress;
		}
	}
	
	public HpcsLoaderProgressDialog(AssignmentUpdaterServices appServices,
			int accountId, int classId) {
		this.setTitle("Downloading data");
		this.setModal(true);
		this.accountId = accountId;
		this.classId = classId;
		this.setResizable(false);
		
		statusTextField = new JTextField("Status");	
		statusTextField.setEditable(false);
		currentBar = new JProgressBar();
		totalBar = new JProgressBar();
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		JPanel topLevelPanel = new JPanel();
		JLabel currentLabel = new JLabel("Current: ");
		JLabel totalLabel = new JLabel("Total: ");
		
		
		GroupLayout layout = new GroupLayout(topLevelPanel);
		topLevelPanel.setLayout(layout);
		this.setContentPane(topLevelPanel);

		layout.setAutocreateGaps(true);
		layout.setAutocreateContainerGaps(true);
		
		
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.CENTER)
				.add(this.statusTextField)
				.add(layout.createSequentialGroup()
					.add(layout.createParallelGroup(GroupLayout.LEADING)
						.add(currentLabel)
						.add(totalLabel))
					.add(layout.createParallelGroup(GroupLayout.LEADING)
						.add(currentBar)
						.add(totalBar)))
				.add(cancelButton)
		);
		
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.add(this.statusTextField)
				.add(layout.createParallelGroup(GroupLayout.LEADING)
						.add(currentLabel)
						.add(currentBar))
				.add(layout.createParallelGroup(GroupLayout.LEADING)
						.add(totalLabel)
						.add(totalBar))
				.add(cancelButton)
		);
		
		this.pack();
		worker = new HpcsLoaderWorker(this,appServices);
		worker.execute();
		this.setVisible(true);
	}


	public void updateStatus(StatusInfo status) {
		if(status.status != null) {
			this.statusTextField.setText(status.status);
		}
		
		if(status.shortProgress != null ) {
			this.currentBar.setValue(status.shortProgress);
		}
		
		if(status.longProgress != null) {
			this.totalBar.setValue(status.longProgress);
		}
	}
	
	public void updateText(String text) {
		this.updateStatus(new StatusInfo(text,null,null));
	}
	
	public void updateCurrent(int current) {
		this.updateStatus(new StatusInfo(null,current,null));
	}
	
	public void updateTotal(int total) {
		this.updateStatus(new StatusInfo(null,null,total));
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command=="Cancel") {
			worker.cancel(true);
		}
		
	}

}
