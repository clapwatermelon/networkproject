package mju.cn.client.controller;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Vector;

import mju.cn.client.gui.item.SCAvatar;
import mju.cn.client.gui.panel.SCGameRoomPanel;
import mju.cn.client.gui.panel.SCSlotPanel;
import mju.cn.client.network.SCClientStub;
import mju.cn.common.RequestPacket;
import mju.cn.common.RequestPacket.SYNC_TYPE;
import mju.cn.common.ResponsePacket;
import mju.cn.common.Word;


public class SCGameController extends SCClientStub {

	// Components
	private SCGameRoomPanel m_gamePanel; // 게임화면 패널

	// Constructor
	public SCGameController(String ip, SCGameRoomPanel gamePanel) {
		super(ip);
		m_gamePanel = gamePanel;
	}

	// Initialization
	public void init(String id) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSGameController");
		packet.setMethodName("init");
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
		packet.setArgs(new Object[] { id });
		this.send(packet);
	}

	// 게임 초기화 함수
	public void initGame(int roomNumber) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSGameController");
		packet.setMethodName("initGame");
		packet.setSyncType(SYNC_TYPE.ASYNCHRONOUS);
		packet.setArgs(new Object[] { roomNumber });
		this.send(packet);
	}
	
	//단어엔터 효과
	public void scoringWord(int roomNumber, String userId, String inputText) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSGameController");
		packet.setMethodName("scoringWord");
		packet.setSyncType(SYNC_TYPE.ASYNCHRONOUS);
		packet.setArgs(new Object[] { roomNumber, userId, inputText });
		this.send(packet);
	}
	
	// 채력깍는 함수
	public void minusHp(String id, int roomNumber) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSGameController");
		packet.setMethodName("minusHp");
		packet.setSyncType(SYNC_TYPE.ASYNCHRONOUS);
		packet.setArgs(new Object[] { id, roomNumber });
		this.send(packet);
	}
	

	// 사용자 리스트 획득 함수
	public void getPlayerList(int roomNumber) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSGameController");
		packet.setMethodName("getPlayerList");
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
		packet.setArgs(new Object[] { roomNumber });
		this.send(packet);
	}
	
	
	
	
	
	
	//-----------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------
	
	
	
	// 서버측 initGame 결과 처리 함수
	public void resultInitGame(ResponsePacket packet) {
		System.out.println("in resultInitGame");
		m_gamePanel.setStartGame((Boolean) packet.getArgs()[0]);
		m_gamePanel.refleshButton();
		m_gamePanel.refleshSlot();
		m_gamePanel.getGameBoxPanel().showButton("hide");
		m_gamePanel.getGameBoxPanel().showReadyImage(); //** 시작 전 이미지 올리기
		m_gamePanel.updateUI();
		/* 게임 BGM*/
		m_gamePanel.stopGameRoomSound();		//진행중인 노래를 끈다 없으면 NULL 확인
		m_gamePanel.playGameBGM();
		
	}
	
	// 서버측 startGame 결과 처리 함수
	public void resultStartGame(ResponsePacket packet) {
		System.out.println("in resultStartGame");
		m_gamePanel.getGameBoxPanel().hideReadyImage(); //** 시작 전 이미지 내리기
		m_gamePanel.getGameBoxPanel().playGame();
	}
	
	//게임 데이터 결과 반영
	@SuppressWarnings("unchecked")
	public void resultSendGameData(ResponsePacket packet) {
		System.out.println("in resultSendGameData");
		HashMap<String, Point> wordData = (HashMap<String, Point>) packet.getArgs()[0];
		HashMap<String, Vector<String>> itemData = (HashMap<String, Vector<String>>) packet.getArgs()[1];
		m_gamePanel.getGameBoxPanel().getGameData(wordData, itemData);
	}
	
	//단어 엔터 전송결과 반영
	@SuppressWarnings("unchecked")
	public void resultScoringWord(ResponsePacket packet) {
		System.out.println("in resultScoringWord");
		String id = (String) packet.getArgs()[0];
		int score = (int)packet.getArgs()[1];
		HashMap<String, Point> wordData = (HashMap<String, Point>) packet.getArgs()[2];
		HashMap<String, Vector<String>> itemData = (HashMap<String, Vector<String>>) packet.getArgs()[3];
		for (SCSlotPanel slot : m_gamePanel.getSlot()) {
			if (slot.getId().equals(id)) {
				slot.setScore(score);
			}
		}
		if(id.equals(m_gamePanel.getUserId())) {
			m_gamePanel.getGameBoxPanel().setScore(score);
		}
		m_gamePanel.getGameBoxPanel().getGameData(wordData, itemData);
		m_gamePanel.scoreSound();
		m_gamePanel.getGameBoxPanel().repaint();
		m_gamePanel.refleshSlot();
	}
	
	//전체 채력 감소 반영
	@SuppressWarnings("unchecked")
	public void resultMinusHP(ResponsePacket packet) {
		System.out.println("in resultMinusHP");
		HashMap<String, Integer> playersHP = (HashMap<String, Integer>)packet.getArgs()[0];
		for (SCSlotPanel slot : m_gamePanel.getSlot()) {
			if(playersHP.get(slot.getId()) == 0) {
				slot.changeDead();
				m_gamePanel.getGameBoxPanel().showResultImage();	
				m_gamePanel.getGameBoxPanel().setIsPlay(false);

			}
		}
		int userHP = playersHP.get(m_gamePanel.getUserId());
		m_gamePanel.getGameBoxPanel().refleshHP(userHP);
		m_gamePanel.refleshBG(userHP);
		m_gamePanel.getGameBoxPanel().repaint();
		m_gamePanel.refleshSlot();
	}
	
//	public void resultBlindOn(ResponsePacket responesPacket) {
//		m_gamePanel.getGameBoxPanel().setIsBlind(true);
//		
//	}
//	
//	private void resultBlindOff(ResponsePacket responesPacket) {
//		m_gamePanel.getGameBoxPanel().setIsBlind(false);
//		
//	}
	
	public void resultUpdateHP(ResponsePacket packet) {
		System.out.println("in resultUpdateHP");
		m_gamePanel.getGameBoxPanel().refleshHP((int)packet.getArgs()[0]);
	}
	
	@SuppressWarnings("unchecked")
	public void resultFinishGame(ResponsePacket packet) {
		System.out.println("in resultFinishGame");
		HashMap<String, Integer> playersHP = (HashMap<String, Integer>)packet.getArgs()[0];
		int userHP = playersHP.get(m_gamePanel.getUserId());
		if(userHP > 0) {
			m_gamePanel.getGameBoxPanel().setIsWinner(true);
		}
		m_gamePanel.getGameBoxPanel().showResultImage();
		m_gamePanel.getGameBoxPanel().setIsPlay(false);
		m_gamePanel.getGameBoxPanel().repaint();
		
	}
	
	@SuppressWarnings("unchecked")
	public void resultResultGame(ResponsePacket packet) {
		System.out.println("in resultResultGame");
		HashMap<String, Integer> playersHP = (HashMap<String, Integer>)packet.getArgs()[1];
		HashMap<String, Integer> playersScore = (HashMap<String, Integer>)packet.getArgs()[2];
		int surviveCnt = (int) packet.getArgs()[3];
		m_gamePanel.setStartGame((Boolean) packet.getArgs()[0]);
		m_gamePanel.refleshButton();
		m_gamePanel.refleshSlot();
		/* 게임끝났으니까 hp, 배경 init*/
		m_gamePanel.getGameBoxPanel().refleshHP(3);
		m_gamePanel.refleshBG(3);
		//m_gamePanel.getGameBoxPanel().cleanWords();
		m_gamePanel.updateUI();
		
		/* 게임 BGM*/
		m_gamePanel.stopGameBGM();
		m_gamePanel.playGameRoomSound();
		
		/* 임의로 데이터를 넣어서 Test  (원래는 packet에서 받아와야하는 데이터)*/
		Vector<Object[]> playerList = new Vector<Object[]>();
		int maxScore = 0;
		for (SCSlotPanel slot : m_gamePanel.getSlot()) {
			if(playersScore.get(slot.getId()) > maxScore) {
				maxScore = playersScore.get(slot.getId());
			}
		}
		if(surviveCnt > 0){										//살아남은사람이 있을때
			for (SCSlotPanel slot : m_gamePanel.getSlot()) {
				SCAvatar avatar =  slot.getAvatar();
				boolean isWinner = false;
				if(playersHP.get(slot.getId()) > 0) {
					if(playersScore.get(slot.getId()) == maxScore) {
						isWinner = true;
					}
				}
				playerList.add(new Object[]{slot.getId(), avatar.getAvatarName(), slot.getScore(), isWinner});
			}
		}
		else {													//모두 죽었을때

			for (SCSlotPanel slot : m_gamePanel.getSlot()) {
				SCAvatar avatar =  slot.getAvatar();
				boolean isWinner = false;
				System.out.println("player : max = " + playersScore.get(slot.getId()) + " : " + maxScore);
				if(playersScore.get(slot.getId()).equals(maxScore)) {
					isWinner = true;
					if(slot.getId().equals(m_gamePanel.getUserId())) {
						m_gamePanel.getGameBoxPanel().setIsWinner(isWinner);
					}
				}
				playerList.add(new Object[]{slot.getId(), avatar.getAvatarName(), slot.getScore(), isWinner});
			}
		}
		/* gameRoomPanel에게 gameResultPanel을 띄우게 하기위해 */
		m_gamePanel.showGameResultPanel(playerList);
		for (SCSlotPanel slot : m_gamePanel.getSlot()) {
			slot.rollBack();
		}
		m_gamePanel.getGameBoxPanel().rollBack();
		m_gamePanel.endSound();

		
	}

	// 서버측 AddPlayer 결과 처리 함수
	public void resultAddPlayer(ResponsePacket packet) {
		m_gamePanel.addSlot((String) packet.getArgs()[0],
				(String) packet.getArgs()[1], (String) packet.getArgs()[2],
				(Integer) packet.getArgs()[3]);
		m_gamePanel.refleshSlot();
		m_gamePanel.setHostId((String) packet.getArgs()[4]);
		m_gamePanel.refleshButton();
	}

	// 서버측 delPlayer 결과 처리 함수
	public void resultDelPlayer(ResponsePacket packet) {
		m_gamePanel.delSlot((String) packet.getArgs()[0]);
		m_gamePanel.refleshSlot();
		m_gamePanel.setHostId((String) packet.getArgs()[1]);
		m_gamePanel.refleshButton();
	}
	

	// 리스트 획득 함수
	@SuppressWarnings("unchecked")
	public void initList(ResponsePacket packet) {
		Vector<Object[]> list = (Vector<Object[]>) (packet.getArgs()[0]);
		m_gamePanel.getSlot().clear();
		for (int i = 0; i < list.size(); i++) {
			m_gamePanel.addSlot((String) list.get(i)[0],
					(String) list.get(i)[1], (String) list.get(i)[2],
					(Integer) list.get(i)[3]);
		}
		m_gamePanel.refleshSlot();
	}

	@Override
	public void run() {
		while (m_isConnected) {
			try {
				Object obj = inputStream.readObject();
				ResponsePacket responesPacket = (ResponsePacket) obj;
				String responseType = responesPacket.getResponseType();
				if (responseType.equals("addPlayer")) {
					this.resultAddPlayer(responesPacket);
				} else if (responseType.equals("delPlayer")) {
					this.resultDelPlayer(responesPacket);
				} else if (responseType.equals("getPlayerList")) {
					this.initList(responesPacket);
				} else if (responseType.equals("scoringWord")) {
					this.resultScoringWord(responesPacket);
				} else if (responseType.equals("initGame")) {
					this.resultInitGame(responesPacket);
				} else if (responseType.equals("startGame")){
					this.resultStartGame(responesPacket);
				}else if (responseType.equals("sendGameData")) {
					this.resultSendGameData(responesPacket);
				}else if (responseType.equals("finishGame")) {
					this.resultFinishGame(responesPacket);
				}else if (responseType.equals("resultGame")) {
					this.resultResultGame(responesPacket);
				} else if(responseType.equals("minusHP")){
					this.resultMinusHP(responesPacket);
				} else if(responseType.equals("updateHP")){
					this.resultUpdateHP(responesPacket);
//				} else if(responseType.equals("blindOn")){
//					this.resultBlindOn(responesPacket);
//				} else if(responseType.equals("blindOff")){
//					this.resultBlindOff(responesPacket);
				} else {
					// 모르는 패킷은 버린다.
				}
			} catch (Exception e) {
				e.printStackTrace();
				m_isConnected = false;
			}
		}
	}








}