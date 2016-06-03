package mju.cn.client.gui.item;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JPanel;

import mju.cn.client.gui.panel.SCLobbyPanel;


public class SCPlayerListItem extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// Attributes
	private String m_id; // 사용자 id
	private String m_name; // 사용자 이름
	private SCAvatar m_avatar; // 아바타
	private int m_exp; // 총 경험치
	private Image m_background; // 배경화면

	// Constructor
	public SCPlayerListItem(SCLobbyPanel parent, String id, String name,
			int exp, SCAvatar avatar) {
		m_id = id;
		m_name = name;
		m_exp = exp;
		m_avatar = avatar;
		m_background = Toolkit.getDefaultToolkit().getImage(
				"images/list_bg.png");
		this.init();
	}

	// Initialization
	private void init() {
		this.setPreferredSize(new Dimension(210, 40));
		this.setOpaque(false);

	}

	// 그리기 함수
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(m_background, 0, 0, 210, 40, this);
		m_avatar.drawAvatar(g, 10, 5, 40, 37);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Font oldFont = g2d.getFont();
		g2d.setFont(new Font("돋움", Font.BOLD, 14));
		g2d.setColor(Color.GRAY);
		g2d.drawString(m_name + "(" + m_id + ")", 90, 25);
		g2d.setFont(oldFont);
		super.paintComponent(g);
	}

	// Getters and Setters
	public String getId() {
		return m_id;
	}

	public void setId(String mId) {
		m_id = mId;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String mName) {
		m_name = mName;
	}

	public SCAvatar getAvatar() {
		return m_avatar;
	}

	public void setAvatar(SCAvatar mAvatar) {
		m_avatar = mAvatar;
	}

	public int getExp() {
		return m_exp;
	}

	public void setExp(int exp) {
		m_exp = exp;
	}

}