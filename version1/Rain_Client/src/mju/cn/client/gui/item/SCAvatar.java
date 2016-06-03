package mju.cn.client.gui.item;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;


public class SCAvatar {
	// Attributes
	private Image m_avatarImage; // 아바타 이미지
	private String m_avatarName; // 아바타 식별자
	
	// Components
	private JPanel m_owner; // 상위 패널

	// Constructor
	public SCAvatar(Image img, String name, JPanel owner) {
		m_avatarImage = img;
		m_avatarName = name;
		m_owner = owner;
	}

	// 아바타 그리기 함수
	public void drawAvatar(Graphics g, int x, int y, int width, int height) {
		g.drawImage(m_avatarImage, x, y, width, height, m_owner);
	}

	// Getters and Setters
	public String getAvatarName() {
		return m_avatarName;
	}

	public Image getAvatarImage() {
		return m_avatarImage;
	}

}
