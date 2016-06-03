package mju.cn.client.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

import javax.swing.JPanel;
import mju.cn.client.gui.btns.SCExitButton;
import mju.cn.client.gui.dialog.SCCreateRoomDialog;
import mju.cn.client.gui.dialog.SCExitDialog;
import mju.cn.client.gui.dialog.SCLoadingDialog;
import mju.cn.client.gui.dialog.SCMessageDialog;
import mju.cn.client.gui.panel.SCGameResultPanel;
import mju.cn.client.gui.panel.SCGameRoomPanel;
import mju.cn.client.gui.panel.SCJoinPanel;
import mju.cn.client.gui.panel.SCLobbyPanel;
import mju.cn.client.gui.panel.SCLoginPanel;

public class SCContentPane extends JPanel {
	private static final long serialVersionUID = 1L;

	// Attributes
	private CardLayout m_cardLayout; // 카드레이아웃
	private SCMainFrame m_frame; // 메인프레임
	private JPanel m_topPanel; // 위쪽패널
	private JPanel m_content; // 내용
	private Image m_titleImage; // 타이틀자리에 표시할 그림
	private boolean m_isFullScreen = false; // 풀스크린

	// Components
	private SCExitButton m_exitButton; // X버튼
	private SCGlassPane m_glassPane; // GLASSPANE
	private SCLoginPanel m_loginPanel; // 로그인화면
	private SCJoinPanel m_joinPanel; // 회원가입화면
	private SCLobbyPanel m_lobbyPanel; // 로비 화면
	private SCGameRoomPanel m_gameRoomPanel; // 방 화면

	// Constructor
	public SCContentPane(SCMainFrame parent) {
		m_frame = parent;

		m_content = new JPanel();
		m_topPanel = new JPanel();
		m_exitButton = new SCExitButton();
		m_glassPane = new SCGlassPane();

		m_loginPanel = new SCLoginPanel(this);
		m_joinPanel = new SCJoinPanel(this);
		m_lobbyPanel = new SCLobbyPanel(this);
		m_gameRoomPanel = new SCGameRoomPanel(this);
	}

	// Initialization
	public void init(String serverIP) {
		m_glassPane.init();
		m_frame.getLoadingText().setText("글라스 페인 초기화...");
		m_loginPanel.init(serverIP);
		m_frame.getLoadingText().setText("로그인 패널 초기화...");
		m_joinPanel.init(serverIP);
		m_frame.getLoadingText().setText("회원가입 패널 초기화...");
		m_lobbyPanel.init(serverIP);
		m_frame.getLoadingText().setText("대기룸 초기화...");
		m_gameRoomPanel.init(serverIP);
		m_frame.getLoadingText().setText("게임룸 초기화...");

		m_titleImage = Toolkit.getDefaultToolkit().getImage("images/Team_Logo.png");

		m_content.setOpaque(false);
		m_topPanel.setOpaque(false);
		m_topPanel.setPreferredSize(new Dimension(0, 100));

		m_topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		m_topPanel.add(this.m_exitButton);

		m_cardLayout = new CardLayout();
		m_content.setLayout(this.m_cardLayout);
		m_content.add(m_loginPanel, m_loginPanel.getClass().getName());
		m_content.add(m_joinPanel, m_joinPanel.getClass().getName());
		m_content.add(m_lobbyPanel, m_lobbyPanel.getClass().getName());
		m_content.add(m_gameRoomPanel, m_gameRoomPanel.getClass().getName());
		
		m_frame.setGlassPane(m_glassPane);

		this.setPreferredSize(new Dimension(SCMainFrame.FRAME_WIDTH,
				SCMainFrame.FRAME_HEIGHT));
		this.setLayout(new BorderLayout());
		this.add(m_topPanel, BorderLayout.NORTH);
		this.add(createEmptyPanel(0, 50), BorderLayout.SOUTH);
		this.add(createEmptyPanel(50, 0), BorderLayout.EAST);
		this.add(createEmptyPanel(50, 0), BorderLayout.WEST);
		this.add(m_content, BorderLayout.CENTER);
		this.setOpaque(false);

		this.initEventHandler();
	}

	// 패널추가 함수 함수
	public void addPanel(Component comp, String panelName) {
		this.m_content.add(comp, panelName);
	}

	// 빈 패널 생성 함수
	private JPanel createEmptyPanel(int width, int height) {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, height));
		panel.setOpaque(false);
		return panel;
	}

	// 이벤트핸들러 초기화 함수
	public void initEventHandler() {
		this.m_exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showExitDialog();
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			Point point;

			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
				if (m_isFullScreen) {

				} else {
					if (point == null) {
						point = e.getPoint();
					}
					m_frame.setLocation(e.getLocationOnScreen().x - point.x,
							e.getLocationOnScreen().y - point.y);
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				point = null;
			}
		});
	}

	// 배경 그리기 함수
	public void drawBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();

		GradientPaint gradient = new GradientPaint(0, 0, Color.YELLOW, 0,
				this.getHeight(), Color.YELLOW);

		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

		g2d.setPaint(oldPaint);

		g2d.drawImage(this.m_titleImage, 35, 10,
				this.m_titleImage.getWidth(this),
				this.m_titleImage.getHeight(this), this);
	}

	// 컴포넌트 그리기 함수
	@Override
	public void paintComponent(Graphics g) {
		this.drawBackground(g);
		super.paintComponent(g);
	}

	// 패널 표시 함수
	public void viewPanel(String panelName) {
		this.m_cardLayout.show(this.m_content, panelName);
	}

	// 메시지 다이어로그 표시 함수
	public void showMessageDialog(String msg) {
		m_glassPane.setComponent(new SCMessageDialog(m_frame, msg));
		m_frame.getGlassPane().setVisible(true);
	}

	// 종료 다이어로그 표시 함수
	public void showExitDialog() {
		m_glassPane.setComponent(new SCExitDialog(m_frame));
		m_frame.getGlassPane().setVisible(true);
	}

	// 로딩화면 다이어로그 표시 함수
	public void showLoadingDialog(String msg) {
		m_glassPane.setComponent(new SCLoadingDialog(msg));
		m_frame.getGlassPane().setVisible(true);
	}

	// 방 만들기 다이어로그 표시 함수
	public void showCreateRoomDialog() {
		m_glassPane.setComponent(new SCCreateRoomDialog(m_frame));
		m_frame.getGlassPane().setVisible(true);
	}

	// 다이어로그 숨기기 함수
	public void hideDialog() {
		m_frame.getGlassPane().setVisible(false);
	}
	
	//* 게임결과창 띄우는 함수 *//
	public void showGameResultPanel(String m_userId,String m_HostId,Vector<Object[]> playerList,boolean isWinner){
		m_glassPane.setComponent(new SCGameResultPanel( m_frame,m_userId, m_HostId, playerList,isWinner));
		m_frame.getGlassPane().setVisible(true);


	}

	// Getters and Setters
	public SCLoginPanel getLoginPanel() {
		return m_loginPanel;
	}

	public SCJoinPanel getJoinPanel() {
		return m_joinPanel;
	}

	public SCLobbyPanel getLobbyPanel() {
		return m_lobbyPanel;
	}

	public SCGameRoomPanel getGameRoomPanel() {
		return m_gameRoomPanel;
	}

}