import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class GUIHomePage extends JPanel implements ItemListener
{
	
	JPanel cards;
	
	public GUIHomePage()
	{
		super(new BorderLayout());
		
		String[] options = {"Search", "Profile"};
		
		JComboBox optionsList = new JComboBox(options);
		optionsList.setFont(new Font("Tahoma", Font.PLAIN, 18));
		optionsList.setSelectedIndex(0);
		optionsList.addItemListener(this);

		add(optionsList, BorderLayout.NORTH);
		setBorder(new EmptyBorder(40, 40, 40, 40));
		
		JPanel searchCard = new GUISearchBPReserve();
		JPanel profileCard = new GUIProfile();
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
		
		JComponent newContentPane = new GUIHomePage();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		
		frame.setSize(new Dimension(1000, 600));
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