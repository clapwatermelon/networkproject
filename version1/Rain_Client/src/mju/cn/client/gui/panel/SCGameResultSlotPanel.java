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



public class SCGameResultSlotPanel extends JPanel{
	// Attributes
	public static final int SLOT_LEFT_ALIGNMENT = 0x00aa;
	public static final int SLOT_RIGHT_ALIGNMENT = 0x00bb;
	private String m_id; 
	private int m_score;
	private Image m_background;
	private SCAvatar m_avatar;
	private int m_alignment;
	// Components
	private SCGameResultPanel m_parent; 

		// Constructor
		public SCGameResultSlotPanel(String id,  String avatar, int score,	SCGameResultPanel parent) {
			m_parent = parent;
			m_id = id;
			
			m_avatar = new SCAvatar(Toolkit.getDefaultToolkit().getImage(
					"character/game_" + avatar + ".png"), avatar, this);
				m_background = Toolkit.getDefaultToolkit().getImage("images/Result_Player_Box.png");
		
			m_score = score;
			m_alignment = SLOT_LEFT_ALIGNMENT;

			this.init();
		}
		// Initialization
		private void init() {
			Dimension size = new Dimension(492, 43);
			this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
			this.setPreferredSize(size);
			this.setSize(size);
			this.setOpaque(false);

		}
		

		@Override
		protected void paintComponent(Graphics g) {
			int bgWidth = 492;
			int bgHeight = 43;
			int hgap = 0;
			int vgap = 0;

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			Font oldFont = g2d.getFont();
			g2d.setFont(new Font("ï¿½ï¿½ï¿½ï¿½", Font.BOLD, 14));
			g2d.setColor(Color.BLACK);

			if (m_alignment == SLOT_LEFT_ALIGNMENT) {
				this.drawUserInfo(g2d, hgap, vgap, bgWidth, bgHeight);
			} else if (m_alignment == SLOT_RIGHT_ALIGNMENT) {
				this.drawUserInfo(g2d, hgap, vgap, bgWidth, bgHeight);
			}
			g2d.setFont(oldFont);

			super.paintComponent(g);
		}


		private void drawUserInfo(Graphics g2d, int hgap, int vgap, int bgWidth, int bgHeight) {
			g2d.drawImage(m_background, hgap, vgap, bgWidth, bgHeight, this); // slot_bg
										
			m_avatar.drawAvatar(g2d, hgap + 20, vgap + 2, 40, 40);
			if (m_parent.getHostId().equals(m_id)) {
				g2d.setColor(Color.black);
				g2d.drawString(m_id + "(¹æÀå)", hgap + 75, vgap + 28);
				g2d.setColor(Color.black);
			} else {
				g2d.setColor(Color.black);
				g2d.drawString(m_id, hgap + 95, vgap + 28);
				g2d.setColor(Color.black);
			}
			g2d.drawString("" + m_score, hgap + 390, vgap + 27);
		}

}
