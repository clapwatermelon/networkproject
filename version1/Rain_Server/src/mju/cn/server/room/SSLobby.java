package mju.cn.server.room;

import java.util.Vector;

import mju.cn.server.controller.SSGameController;
import mju.cn.server.controller.SSPlayerListController;
import mju.cn.server.controller.SSRoomListController;
import mju.cn.server.db.DBManager;
import mju.cn.server.network.SSSocketManager;
import mju.cn.server.player.SSPlayer;

public class SSLobby {
	// Attributes
	public static final int MAX_PLAYER = 6;  //최대 입장 가능한 플레이어 명수
	private static SSLobby lobby = new SSLobby(); // 가장 부모 모델로 Singleton 패턴을 위해 사용한다.
	
	// Components
	private Vector<SSPlayer> m_waitPlayer; // 대기방의 플레이어들
	private Vector<SSRoom> m_room; // 생성된 게임방들
	private DBManager m_dbManager; // DB 메니져
	private Vector<SSSocketManager> m_socketManager; // 접속된 소켓 메니져
	
	// Constructor
	private SSLobby(){
		m_waitPlayer = new Vector<SSPlayer>();
		m_room = new Vector<SSRoom>();
		m_socketManager = new Vector<SSSocketManager>();
		m_dbManager = new DBManager();
	}
	
	// 소켓 연쇄 삭제 함수
	@SuppressWarnings("unchecked")
	public void removeSocketManager(SSSocketManager manager){
		// 로비에 있는 플레이어가 가지고 있는 SocketManager에서 삭제
		Vector<SSPlayer> cloneWaitPlayers = (Vector<SSPlayer>)(m_waitPlayer.clone());
		for(SSPlayer player : cloneWaitPlayers){
			player.removeSocketManager(manager);
			if(player.getManagerNumber()==0){
				SSPlayerListController playerListController = new SSPlayerListController();
				m_waitPlayer.remove(player);
				playerListController.getList(null);
				break;
			}
		}
		
		// 방에 입장한 플레이어가 가지고 있는 SocketManager에서 삭제
		Vector<SSRoom> cloneRooms = (Vector<SSRoom>)m_room.clone();
		for(SSRoom room : cloneRooms){
			Vector<SSPlayer> players = room.getPlayerList();
			Vector<SSPlayer> cloneplayers = (Vector<SSPlayer>)(players.clone());
			for(SSPlayer player : cloneplayers){
				player.removeSocketManager(manager);
				if(player.getManagerNumber()==0){
					players.remove(player);
					
					// 플레이어가 삭제되고 방에 아무도 없으면 방도 삭제
					if(players.size() == 0){
						m_room.remove(room);
					}else{
						// 플레이어 유동을 다른 클라이언트에게 브로드 캐스트
						SSGameController gameController = new SSGameController();
						gameController.delPlayer(player.getId(), room.getRoomNum());
						if(room.isStart()){
//							gameController.finishTurn(player.getId(), room.getRoomNum());
						}
					}
					// 플레이어 유동을 다른 클라이언트에게 브로드 캐스트
					SSRoomListController roomListController = new SSRoomListController();
					roomListController.getList(null);
					break;
				}
			}
		}
		// 마지막으로 로비에서 삭제
		m_socketManager.remove(manager);
	}
	
	// 플레이어 생성 함수
	public void createPlayer(String id){
		Object[] data = m_dbManager.getRecord(id);
		m_waitPlayer.add(new SSPlayer(id, (String)data[0], (String)data[3], (Integer)data[1]));
	}
	
	// 게임룸 생성 함수
	public int createRoom(String id, String roomName, String level){
		if(this.getPlayer(id) == null){
			return -1;
		}
		int roomNumber=0;
		boolean findNumber = false;
		while(true){
			roomNumber++;
			for(SSRoom room : m_room){
				if(room.getRoomNum() == roomNumber){
					findNumber = true;
					break;
				}
			}
			if(findNumber == false){
				break;
			}else{
				findNumber = false;
			}
		}
		m_room.add(new SSRoom(roomNumber, MAX_PLAYER, roomName, lobby, level));
		return roomNumber;
	}
	
	// 게임룸 삭제 함수
	public void deleteRoom(SSRoom room){
		m_room.remove(room);
	}
	
	public void moveManager(String id, SSSocketManager manager){
		for(SSPlayer player : m_waitPlayer){
			if(player.getId().equals(id)){
				player.addSocketManager(manager.getSocketManagerName(), manager);
			}
		}
	}
	
	public void addSocketManager(SSSocketManager manager) {
		m_socketManager.add(manager);
	}
	
	// Getters and Setters
	public SSPlayer getPlayer(String id) {
		for (SSPlayer player : m_waitPlayer) {
			if (player.getId().equals(id)) {
				return player;
			}
		}
		for (SSRoom room : m_room) {
			Vector<SSPlayer> players = room.getPlayerList();
			for (SSPlayer player : players) {
				if (player.getId().equals(id)) {
					return player;
				}
			}
		}
		return null;
	}

	public SSRoom getRoom(int roomNumber) {
		for (SSRoom room : m_room) {
			if (room.getRoomNum() == roomNumber) {
				return room;
			}
		}
		return null;
	}

	public int getConnectionNumber() {
		return m_socketManager.size();
	}

	public Vector<SSPlayer> getWaitPlayerList() {
		return m_waitPlayer;
	}

	public Vector<SSRoom> getRoomList() {
		return m_room;
	}

	public Vector<SSSocketManager> getSocketManager() {
		return m_socketManager;
	}

	public DBManager getDBManager() {
		return m_dbManager;
	}

	public static SSLobby getLobby() {
		return lobby;
	}
}
