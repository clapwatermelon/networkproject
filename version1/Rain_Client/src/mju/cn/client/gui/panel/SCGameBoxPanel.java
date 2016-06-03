package mju.cn.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import mju.cn.client.controller.SCGameController;
import mju.cn.client.gui.item.SCSound;
import mju.cn.common.Word;


public class SCGameBoxPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	// Attributes
	private Image chatBackground;
	private JPanel inputPanel;

	// Components
	private JButton sendButton;
	private JButton exitButton;
	private JButton startButton;
	private JTextArea scoreTextArea;
	private JTextField inputField;
	private SCGameRoomPanel parent;
	private Image m_scoreBox;
	private Image m_hpImg;
	private Image m_typo_box;
	private Image readyImage; //** 시작전3초동안 보여줄 component의 배경
	private boolean isReady;
	private boolean isPlay;
//	private boolean isBlind;


	private int m_HP;
	private int m_score;

    private Vector<Word> m_viewingWords;
    private Vector<String> m_viewingBlueWords;
    private Vector<String> m_viewingRedWords;
    private Vector<String> m_viewingStopWords;
//    private Vector<String> m_viewingBlindWords;
    private int level;
    private DropArea dropArea;


	private Image victoryImage; //* 이기면 띄우는 이미지
	private Image defeatImage; //* 지면 띄우는 이미지
	private boolean gameResultImageFlag; // 게임결과이미지 띄울지 여부 flag
	private boolean isWinner; //게임에서 이겼는지 여부



	// Constructor
	public SCGameBoxPanel(SCGameRoomPanel parent) {
		this.parent = parent;
		sendButton = new JButton();
		exitButton = new JButton();
		startButton = new JButton();
		inputField = new JTextField();
		scoreTextArea = new JTextArea();
		m_viewingWords = new Vector<Word>();
		m_viewingBlueWords = new Vector<String>();
		m_viewingRedWords = new Vector<String>();
		m_viewingStopWords = new Vector<String>();

		m_scoreBox = Toolkit.getDefaultToolkit().getImage("images/Score_Box.png");
		m_hpImg = Toolkit.getDefaultToolkit().getImage("images/HP.png");
		m_typo_box = Toolkit.getDefaultToolkit().getImage("images/Typo_Box_1.png");
		readyImage =  Toolkit.getDefaultToolkit().getImage("images/Ready.png");


		 /* 이기면, 지면 띄우는 이미지 세팅, 필요한 boolean값 세팅*/
		victoryImage = Toolkit.getDefaultToolkit().getImage("images/Victory.png");
		defeatImage = Toolkit.getDefaultToolkit().getImage("images/Defeated.png");

		gameResultImageFlag = false;
		isWinner = false;

		m_HP = 3;
		isReady = false;
		isPlay = false;

		this.init();
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if(isReady){
			g2d.drawImage(readyImage, 7, 20, 550, 550, this);
		}
		/* 이기면 or 지면 잠깐 띄우는 이미지 gameResultImageFlag  : true => 보여준다 flase => 지운다*/
		if(gameResultImageFlag){
			if(isWinner){
				g.drawImage(victoryImage, 130, 30, 300, 300, this);
			}
			else{
				g.drawImage(defeatImage, 130,30, 300, 300, this);
			}
		}


		g2d.setFont(new Font("돋움", Font.BOLD, 14));
		g2d.setColor(Color.BLACK);

		g2d.drawImage(m_scoreBox, 430, 570, 100, 20, this);
		for(int i=0;i<m_HP;i++){
			g2d.drawImage(m_hpImg, 14+(i*32), 565, 30, 30, this);

		}
		g2d.drawImage(m_typo_box, 140, 527, 200, 51, this);
		g2d.drawString(String.valueOf(m_score), 480, 583);
		super.paintComponent(g2d);
	}


	public void showResultImage(){
		this.gameResultImageFlag = true; // 결과이미지를 보여주기위해
		repaint();
	}

	public void hideResultImage(){
		this.gameResultImageFlag = false; // 결과이미지를 보여주기위해
		m_viewingWords.clear();
		dropArea.repaint();
		repaint();
	}

    //******* hp reflesh
	public void refleshHP(int count_hp){
		m_HP = count_hp;
		repaint();


	}

	public void addScore(int score) {
		m_score += score;
	}

	public void subScore(int score) {
		m_score -= score;
	}

    public void rollBack() {
    	setScore(0);
    	isWinner = false;
    	m_HP = 3;
    	m_viewingWords.clear();
    	dropArea.repaint();
    	repaint();
    }

	// Initialization
	private void init() {
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(550, 550));
	//	this.setLayout(new BorderLayout());


	//	this.add(this.createMainSectionPanel(), BorderLayout.CENTER);
		this.scoreTextArea.setText(String.valueOf(m_score));
		this.setLayout(null);

		inputPanel = createInputPanel();
		inputPanel.setBounds(170, 522, 300, 100);

		inputField.requestFocus();
		startButton.setIcon(new ImageIcon("images/btn_start.png"));
		startButton.setBorder(null);
		startButton.setContentAreaFilled(false);
		startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		startButton.setPreferredSize(new Dimension(130, 50));
		startButton.setBounds(150, 225, 130, 50);

		exitButton.setIcon(new ImageIcon("images/btn_exit.png"));
		exitButton.setBorder(null);
		exitButton.setContentAreaFilled(false);
		exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		exitButton.setPreferredSize(new Dimension(130, 50));
		exitButton.setBounds(290, 225, 130, 50);

		dropArea = new DropArea();
		dropArea.setPreferredSize(new Dimension(550, 550));
		dropArea.setBounds(0, 0, 550, 550);

   	    this.add(dropArea);
		this.add(startButton);
		this.add(exitButton);
		this.add(inputPanel);
		initEventHandler();

		if(parent.isHost()) {showButton("host");}
		else {showButton("player");}
		System.out.println("불림`");
	}


 	public void playGame() {

 		setIsPlay(true);
	   	setVisible(true);
//	   	isBlind = false;
    }

 	public void getGameData(HashMap<String, Point> wordData, HashMap<String, Vector<String>> itemData) {
 		m_viewingWords = new Vector<Word>();
 		m_viewingBlueWords = itemData.get("blue");
 		m_viewingRedWords = itemData.get("red");
 		m_viewingStopWords = itemData.get("stop");
// 		m_viewingBlindWords = itemData.get("blind");
		Set key = wordData.keySet();
		for (Iterator iterator = key.iterator(); iterator.hasNext();) {
			String str = (String) iterator.next();
			Point point = wordData.get(str);

			m_viewingWords.add(new Word(str, (int)point.getX(), (int)point.getY()));
		}
 		repaint();
 	}

	public void showButton(String type){
		switch(type){
		case "host":
			exitButton.setVisible(true);
			startButton.setVisible(true);
			exitButton.setBounds(290, 225, 130, 50);

			break;
		case "player":
			exitButton.setVisible(true);
			startButton.setVisible(false);
			exitButton.setBounds(210, 225, 130, 50);
			break;
		case "hide":
			exitButton.setVisible(false);
			startButton.setVisible(false);
			break;
		}
	}
	private void initEventHandler() {
	    	exitButton.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	           parent.exitRoom();
	         }
	      });

	      startButton.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	 		    parent.getGameController().initGame(parent.getRoomNumber());
	         }
	      });
  }

	/* 시작전 화면  */
	public void showReadyImage(){
		isReady = true;
	   	repaint();

	}

	public void hideReadyImage() {
		isReady = false;
	   	repaint();
	}


	private void initEventHadler() {
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		};
	}





	private JPanel createInputPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 100));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));

		sendButton.setIcon(new ImageIcon("images/Typo_Box_2.png"));
		sendButton.setBorder(null);
		sendButton.setContentAreaFilled(false);
		sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		sendButton.setPreferredSize(new Dimension(59, 51));

		inputField.setPreferredSize(new Dimension(170, 20));
		inputField.setBorder(null);
	     inputField.addActionListener(this);

        panel.add(inputField);
        panel.add(sendButton);

		panel.add(inputField);
		panel.add(sendButton);



		return panel;
	}
	class BGComponent extends JComponent{
		Image img_cloud;
		Image img_thunder;
		Image img_transparent;
		Image img_transparent2;
		Image default1;
		Image default2;

		BGComponent(){
			img_cloud = Toolkit.getDefaultToolkit().getImage("images/cloud.png");
			img_thunder = Toolkit.getDefaultToolkit().getImage("images/thunder.png");
			img_transparent = Toolkit.getDefaultToolkit().getImage("images/transparent.png");
			img_transparent2= Toolkit.getDefaultToolkit().getImage("images/transparent2.png");
			default1 =  img_transparent;
			default2 = img_transparent2;
		}
		public void refleshBG(){
			switch(m_HP){
			case 1: default1 = img_thunder;
					default2 = img_cloud;
			case 2: default2 = img_cloud;
					default1 = img_transparent;
			case 3: default2 = img_transparent2;
					default1 = img_transparent;
			}
		}
		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			switch(m_HP){
			case 1:g.drawImage(default1, 130, 60, 300,160, this);
			case 2:g.drawImage(default2, 13, 120, 300,333, this); break;
		}
		}


	}

	class DropArea extends JComponent {
		String itemString="";
		int occur= 20;
		int occurIndex=0;


		public void paint(Graphics g) {

				Graphics2D g2d = (Graphics2D) g;
				for (Word word : m_viewingWords) {
					if(m_viewingBlueWords.contains(word.getString())) {
						g.setColor(Color.BLUE);
					}
					else if(m_viewingRedWords.contains(word.getString())){
						g.setColor(Color.RED);
					}
					else if(m_viewingStopWords.contains(word.getString())){
						g.setColor(Color.GREEN);
					}
//					else if(m_viewingBlindWords.contains(word.getString())){
//						g.setColor(Color.GRAY);
//					}
					else {
						g.setColor(Color.BLACK);
					}
					g.drawString(word.getString(), word.getX(), word.getY());
					
				}

      }
   }

	private JPanel createMessagePanel() {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(chatBackground,10, 0, this.getWidth(),
						this.getHeight(), this);
				super.paintComponent(g);
			}
		};

		panel.setPreferredSize(new Dimension(280, 190));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));

		return panel;
	}

    public void actionPerformed(ActionEvent e) {
    	parent.getGameController().scoringWord(parent.getRoomNumber(), parent.getUserId(), inputField.getText());
    	inputField.setText("");
    }


	//** 게임끝나고 word지우기
	public void cleanWords(){
		isPlay = false;
		dropArea.repaint();
	}

	// Getters and Setters
	public JTextArea getTextArea() {
		return scoreTextArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.scoreTextArea = textArea;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setHP(int userHP) {
		this.m_HP = userHP;
	}

	public int getHp() {
		return m_HP;
	}

	public void setScore(int score) {
		this.m_score = score;
	}

	public int getScore() {
		return m_score;
	}

	public void setIsWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	public boolean getIsWinner() {
		return isWinner;
	}

	public void setIsPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	public boolean getIsPlay() {
		return isPlay;
	}

//	public void setIsBlind(boolean isBlind) {
//		this.isBlind = isBlind;
//	}


}
