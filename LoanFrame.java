/**
 * This is the loan frame which allows the reader service clerk to issue loans or renew loans.
 * It also displays the relevant message as to whether a loan has successfully been renewed
 * or issued as well as showing whether a loan has failed.
 */
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class LoanFrame extends JPanel implements ActionListener
{
	/**
	 * Create text fields to take in the dewey index number and the member id number.
	 * Create buttons for creating loans and renewing loans.
	 * Store the database abstraction layer.
	 */
	private JTextField deweyTextField;
	private JTextField memNoTextField;
	private JButton createLoanButton, renewLoanButton;
	private Mysql db = new Mysql();

	/**
	 * Initialises the JPanel, puts the relevant labels, text fields and buttons.
	 */
	public LoanFrame()
	{
		setLayout(null);

		JLabel lblMembershipNumber = new JLabel("Membership Number:");
		lblMembershipNumber.setBounds(30, 101, 169, 16);
		lblMembershipNumber.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblMembershipNumber);

		JLabel lblDeweyIndex = new JLabel("Dewey Index:");
		lblDeweyIndex.setBounds(30, 35, 118, 16);
		lblDeweyIndex.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblDeweyIndex);

		deweyTextField = new JTextField();
		deweyTextField.setBounds(208, 29, 134, 28);
		deweyTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(deweyTextField);
		deweyTextField.setColumns(10);

		memNoTextField = new JTextField();
		memNoTextField.setBounds(208, 95, 134, 28);
		memNoTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(memNoTextField);
		memNoTextField.setColumns(10);

		createLoanButton = new JButton("Create Loan");
		createLoanButton.setBounds(25, 171, 140, 29);
		createLoanButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		createLoanButton.addActionListener(this);
		add(createLoanButton);

		renewLoanButton = new JButton("Renew Loan");
		renewLoanButton.setBounds(208, 171, 150, 29);
		renewLoanButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		renewLoanButton.addActionListener(this);

		add(renewLoanButton);
	}

	/**
	 * This takes the relevant information from the text fields and creates a loan.
	 */
	public void createLoan()
	{
		Date today = new Date();
		try
		{
			try 
			{
				db.addLoan(Integer.parseInt(memNoTextField.getText()), deweyTextField.getText(), today, today);
				new MessageFrame("Loan has been issued.");
			} 
			catch (NumberFormatException e) 
			{
				new MessageFrame("No input.");
			} 
			catch (LibraryRulesException e1) 
			{
				new MessageFrame(e1.getMessage());
			} 
			catch (DataNotFoundException e2)
			{
				new MessageFrame(e2.getMessage());
			}
		}
		catch (SQLException exception) 
		{
			new MessageFrame(exception.getMessage());
		}
	}

	/**
	 * Takes the data from the text fields and renews a loan.
	 */
	public void renewLoan()
	{
		Date today = new Date();
		try 
		{
			db.renewLoan(Integer.parseInt(memNoTextField.getText()), deweyTextField.getText());
			new MessageFrame("Loan has been renewed.");
		} 
		catch (Exception e) 
		{
			new MessageFrame(e.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == createLoanButton)
		{
			createLoan();
		}
		else if(e.getSource() == renewLoanButton)
		{
			renewLoan();
		}
	}

}
