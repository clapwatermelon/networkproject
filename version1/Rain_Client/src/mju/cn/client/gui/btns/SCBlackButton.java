package mju.cn.client.gui.btns;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;


public class SCBlackButton extends JButton {
	private static final long serialVersionUID = 1L;

	// Constructor
	public SCBlackButton(String title) {
		super(title);
		this.setContentAreaFilled(false);
		this.setBorder(null);
		this.setIcon(new ImageIcon("images/blackbutton_rollout.png"));
		this.setRolloverIcon(new ImageIcon("images/blackbutton_rollover.png"));
		this.setPressedIcon(new ImageIcon("images/blackbutton_press.png"));
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setHorizontalTextPosition(CENTER);
		this.setForeground(Color.WHITE);
		this.setFont(new Font("돋움", Font.BOLD, 15));
		this.setSize(226, 42);
	}

}
