package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;

public class PromptDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblMessage;

	/**
	 * Create the dialog.
	 */
	public PromptDialog() {
		setTitle("\u63D0\u793A");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 399, 227);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblMessage = new JLabel("message here...");
		lblMessage.setFont(new Font("新宋体", Font.BOLD, 20));
		lblMessage.setBounds(101, 54, 192, 61);
		contentPanel.add(lblMessage);
	}
	
	public void setMessage(String msg) {
		lblMessage.setText(msg);
	}
	
}
