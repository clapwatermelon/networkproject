package mju.cn.client.gui.panel;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import mju.cn.client.controller.SCJoinController;
import mju.cn.client.gui.SCContentPane;
import mju.cn.client.gui.SCGUIConstant;
import mju.cn.client.gui.item.SCAvatar;
import mju.cn.client.gui.item.SCSound;


public class SCJoinPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Attributes
	private Image m_background; // 배경이미지

	// Components
	private LinkedList<SCAvatar> m_avatarImage; // 아바타이미지 리스트
	private JPanel m_avatarPanel; // 아바타패널
	private JButton m_okButton; // 가입OK버튼
	private JButton m_cancelButton; // 취소버튼
	private JButton m_sliderLeftButton; // 아바타왼쪽선택버튼
	private JButton m_sliderRightButton; // 아바타오른쪽선택버튼
	private JTextField m_idField; // 아이디필드
	private JPasswordField m_pwField; // 패스워드필드
	private JPasswordField m_pwField2; // 패스워드확인필드
	private JTextField m_nameField; // 이름필드
	private String m_avatarName; // 선택된아바타식별자
	private SCJoinController m_joinController; // 조인컨트롤러
	private SCContentPane m_parent; // 상위패널
	private SCSound m_sound;

	// Constructor
	public SCJoinPanel(SCContentPane parent) {
		super();
		m_parent = parent;
		m_background = Toolkit.getDefaultToolkit().getImage(
				"images/section.png");


		m_avatarImage = new LinkedList<SCAvatar>();
		for (String avatarName : SCGUIConstant.AVATAR_NAME) {
			Image avImg = Toolkit.getDefaultToolkit().getImage(
					"character/" + avatarName + ".png");
			SCAvatar avatar = new SCAvatar(avImg, avatarName, this);
			m_avatarImage.add(avatar);
		}
		m_okButton = new JButton("가입");
		m_cancelButton = new JButton("취소");
		m_sliderLeftButton = new JButton();
		m_sliderRightButton = new JButton();
		m_idField = new JTextField();
		m_pwField = new JPasswordField();
		m_pwField2 = new JPasswordField();
		m_nameField = new JTextField();
		m_sound = new SCSound();
		m_avatarName = SCGUIConstant.AVATAR_NAME[0];
	}

	// Initialization
	public void init(String serverIp) {
		this.initButton(m_okButton);
		this.initButton(m_cancelButton);
		this.initSliderButton(m_sliderLeftButton, "left");
		this.initSliderButton(m_sliderRightButton, "right");

		this.setLayout(new BorderLayout());
		this.add(this.createMainPanel(), BorderLayout.CENTER);
		this.setOpaque(false);

		m_joinController = new SCJoinController(serverIp, this);
		m_joinController.start();

		this.initEventHandler();
	}

	// 이벤트핸들러 초기화 함수
		private void initEventHandler() {
			m_okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String id = m_idField.getText();
					String pw = new String(m_pwField.getPassword());
					String pw2 = new String(m_pwField2.getPassword());
					String name = m_nameField.getText();
					m_avatarName = m_avatarImage.get(1).getAvatarName();

					if (id.trim().equals("")) {
						m_parent.showMessageDialog("아이디를 입력하세요.");
						m_sound.alertSound();
					} else if (pw.trim().equals("")) {
						m_parent.showMessageDialog("암호를 입력하세요.");
						m_sound.alertSound();
					} else if (pw2.trim().equals("")) {
						m_parent.showMessageDialog("암호를 재 입력하세요.");
						m_sound.alertSound();
					} else if (!pw.trim().equals(pw2.trim())) {
						m_parent.showMessageDialog("암호가 일치하지 않습니다.");
						m_sound.alertSound();
					} else if (name.trim().equals("")) {
						m_parent.showMessageDialog("이름을 입력하세요.");
						m_sound.alertSound();
					} else {
						m_joinController.submit(id, pw, name, m_avatarName);
						System.out.println(m_avatarName);

					}
					m_idField.setText("");
					m_pwField.setText("");
					m_pwField2.setText("");
					m_nameField.setText("");
				}
			});

			m_cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String loginPanelName = m_parent.getLoginPanel().getClass()
							.getName();
					m_parent.viewPanel(loginPanelName);
					m_idField.setText("");
					m_pwField.setText("");
					m_pwField2.setText("");
					m_nameField.setText("");
				}
			});
		m_sliderLeftButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SCAvatar moveAvatar = m_avatarImage.removeLast();
				m_avatarImage.addFirst(moveAvatar);
				m_avatarName = m_avatarImage.get(1).getAvatarName();
				m_avatarPanel.repaint();
				m_sound.sliderSound();
			}
		});

		m_sliderRightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SCAvatar moveAvatar = m_avatarImage.removeFirst();
				m_avatarImage.addLast(moveAvatar);
				m_avatarName = m_avatarImage.get(1).getAvatarName();
				m_avatarPanel.repaint();
				m_sound.sliderSound();
			}
		});
	}

	// 버튼 초기화 함수
	private void initButton(JButton btn) {
		btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setIcon(new ImageIcon("images/button_brown.png"));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setHorizontalTextPosition(JButton.CENTER);
		btn.setForeground(Color.white);
		btn.setFont(new Font("돋움", Font.BOLD, 12));
		btn.setPreferredSize(new Dimension(120, 40));
		btn.setMargin(new Insets(10,10,10,10));
	}

	// 아바타 선택 버튼 초기화 함수
	private void initSliderButton(JButton btn, String direction) {
		btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setIcon(new ImageIcon("images/arrow_" + direction + ".png"));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setPreferredSize(new Dimension(40, 140));
	}

	// 메인패널 생성 함수
	private JPanel createMainPanel() {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				g.drawImage(m_background, 200, 0, 500,
						600, this);
				super.paintComponent(g);
			}
		};

		panel.setPreferredSize(new Dimension(300, 580));
		panel.setBorder(new EmptyBorder(new Insets(170, 230, 30,230 )));
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout());
		panel.add(this.createJoinForm(), BorderLayout.CENTER);
		panel.add(this.createBottonPanel(), BorderLayout.SOUTH);
		
		return panel;
	}

	// 버튼패널 생성 함수
	private JPanel createBottonPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, 50));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		panel.add(m_okButton);
		panel.add(m_cancelButton);
		return panel;
	}

	// 회원가입 양식
	private JPanel createJoinForm() {
		JPanel panel = new JPanel();
		Box box = Box.createVerticalBox();
		box.add(this.createInputLine("아이디", m_idField));
		box.add(this.createInputLine("암호", m_pwField));
		box.add(this.createInputLine("암호 재입력", m_pwField2));
		box.add(this.createInputLine("이름", m_nameField));
		Border padding = BorderFactory.createEmptyBorder(0, 0, 0, 60);
		box.setBorder(padding);
		
		
		panel.setOpaque(false);
		
		panel.setLayout(new BorderLayout());
		panel.add(box, BorderLayout.EAST);
		JPanel avatarPanel = new JPanel();
		avatarPanel.setLayout(new BorderLayout());
		avatarPanel.add(this.createAvatarSlider(),BorderLayout.CENTER);
		avatarPanel.add(m_sliderLeftButton, BorderLayout.WEST);
		avatarPanel.add(m_sliderRightButton, BorderLayout.EAST);
		
		panel.add(avatarPanel, BorderLayout.SOUTH);
		return panel;
	}

	// 입력필드 생성 함수
	private JPanel createInputLine(String title, JTextField input) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(title);

		label.setPreferredSize(new Dimension(80, 30));
		label.setFont(new Font("돋움", Font.BOLD, 12));
		label.setForeground(Color.BLACK);
		label.setHorizontalAlignment(JLabel.LEFT);
		
		input.setPreferredSize(new Dimension(220, 30));
		input.setFont(new Font("돋움", Font.BOLD, 20));
		input.setForeground(Color.BLACK);
		input.setOpaque(false);
		input.setHorizontalAlignment(JTextField.CENTER);
		input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
		panel.setOpaque(false);
		panel.add(label);
		panel.add(input);
	
		return panel;
	}

	// 아바타선택화면 생성 함수
	private JPanel createAvatarSlider() {
		m_avatarPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				SCAvatar subLeft = m_avatarImage.get(0);
				SCAvatar center = m_avatarImage.get(1);
				SCAvatar subRight = m_avatarImage.get(2);
				Rectangle rect = new Rectangle();

				int subWidth = (int) (subLeft.getAvatarImage().getWidth(this) * 0.6);
				int subHeight = (int) (subLeft.getAvatarImage().getHeight(this) * 0.6);
				int distance = 50;
				int panelWidth = (int) this.getPreferredSize().getWidth();
				int panelHeight = (int) this.getPreferredSize().getHeight();

				subLeft.drawAvatar(g, (panelWidth / 2) - (subWidth + distance),
						(panelHeight / 2) - subHeight / 2, subWidth, subHeight);
				rect.setRect((panelWidth / 2) - (subWidth + distance) + 3,
						(panelHeight / 2) - subHeight / 2 + 3, subWidth - 5,
						subHeight - 5);
				drawAlphaRect(g, Color.white, rect);

				subRight.drawAvatar(g, (panelWidth / 2) + distance,
						(panelHeight / 2) - subHeight / 2, subWidth, subHeight);
				rect.setRect((panelWidth / 2) + distance + 3, (panelHeight / 2)
						- subHeight / 2 + 3, subWidth - 5, subHeight - 5);
				drawAlphaRect(g, Color.white, rect);

				center.drawAvatar(
						g,
						(panelWidth / 2)
								- (center.getAvatarImage().getWidth(this) / 2),
						(panelHeight / 2)
								- (center.getAvatarImage().getHeight(this) / 2),
						center.getAvatarImage().getWidth(this), center
								.getAvatarImage().getHeight(this));

				super.paintComponent(g);
			}
		};
		m_avatarPanel.setPreferredSize(new Dimension(360, 140));
		m_avatarPanel.setOpaque(false);
		m_avatarPanel.setLayout(new BorderLayout());
		m_avatarPanel.add(m_sliderLeftButton, BorderLayout.WEST);
		m_avatarPanel.add(m_sliderRightButton, BorderLayout.EAST);
		m_avatarPanel.repaint();

		return m_avatarPanel;
	}

	// 투명 사각형 그리기 함수
	public void drawAlphaRect(Graphics g, Color color, Rectangle rect) {
		Graphics2D g2d = (Graphics2D) g;
		Composite oldComposite = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.6f));
		g2d.setColor(color);
		g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
		g2d.setComposite(oldComposite);
	}

	// 배경그리기 함수
	@Override
	public void paintComponent(Graphics g) {
		
		

		super.paintComponent(g);
	}

	public SCContentPane getContentPane() {
		return m_parent;
	}
	public SCSound getSound(){
		return m_sound;
	}
	

}
