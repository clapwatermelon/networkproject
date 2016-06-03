package mju.cn.client.gui.item;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import mju.cn.client.gui.panel.SCLobbyPanel;


public class SCRoomListItem extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// Attributes
	private int m_roomNumber; // 방번호
	private int m_playerNumber; // 플레이어숫자
	private int m_maxPlayerNumber; // 최대플레이어숫자
	private boolean m_isStart; // 시작했는지여부
	private String m_roomName; // 방제목
	private Image m_background; // 배경이미지
	private Image m_ok; // 방만들기이미지
	private Image m_no; // 취소 이미지
	private boolean m_isMouseEntered; // 마우스Over
	
	// Components
	private SCLobbyPanel m_parent; // 상위패널

	// Constructor
	public SCRoomListItem(SCLobbyPanel parent, int roomNumber,
			int playerNumber, String roomName, int maxPlayerNumber,
			boolean isStart) {
		m_parent = parent;
		m_roomNumber = roomNumber;
		m_playerNumber = playerNumber;
		m_roomName = roomName;
		m_maxPlayerNumber = maxPlayerNumber;
		m_isStart = isStart;
		m_background = Toolkit.getDefaultToolkit().getImage(
				"images/room_list_bg.png");
		m_ok = Toolkit.getDefaultToolkit().getImage("images/accept.png");
		m_no = Toolkit.getDefaultToolkit().getImage("images/deny.png");
		this.init();
	}

	// Initialization
	private void init() {
		this.setPreferredSize(new Dimension(600, 40));
		this.setSize(new Dimension(600, 40));
		this.setOpaque(false);

		this.initEventHandler();
	}

	// 이벤트핸들러 초기화 함수
	private void initEventHandler() {
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if ((m_playerNumber < m_maxPlayerNumber) && (!m_isStart)) {
					m_parent.getContentPane().getGameRoomPanel()
							.getGameController().getPlayerList(m_roomNumber);
					m_parent.getRoomListController().enterRoom(
							m_parent.getUserId(), m_roomNumber);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				m_isMouseEntered = true;
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				updateUI();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				m_isMouseEntered = false;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				updateUI();
			}

		});
	}

	// 그리기 함수
	@Override
	protected void paintComponent(Graphics g) {
		if (m_isMouseEntered == true) {
			g.setColor(new Color(230, 230, 230));
			g.fillRect(0, 0, 600, 40);
		}

		g.drawImage(m_background, 0, 0, 600, 40, this);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Font oldFont = g2d.getFont();
		g2d.setFont(new Font("돋움", Font.BOLD, 14));
		g2d.setColor(Color.black);
		g2d.drawString(m_roomNumber + "번방", 20, 25);
		g2d.drawString(m_roomName, 150, 25);
		g2d.drawString(m_playerNumber + "/" + m_maxPlayerNumber, 350, 25);

		Image img;
		if (m_playerNumber == m_maxPlayerNumber || (m_isStart)) {
			img = m_no;
		} else {
			img = m_ok;
		}
		g2d.drawImage(img, 520, 10, 21, 21, this);

		g2d.setFont(oldFont);

		super.paintComponent(g);
	}

}
