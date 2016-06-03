package mju.cn.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.Timer;

import mju.cn.client.controller.SCGameChatController;
import mju.cn.client.controller.SCGameController;
import mju.cn.client.gui.SCContentPane;
import mju.cn.client.gui.SCMainFrame;
import mju.cn.client.gui.item.SCSound;

public class SCGameRoomPanel extends JPanel  {
   private static final long serialVersionUID = 1L;

   // Attributes
   private SCContentPane m_parent; // 상위패널
   private String m_userId; // 유저아이디
   private String m_hostId; // 방장아이디
   private String m_orderId; // 현재차례 유저아이디
   private String m_state; // 현재차례 유저상태
   private int m_roomNumber; // 방번호
 
   private Image m_playerlistBackground;
   private Image m_gameBackground;//게임 배경

   private SCGameRoomPanel instance ;
   private int hp;

   // Components
   private Vector<SCSlotPanel> m_slot; // 사용자 표시 슬롯 리스트
   private SCGameChatPanel m_gameChatPanel; // 게임 채팅 패널
   private Vector<JPanel> m_blank; // 빈 패널
   private SCGameController m_gameController; // 게임컨트롤러
   private SCGameChatController m_gameChatController; // 채팅컨트롤러
   private boolean m_startGame; // 게임시작여부

   private SCSound m_sound; // 사운드

   private SCGameBoxPanel m_gameBoxPanel;


   // Constructor
   public SCGameRoomPanel(SCContentPane parent) {
      super();
      m_parent = parent;
      m_hostId = "NONE";
      m_orderId = "NONE";
      m_state = "NORMAL";
     
      m_playerlistBackground = Toolkit.getDefaultToolkit().getImage(
            "images/game_playerlist.png");
      m_gameBackground = Toolkit.getDefaultToolkit().getImage(
            "images/Game_Box.png");
   
      m_slot = new Vector<SCSlotPanel>();
      m_blank = new Vector<JPanel>();  
      m_gameChatPanel = new SCGameChatPanel(this);
      m_gameChatPanel.setVisible(true);
      
      instance = this;
      
      m_gameBoxPanel = new SCGameBoxPanel(instance);

      m_sound = new SCSound();
   }

   // Initialization
   public void init(String serverIP) {

      m_gameController = new SCGameController(serverIP, this);
      m_gameController.start();
      m_gameChatController = new SCGameChatController(serverIP, this);
      m_gameChatController.start();
      this.setLayout(new BorderLayout());
      this.setOpaque(false);

      this.add(this.createEastPanel(), BorderLayout.EAST);
    
      /* 구름이랑 번개 그려주는 패널 */
      MiddlePanel middlepanel = new MiddlePanel();
      middlepanel.setOpaque(false);
      this.add(middlepanel,BorderLayout.WEST);

     
   }
   /* 구름이랑 번개 그려주는 패널 */
   private class MiddlePanel extends JPanel{
	   Image img_cloud;
	   Image img_thunder;
	   public MiddlePanel(){
		   this.setPreferredSize(new Dimension(550, 600));

		   img_cloud = Toolkit.getDefaultToolkit().getImage("images/cloud.png");
		   img_thunder = Toolkit.getDefaultToolkit().getImage("images/thunder.png");

		   m_gameBoxPanel.setOpaque(true);
		   m_gameBoxPanel.setVisible(true);
		   this.setLayout(null);
		   m_gameBoxPanel.setBounds(0, 0, 550, 600);
		   m_gameBoxPanel.setOpaque(false);
		   this.add(m_gameBoxPanel);
	   }
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
			/* hp가 2면 구름 hp가 1이면 구름+번개 */
			switch(hp){
			case 1:g.drawImage(img_thunder, 130, 150, 300,333, this);
			case 2:g.drawImage(img_cloud, 130, 60, 300,160, this);break;
	}
			super.paintChildren(g);



	}
   }

   // 컨트롤러 초기화 함수
   public void initManager(String uid) {
      m_userId = uid;
      m_gameController.init(m_userId);
      m_gameChatController.init(m_userId);
   }

   public void refleshBG(int hp){
	   this.hp = hp;
	   this.repaint();
	   this.validate();

   }


   // 아래쪽 패널 생성 함수
   private JPanel createBottomPanel() {
      JPanel panel = new JPanel();
      panel.setPreferredSize(new Dimension(0, 235));
      panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
      panel.setOpaque(false);

      m_gameChatPanel = new SCGameChatPanel(this);
      m_gameChatPanel.setBounds(10, 400, 420, 370);
      m_gameChatPanel.setVisible(true);
      panel.add(m_gameChatPanel);
      return panel;
   }
   
   private JPanel createEastPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());

      JPanel slotpanel= new JPanel();
      //boxLayout.
      slotpanel.setLayout(new BoxLayout(slotpanel,BoxLayout.Y_AXIS));
      slotpanel.setOpaque(false);
      for (int i = 0; i < 6; i++) {
         JPanel blank = new JPanel();
         blank.setOpaque(true);
         m_blank.add(blank);
      }
      for (JPanel blankPanel : m_blank) {
         slotpanel.add(blankPanel);
      }

      JPanel chatpanel = new JPanel();

      chatpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
      chatpanel.setOpaque(false);
      chatpanel.setPreferredSize(new Dimension(300,270));
      m_gameChatPanel.setVisible(true);
      chatpanel.add(m_gameChatPanel);
   
      panel.add(slotpanel,BorderLayout.NORTH);
      panel.add(chatpanel,BorderLayout.SOUTH );
      return panel;
   }

   public void exitRoom(){
	   m_gameChatPanel.getTextArea().setText(null);
	   System.out.println(m_userId + " " + m_roomNumber);
       m_parent.getLobbyPanel().getRoomListController()
             .exitRoom(m_userId, m_roomNumber);
       m_parent.getLobbyPanel().repaint();
   }
   public void startGame(){
       m_gameController.initGame(m_roomNumber);


   }
   public boolean isHost(){
	   return m_hostId.equals(m_userId);
   }
   // 버튼 갱신 함수
   public void refleshButton() {
         if (m_hostId.equals(m_userId) && (m_slot.size() >= 3)) {
        	 m_gameBoxPanel.showButton("host");
         } else {
        	 m_gameBoxPanel.showButton("player");

         }
   }

 
   // 슬롯 갱신 함수
   public void refleshSlot() {
      for (JPanel blankPanel : m_blank) {
         blankPanel.removeAll();
      }

      for (int i = 0; i < m_slot.size(); i++) {
         SCSlotPanel slot = m_slot.get(i);
         if (i < 3) {
            slot.setAlignment(SCSlotPanel.SLOT_LEFT_ALIGNMENT);
         } else {
            slot.setAlignment(SCSlotPanel.SLOT_RIGHT_ALIGNMENT);
         }
         m_blank.get(i).add(slot);
      }

      this.updateUI();
   }



   // 슬롯 추가 함수
   public void addSlot(String id, String name, String avatar, int exp) {
      m_slot.add(new SCSlotPanel(id, name, avatar, this));
      m_sound.slotJoin();
   }

   // 슬롯 삭제 함수
   public void delSlot(String id) {
      for (int i = 0; i < m_slot.size(); i++) {
         if (m_slot.get(i).getId().equals(id)) {
            m_slot.remove(i);
            m_sound.slotLeave();
            return;
         }
      }
   }

   // 슬롯 초기화 함수
   public void initSlot() {
      m_slot.clear();
      this.updateUI();
   }

   // 그리기 함수
   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);

      g.drawImage(m_gameBackground, 0, 0, 550, this.getHeight(), this);
      g.drawImage(m_playerlistBackground,600, 100, 300, 258, this);
   }

   /* gamecontroller가 게임종료했을때, 게임결과창을 띄워주기위해서 호출하는 함수*/
   public void showGameResultPanel(Vector<Object[]> playerList){

	   m_gameBoxPanel.hideResultImage();
	   m_parent.showGameResultPanel(m_userId, m_hostId, playerList, m_gameBoxPanel.getIsWinner()); //contentPanel에서 게임결과창을 띄움
	   if(isHost()) m_gameBoxPanel.showButton("host");
	   else m_gameBoxPanel.showButton("player"); //버튼 되돌리기

   }
   /* 게임 BGM*/
	public void playGameBGM(){
		m_sound.gameSound();
	}

	/* 게임 BGM*/
	public void stopGameBGM(){
		m_sound.stopGameSound();
	}
	/* 게임 BGM*/
	public void playGameRoomSound(){
		m_sound.gameroomSound();
	}

	/* 게임 BGM*/
	public void stopGameRoomSound(){
		m_sound.stopgameroomSound();
	}	
	
   //끝났을때 소리함수
	public void endSound(){
		m_sound.endSound();
	}
	//점수 얻었을때 소리함수
	public void scoreSound(){
		m_sound.scoreSound();
	}
   // Getters and Setters
   public SCContentPane getContentPane() {
      return m_parent;
   }

   public String getUserId() {
      return m_userId;
   }

   public SCGameController getGameController() {
      return m_gameController;
   }

   public void setRoomNumber(int roomNumber) {
      m_roomNumber = roomNumber;
   }

   public int getRoomNumber() {
      return m_roomNumber;
   }

   public String getHostId() {
      return m_hostId;
   }

   public void setHostId(String hostId) {
      m_hostId = hostId;
   }

   public boolean isStartGame() {
      return m_startGame;
   }

   public void setStartGame(boolean isStart) {
      m_startGame = isStart;
   }

   public Vector<SCSlotPanel> getSlot() {
      return m_slot;
   }

   public String getOrderId() {
      return m_orderId;
   }

   public String getState() {
      return m_state;
   }

   public void setState(String state) {
      m_state = state;
   }

   public SCGameChatPanel getGameChatPanel() {
      return m_gameChatPanel;
   }

   public void setGameChatPanel(SCGameChatPanel gameChatPanel) {
      this.m_gameChatPanel = gameChatPanel;
   }

   public SCGameBoxPanel getGameBoxPanel() {
	   return m_gameBoxPanel;
   }

   public void setGameBoxPanel(SCGameBoxPanel gameBoxPanel) {
      this.m_gameBoxPanel = gameBoxPanel;
   }

   public SCGameChatController getGameChatController() {
      return m_gameChatController;
   }

   public void setGameChatController(SCGameChatController m_gameChatController) {
      this.m_gameChatController = m_gameChatController;
   }



}
