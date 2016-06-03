package mju.cn.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import mju.cn.client.controller.SCLoginController;
import mju.cn.client.gui.SCContentPane;
import mju.cn.client.gui.btns.SCRectButton;
import mju.cn.client.gui.item.SCSound;


public class SCLoginPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Attributes
	private Image m_welcomeImage; // 환영메시지
	private Image m_rightImage; // 그냥 장식용그림

	// Components
	private JTextField m_idField; // 아이디
	private JPasswordField m_pwField; // 패스워드
	private SCRectButton m_loginButton; // 로그인버튼
	private SCRectButton m_joinButton; // 회원가입버튼
	private SCContentPane m_parent; // 상위패널
	

	private SCLoginController m_loginController; // 로그인 컨트롤러
	
	private SCSound m_sound;

	// Constructor
	public SCLoginPanel(SCContentPane parent) {
		super();
		m_parent = parent;
		
		this.m_rightImage = Toolkit.getDefaultToolkit().getImage(
				"images/login_image.png");
				

		this.m_idField = new JTextField();
		this.m_pwField = new JPasswordField();
		this.m_loginButton = new SCRectButton("로그인");
		this.m_joinButton = new SCRectButton("회원가입");
		this.m_sound = new SCSound();
	}
	

	// Initialization
	public void init(String serverIP) {
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		Box inputBox = Box.createVerticalBox();
		Box btnBox = Box.createHorizontalBox();
		bottomPanel.add(inputBox);
		bottomPanel.add(btnBox);

		this.initTextField(m_idField);
		this.initTextField(m_pwField);

		inputBox.add(this.m_idField);
		inputBox.add(Box.createVerticalStrut(10));
		inputBox.add(this.m_pwField);

		btnBox.add(Box.createHorizontalStrut(20));
		btnBox.add(this.m_loginButton);
		btnBox.add(Box.createHorizontalStrut(10));
		btnBox.add(m_joinButton);

		topPanel.setOpaque(false);
		bottomPanel.setOpaque(false);

		this.setLayout(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension(0, 150));
		this.add(bottomPanel, BorderLayout.SOUTH);

		this.setOpaque(false);
		m_loginController = new SCLoginController(serverIP, this);
		m_loginController.start();

		this.initEventHandler();

		m_idField.requestFocus();
		
		playSound("playbgm");
	}
	
	//소리실행모듈함수
	public void playSound(String type){
		switch(type){
		case "playbgm":m_sound.loginSound(); break;
		case "stopbgm":m_sound.stoploginSound(); break;
		case "success":m_sound.enterSound(); break;
		case "fail":m_sound.alertSound(); break;
		}
		try{Thread.sleep(500);}catch(Exception e){}
	}
	
	
	// 이벤트 핸들러 초기화 함수
	private void initEventHandler() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = m_idField.getText();
				String pw = new String(m_pwField.getPassword());
				if (id.trim().equals("")) {
					m_parent.showMessageDialog("아이디를 입력하세요.");
					m_sound.alertSound();
					return;
				}

				if (pw.trim().equals("")) {
					m_parent.showMessageDialog("암호를 입력하세요.");
					m_sound.alertSound();
					return;
				}
				m_loginController.login(id, pw);

			}
		};

		m_joinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String joinPanelName = m_parent.getJoinPanel().getClass()
						.getName();
				m_sound.clickSound();
				m_parent.viewPanel(joinPanelName);
				m_idField.setText("");
				m_pwField.setText("");
				
			}
		});

		m_loginButton.addActionListener(listener);
		m_idField.addActionListener(listener);
		m_pwField.addActionListener(listener);
	}
	// 텍스트 필드 초기화 함수
	private void initTextField(JTextField txt) {
		Font font = new Font("돋움", Font.PLAIN, 30);
		txt.setFont(font);
		txt.setBorder(null);
		txt.setBackground(Color.WHITE);
		txt.setHorizontalAlignment(JTextField.CENTER);
		txt.setPreferredSize(new Dimension(230, 40));
	}

	// 그리기 함수
	@Override
	public void paintComponent(Graphics g) {
			
		g.drawImage(this.m_rightImage, 300, 70,
				this.m_rightImage.getWidth(this),
				this.m_rightImage.getHeight(this), this);

		super.paintComponent(g);
	}

	public SCContentPane getContentPane() {
		return m_parent;
	}
}
