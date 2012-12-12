/**
 * The home page for the borrower interface. They can select to either search for and reserve items
 * or look at their details, including current loans.
 * 
 * Created with help from http://docs.oracle.com/javase/tutorial/uiswing/
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class BorrowerHomepage extends JPanel implements ItemListener
{
	/**
	 * Create the panel for the cards so we can switch between them.
	 */
	JPanel cards;

	/**
	 * Initialises the JPanel. Cards are used to associate a different JPanel to each option in the drop down list.
	 */
	public BorrowerHomepage()
	{
		super(new BorderLayout());

		String[] options = {"Search", "Profile"};

		JComboBox optionsList = new JComboBox(options);
		optionsList.setFont(new Font("Tahoma", Font.PLAIN, 18));
		optionsList.setSelectedIndex(0);
		optionsList.addItemListener(this);

		add(optionsList, BorderLayout.NORTH);
		setBorder(new EmptyBorder(40, 40, 40, 40));

		JPanel searchCard = new SearchItemReserveFrame();
		JPanel profileCard = new ProfileFrame();
		cards = new JPanel(new CardLayout());
		cards.add(searchCard, "Search");
		cards.add(profileCard, "Profile");

		add(cards, BorderLayout.CENTER);
	}

	public void itemStateChanged(ItemEvent e) 
	{
		CardLayout cardLayout = (CardLayout)(cards.getLayout());
		cardLayout.show(cards, (String)e.getItem());
	}

	private static void createAndShowGUI()
	{
		JFrame frame = new JFrame("Library System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent contentPane = new BorrowerHomepage();
		contentPane.setOpaque(true);
		frame.setContentPane(contentPane);

		frame.setSize(new Dimension(1200, 700));
		frame.setVisible(true);	
	}

	public static void main(String[] args) 
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				createAndShowGUI();
			}
		});
	}
}