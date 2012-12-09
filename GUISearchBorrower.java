import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.Component;

public class GUISearchBorrower extends JPanel implements ActionListener, TableModelListener
{

	private Object[] borrowerTableHeadings = {"Membership Number", "Forename", "Surname", "email"};
	private JTable resultsTable;
	private Object[] loanHeadings = {"Membership Number", "Title", "Overdue"};
	private JTable borrowerLoans;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField forenameTextField, surnameTextField, memNoTextField;
	private JButton buttonSearch;
	private ArrayList<Borrower> searchResults;
	private JTextField emailTextField;
	
	public GUISearchBorrower() 
	{
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		searchResults = new ArrayList<Borrower>();
		
		JTextField forenameTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, forenameTextField, 17, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, forenameTextField, 163, SpringLayout.WEST, this);
		forenameTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(forenameTextField);
		forenameTextField.setColumns(10);
		
		JTextField surnameTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, surnameTextField, 57, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, surnameTextField, 163, SpringLayout.WEST, this);
		surnameTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(surnameTextField);
		surnameTextField.setColumns(10);
		
		JTextField memNoTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, memNoTextField, 92, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, memNoTextField, 209, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, memNoTextField, 344, SpringLayout.WEST, this);
		memNoTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(memNoTextField);
		memNoTextField.setColumns(10);
		
		JButton buttonSearch = new JButton("Search");
		springLayout.putConstraint(SpringLayout.NORTH, buttonSearch, 186, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, buttonSearch, 95, SpringLayout.WEST, this);
		buttonSearch.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(buttonSearch);
		buttonSearch.addActionListener(this);
		
		JLabel forenameLabel = new JLabel("Forename: ");
		springLayout.putConstraint(SpringLayout.NORTH, forenameLabel, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, forenameLabel, 20, SpringLayout.WEST, this);
		forenameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(forenameLabel);
		
		JLabel memNoLabel = new JLabel("Membership Number: ");
		springLayout.putConstraint(SpringLayout.NORTH, memNoLabel, 100, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, memNoLabel, 20, SpringLayout.WEST, this);
		memNoLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(memNoLabel);
		
		JLabel surnameLabel = new JLabel("Surname: ");
		springLayout.putConstraint(SpringLayout.NORTH, surnameLabel, 60, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, surnameLabel, 20, SpringLayout.WEST, this);
		surnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(surnameLabel);
		
		Object[][] results = {{"", "", "", ""}};
		DefaultTableModel tableModel = new DefaultTableModel(results, borrowerTableHeadings);
		resultsTable = new JTable(tableModel);
		resultsTable.setAutoCreateRowSorter(true);
		resultsTable.setShowVerticalLines(false);
		resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsTable.getModel().addTableModelListener(this);
		resultsTable.setFont(new Font("Tahoma", Font.PLAIN, 18));
	
		Object[][] loans = {{"", "", ""}};
		DefaultTableModel tableModel1 = new DefaultTableModel(loans, loanHeadings);
		borrowerLoans = new JTable(tableModel1);
		borrowerLoans.setAutoCreateRowSorter(true);
		borrowerLoans.setShowVerticalLines(false);
		borrowerLoans.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		borrowerLoans.getModel().addTableModelListener(this);
		borrowerLoans.setFont(new Font("Tahoma", Font.PLAIN, 18));
	
		
		JScrollPane resultsScroll = new JScrollPane(resultsTable);
		springLayout.putConstraint(SpringLayout.NORTH, resultsScroll, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, resultsScroll, 6, SpringLayout.EAST, memNoTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, resultsScroll, -20, SpringLayout.SOUTH, buttonSearch);
		springLayout.putConstraint(SpringLayout.EAST, resultsScroll, -10, SpringLayout.EAST, this);
		resultsScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(resultsScroll);
		
		JScrollPane loanScroll = new JScrollPane(borrowerLoans);
		springLayout.putConstraint(SpringLayout.NORTH, loanScroll, 9, SpringLayout.SOUTH, resultsScroll);
		springLayout.putConstraint(SpringLayout.WEST, loanScroll, 335, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, loanScroll, -65, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, loanScroll, -10, SpringLayout.EAST, this);
		loanScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
		add(loanScroll);
		
		JLabel lblEmail = new JLabel("Email: ");
		springLayout.putConstraint(SpringLayout.NORTH, lblEmail, 17, SpringLayout.SOUTH, memNoLabel);
		springLayout.putConstraint(SpringLayout.WEST, lblEmail, 0, SpringLayout.WEST, forenameLabel);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblEmail);
		
		emailTextField = new JTextField();
		emailTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		springLayout.putConstraint(SpringLayout.WEST, emailTextField, 0, SpringLayout.WEST, forenameTextField);
		springLayout.putConstraint(SpringLayout.SOUTH, emailTextField, 0, SpringLayout.SOUTH, lblEmail);
		add(emailTextField);
		emailTextField.setColumns(10);
		
	}
	
	public void search()
	{
		
		Hashtable<String, String> searchData = new Hashtable<String, String>();
		searchData.put("forename", forenameTextField.getText());
		searchData.put("surname", surnameTextField.getText());
		searchData.put("id", memNoTextField.getText());
		searchData.put("email", emailTextField.getText());
		
		try {
			searchResults = Database.find_borrowers(searchData);
		} catch (InvalidArgumentException e) 
		{
			// TODO 
		} catch (DataNotFoundException e)
		{
			//TODO
		}
	}
	
	public void populateResultsBorrower()
	{				
		DefaultTableModel tableModel = (DefaultTableModel) resultsTable.getModel();
		Object[][] newResults = new Object[searchResults.size()][4];
		
		for(int i = 0; i < searchResults.size(); i++)
		{
			newResults[i][0] = searchResults.get(i).getValue("id");
			newResults[i][1] = searchResults.get(i).getValue("forename");
			newResults[i][2] = searchResults.get(i).getValue("surname");
			newResults[i][3] = searchResults.get(i).getValue("email");
		}
		
		tableModel.setDataVector(newResults, borrowerTableHeadings);
	}
	
	public void populateResultsLoans()
	{	
		ArrayList<Loan> loanResults = new ArrayList<Loan>();
		
		DefaultTableModel tableModelLoan = (DefaultTableModel) borrowerLoans.getModel();
		Object[][] newResults2 = new Object[loanResults.size()][3];
		
		for(int i = 0; i < loanResults.size(); i++)
		{
			newResults2[i][0] = loanResults.get(i).borrower_id;
			if (loanResults.get(i).item.getType() == "Book")
			{
				newResults2[i][1] = loanResults.get(i).book_isbn;
			}
			else
			{
				newResults2[i][1] = loanResults.get(i).periodical_issn;
			}
			
			newResults2[i][2] = loanResults.get(i).overDue();
		}
		
		tableModelLoan.setDataVector(newResults2, loanHeadings);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == buttonSearch)
		{
			search();
		}
		
		populateResultsBorrower();
	}

	@Override
	public void tableChanged(TableModelEvent e) 
	{
		// TODO Auto-generated method stub
			resultsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
	        int selRow = resultsTable.getSelectedRow();
	        int selCol = resultsTable.getSelectedColumn();
	    }
	});
		
	}
}
