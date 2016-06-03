package mju.cn.client.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SCLoadingDialog extends JPanel {
	private static final long serialVersionUID = 1L;

	// Attributes
	private Image m_background; // 배경이미지
	
	// Components
	private JTextField m_message; // 로딩 메시지 필드
	private JLabel m_loading; // 로딩 메시지 라벨
	
	// Constructor
	public SCLoadingDialog(String msg) {
		m_message = new JTextField();
		m_message.setText(msg);

		this.init();
	}

	// Initialization
	private void init() {
		m_loading = new JLabel(new ImageIcon("images/loading.gif"));
		m_loading.setOpaque(false);
		m_loading.setPreferredSize(new Dimension(150, 50));

		m_message.setBorder(null);
		m_message.setBackground(null);
		m_message.setFont(new Font("돋움", Font.BOLD, 15));
		m_message.setHorizontalAlignment(JTextField.CENTER);
		m_message.setEnabled(false);
		m_message.setDisabledTextColor(Color.BLACK);
		m_message.setOpaque(false);

		m_background = Toolkit.getDefaultToolkit().getImage(
				"images/loading_bg.png");

		this.setOpaque(false);
		this.setPreferredSize(new Dimension(320, 230));
		this.setLayout(new BorderLayout());
		this.add(this.createTopPanel(), BorderLayout.CENTER);
		this.add(this.createBottomPanel(), BorderLayout.SOUTH);
	}

	// 위쪽 패널  생성 함수
	private JPanel createTopPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 80));
		panel.add(m_message);
		return panel;
	}

	// 아래쪽 패널  생성 함수
	private JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, 100));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.add(m_loading);
		return panel;
	}

	// 그리기 함수
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(m_background, 0, 0, m_background.getWidth(this),
				m_background.getHeight(this), this);
		super.paintComponent(g);
	}
}
