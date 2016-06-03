package mju.cn.client.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Robot;


public class SCMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	// Attributes
	public static final int FRAME_WIDTH = 1000; // 프레임너비
	public static final int FRAME_HEIGHT = 750; // 프레임높이
	private static final String FRAME_TITLE = "Rain"; // 프레임제목
	private static final Dimension SCR_DIM = Toolkit.getDefaultToolkit()
			.getScreenSize(); // 스크린사이즈
	

	// Components
	private SCContentPane m_rootPanel; // 루트패널
	private JTextField m_loadingText; // 로딩중 표시할 메시지
	private JPanel m_loadingPanel; // 로딩중 표시할 패널

	// Constructor
	public SCMainFrame() {
		super(SCMainFrame.FRAME_TITLE);

		m_rootPanel = new SCContentPane(this);
		this.setResizable(false);
		this.setUndecorated(true);
		this.setLayout(new BorderLayout());
		this.loading();
		this.restoreScreen();
	}

	// Initialization
	public void init(String serverIP) throws AWTException {
		m_rootPanel.init(serverIP);
		this.remove(m_loadingPanel);
		this.add(m_rootPanel, BorderLayout.CENTER);

		int mdelayTime;
		mdelayTime = 2 * 1000;
		Robot robot = new Robot();
		robot.delay(mdelayTime);
		
		this.validate();
		this.repaint();
	}

	// 로딩화면 표시 함수
	public void loading() {
		m_loadingText = new JTextField();
		m_loadingPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			private Image m_bg = Toolkit.getDefaultToolkit().getImage(
					"images/loading_kakao.png");

			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(m_bg, 0, 0, 1000, 750, this);
				super.paintComponent(g);
			}
		};

		m_loadingText.setOpaque(false);
		m_loadingText.setEditable(false);
		m_loadingText.setEnabled(false);
		m_loadingText.setBorder(null);
		m_loadingText.setFont(new Font("돋움", Font.BOLD, 30));
		m_loadingText.setHorizontalAlignment(JTextField.CENTER);
		m_loadingText.setPreferredSize(new Dimension(0, 50));

		m_loadingPanel.setOpaque(false);
		m_loadingPanel.setPreferredSize(new Dimension(400, 300));
		m_loadingPanel.setLayout(new BorderLayout());
		m_loadingPanel.add(m_loadingText, BorderLayout.SOUTH);

		this.add(m_loadingPanel);
		this.setSize(1000, 750);
		this.setVisible(true);
	}

	// 패널 표시 함수
	public void viewPanel(String panelName) {
		this.m_rootPanel.viewPanel(panelName);
	}

	// 패널 추가 함수
	public void addPanel(Component comp, String panelName) {
		this.m_rootPanel.addPanel(comp, panelName);
	}

	// 화면복구 함수
	public void restoreScreen() {
		this.setSize(SCMainFrame.FRAME_WIDTH, SCMainFrame.FRAME_HEIGHT);
		this.setLocation(SCR_DIM.width / 2 - SCMainFrame.FRAME_WIDTH / 2,
				SCR_DIM.height / 2 - SCMainFrame.FRAME_HEIGHT / 2);
		m_rootPanel.updateUI();
	}

	// Getters and Setters
	public SCContentPane getChild() {
		return m_rootPanel;
	}

	public JTextField getLoadingText() {
		return m_loadingText;
	}

	public void setLoadingText(JTextField mLoadingText) {
		m_loadingText = mLoadingText;
	}
}
