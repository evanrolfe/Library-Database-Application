import javax.swing.JPanel;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JTextField;


public class GUIDischargeFrame extends JPanel 
{
	private JTextField deweyTextField;
	public GUIDischargeFrame() 
	{
		setLayout(null);
		
		JLabel lblDeweyIndex = new JLabel("Dewey Index:");
		lblDeweyIndex.setBounds(40, 11, 134, 16);
		lblDeweyIndex.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblDeweyIndex);
		
		deweyTextField = new JTextField();
		deweyTextField.setBounds(171, 5, 177, 28);
		deweyTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(deweyTextField);
		deweyTextField.setColumns(10);
		
		JButton dischargeButton = new JButton("Discharge Loan");
		dischargeButton.setBounds(54, 87, 177, 29);
		dischargeButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(dischargeButton);
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		String deweyNumber = deweyTextField.getText();
		Loan result;
		try {
			result = Database.find_loans_by_deweyid(deweyNumber);
			Database.delete_loan(result);
		} 
		catch (DataNotFoundException e1)
		{
			// TODO
		}
		catch (InvalidArgumentException e2)
		{
			//TODO
		}
	}

}
