/**
 * The user can view their details along with loans they currently have.
 */

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class ProfileFrame extends JPanel implements ActionListener
{

	/**
	 * Create loans table headings.
	 */
	private Object[] loansTableHeadings = {"Membership Number", "Dewey Index", "Overdue"};
	
	/**
	 * Create loans table.
	 */
	private JTable loansTable;

	/**
	 * Create information fields in profile.
	 */
	private String forename = "Forename: ";
	private String surname = "Surname: ";
	private String email = "Email: ";
	
	/**
	 * Create the search button.
	 */
	private JButton searchButton;
	
	/**
	 * Create the labels.
	 */
	private JLabel forenameLabel;
	private JLabel surnameLabel;
	private JLabel emailLabel;
	
	/**
	 * Create the id text field used by the borrower to search for themselves.
	 */
	private JTextField idTextField;
	
	/**
	 * Create database abstraction layer.
	 */
	private Mysql db = new Mysql();
	
	/**
	 * Creates the borrower thats searching for themselves.
	 */
	private Borrower borrower;

	/**
	 * Initialises the JPanel.
	 */
	public ProfileFrame() 
	{
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		idTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.SOUTH, idTextField, -356, SpringLayout.SOUTH, this);
		idTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(idTextField);
		idTextField.setColumns(10);

		JLabel idLabel = new JLabel("ID: ");
		springLayout.putConstraint(SpringLayout.NORTH, idLabel, 3, SpringLayout.NORTH, idTextField);
		springLayout.putConstraint(SpringLayout.EAST, idLabel, -6, SpringLayout.WEST, idTextField);
		idLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(idLabel);

		searchButton = new JButton("Search");
		springLayout.putConstraint(SpringLayout.NORTH, searchButton, 6, SpringLayout.SOUTH, idTextField);
		springLayout.putConstraint(SpringLayout.WEST, searchButton, 0, SpringLayout.WEST, idLabel);
		searchButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(searchButton);
		searchButton.addActionListener(this);

		JPanel detailsPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.WEST, detailsPanel, 219, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, detailsPanel, -10, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, idTextField, -6, SpringLayout.WEST, detailsPanel);
		springLayout.putConstraint(SpringLayout.NORTH, detailsPanel, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, detailsPanel, -17, SpringLayout.SOUTH, this);
		add(detailsPanel);
		SpringLayout detailsPanelLayout = new SpringLayout();
		detailsPanel.setLayout(detailsPanelLayout);
		detailsPanel.setBorder(new EmptyBorder(0, 80, 0, 0));

		JLabel yourDetailsLabel = new JLabel("Your Details");
		detailsPanelLayout.putConstraint(SpringLayout.NORTH, yourDetailsLabel, 0, SpringLayout.NORTH, detailsPanel);
		detailsPanelLayout.putConstraint(SpringLayout.WEST, yourDetailsLabel, 145, SpringLayout.WEST, detailsPanel);
		yourDetailsLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		detailsPanel.add(yourDetailsLabel);

		forenameLabel = new JLabel(forename);
		detailsPanelLayout.putConstraint(SpringLayout.NORTH, forenameLabel, 51, SpringLayout.NORTH, detailsPanel);
		detailsPanelLayout.putConstraint(SpringLayout.WEST, forenameLabel, 0, SpringLayout.WEST, detailsPanel);
		forenameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		detailsPanel.add(forenameLabel);

		surnameLabel = new JLabel(surname);
		surnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		detailsPanelLayout.putConstraint(SpringLayout.NORTH, surnameLabel, 6, SpringLayout.SOUTH, forenameLabel);
		detailsPanelLayout.putConstraint(SpringLayout.WEST, surnameLabel, 0, SpringLayout.WEST, forenameLabel);
		detailsPanel.add(surnameLabel);

		emailLabel = new JLabel(email);
		emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		detailsPanelLayout.putConstraint(SpringLayout.NORTH, emailLabel, 6, SpringLayout.SOUTH, surnameLabel);
		detailsPanelLayout.putConstraint(SpringLayout.WEST, emailLabel, 0, SpringLayout.WEST, forenameLabel);
		detailsPanel.add(emailLabel);

		JLabel yourLoansLabel = new JLabel("Your Loans");
		detailsPanelLayout.putConstraint(SpringLayout.EAST, yourLoansLabel, 0, SpringLayout.EAST, yourDetailsLabel);
		yourLoansLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		detailsPanel.add(yourLoansLabel);

		Object[][] results = {{"", "", ""}};
		DefaultTableModel tableModel = new DefaultTableModel(results, loansTableHeadings);
		loansTable = new JTable(tableModel);
		loansTable.setAutoCreateRowSorter(true);
		loansTable.setShowVerticalLines(false);
		loansTable.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JScrollPane loansScrollPane = new JScrollPane(loansTable);
		detailsPanelLayout.putConstraint(SpringLayout.SOUTH, yourLoansLabel, -6, SpringLayout.NORTH, loansScrollPane);
		detailsPanelLayout.putConstraint(SpringLayout.SOUTH, loansScrollPane, 0, SpringLayout.SOUTH, detailsPanel);
		detailsPanelLayout.putConstraint(SpringLayout.NORTH, loansScrollPane, 212, SpringLayout.NORTH, detailsPanel);
		detailsPanelLayout.putConstraint(SpringLayout.EAST, loansScrollPane, 409, SpringLayout.WEST, detailsPanel);
		detailsPanelLayout.putConstraint(SpringLayout.WEST, loansScrollPane, 0, SpringLayout.WEST, detailsPanel);
		detailsPanel.add(loansScrollPane);
	}

	/**
	 * Populates the information fields for the user.
	 */
	public void populateBorrower()
	{
		try {
			borrower = db.getBorrower(Integer.parseInt(idTextField.getText()));
			forenameLabel.setText(forename + borrower.forename);
			surnameLabel.setText(surname + borrower.surname);
			emailLabel.setText(email + borrower.email);
		} 
		catch (NumberFormatException e) 
		{
			new MessageFrame(e.getMessage());
		} 
		catch (DataNotFoundException e)
		{
			new MessageFrame(e.getMessage());
		}
		catch (InvalidArgumentException e)
		{
			new MessageFrame(e.getMessage());
		}

	}

	/**
	 * Populates a table with any loans the user currently has.
	 */
	public void populateLoans()
	{	
		DefaultTableModel tableModelLoan = (DefaultTableModel) loansTable.getModel();


		ArrayList<Loan> loanResults = new ArrayList<Loan>();
		try 
		{
			loanResults = borrower.getLoans();
		} 
		catch (Exception e)
		{
			new MessageFrame(e.getMessage()); 
		} 
		Object[][] newResults2 = new Object[loanResults.size()][3];

		for(int i = 0; i < loanResults.size(); i++)
		{
			newResults2[i][0] = loanResults.get(i).borrower_id;
			newResults2[i][1] = loanResults.get(i).deweyID;
			newResults2[i][2] = loanResults.get(i).overDue();
		}

		tableModelLoan.setDataVector(newResults2, loansTableHeadings);
	} 

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		populateBorrower();
		populateLoans();
	}
}
