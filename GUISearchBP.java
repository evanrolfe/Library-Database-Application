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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;

public class GUISearchBP extends JPanel implements ActionListener, TableModelListener
{
	private Object[] bookTableHeadings = {"ISBN", "Title", "Author", "Publisher", "Date"};
	private Object[] periodicalTableHeadings = {"ISSN", "Title", "Volume", "Number", "Publisher", "Date"};
	private JTable resultsTable;
	
	private JRadioButton radioInBooks, radioInPeriodicals;
	private JButton buttonSearch;
	
	private ArrayList<Item> searchResults;
	
	private int index;
	
	private JTextField titleTextField, authorTextField, publisherTextField, volumeTextField, numberTextField, dateTextField;
	
	SpringLayout springLayout;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public GUISearchBP() 
	{
		springLayout = new SpringLayout();
		setLayout(springLayout);
		
		searchResults = new ArrayList<Item>();
		
		titleTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, titleTextField, 17, SpringLayout.NORTH, this);
		titleTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(titleTextField);
		titleTextField.setColumns(10);
		
		authorTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, authorTextField, 6, SpringLayout.SOUTH, titleTextField);
		springLayout.putConstraint(SpringLayout.WEST, authorTextField, 0, SpringLayout.WEST, titleTextField);
		authorTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(authorTextField);
		authorTextField.setColumns(10);
		
		publisherTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, publisherTextField, 6, SpringLayout.SOUTH, authorTextField);
		springLayout.putConstraint(SpringLayout.WEST, publisherTextField, 0, SpringLayout.WEST, titleTextField);
		publisherTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(publisherTextField);
		publisherTextField.setColumns(10);
		
		buttonSearch = new JButton("Search");
		buttonSearch.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(buttonSearch);
		buttonSearch.addActionListener(this);
		
		JLabel titleLabel = new JLabel("Title: ");
		springLayout.putConstraint(SpringLayout.WEST, titleLabel, 20, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.WEST, buttonSearch, 0, SpringLayout.WEST, titleLabel);
		springLayout.putConstraint(SpringLayout.WEST, titleTextField, 54, SpringLayout.EAST, titleLabel);
		springLayout.putConstraint(SpringLayout.NORTH, titleLabel, 3, SpringLayout.NORTH, titleTextField);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(titleLabel);
		
		JLabel dateLabel = new JLabel("Date: ");
		springLayout.putConstraint(SpringLayout.WEST, dateLabel, 20, SpringLayout.WEST, this);
		dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(dateLabel);
		
		JLabel publisherLabel = new JLabel("Publisher: ");
		springLayout.putConstraint(SpringLayout.NORTH, publisherLabel, 3, SpringLayout.NORTH, publisherTextField);
		springLayout.putConstraint(SpringLayout.WEST, publisherLabel, 20, SpringLayout.WEST, this);
		publisherLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(publisherLabel);
		
		radioInBooks = new JRadioButton("In books? ");
		buttonGroup.add(radioInBooks);
		radioInBooks.setSelected(true);
		springLayout.putConstraint(SpringLayout.WEST, radioInBooks, 20, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, buttonSearch, 6, SpringLayout.SOUTH, radioInBooks);
		springLayout.putConstraint(SpringLayout.NORTH, radioInBooks, 6, SpringLayout.SOUTH, dateLabel);
		radioInBooks.setFont(new Font("Tahoma", Font.PLAIN, 18));
		radioInBooks.addActionListener(this);
		add(radioInBooks);
		
		radioInPeriodicals = new JRadioButton("In periodicals?");
		buttonGroup.add(radioInPeriodicals);
		springLayout.putConstraint(SpringLayout.NORTH, radioInPeriodicals, 0, SpringLayout.NORTH, radioInBooks);
		springLayout.putConstraint(SpringLayout.WEST, radioInPeriodicals, 6, SpringLayout.EAST, radioInBooks);
		radioInPeriodicals.setFont(new Font("Tahoma", Font.PLAIN, 18));
		radioInPeriodicals.addActionListener(this);
		add(radioInPeriodicals);
		
		JLabel authorLabel = new JLabel("Author: ");
		springLayout.putConstraint(SpringLayout.NORTH, authorLabel, 3, SpringLayout.NORTH, authorTextField);
		springLayout.putConstraint(SpringLayout.WEST, authorLabel, 20, SpringLayout.WEST, this);
		authorLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(authorLabel);
		
		JLabel volumeLabel = new JLabel("Volume: ");
		springLayout.putConstraint(SpringLayout.WEST, volumeLabel, 20, SpringLayout.WEST, this);
		volumeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(volumeLabel);
		
		JLabel numberLabel = new JLabel("Number: ");
		springLayout.putConstraint(SpringLayout.WEST, numberLabel, 20, SpringLayout.WEST, this);
		numberLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(numberLabel);
		
		volumeTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, volumeLabel, 3, SpringLayout.NORTH, volumeTextField);
		springLayout.putConstraint(SpringLayout.NORTH, volumeTextField, 6, SpringLayout.SOUTH, publisherTextField);
		springLayout.putConstraint(SpringLayout.WEST, volumeTextField, 0, SpringLayout.WEST, titleTextField);
		volumeTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(volumeTextField);
		volumeTextField.setColumns(10);
		
		numberTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, numberLabel, 3, SpringLayout.NORTH, numberTextField);
		springLayout.putConstraint(SpringLayout.NORTH, numberTextField, 6, SpringLayout.SOUTH, volumeTextField);
		springLayout.putConstraint(SpringLayout.WEST, numberTextField, 0, SpringLayout.WEST, titleTextField);
		numberTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(numberTextField);
		numberTextField.setColumns(10);
		
		dateTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, dateLabel, 3, SpringLayout.NORTH, dateTextField);
		springLayout.putConstraint(SpringLayout.NORTH, dateTextField, 6, SpringLayout.SOUTH, numberTextField);
		springLayout.putConstraint(SpringLayout.WEST, dateTextField, 0, SpringLayout.WEST, titleTextField);
		dateTextField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(dateTextField);
		dateTextField.setColumns(10);
		
		DefaultTableModel tableModel;
		if (radioInBooks.isSelected())
		{
			Object[][] results = {{"", "", "", ""}};
			tableModel = new DefaultTableModel(results, bookTableHeadings);
		}
		else
		{
			Object[][] results = {{"", "", "", "", ""}};
			tableModel = new DefaultTableModel(results, periodicalTableHeadings);
		}
		resultsTable = new JTable(tableModel);
		resultsTable.setAutoCreateRowSorter(true);
		resultsTable.setShowVerticalLines(false);
		resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsTable.getModel().addTableModelListener(this);
		resultsTable.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		JScrollPane resultsScroll = new JScrollPane(resultsTable);
		springLayout.putConstraint(SpringLayout.SOUTH, resultsScroll, 0, SpringLayout.SOUTH, radioInBooks);
		springLayout.putConstraint(SpringLayout.EAST, resultsScroll, -84, SpringLayout.EAST, this);
		resultsScroll.setBorder(new EmptyBorder(0, 40, 0, 0));
		springLayout.putConstraint(SpringLayout.NORTH, resultsScroll, 0, SpringLayout.NORTH, titleTextField);
		springLayout.putConstraint(SpringLayout.WEST, resultsScroll, 6, SpringLayout.EAST, titleTextField);
		add(resultsScroll);
	}
	
	public void search()
	{
		Hashtable<String, Object> searchData = new Hashtable<String, Object>();
		Mysql db = new Mysql();
		
		if(!titleTextField.getText().isEmpty())
		{
			searchData.put("title", titleTextField.getText());
		}
		
		if(!publisherTextField.getText().isEmpty())
		{
			searchData.put("publisher", publisherTextField.getText());
		}
		
		if(!dateTextField.getText().isEmpty())
		{
			searchData.put("data", dateTextField.getText());
		}
		
		if (radioInBooks.isSelected())
		{
			if(!authorTextField.getText().isEmpty())
			{
				searchData.put("author", authorTextField.getText());
			}
		}
		else
		{
			if(!volumeTextField.getText().isEmpty())
			{
				searchData.put("volume", volumeTextField.getText());
			}
			
			if(!numberTextField.getText().isEmpty())
			{
				searchData.put("number", numberTextField.getText());
			}
		}
			
		try {
			if (radioInBooks.isSelected())
			{
				searchResults = db.getBooks(searchData);
			}
			else 
			{
				searchResults = db.getPeriodicals(searchData);
			}
			
		} catch (InvalidArgumentException e) 
		{
			// TODO 
		} catch (DataNotFoundException e)
		{
			// TODO
		}
		
		populateResults();
	}
	
	public void populateResults()
	{	
		DefaultTableModel tableModel = (DefaultTableModel) resultsTable.getModel();

		if (radioInBooks.isSelected())
		{
			Object[][] newResults = new Object[searchResults.size()][5];
			
			for(int i = 0; i < searchResults.size(); i++)
			{
				newResults[i][0] = searchResults.get(i).isbn;
				newResults[i][1] = searchResults.get(i).title;
				newResults[i][2] = searchResults.get(i).author;
				newResults[i][3] = searchResults.get(i).publisher;
				newResults[i][4] = searchResults.get(i).date;
			}
			tableModel.setDataVector(newResults, bookTableHeadings);
		}
		else
		{
			Object[][] newResults = new Object[searchResults.size()][6];
			
			for(int i = 0; i < searchResults.size(); i++)
			{
				newResults[i][0] = searchResults.get(i).issn;
				newResults[i][1] = searchResults.get(i).title;
				newResults[i][2] = searchResults.get(i).volume;
				newResults[i][3] = searchResults.get(i).number;
				newResults[i][4] = searchResults.get(i).publisher;
				newResults[i][5] = searchResults.get(i).date;
			}
			tableModel.setDataVector(newResults, periodicalTableHeadings);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		DefaultTableModel tableModel = (DefaultTableModel) resultsTable.getModel();
		if (e.getSource() == buttonSearch)
		{
			search();
		}
		else if (e.getSource() == radioInBooks)
		{
			Object[][] results = {{"", "", "", "", ""}};
			tableModel = new DefaultTableModel(results, bookTableHeadings);
		}
		else if (e.getSource() == radioInPeriodicals)
		{
			Object[][] results = {{"", "", "", "", "", ""}};
			tableModel = new DefaultTableModel(results, periodicalTableHeadings);
		}
		
		
	}

	@Override
	public void tableChanged(TableModelEvent e) 
	{
		index = resultsTable.getSelectedRow();
	}
	
}
