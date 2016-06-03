package mju.cn.client.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SCMessageDialog extends JPanel {
	private static final long serialVersionUID = 1L;

	// Components
	private JButton m_okay; // OK버튼
	private JTextField m_message; // 표시할 메시지
	private Image m_background; // 배경이미지
	private JFrame m_owner; // 상위 프레임

	// Constructor
	public SCMessageDialog(JFrame owner, String msg) {
		m_okay = new JButton();
		m_message = new JTextField();
		m_owner = owner;
		m_message.setText(msg);

		this.init();
		this.initEventHandler();
	}

	// Initialization
	private void init() {
		m_okay.setContentAreaFilled(false);
		m_okay.setBorder(null);
		m_okay.setIcon(new ImageIcon("images/message_okay.png"));
		m_okay.setCursor(new Cursor(Cursor.HAND_CURSOR));
		m_okay.setSize(126, 27);
		m_okay.setOpaque(false);

		m_message.setBorder(null);
		m_message.setBackground(null);
		m_message.setFont(new Font("돋움", Font.BOLD, 15));
		m_message.setHorizontalAlignment(JTextField.CENTER);
		m_message.setEnabled(false);
		m_message.setDisabledTextColor(Color.BLACK);
		m_message.setOpaque(false);

		m_background = Toolkit.getDefaultToolkit().getImage(
				"images/message_bg.png");

		this.setOpaque(false);
		this.setPreferredSize(new Dimension(310, 230));
		this.setLayout(new BorderLayout());
		this.add(this.createBottomPanel(), BorderLayout.SOUTH);
		this.add(this.createTopPanel(), BorderLayout.CENTER);
	}

	// 이벤트핸들러 초기화 함수
	private void initEventHandler() {
		m_okay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_owner.getGlassPane().setVisible(false);
			}
		});
	}

	// 위쪽패널 생성 함수
	private JPanel createTopPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100));
		panel.add(m_message);
		return panel;
	}

	// 아래쪽패널 생성 함수
	private JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(0, m_okay.getHeight() + 30));
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));
		panel.add(m_okay);
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
