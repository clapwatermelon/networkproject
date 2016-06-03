package mju.cn.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mju.cn.client.gui.item.SCSound;


public class SCRoomListPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Attributes
	private SCLobbyPanel m_parent; // 상위패널
	private JButton m_roomCreate; // 방만들기 버튼
	private JPanel m_mainPanel; // 메인패널
	private JScrollPane m_scrollPane; // 스크롤공간
	private Image m_background; // 배경

	// Components
	private SCSound m_sound;

	// Constructor
	public SCRoomListPanel(SCLobbyPanel parent) {
		m_parent = parent;
		m_roomCreate = new JButton();
		m_mainPanel = new JPanel();
		m_scrollPane = new JScrollPane(m_mainPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		m_background = Toolkit.getDefaultToolkit().getImage(
				"images/roomlist.png");
		m_sound = new SCSound();

		this.init();
	}

	// Initialization
	private void init() {
		m_roomCreate.setIcon(new ImageIcon("images/create_roomButton2.png"));
		m_roomCreate.setBorder(null);
		m_roomCreate.setContentAreaFilled(false);
		m_roomCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
		m_roomCreate.setPreferredSize(new Dimension(100, 60));

		m_mainPanel.setOpaque(false);
		m_mainPanel.setBorder(null);
		m_mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		m_scrollPane.setPreferredSize(new Dimension(600, 230));
		m_scrollPane.setOpaque(false);
		m_scrollPane.getViewport().setOpaque(false);
		m_scrollPane.setBorder(null);

		this.setPreferredSize(new Dimension(655, 355));
		this.setOpaque(false);
		this.setLayout(new BorderLayout());

		this.add(this.createButtonPanel(), BorderLayout.NORTH);
		this.add(this.createRoomListPanel(), BorderLayout.SOUTH);

		this.initEventHandler();
	}

	// 이벤트 핸들러 초기화 함수
	private void initEventHandler() {
		m_roomCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_parent.getContentPane().showCreateRoomDialog();
				m_sound.clickSound();
			}
		});
	}

	// 메인패널 초기화 함수
	public void initMainPanel() {
		m_mainPanel.removeAll();
		m_mainPanel.setOpaque(false);
		m_mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		int height = 0;
		for (int i = 0; i < m_parent.getRoomList().size(); i++) {
			m_mainPanel.add(m_parent.getRoomList().get(i));
			height += 40;
		}
		m_mainPanel.setPreferredSize(new Dimension(600, height));
		m_scrollPane.setViewportView(m_mainPanel);
		this.updateUI();
	}

	// 버튼패널 생성 함수
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();

		panel.setPreferredSize(new Dimension(0, 50));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, -12));

		panel.add(m_roomCreate);

		return panel;
	}

	// 방 목록 생성 함수
	private JPanel createRoomListPanel() {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(m_background, 25, 0, 610, 280, this);
				super.paintComponent(g);
			}
		};

		panel.setPreferredSize(new Dimension(0, 305));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 50));
		panel.add(m_scrollPane);

		return panel;
	}

}
