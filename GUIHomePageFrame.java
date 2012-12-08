import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIHomePageFrame extends JPanel implements ItemListener
{
	
	JPanel cards;
	
	public GUIHomePageFrame()
	{
		super(new BorderLayout());
		
		String[] options = {"Search for Borrower","Search for Book/Periodical", "Create/Renew a Loan", "Discharge Loan"};
		
		JComboBox optionsList = new JComboBox(options);
		optionsList.setFont(new Font("Tahoma", Font.PLAIN, 18));
		optionsList.setSelectedIndex(0);
		optionsList.addItemListener(this);

		add(optionsList, BorderLayout.NORTH);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JPanel searchCard1 = new GUISearchBorrower();
		JPanel searchCard2 = new GUISearchBP();
		JPanel searchCard3 = new GUILoanFrame();
		JPanel searchCard4 = new GUIDischargeFrame();
		cards = new JPanel(new CardLayout());
		cards.add(searchCard1, "Search for Borrower");
		cards.add(searchCard2, "Search for Book/Periodical");
		cards.add(searchCard3, "Create/Renew a Loan");
		cards.add(searchCard4, "Discharge Loan");
		
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
		
		JComponent newContentPane = new GUIHomePageFrame();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		
		frame.setSize(new Dimension(900,600));
		frame.setVisible(true);	
	}
	
	public static void main(String[] args) 
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}