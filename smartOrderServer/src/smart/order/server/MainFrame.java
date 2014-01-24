package smart.order.server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JCheckBox statusServerRunning;
	private JPasswordField passwordField;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("SmartOrder im StePub");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 641, 446);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		statusServerRunning = new JCheckBox("Server online");
		statusServerRunning.setEnabled(false);
		GridBagConstraints gbc_statusServerRunning = new GridBagConstraints();
		gbc_statusServerRunning.anchor = GridBagConstraints.WEST;
		gbc_statusServerRunning.insets = new Insets(0, 0, 5, 0);
		gbc_statusServerRunning.gridx = 0;
		gbc_statusServerRunning.gridy = 0;
		panel.add(statusServerRunning, gbc_statusServerRunning);
		statusServerRunning.setHorizontalAlignment(SwingConstants.LEFT);
		
		JCheckBox statusClient1Connected = new JCheckBox("Client 1 verbunden");
		GridBagConstraints gbc_statusClient1Connected = new GridBagConstraints();
		gbc_statusClient1Connected.insets = new Insets(0, 0, 5, 0);
		gbc_statusClient1Connected.gridx = 0;
		gbc_statusClient1Connected.gridy = 1;
		panel.add(statusClient1Connected, gbc_statusClient1Connected);
		statusClient1Connected.setEnabled(false);
		statusClient1Connected.setHorizontalAlignment(SwingConstants.LEFT);
		
		JCheckBox statusClient2Connected = new JCheckBox("Client 2 verbunden");
		GridBagConstraints gbc_statusClient2Connected = new GridBagConstraints();
		gbc_statusClient2Connected.insets = new Insets(0, 0, 5, 0);
		gbc_statusClient2Connected.gridx = 0;
		gbc_statusClient2Connected.gridy = 2;
		panel.add(statusClient2Connected, gbc_statusClient2Connected);
		statusClient2Connected.setEnabled(false);
		statusClient2Connected.setVisible(false);
		statusClient2Connected.setHorizontalAlignment(SwingConstants.LEFT);
		
		JCheckBox statusClient3Connected = new JCheckBox("Client 3 verbunden");
		GridBagConstraints gbc_statusClient3Connected = new GridBagConstraints();
		gbc_statusClient3Connected.gridx = 0;
		gbc_statusClient3Connected.gridy = 3;
		panel.add(statusClient3Connected, gbc_statusClient3Connected);
		statusClient3Connected.setEnabled(false);
		statusClient3Connected.setVisible(false);
		statusClient3Connected.setHorizontalAlignment(SwingConstants.LEFT);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Tagesabschluss", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 3;
		contentPane.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton btnTagesabschluss = new JButton("Tagesabschluss");
		btnTagesabschluss.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main.quickTest();
			}
		});
		GridBagConstraints gbc_btnTagesabschluss = new GridBagConstraints();
		gbc_btnTagesabschluss.insets = new Insets(0, 0, 5, 0);
		gbc_btnTagesabschluss.gridx = 0;
		gbc_btnTagesabschluss.gridy = 0;
		panel_1.add(btnTagesabschluss, gbc_btnTagesabschluss);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 0);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 0;
		gbc_passwordField.gridy = 1;
		passwordField.setVisible(false);
		panel_1.add(passwordField, gbc_passwordField);
		
		JButton btnAbschlieen = new JButton("Abschlie\u00DFen");
		GridBagConstraints gbc_btnAbschlieen = new GridBagConstraints();
		gbc_btnAbschlieen.gridx = 0;
		gbc_btnAbschlieen.gridy = 2;
		btnAbschlieen.setVisible(false);
		panel_1.add(btnAbschlieen, gbc_btnAbschlieen);
		
		JButton btnBeenden = new JButton("Beenden");
		GridBagConstraints gbc_btnBeenden = new GridBagConstraints();
		gbc_btnBeenden.gridx = 6;
		gbc_btnBeenden.gridy = 6;
		contentPane.add(btnBeenden, gbc_btnBeenden);
	}

}
