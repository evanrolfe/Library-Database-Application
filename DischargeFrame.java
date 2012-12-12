/**
 * This is just the frame for discharging a loan.
 */

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class DischargeFrame extends JPanel implements ActionListener

{
	/**
	 * The text field to enter the dewey index number.
	 */
	private JTextField deweyTextField;

	/**
	 * Stores the database abstraction layer.
	 */
	private Mysql db = new Mysql();


	/**
	 * Initialises the JPanel.
	 * Creates relevant text fields, labels and button.
	 */
	public DischargeFrame() 
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
		dischargeButton.addActionListener(this);
		add(dischargeButton);
	}

	/** This performs the discharge and displays the correct error
	 * message.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		try 
		{
			db.deleteLoan(deweyTextField.getText());
			new MessageFrame("Loan has been discharged.");
		} 
		catch (SQLException e1) 
		{
			new MessageFrame(e1.getMessage());
		}
		catch (LibraryRulesException e1) 
		{
			new MessageFrame(e1.getMessage());
		} 
		catch (DataNotFoundException e1)
		{
			new MessageFrame(e1.getMessage());
		}
	}
}
