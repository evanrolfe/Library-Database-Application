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

public class GUIProfile extends JPanel implements ActionListener
{
	
	private Object[] loansTableHeadings = {"Membership Number", "Title", "Overdue"};
	private JTable loansTable;
	
	private String forename = "Forename: ";
	private String surname = "Surname: ";
	private String email = "Email: ";
	
	private JLabel forenameLabel;
	private JLabel surnameLabel;
	private JLabel emailLabel;
	
	public GUIProfile() 
	{
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		JTextField idTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.SOUTH, idTextField, -356, SpringLayout.SOUTH, this);
		idTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(idTextField);
		idTextField.setColumns(10);
		
		JLabel idLabel = new JLabel("ID: ");
		springLayout.putConstraint(SpringLayout.NORTH, idLabel, 3, SpringLayout.NORTH, idTextField);
		springLayout.putConstraint(SpringLayout.EAST, idLabel, -6, SpringLayout.WEST, idTextField);
		idLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(idLabel);
		
		JButton searchButton = new JButton("Search");
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
		loansTable.setRowSelectionAllowed(false);
		loansTable.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		JScrollPane loansScrollPane = new JScrollPane(loansTable);
		detailsPanelLayout.putConstraint(SpringLayout.SOUTH, yourLoansLabel, -6, SpringLayout.NORTH, loansScrollPane);
		detailsPanelLayout.putConstraint(SpringLayout.SOUTH, loansScrollPane, 0, SpringLayout.SOUTH, detailsPanel);
		detailsPanelLayout.putConstraint(SpringLayout.NORTH, loansScrollPane, 212, SpringLayout.NORTH, detailsPanel);
		detailsPanelLayout.putConstraint(SpringLayout.EAST, loansScrollPane, 409, SpringLayout.WEST, detailsPanel);
		detailsPanelLayout.putConstraint(SpringLayout.WEST, loansScrollPane, 0, SpringLayout.WEST, detailsPanel);
		detailsPanel.add(loansScrollPane);
	}
	
	public void populateBorrower()
	{
		//TODO
		forenameLabel.setText(forename + "Fred");
		surnameLabel.setText(surname + "Ers");
		emailLabel.setText(email + "jsfa@ksf");
	}

	public void populateLoans()
	{				
		ArrayList<Loan> searchResults = new ArrayList<Loan>();
		DefaultTableModel tableModel = (DefaultTableModel) loansTable.getModel();
		Object[][] newResults = new Object[searchResults.size()][3];
		
		for(int i = 0; i < searchResults.size(); i++)
		{
			newResults[i][0] = searchResults.get(i).borrower_id;
			if (searchResults.get(i).item.getType() == "Book")
			{
				newResults[i][1] = searchResults.get(i).book_isbn;
			}
			else
			{
				newResults[i][1] = searchResults.get(i).periodical_issn;
			}
			
			newResults[i][2] = searchResults.get(i).overDue();
		}
		
		tableModel.setDataVector(newResults, loansTableHeadings);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		//TODO get input
		populateBorrower();
		populateLoans();
	}
}
