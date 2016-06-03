package mju.cn.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JPanel;

import mju.cn.client.controller.SCChatController;
import mju.cn.client.controller.SCPlayerListController;
import mju.cn.client.controller.SCRoomListController;
import mju.cn.client.gui.SCContentPane;
import mju.cn.client.gui.item.SCAvatar;
import mju.cn.client.gui.item.SCPlayerListItem;
import mju.cn.client.gui.item.SCRoomListItem;
import mju.cn.client.gui.item.SCSound;


public class SCLobbyPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	// Attributes
	private String m_userId; // 사용자 아이디
	private String m_name; // 사용자이름
	private String m_avatarName; // 아바타이름
	private int m_exp; // 총 경험치
	private Vector<SCPlayerListItem> m_playerList; // 플레이어목록
	private Vector<SCRoomListItem> m_roomList; // 방목록
	private Image m_userBackground; // 유저 배경

	// Components
	private SCContentPane m_parent; // 상위패널
	private SCChatPanel m_chatPanel; // 채팅패널
	private SCRoomListPanel m_roomPanel; // 룸목록패널
	private SCAvatar m_avatar; // 아바타
	private SCPlayerListPanel m_playerListPanel; // 플레이어목록패널
	private SCChatController m_chatController; // 채팅컨트롤러
	private SCPlayerListController m_playerListController; // 플레이어리스트컨트롤러
	private SCRoomListController m_roomListController; // 룸리스트컨트롤러

	private SCSound m_sound; // 사운드

	// Constructor
	public SCLobbyPanel(SCContentPane parent) {
		super();
		m_parent = parent;
		m_userBackground = Toolkit.getDefaultToolkit().getImage(
				"images/user_info.png");	// 사용자 정보 배경 설정
		m_playerList = new Vector<SCPlayerListItem>();
		m_roomList = new Vector<SCRoomListItem>();
		m_chatPanel = new SCChatPanel(this);
		m_playerListPanel = new SCPlayerListPanel(this); // 플레이어 리스트를 표시할 panel
		m_roomPanel = new SCRoomListPanel(this); // 대기방 리스트를 표시할 panel
	    m_sound = new SCSound();

	}

	// Initialization
	public void init(String serverIP) {
		this.setLayout(new BorderLayout());
		this.setOpaque(false);

		this.add(this.createWestPanel(), BorderLayout.EAST);
		this.add(this.createEastPanel(), BorderLayout.WEST);

		m_chatController = new SCChatController(serverIP, this);
		m_chatController.start();
		m_playerListController = new SCPlayerListController(serverIP, this);
		m_playerListController.start();
		m_roomListController = new SCRoomListController(serverIP, this);
		m_roomListController.start();

	}

	// 매니저초기화 함수
	public void initManager(String uid) {
		m_userId = uid;
		m_chatController.init(m_userId);
		m_playerListController.init(m_userId);
		m_roomListController.init(m_userId);
		m_parent.getGameRoomPanel().initManager(uid);
	}

	// 왼쪽 패널그룹 생성 함수
	private JPanel createWestPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(655, 0));
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout());

		panel.add(m_chatPanel, BorderLayout.SOUTH);
		panel.add(m_roomPanel, BorderLayout.NORTH);

		return panel;
	}

	// 오른쪽 패널 생성 함수
	private JPanel createEastPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(245, 0));
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout());

		panel.add(m_playerListPanel, BorderLayout.SOUTH);
		panel.add(this.createUserInfo(), BorderLayout.NORTH);

		return panel;
	}

	// 유저정보 생성 함수
	private JPanel createUserInfo() {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {					//이미지크기에맞춰서 그린다.
				g.drawImage(m_userBackground, 0, 50, this.getWidth(),
						200, this);
				if(m_avatarName.equals("apeach")||m_avatarName.equals("jay_g")){
				m_avatar.drawAvatar(g, 10, 100, 101, 117);
				}else if(m_avatarName.equals("frodo")){
					m_avatar.drawAvatar(g, 10, 100, 78, 100);
				}else if(m_avatarName.equals("lion")){
					m_avatar.drawAvatar(g, 10, 100, 94, 117);
				}else if(m_avatarName.equals("neo")){
						m_avatar.drawAvatar(g, 10, 100, 89, 117);
				}else if((m_avatarName.equals("tube"))){
					m_avatar.drawAvatar(g, 10, 100, 88, 110);
				}else if((m_avatarName.equals("muzi"))){
					m_avatar.drawAvatar(g, 10, 100, 88, 117);

				}

				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				Font oldFont = g2d.getFont();
				g2d.setFont(new Font("돋움", Font.BOLD, 17));
				g2d.setColor(Color.BLACK);
				g2d.drawString(m_userId, 140, 150);
				g2d.drawString(" " , 140, 150);
				g2d.setColor(Color.BLACK);
				g2d.drawString(m_name, 140, 210);
				g2d.setFont(oldFont);
				super.paintComponent(g);
			}
		};
		panel.setPreferredSize(new Dimension(245, 245));
		panel.setOpaque(false);

		return panel;
	}

	// 유저정보 초기화 함수
	public void initUserInfo(String id, String name, String avatar, int exp) {
		m_userId = id;
		m_name = name;
		m_avatarName = avatar;
		m_exp = exp;
		m_avatar = new SCAvatar(Toolkit.getDefaultToolkit().getImage(
				"character/" + m_avatarName + ".png"), m_avatarName, this);
	}

	// 플레이어 목록 추가 함수
	public void addPlayerList(String id, String name, String avatar, int exp) {
		m_playerList.add(new SCPlayerListItem(this, id, name, exp,
				new SCAvatar(Toolkit.getDefaultToolkit().getImage(
						"character/game_" + avatar + ".png"), avatar, this)));
	}

	// 방목록 추가 함수
	public void addRoomList(int roomNumber, int playerNumber, String roomName,
			int maxPlayerNumber, boolean isStart) {

		m_roomList.add(new SCRoomListItem(this, roomNumber, playerNumber,
				roomName, maxPlayerNumber, isStart));
	}
	
	/* 로비 bgm */
	public void startLobbySound(){
		m_sound.lobbySound();
	}
	/* 로비 bgm */
	public void stopLobbySound(){
		
		m_sound.stoplobbySound();
	}
	/* gameroom bgm */
	public void startGameRoomSound(){
		m_sound.gameroomSound();
	}
	/* gameroom bgm */
	public void stopGameRoomSound(){
		
		m_sound.stopgameroomSound();
	}

	// Getters and Setters
	public SCContentPane getContentPane() {
		return m_parent;
	}

	public SCChatPanel getChatPanel() {
		return m_chatPanel;
	}

	public SCChatController getChatController() {
		return m_chatController;
	}

	public SCPlayerListController getPlayerListController() {
		return m_playerListController;
	}

	public SCRoomListController getRoomListController() {
		return m_roomListController;
	}

	public SCPlayerListPanel getPlayListPanel() {
		return m_playerListPanel;
	}

	public SCRoomListPanel getRoomListPanel() {
		return m_roomPanel;
	}

	public String getUserId() {
		return m_userId;
	}

	public Vector<SCPlayerListItem> getPlayerList() {
		return m_playerList;
	}

	public Vector<SCRoomListItem> getRoomList() {
		return m_roomList;
	}

	public int getExp() {
		return m_exp;
	}

	public void setExp(int exp) {
		this.m_exp = exp;
	}

}