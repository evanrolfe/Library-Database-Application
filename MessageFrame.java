/**
 * Creates the JFrame to display relevant error/success messages.
 */
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;


public class MessageFrame extends JFrame implements ActionListener

{
	/**
	 * Create the JFrame for the error/success message.
	 */
	private JFrame frame;
	
	/**
	 * Initialise the JFrame with relevant label and an ok button to exit.
	 * @param newMessage	is displayed in the JFrame
	 */
	public MessageFrame(String newMessage)
	{
		JPanel panelMessage = new JPanel();
		SpringLayout springLayout = new SpringLayout();
		panelMessage.setLayout(springLayout);

		JLabel message = new JLabel(newMessage);
		message.setFont(new Font("Tahoma", Font.PLAIN, 18));
		springLayout.putConstraint(SpringLayout.NORTH, message, 32, SpringLayout.NORTH, panelMessage);
		springLayout.putConstraint(SpringLayout.WEST, message, 75, SpringLayout.WEST, panelMessage);
		springLayout.putConstraint(SpringLayout.SOUTH, message, 54, SpringLayout.NORTH, panelMessage);
		springLayout.putConstraint(SpringLayout.EAST, message, -67, SpringLayout.EAST, panelMessage);
		panelMessage.add(message);

		JButton okButton = new JButton("Ok");
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		springLayout.putConstraint(SpringLayout.NORTH, okButton, 54, SpringLayout.SOUTH, message);
		springLayout.putConstraint(SpringLayout.WEST, okButton, 129, SpringLayout.WEST, panelMessage);
		okButton.addActionListener(this);
		panelMessage.add(okButton);
		
		frame = new JFrame("Message");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(200, 250);
		
		JComponent newContentPane = panelMessage;
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);

		frame.setSize(new Dimension(700, 200));
		frame.setVisible(true);	
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
			frame.dispose();
	}

}
