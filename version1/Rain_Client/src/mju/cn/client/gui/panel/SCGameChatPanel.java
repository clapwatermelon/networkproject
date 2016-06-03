package mju.cn.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import mju.cn.client.gui.item.SCSound;


public class SCGameChatPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Attributes
	private Image chatBackground; 
	private Image inputBackground; 
	private Image chatBar; 

	// Components
	private JButton sendButton;
	private JScrollPane scrollPane; 
	private JTextArea textArea;
	private JTextField inputField; 
	private SCGameRoomPanel parent; 
	private SCSound m_sound;

	// Constructor
	public SCGameChatPanel(SCGameRoomPanel parent) {
		this.parent = parent;
		sendButton = new JButton();
		inputField = new JTextField();
		textArea = new JTextArea();
		m_sound = new SCSound();
		scrollPane = new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatBackground = Toolkit.getDefaultToolkit().getImage(
				"images/Chatroom_Box.png");
		inputBackground = Toolkit.getDefaultToolkit().getImage(
				"images/Typo_Box_1.png");
		chatBar = Toolkit.getDefaultToolkit().getImage(
				"images/game_chatBar.png");
		this.init();
	}

	// Initialization
	private void init() {
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(400, 300));
		this.setLayout(new BorderLayout());
		this.add(this.createMessagePanel(), BorderLayout.NORTH);
		this.add(this.createInputPanel(), BorderLayout.SOUTH);
		this.initEventHadler();

		inputField.requestFocus();
	}


	private void initEventHadler() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = parent.getUserId();
				String order = parent.getOrderId();
				String txt = inputField.getText();
				int roomNumber = parent.getRoomNumber();

				if (!id.equals(order) || !parent.isStartGame()) {
					if (!txt.trim().equals("")) {
						parent.getGameChatController().gameChat(roomNumber, id,	txt);
					}
				} else {

				}
				inputField.setText("");
				inputField.requestFocus();
			}
		};

		sendButton.addActionListener(listener);
		inputField.addActionListener(listener);
	}

	private JPanel createInputPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 100));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
		
		panel.setBorder(new EmptyBorder(new Insets(0,44,0,0)));

		sendButton.setIcon(new ImageIcon("images/Typo_Box_2.png"));
		sendButton.setBorder(null);
		sendButton.setContentAreaFilled(false);
		sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		sendButton.setPreferredSize(new Dimension(59, 51));

		inputField.setPreferredSize(new Dimension(190, 25));
		inputField.setBorder(null);

		panel.add(inputField);

		panel.add(sendButton);

		return panel;
	}
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(inputBackground, 10, 205, 224, 51, this);
	}


	private JPanel createMessagePanel() {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(chatBar, 10, 0, 280, 38, this);

				g.drawImage(chatBackground,10, 38, 280,
						169, this);
				super.paintComponent(g);
			}
		};

		panel.setPreferredSize(new Dimension(278, 300));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 50));
		textArea.setOpaque(false);
		textArea.setDisabledTextColor(Color.black);
		textArea.setEditable(false);
		textArea.setEnabled(false);
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBorder(null);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setPreferredSize(new Dimension(270, 130));

		panel.add(scrollPane);
		return panel;
	}

	public void appendText(String id, String txt) {
		textArea.append("[" + id + "] : " + txt + "\n");
		textArea.setLineWrap(true);
		scrollPane.getVerticalScrollBar().setValue(
				scrollPane.getVerticalScrollBar().getMaximum());
	}

	// Getters and Setters
	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}
}
