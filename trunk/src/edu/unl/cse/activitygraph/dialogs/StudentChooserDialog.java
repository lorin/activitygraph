package edu.unl.cse.activitygraph.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import edu.unl.cse.activitygraph.hpcs.AssignmentUpdaterServices;
import edu.unl.cse.activitygraph.hpcs.HpcsClass;
import edu.unl.cse.activitygraph.hpcs.StudentAccount;


@SuppressWarnings("serial")
public class StudentChooserDialog extends JDialog {
	
    private JComboBox studentChooserClassIdComboBox;
    private JComboBox studentChooserStudentIdComboBox;
    private Vector<StudentAccount> studentAccounts;
	private boolean userClickedOk;
	private AssignmentUpdaterServices appServices;
	private int classId;
	private int accountId;
 

	
	public StudentChooserDialog(AssignmentUpdaterServices appServices) {
		this.appServices = appServices;
        JLabel studentChooserClassIdLabel = new JLabel();
        JLabel studentChooserAccountIdLabel = new JLabel();
        JButton studentChooserOkButton = new JButton();
        JButton studentChooserCancelButton = new JButton();
        studentChooserClassIdComboBox = new JComboBox();
        studentChooserStudentIdComboBox = new JComboBox();

        this.setTitle("Choose Account");
        this.setResizable(false);
        
        studentChooserClassIdLabel.setText("Class_ID:");
        studentChooserAccountIdLabel.setText("Account_ID:");
        studentChooserOkButton.setText("OK");
        studentChooserOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentChooserOkButtonActionPerformed(evt);
            }
        });

        studentChooserCancelButton.setText("Cancel");
        studentChooserCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentChooserCancelButtonActionPerformed(evt);
            }
        });

        studentChooserClassIdComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                studentChooserClassIdComboBoxItemStateChanged(evt);
            }
        });

        studentChooserStudentIdComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                studentChooserStudentIdComboBoxItemStateChanged(evt);
            }
        });

        GroupLayout studentChooserJDialogLayout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(studentChooserJDialogLayout);
        studentChooserJDialogLayout.setHorizontalGroup(
            studentChooserJDialogLayout.createParallelGroup(GroupLayout.LEADING)
            .add(studentChooserJDialogLayout.createSequentialGroup()
                .addContainerGap()
                .add(studentChooserJDialogLayout.createParallelGroup(GroupLayout.TRAILING)
                    .add(studentChooserJDialogLayout.createSequentialGroup()
                        .add(studentChooserOkButton)
                        .addPreferredGap(LayoutStyle.RELATED)
                        .add(studentChooserCancelButton))
                    .add(GroupLayout.LEADING, studentChooserJDialogLayout.createSequentialGroup()
                        .add(studentChooserAccountIdLabel)
                        .addPreferredGap(LayoutStyle.RELATED, 12, Short.MAX_VALUE)
                        .add(studentChooserStudentIdComboBox, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.RELATED))
                    .add(GroupLayout.LEADING, studentChooserJDialogLayout.createSequentialGroup()
                        .add(studentChooserClassIdLabel)
                        .add(26, 26, 26)
                        .add(studentChooserClassIdComboBox, 0, 298, Short.MAX_VALUE)))
                .addContainerGap())
        );
        studentChooserJDialogLayout.setVerticalGroup(
            studentChooserJDialogLayout.createParallelGroup(GroupLayout.LEADING)
            .add(studentChooserJDialogLayout.createSequentialGroup()
                .add(26, 26, 26)
                .add(studentChooserJDialogLayout.createParallelGroup(GroupLayout.BASELINE)
                    .add(studentChooserClassIdLabel)
                    .add(studentChooserClassIdComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.RELATED)
                .add(studentChooserJDialogLayout.createParallelGroup(GroupLayout.BASELINE)
                    .add(studentChooserAccountIdLabel)
                    .add(studentChooserStudentIdComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .add(23, 23, 23)
                .add(studentChooserJDialogLayout.createParallelGroup(GroupLayout.BASELINE)
                    .add(studentChooserCancelButton)
                    .add(studentChooserOkButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        this.populateClassInfo();
        this.setModal(true);
        this.setLocationRelativeTo(null);
        this.pack();


	}

	private void populateClassInfo() {
		Vector<HpcsClass> classes = this.appServices.getHpcsClasses();
        for (HpcsClass tmp : classes){
            this.studentChooserClassIdComboBox.addItem(tmp.getId()+" ("+tmp.getProfessorName()+" / "+tmp.getSemester()+")");
        }
        this.studentChooserClassIdComboBox.setSelectedIndex(0);
        this.studentChooserClassIdComboBox.setToolTipText(classes.get(0).getToolTipText());

		
	}

	protected void studentChooserStudentIdComboBoxItemStateChanged(ItemEvent evt) {
        if (evt.getStateChange() != ItemEvent.SELECTED)
            return;
        
        int index = studentChooserStudentIdComboBox.getSelectedIndex();
        if (index != -1){
            StudentAccount account = this.studentAccounts.get(index);
            this.studentChooserStudentIdComboBox.setToolTipText(account.getToolTipText());
        }		
        this.accountId = this.studentAccounts.get(studentChooserStudentIdComboBox.getSelectedIndex()).getId();
	}


	protected void studentChooserClassIdComboBoxItemStateChanged(ItemEvent evt) {
        if (evt.getStateChange() != ItemEvent.SELECTED)
            return;
        
        int index = this.studentChooserClassIdComboBox.getSelectedIndex();
        HpcsClass hpcsClass = this.appServices.getHpcsClasses().get(index);
        this.studentChooserClassIdComboBox.setToolTipText(hpcsClass.getToolTipText());
        
        this.classId = hpcsClass.getId();
        this.studentAccounts = this.appServices.getStudentAccounts(classId);
        
        this.studentChooserStudentIdComboBox.removeAllItems();
        if (this.studentAccounts.size() > 0){
            for (StudentAccount tmp : this.studentAccounts){
                this.studentChooserStudentIdComboBox.addItem(tmp.getId());
            }
            this.studentChooserStudentIdComboBox.setSelectedIndex(0);
            this.studentChooserStudentIdComboBox.setToolTipText(studentAccounts.get(0).getToolTipText());
        }		
		
	}


	protected void studentChooserCancelButtonActionPerformed(ActionEvent evt) {
		this.setVisible(false);
		this.userClickedOk = false;
		
	}


	protected void studentChooserOkButtonActionPerformed(ActionEvent evt) {
		this.setVisible(false);
		this.userClickedOk = true;
	}
	
	public boolean userClickedOk() {
		return userClickedOk;
	}


	public int getAccountId() {
		return this.accountId;
	}


	public int getClassId() {
		return this.classId;
	}
	

}
