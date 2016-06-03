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


public class SCExitDialog extends JPanel {
	private static final long serialVersionUID = 1L;

	// Attributes
	private Image m_background; // 배경 이미지

	// Components
	private JButton m_yes; // 나가기 버튼
	private JButton m_no; // 나가기취소 버튼
	private JTextField m_message; // 메시지 필드
	private JFrame m_owner; // 상위 프레임

	// Constructor
	public SCExitDialog(JFrame owner) {
		m_yes = new JButton();
		m_no = new JButton();
		m_message = new JTextField();
		m_owner = owner;
		m_message.setText("정말 종료하시겠습니까?");

		this.init();
		this.initEventHandler();
	}

	//  Initialization
	private void init() {
		m_yes.setContentAreaFilled(false);
		m_yes.setBorder(null);
		m_yes.setIcon(new ImageIcon("images/exit_yes.png"));
		m_yes.setCursor(new Cursor(Cursor.HAND_CURSOR));
		m_yes.setSize(126, 27);
		m_yes.setOpaque(false);

		m_no.setContentAreaFilled(false);
		m_no.setBorder(null);
		m_no.setIcon(new ImageIcon("images/exit_no.png"));
		m_no.setCursor(new Cursor(Cursor.HAND_CURSOR));
		m_no.setSize(126, 27);
		m_no.setOpaque(false);

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
		this.setPreferredSize(new Dimension(320, 230));
		this.setLayout(new BorderLayout());
		this.add(this.createBottomPanel(), BorderLayout.SOUTH);
		this.add(this.createTopPanel(), BorderLayout.CENTER);
	}

	// 이벤트핸들러 초기화 함수
	private void initEventHandler() {
		m_yes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		m_no.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_owner.getGlassPane().setVisible(false);
			}
		});
	}

	// 위쪽 패널 생성 함수
	private JPanel createTopPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100));
		panel.add(m_message);
		return panel;
	}

	// 아래쪽 패널 생성 함수
	private JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(0, m_yes.getHeight() + 30));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.add(m_yes);
		panel.add(m_no);
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
