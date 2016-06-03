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


public class SCChatPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Components
	private JButton m_sendButton; // 보내기버튼
	private JScrollPane m_scrollPane; // 스크롤화면
	private JTextArea m_textArea; // 텍스트에어리어
	private JTextField m_inputField; // 입력필드
	private Image m_chatBackground; // 채팅 배경화면
	private SCLobbyPanel m_parent; // 상위패널

	// Constructor
	public SCChatPanel(SCLobbyPanel parent) {
		m_parent = parent;
		m_sendButton = new JButton();
		m_inputField = new JTextField();
		m_textArea = new JTextArea();
		m_scrollPane = new JScrollPane(m_textArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		m_chatBackground = Toolkit.getDefaultToolkit().getImage(
				"images/chat_rect.png");

		this.init();
	}

	// Initialization
	private void init() {
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(655, 245));
		this.setLayout(new BorderLayout());
		this.add(this.createMessagePanel(), BorderLayout.NORTH);
		this.add(this.createInputPanel(), BorderLayout.SOUTH);
		this.initEventHadler();

		m_inputField.requestFocus();
	}

	// 이벤트핸들러 초기화 함수
	private void initEventHadler() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = m_parent.getUserId();
				String txt = m_inputField.getText();
				if (!txt.trim().equals("")) {
					m_parent.getChatController().chat(id,
							m_inputField.getText());
				}
				m_inputField.setText("");
				m_inputField.requestFocus();
			}
		};

		m_sendButton.addActionListener(listener);
		m_inputField.addActionListener(listener);
	}

	// 입력패널 생성 함수
	private JPanel createInputPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, 55));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 15));

		m_sendButton.setIcon(new ImageIcon("images/chat_input.PNG"));
		m_sendButton.setBorder(null);
		m_sendButton.setContentAreaFilled(false);
		m_sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		m_sendButton.setPreferredSize(new Dimension(90, 25));

		m_inputField.setPreferredSize(new Dimension(500, 25));
		m_inputField.setBorder(null);

		panel.add(m_inputField);
		panel.add(m_sendButton);

		return panel;
	}

	// 메시지 패널 생성 함수
	private JPanel createMessagePanel() {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(m_chatBackground, 25, 0, 610,
						this.getHeight(), this);
				super.paintComponent(g);
			}
		};

		panel.setPreferredSize(new Dimension(0, 190));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));
		m_textArea.setOpaque(false);
		m_textArea.setDisabledTextColor(Color.WHITE);
		m_textArea.setEditable(false);
		m_textArea.setEnabled(false);
		m_scrollPane.setOpaque(false);
		m_scrollPane.setBorder(null);
		m_scrollPane.getViewport().setBorder(null);
		m_scrollPane.getViewport().setOpaque(false);
		m_scrollPane.setPreferredSize(new Dimension(570, 130));

		panel.add(m_scrollPane);
		return panel;
	}

	// 텍스트 삽입 함수
	public void appendText(String id, String txt) {
		m_textArea.append("[" + id + "] : " + txt + "\n");
		m_textArea.setLineWrap(true);
		m_scrollPane.getVerticalScrollBar().setValue(
				m_scrollPane.getVerticalScrollBar().getMaximum());
	}
}
