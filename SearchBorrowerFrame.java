/**
 * Contains a form to search for a borrower/borrowers. The results are displayed in a table and a borrower
 * can be selected to displayed any loans they may have. 
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;

public class SearchBorrowerFrame extends JPanel implements ActionListener, ListSelectionListener
{

	/**
	 * Create borrower table headings.
	 */
	private Object[] borrowerTableHeadings = {"Membership Number", "Forename", "Surname", "email"};
	
	/**
	 * Create results and loans table.
	 */
	private JTable resultsTable, borrowerLoans;
	
	/**
	 * Create loan table headings.
	 */
	private Object[] loanHeadings = {"Membership Number", "Dewey Index", "Overdue"};
	
	/**
	 * int index gets selected row.
	 */
	private int index;
	
	/**
	 * Group radio buttons together.
	 */
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	/**
	 * Creates relevant text fields for searching for a borrower.
	 */
	private JTextField forenameTextField, surnameTextField, memNoTextField, emailTextField;
	
	/**
	 * Creates the search button.
	 */
	private JButton buttonSearch;
	
	/**
	 * Creates array list to store the search results.
	 */
	private ArrayList<Borrower> searchResults;
	
	/**
	 * Stores database abstraction layer.
	 */
	private Mysql db;

	/**
	 * Initialises the JPanel. 
	 */
	public SearchBorrowerFrame() 
	{
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		searchResults = new ArrayList<Borrower>();
		db = new Mysql();

		memNoTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, memNoTextField, 17, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, memNoTextField, 163, SpringLayout.WEST, this);
		memNoTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(memNoTextField);
		memNoTextField.setColumns(10);

		forenameTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, forenameTextField, 57, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, forenameTextField, 163, SpringLayout.WEST, this);
		forenameTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(forenameTextField);
		forenameTextField.setColumns(10);

		surnameTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, surnameTextField, 92, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, surnameTextField, 163, SpringLayout.WEST, this);
		surnameTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(surnameTextField);
		surnameTextField.setColumns(10);

		buttonSearch = new JButton("Search");
		springLayout.putConstraint(SpringLayout.NORTH, buttonSearch, 186, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, buttonSearch, 95, SpringLayout.WEST, this);
		buttonSearch.setFont(new Font("Tahoma", Font.PLAIN, 18));
		buttonSearch.addActionListener(this);
		add(buttonSearch);	

		JLabel memNoLabel = new JLabel("Membership No.: ");
		springLayout.putConstraint(SpringLayout.NORTH, memNoLabel, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, memNoLabel, 20, SpringLayout.WEST, this);
		memNoLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(memNoLabel);

		JLabel surnameLabel = new JLabel("Surname: ");
		springLayout.putConstraint(SpringLayout.NORTH, surnameLabel, 100, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, surnameLabel, 20, SpringLayout.WEST, this);
		surnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(surnameLabel);

		JLabel forenameLabel = new JLabel("Forename: ");
		springLayout.putConstraint(SpringLayout.NORTH, forenameLabel, 60, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, forenameLabel, 20, SpringLayout.WEST, this);
		forenameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(forenameLabel);

		Object[][] results = {{"", "", "", ""}};
		DefaultTableModel tableModel = new DefaultTableModel(results, borrowerTableHeadings);
		resultsTable = new JTable(tableModel);
		resultsTable.setAutoCreateRowSorter(true);
		resultsTable.setShowVerticalLines(false);
		resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);;
		resultsTable.getSelectionModel().addListSelectionListener(this);
		resultsTable.setFont(new Font("Tahoma", Font.PLAIN, 18));

		Object[][] loans = {{"", "", ""}};
		DefaultTableModel tableModel1 = new DefaultTableModel(loans, loanHeadings);
		borrowerLoans = new JTable(tableModel1);
		borrowerLoans.setAutoCreateRowSorter(true);
		borrowerLoans.setShowVerticalLines(false);
		borrowerLoans.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		borrowerLoans.setFont(new Font("Tahoma", Font.PLAIN, 18));


		JScrollPane resultsScroll = new JScrollPane(resultsTable);
		springLayout.putConstraint(SpringLayout.NORTH, resultsScroll, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, resultsScroll, 6, SpringLayout.EAST, memNoTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, resultsScroll, -20, SpringLayout.SOUTH, buttonSearch);
		springLayout.putConstraint(SpringLayout.EAST, resultsScroll, -10, SpringLayout.EAST, this);
		resultsScroll.setBorder(new EmptyBorder(10, 40, 0, 0));
		add(resultsScroll);

		JScrollPane loanScroll = new JScrollPane(borrowerLoans);
		springLayout.putConstraint(SpringLayout.NORTH, loanScroll, 9, SpringLayout.SOUTH, resultsScroll);
		springLayout.putConstraint(SpringLayout.WEST, loanScroll, 335, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, loanScroll, -65, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, loanScroll, -10, SpringLayout.EAST, this);
		loanScroll.setBorder(new EmptyBorder(0, 40, 0, 0));
		add(loanScroll);

		JLabel lblEmail = new JLabel("Email: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblEmail, 17, SpringLayout.SOUTH, surnameLabel);
		springLayout.putConstraint(SpringLayout.WEST, lblEmail, 0, SpringLayout.WEST, memNoLabel);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblEmail);

		emailTextField = new JTextField();
		emailTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		springLayout.putConstraint(SpringLayout.WEST, emailTextField, 0, SpringLayout.WEST, memNoTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, emailTextField, 0, SpringLayout.SOUTH, lblEmail);
		add(emailTextField);
		emailTextField.setColumns(10);

	}

	/** 
	 * Searches the database for borrowers by any combination of fields.
	 */
	public void search()
	{
		Hashtable<String, Object> searchData = new Hashtable<String, Object>();

		if(!forenameTextField.getText().isEmpty())
		{
			searchData.put("forename", forenameTextField.getText());
		}
		if(!surnameTextField.getText().isEmpty())
		{
			searchData.put("surname", surnameTextField.getText());
		}
		if(!memNoTextField.getText().isEmpty())
		{
			searchData.put("borrowerID", memNoTextField.getText());
		}
		if(!emailTextField.getText().isEmpty())
		{
			searchData.put("email", emailTextField.getText()); 
		}

		try 
		{
			searchResults = db.getBorrowers(searchData);
			populateResultsBorrower();
		} 
		catch (InvalidArgumentException e) 
		{
			new MessageFrame(e.getMessage());
		} 
		catch (DataNotFoundException e)
		{
			new MessageFrame(e.getMessage());
		}
	}

	/** 
	 * Displays the found borrowers in a table
	 */
	public void populateResultsBorrower()
	{				
		DefaultTableModel tableModel = (DefaultTableModel) resultsTable.getModel();
		Object[][] newResults = new Object[searchResults.size()][4];

		for(int i = 0; i < searchResults.size(); i++)
		{
			newResults[i][0] = searchResults.get(i).id;
			newResults[i][1] = searchResults.get(i).forename;
			newResults[i][2] = searchResults.get(i).surname;
			newResults[i][3] = searchResults.get(i).email;
		}

		tableModel.setDataVector(newResults, borrowerTableHeadings);
	}

	/**
	 * Displays the current loans of a selected borrower from the search.
	 * For each loan, the borrower id, dewey index of the copy and whether or not it is over due is displayed
	 */
	public void populateResultsLoans()
	{	
		DefaultTableModel tableModel = (DefaultTableModel) resultsTable.getModel();
		DefaultTableModel tableModelLoan = (DefaultTableModel) borrowerLoans.getModel();

		Borrower b = null;
		try
		{
			b = db.getBorrower((int) tableModel.getValueAt(index, 0));
		} 
		catch (DataNotFoundException e1) 
		{
			new MessageFrame(e1.getMessage());
		} 
		catch (InvalidArgumentException e1) 
		{
			new MessageFrame(e1.getMessage());
		}

		ArrayList<Loan> loanResults = new ArrayList<Loan>();

		try 
		{
			loanResults = b.getLoans();
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

		tableModelLoan.setDataVector(newResults2, loanHeadings);
	} 

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == buttonSearch)
		{
			search();
			resultsTable.setRowSelectionAllowed(true);

		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		// Gets the index of the borrower search results table that has been selected
		index = resultsTable.getSelectedRow();
		// only populate the loans table if a borrower is selected
		if(index > -1)
		{
			populateResultsLoans();

		}
	}
}
