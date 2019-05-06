package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login {
	private JPasswordField txtPassword;
	private JFrame frame;

	public static void main(String[] args) {
		Login gui = new Login();
		gui.init();
	}

	public void init() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		JLabel lblPassword = new JLabel("Instructor Code:");
		lblPassword.setBounds(100, 93, 102, 16);
		txtPassword = new JPasswordField(20);
		txtPassword.setBounds(24, 121, 250, 26);
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(107, 154, 79, 29);
		btnLogin.addActionListener(new LoginListener());
		panel.setLayout(null);

		panel.add(lblPassword);
		panel.add(txtPassword);
		panel.add(btnLogin);
		frame.getContentPane().add(BorderLayout.CENTER, panel);

		frame.setSize(300, 300);
		frame.setVisible(true);

	}

	public class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (String.valueOf(txtPassword.getPassword()).equals("591")) {
				JOptionPane.showMessageDialog(null, "Login Successfull");
				enterToSystem();
			} else {
				JOptionPane.showMessageDialog(null, "Wrong code");
				txtPassword.setText("");
			}

		}
	}

	void enterToSystem() {
		new ClassPanel().setVisible(true);
		frame.dispose();
	}

}