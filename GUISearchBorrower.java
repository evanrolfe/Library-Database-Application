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

public class GUISearchBorrower extends JPanel implements ActionListener, TableModelListener, ListSelectionListener
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
	private int index;
	private Mysql db;
	
	public GUISearchBorrower() 
	{
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		searchResults = new ArrayList<Borrower>();
		db = new Mysql();
		
		forenameTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, forenameTextField, 17, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, forenameTextField, 163, SpringLayout.WEST, this);
		forenameTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(forenameTextField);
		forenameTextField.setColumns(10);
		
		surnameTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, surnameTextField, 57, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, surnameTextField, 163, SpringLayout.WEST, this);
		surnameTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(surnameTextField);
		surnameTextField.setColumns(10);
		
		memNoTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, memNoTextField, 92, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, memNoTextField, 209, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, memNoTextField, 344, SpringLayout.WEST, this);
		memNoTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(memNoTextField);
		memNoTextField.setColumns(10);
		
		buttonSearch = new JButton("Search");
		springLayout.putConstraint(SpringLayout.NORTH, buttonSearch, 186, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, buttonSearch, 95, SpringLayout.WEST, this);
		buttonSearch.setFont(new Font("Tahoma", Font.PLAIN, 18));
		buttonSearch.addActionListener(this);
		add(buttonSearch);	
		
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
		resultsTable.getSelectionModel().addListSelectionListener(this);
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
			searchData.put("id", memNoTextField.getText());
		}
		if(!emailTextField.getText().isEmpty())
		{
			searchData.put("email", emailTextField.getText()); 
		}
		
		try {
			searchResults = db.getBorrowers(searchData);
		} 
		catch (InvalidArgumentException e) 
		{
			// TODO 
		} 
		catch (DataNotFoundException e)
		{
			//TODO
		}
		
		populateResultsBorrower();
	}
	
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
	
	public void populateResultsLoans()
	{	
		DefaultTableModel tableModel = (DefaultTableModel) resultsTable.getModel();
		DefaultTableModel tableModelLoan = (DefaultTableModel) borrowerLoans.getModel();
		
		Borrower b = null;
		try
		{
			b = Database.find_borrower((int) tableModel.getValueAt(index, 0));
		} 
		catch (DataNotFoundException e1) 
		{
			// TODO 
		} 
		catch (InvalidArgumentException e1) 
		{
			// TODO 
		}
		
		ArrayList<Loan> loanResults = new ArrayList<Loan>();
		try 
		{
			loanResults = b.getLoans();
		} 
		catch (Exception e)
		{
			// TODO 
		} 
		
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
		if(e.getSource() == buttonSearch)
		{
			search();
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) 
	{
		index = resultsTable.getSelectedRow();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{

		if(index > -1)
		{
			populateResultsLoans();
		}
		
	}
}
