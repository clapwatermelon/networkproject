package mju.cn.client.gui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mju.cn.client.gui.item.SCAvatar;


public class SCSlotPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// Attributes
	public static final int SLOT_LEFT_ALIGNMENT = 0x00aa;
	public static final int SLOT_RIGHT_ALIGNMENT = 0x00bb;
	private String m_id; // 아이디
	private int m_score; // 점수
	private String m_name; // 이름
	private Image m_background; // 배경
	private SCAvatar m_avatar; // 사용자 아바타
	private int m_alignment; // 배치 순서

	// Components
	private SCGameRoomPanel m_parent; // 상위 패널

	// Constructor
	public SCSlotPanel(String id, String name, String avatar,
			SCGameRoomPanel parent) {
		m_parent = parent;
		m_id = id;
		m_name = name;
		m_avatar = new SCAvatar(Toolkit.getDefaultToolkit().getImage(
				"character/game_" + avatar + ".png"), avatar, this);
		if (m_parent.getUserId().equals(id)) {
			m_background = Toolkit.getDefaultToolkit().getImage(
					"images/Player_Box.png");
		} else {
			m_background = Toolkit.getDefaultToolkit().getImage(
					"images/Player_Box.png");
		}
		m_score = 0;
		m_alignment = SLOT_LEFT_ALIGNMENT;

		this.init();
	}

	// Initialization
	private void init() {
		Dimension size = new Dimension(300, 43);
		this.setPreferredSize(size);
		this.setSize(size);
		this.setOpaque(false);

	}

	// 그리기 함수
	@Override
	protected void paintComponent(Graphics g) {
		int bgWidth = 300;
		int bgHeight = 43;
		int hgap = 0;
		int vgap = 0;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Font oldFont = g2d.getFont();
		g2d.setFont(new Font("돋움", Font.BOLD, 14));
		g2d.setColor(Color.BLACK);

		if (m_alignment == SLOT_LEFT_ALIGNMENT) {
			this.drawUserInfo(g2d, hgap, vgap, bgWidth, bgHeight);
		} else if (m_alignment == SLOT_RIGHT_ALIGNMENT) {
			this.drawUserInfo(g2d, hgap, vgap, bgWidth, bgHeight);
		}
		g2d.setFont(oldFont);

		super.paintComponent(g);
	}

	// 사용자 정보표시 함수
	private void drawUserInfo(Graphics g2d, int hgap, int vgap, int bgWidth,
			int bgHeight) {
		g2d.drawImage(m_background, hgap, vgap, bgWidth, bgHeight, this); // slot_bg
																			// 범위
		m_avatar.drawAvatar(g2d, hgap + 20, vgap + 2, 40, 37);
		if (m_parent.getHostId().equals(m_id)) {
			g2d.setColor(Color.black);
			g2d.drawString(m_id + "(방장)", hgap + 75, vgap + 28);
			g2d.setColor(Color.black);
		} else {
			g2d.setColor(Color.black);
			g2d.drawString(m_id, hgap + 95, vgap + 28);
			g2d.setColor(Color.black);
		}
		g2d.drawString("" + m_score, hgap + 230, vgap + 27);
	}

	public void addScore(int score) {
		m_score += score;
	}

	public void subScore(int score) {
		m_score -= score;
	}
	
	public void changeDead(){
		m_background = Toolkit.getDefaultToolkit().getImage(
				"images/Player_Box_Dead.png");
	}
	
	public void rollBack(){
		m_background = Toolkit.getDefaultToolkit().getImage(
				"images/Player_Box.png");
		m_score = 0;
	}

	// Getters and Setters
	public String getId() {
		return m_id;
	}

	public void setAlignment(int alignment) {
		m_alignment = alignment;
	}

	public int getScore() {
		return m_score;
	}

	public void setScore(int score) {
		this.m_score = score;
	}
	
	public SCAvatar getAvatar() {
		return m_avatar;
	}
}