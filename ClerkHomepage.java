/**
 * This is the main home page for the reader service clerk and allows them to select the options they want to do with
 * a drop down menu. The default page is set as search for borrower.
 * Created with help from java Oracle tutorials(http://docs.oracle.com/javase/tutorial/uiswing/index.html)
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClerkHomepage extends JPanel implements ItemListener
{
/**
 * The JPanel variable for the cards.
 */
	JPanel cards;
/**
 * Initialise the panel. Utilises search cards to switch between the different panels.
 */
	public ClerkHomepage()
	{
		super(new BorderLayout());
/**
 * Creates the different options that the clerk can select from.
 */
		String[] options = {"Search for Borrower","Search for Book/Periodical", "Create/Renew a Loan", "Discharge Loan"};

		JComboBox optionsList = new JComboBox(options);
		optionsList.setFont(new Font("Tahoma", Font.PLAIN, 18));
		optionsList.setSelectedIndex(0);
		optionsList.addItemListener(this);

		add(optionsList, BorderLayout.NORTH);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JPanel searchCard1 = new SearchBorrowerFrame();
		JPanel searchCard2 = new SearchItemFrame();
		JPanel searchCard3 = new LoanFrame();
		JPanel searchCard4 = new DischargeFrame();
		cards = new JPanel(new CardLayout());
		cards.add(searchCard1, "Search for Borrower");
		cards.add(searchCard2, "Search for Book/Periodical");
		cards.add(searchCard3, "Create/Renew a Loan");
		cards.add(searchCard4, "Discharge Loan");

		add(cards, BorderLayout.CENTER);
	}

	/**
	 * Checks which card has been selected and displays the relevant panel.
	 */
	public void itemStateChanged(ItemEvent e) 
	{
		CardLayout cardLayout = (CardLayout)(cards.getLayout());
		cardLayout.show(cards, (String)e.getItem());
	}

	/**
	 * Displays the frame.
	 */
	private static void createAndShowGUI()
	{
		JFrame frame = new JFrame("Library System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new ClerkHomepage();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.setSize(new Dimension(1200,700));
		frame.setVisible(true);	
	}
/**
 * Main method runs the gui.
 * @param args
 */
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