package mju.cn.server.controller;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import mju.cn.common.ResponsePacket;
import mju.cn.common.Word;
import mju.cn.server.network.SSSocketManager;
import mju.cn.server.player.SSPlayer;
import mju.cn.server.room.SSLobby;
import mju.cn.server.room.SSRoom;


public class SSGameController {

	// 플레이어 목록 호출 함수
	public Object[] getPlayerList(int roomNumber) {
		SSLobby lobby = SSLobby.getLobby();
		Vector<Object[]> list = new Vector<Object[]>();
		synchronized (lobby) {
			for (SSPlayer player : lobby.getRoom(roomNumber).getPlayerList()) {
				list.add(new Object[] { player.getId(), player.getName(),
						player.getAvatar(), player.getExp(),
						lobby.getRoom(roomNumber).getGameHost().getId() });
			}
		}
		return new Object[] { list };
	}

	// 방 입장 함수
	public void addPlayer(String id, int roomNumber) {
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("addPlayer");
		SSLobby lobby = SSLobby.getLobby();
		synchronized (lobby) {
			Object[] obj;
			for (SSPlayer player : lobby.getRoom(roomNumber).getPlayerList()) {
				if (player.getId().equals(id)) {
					obj = new Object[] { player.getId(), player.getName(),
							player.getAvatar(), player.getExp(),
							lobby.getRoom(roomNumber).getGameHost().getId() };
					packet.setArgs(obj);
				}
			}

			for (SSPlayer player : lobby.getRoom(roomNumber).getPlayerList()) {
				SSSocketManager manager = player.getSocketManager("SSGameController");
				if (manager != null) {
					manager.send(packet);
				}
			}
		}
	}

	// 플레이어 삭제 함수
	public void delPlayer(String id, int roomNumber) {
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("delPlayer");
		SSLobby lobby = SSLobby.getLobby();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			Object[] obj;
			obj = new Object[] { id, room.getGameHost().getId() };
			packet.setArgs(obj);

			if (room != null) {
				for (SSPlayer player : room.getPlayerList()) {
					SSSocketManager manager = player.getSocketManager("SSGameController");
					if (manager != null) {
						manager.send(packet);
					}
				}
			}
		}
	}
	
	// 게임 초기화 함수
	public void initGame(int roomNumber) {
		System.out.println("in initGame");
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("initGame");
		SSLobby lobby = SSLobby.getLobby();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			room.initGame();
			packet.setArgs(new Object[] { new Boolean(true) });
			for (SSPlayer player : room.getPlayerList()) {
				SSSocketManager manager = player.getSocketManager("SSGameController");
				if (manager != null) {
					manager.send(packet);
				}
			}
		}
		SSRoomListController roomController = new SSRoomListController();
		roomController.getList(null);
	}
	
	//게임 시작함수 룸 초기화
	public void startGame(SSRoom room) {
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("startGame");
		for (SSPlayer player : room.getPlayerList()) {
			SSSocketManager manager = player.getSocketManager("SSGameController");
			if (manager != null) {
				manager.send(packet);
			}
		}
	}
	
	//게임 화면 상태 전송
	public void sendGameData (int roomNumber) {
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("sendGameData");
		SSLobby lobby = SSLobby.getLobby();
		HashMap<String, Point> wordData = new HashMap<String, Point>();
		HashMap<String, Vector<String>> itemData = new HashMap<String, Vector<String>>();
		Vector<String> blueWordData = new Vector<String>();
		Vector<String> redWordData = new Vector<String>();
		Vector<String> stopWordData = new Vector<String>();
//		Vector<String> blindWordData = new Vector<String>();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			for(Word word : room.getViewingWord()) {
				wordData.put(word.getString(), new Point(word.getX(), word.getY()));
			}
			for(String blueWord : room.getBlueWords()) {
				blueWordData.add(blueWord);
			}
			for(String redWord : room.getRedWords()) {
				redWordData.add(redWord);
			}
			for(String stopWord : room.getStopWords()) {
				stopWordData.add(stopWord);
			}
//			for(String blindWord : room.getBlindWords()) {
//				blindWordData.add(blindWord);
//			}
			itemData.put("blue", blueWordData);
			itemData.put("red", redWordData);
			itemData.put("stop", stopWordData);
//			itemData.put("blind", blindWordData);
			packet.setArgs(new Object[] { wordData, itemData });
			for (SSPlayer player : room.getPlayerList()) {
				SSSocketManager manager = player.getSocketManager("SSGameController");
				if (manager != null) {
					manager.send(packet);
				}
			}
		}
	}
	
	//단어 입력 결과 전송
	public void scoringWord (int roomNumber, String userId, String inputText) {
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("scoringWord");
		SSLobby lobby = SSLobby.getLobby();
		HashMap<String, Point> wordData = new HashMap<String, Point>();
		HashMap<String, Vector<String>> itemData = new HashMap<String, Vector<String>>();
		Vector<String> blueWordData = new Vector<String>();
		Vector<String> redWordData = new Vector<String>();
		Vector<String> stopWordData = new Vector<String>();						//스탑목록 단어장
//		Vector<String> blindWordData = new Vector<String>();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			if(room.scoringWord(userId, inputText)) {
				for(Word word : room.getViewingWord()) {
					wordData.put(word.getString(), new Point(word.getX(), word.getY()));
				}
				for(String blueWord : room.getBlueWords()) {
					blueWordData.add(blueWord);
				}
				for(String redWord : room.getRedWords()) {
					redWordData.add(redWord);
				}
				for(String stopWord : room.getStopWords()) {
					stopWordData.add(stopWord);
				}
//				for(String blindWord : room.getBlindWords()) {
//					blindWordData.add(blindWord);
//				}
				itemData.put("blue", blueWordData);
				itemData.put("red", redWordData);
				itemData.put("stop", stopWordData);
//				itemData.put("blind", blindWordData);
				packet.setArgs(new Object[] { userId, room.getPlayerScore(userId), wordData, itemData });
				for (SSPlayer player : room.getPlayerList()) {
					SSSocketManager manager = player.getSocketManager("SSGameController");
					if (manager != null) {
						manager.send(packet);
					}
				}
			}
			
		}
	}
	
	//전체 채력 감소 
	public void minusHP(int roomNumber) {
		System.out.println("send minusHP");
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("minusHP");
		SSLobby lobby = SSLobby.getLobby();
		HashMap<String, Integer> playsersHP = new HashMap<String, Integer>();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			Set key = room.getPlayersHP().keySet();
			for (Iterator iterator = key.iterator(); iterator.hasNext();) {
				String id = (String) iterator.next();
				int hp = room.getPlayersHP().get(id);
				playsersHP.put(id, hp);
			}
			packet.setArgs(new Object[] { playsersHP });
			for (SSPlayer player : room.getPlayerList()) {
				SSSocketManager manager = player.getSocketManager("SSGameController");
				if (manager != null) {
					manager.send(packet);
				}
			}
		}
	}
	
	//채력 아이템 획득 효과
	public void updateHP (int roomNumber, String userId, int userHP) {
		System.out.println("send updateHP");
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("updateHP");
		SSLobby lobby = SSLobby.getLobby();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			packet.setArgs(new Object[] { userHP });
			for (SSPlayer player : room.getPlayerList()) {
				if(player.getId().equals(userId)) {
					SSSocketManager manager = player.getSocketManager("SSGameController");
					if (manager != null) {
						manager.send(packet);
					}
					break;
				}
				
			}
		}
	}
	
	//자신제외한 블라인드 효과 구현실패
	public void blindOn (int roomNumber, String userId) {
		System.out.println("send blindOn");
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("blindOn");
		SSLobby lobby = SSLobby.getLobby();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			for (SSPlayer player : room.getPlayerList()) {
				if(!(player.getId().equals(userId))) {
					SSSocketManager manager = player.getSocketManager("SSGameController");
					if (manager != null) {
						manager.send(packet);
					}
				}
				
			}
		}
	}
	
	public void blindOff (int roomNumber) {
		System.out.println("send blindOff");
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("blindOff");
		SSLobby lobby = SSLobby.getLobby();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			for (SSPlayer player : room.getPlayerList()) {
				SSSocketManager manager = player.getSocketManager("SSGameController");
				if (manager != null) {
					manager.send(packet);
				}
				
			}
		}
	}
	
	// 게임종료 함수
	public void finishGame(int roomNumber) {
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("finishGame");
		SSLobby lobby = SSLobby.getLobby();
		HashMap<String, Integer> playsersHP = new HashMap<String, Integer>();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			Set key = room.getPlayersHP().keySet();
			for (Iterator iterator = key.iterator(); iterator.hasNext();) {
				String id = (String) iterator.next();
				int hp = room.getPlayersHP().get(id);
				   
				playsersHP.put(id, hp);
			}
			packet.setArgs(new Object[] { playsersHP });
			for (SSPlayer player : room.getPlayerList()) {
				SSSocketManager manager = player.getSocketManager("SSGameController");
				if (manager != null) {
					manager.send(packet);
				}
			}
		}
	}
	
	public void resultGame(int roomNumber) {
		System.out.println("send resultGame");
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("resultGame");
		SSLobby lobby = SSLobby.getLobby();
		HashMap<String, Integer> playsersHP = new HashMap<String, Integer>();
		HashMap<String, Integer> playsersScore = new HashMap<String, Integer>();
		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			Set key = room.getPlayersHP().keySet();
			for (Iterator iterator = key.iterator(); iterator.hasNext();) {
				String id = (String) iterator.next();
				int hp = room.getPlayersHP().get(id);
				playsersHP.put(id, hp);
			}
			key = room.getPlayersScore().keySet();
			for (Iterator iterator = key.iterator(); iterator.hasNext();) {
				String id = (String) iterator.next();
				int score = room.getPlayersScore().get(id);
				playsersScore.put(id, score);
			}
			packet.setArgs(new Object[] { new Boolean(false), playsersHP, playsersScore, room.getSurviveCnt() });
			for (SSPlayer player : room.getPlayerList()) {
				SSSocketManager manager = player.getSocketManager("SSGameController");
				if (manager != null) {
					manager.send(packet);
				}
			}
		}
		SSRoomListController roomController = new SSRoomListController();
		roomController.getList(null);
	}
	
//	public void endGame(int roomNumber) {
//		System.out.println("endGame in : " + roomNumber);
//		ResponsePacket packet = new ResponsePacket();
//		packet.setResponseType("endGame");
//		SSLobby lobby = SSLobby.getLobby();
//		synchronized (lobby) {
//			SSRoom room = lobby.getRoom(roomNumber);
//			room.endGame();/* 게임이 끝났을때 endGame */
//			room.initGame(); /* 게임이 끝났을때 initGame */
//			
//			for (SSPlayer player : room.getPlayerList()) {
//				int hp = room.getPlayerHP(player.getId()); /* 각각의 player의 hp 처음상태로*/
//				System.out.println("hp = "+hp);
//				packet.setArgs(new Object[] { new Boolean(false), hp });
//				SSSocketManager manager = player.getSocketManager("SSGameController");
//				if (manager != null) {
//					manager.send(packet);
//				}
//			}
//		}
//	}
	
	

	
	
	
	
	
	
	
	
	
	
//	// 턴 변경 함수
//	public void finishTurn(String id, int roomNumber) {
//		ResponsePacket packet = new ResponsePacket();
//		packet.setResponseType("endGame");
//		SSLobby lobby = SSLobby.getLobby();
//		synchronized (lobby) {
//			SSRoom room = lobby.getRoom(roomNumber);
//			boolean isNext = room.nextTurn();
//			if (!isNext) {
//				room.endGame();
//				packet.setArgs(new Object[] { new Boolean(false) });
//				for (SSPlayer player : room.getPlayerList()) {
//					SSSocketManager manager = player
//							.getSocketManager("SSGameController");
//					if (manager != null) {
//						manager.send(packet);
//					}
//				}
//
//				SSRoomListController roomController = new SSRoomListController();
//				roomController.getList(null);
//				this.finishGame(room);
//			} else {
//				this.refreshGame(room);
//			}
//
//		}
//	}

	// 마우스 드래그 처리 함수
	// 좌표를 보낸다.
//	public void mouseDragged(int roomNumber, Point point, Color color, int weight) {
//		ResponsePacket packet = new ResponsePacket();
//		packet.setResponseType("mouseDragged");
//		packet.setArgs(new Object[] { point, color, weight });
//		SSLobby lobby = SSLobby.getLobby();
//
//		synchronized (lobby) {
//			SSRoom room = lobby.getRoom(roomNumber);
//			for (SSPlayer player : room.getPlayerList()) {
//				SSSocketManager manager = player
//						.getSocketManager("SSGameController");
//				if (manager != null) {
//					manager.send(packet);
//				}
//			}
//		}
//	}
//
//	// 마우스 이동 처리 함수
//	// 좌표를 보낸다.
//	public void mouseMoved(int roomNumber, Point point) {
//		ResponsePacket packet = new ResponsePacket();
//		packet.setResponseType("mouseMoved");
//		packet.setArgs(new Object[] { point });
//		SSLobby lobby = SSLobby.getLobby();
//
//		synchronized (lobby) {
//			SSRoom room = lobby.getRoom(roomNumber);
//			for (SSPlayer player : room.getPlayerList()) {
//				SSSocketManager manager = player
//						.getSocketManager("SSGameController");
//				if (manager != null) {
//					manager.send(packet);
//				}
//			}
//		}
//	}
}
