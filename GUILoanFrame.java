import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.JButton;


public class GUILoanFrame extends JPanel implements ActionListener
{
	private JTextField deweyTextField;
	private JTextField memNoTextField;
	private JButton createLoanButton;
	
	public GUILoanFrame()
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
		createLoanButton.setBounds(40, 171, 118, 29);
		createLoanButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		createLoanButton.addActionListener(this);
		
		JButton renewLoanButton = new JButton("Renew Loan");
		renewLoanButton.setBounds(208, 171, 134, 29);
		renewLoanButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(renewLoanButton);
		renewLoanButton.addActionListener(this);
	}
	
	public void createLoan()
	{
		Borrower borrower = Database.find_borrower((Integer.parseInt(memNoTextField.getText())));
		try
		{
			Copy copy = Database.find_copies_by_dewey(deweyTextField.getText());
			
			Database.issue_loan(copy, borrower);
		}
		catch (DataNotFoundException exception)
		{
			// TODO display message
		} catch (LoanException exception) 
		{
			// TODO Auto-generated catch block
		} catch (Exception exception) 
		{
			// TODO Auto-generated catch block
		}
	}
	
	public void renewLoan()
	{
		try 
		{
			Loan loan = Database.find_loans_by_deweyid(deweyTextField.getText());
			Database.renew_loan(loan);
		} 
		catch (DataNotFoundException e) 
		{
			// TODO
		} 
		catch (LoanException e) 
		{
			// TODO 
		} 
		catch (Exception e) 
		{
			// TODO 
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == createLoanButton)
		{
			createLoan();
		}
		else
		{
			renewLoan();
		}
	}
	
}
