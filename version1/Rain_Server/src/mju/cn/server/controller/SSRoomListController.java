package mju.cn.server.controller;

import java.util.Vector;

import mju.cn.common.ResponsePacket;
import mju.cn.server.network.SSSocketManager;
import mju.cn.server.player.SSPlayer;
import mju.cn.server.room.SSLobby;
import mju.cn.server.room.SSRoom;

public class SSRoomListController {

	public SSRoomListController() {

	}

	// 방 목록 호출 함수
	public Object[] getRoomList() {
		SSLobby lobby = SSLobby.getLobby();
		Vector<Object[]> list = new Vector<Object[]>();
		synchronized (lobby) {
			for (SSRoom room : lobby.getRoomList()) {
				list.add(new Object[] { room.getRoomNum(),
						room.getPlayerList().size(), room.getRoomSubject(),
						room.getMaxPlayer(), room.isStart() });
			}
		}
		return new Object[] { list };
	}

	public void getList(String empty) {
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("getList");
		packet.setArgs(this.getRoomList());
		SSLobby lobby = SSLobby.getLobby();
		synchronized (lobby) {
			for (SSPlayer player : lobby.getWaitPlayerList()) {
				SSSocketManager manager = player
						.getSocketManager("SSRoomListController");
				if (manager != null) {
					manager.send(packet);
				}
			}
		}
	}

	// 게임룸 생성 함수
	public Object[] createRoom(String id, String roomName, String level) {
		SSLobby lobby = SSLobby.getLobby();
		int roomNumber;
		synchronized (lobby) {
			roomNumber = lobby.createRoom(id, roomName, level);
			this.getList(null);
		}
		if (roomNumber < 0) {
			return new Object[] { new Boolean(false) };
		} else {
			return new Object[] { new Boolean(true), roomNumber };
		}
	}

	// 게임 입장 훔수
	@SuppressWarnings("unused")
	public Object[] enterRoom(String id, int roomNumber) {
		SSLobby lobby = SSLobby.getLobby();
		SSRoom room;
		synchronized (lobby) {
			room = lobby.getRoom(roomNumber);
			int time = room.getPlayTime();

			if (room == null) {
				return new Object[] { new Boolean(false) };
			} else {
				// 로비에서 id로 사용자를 찾아 방에 입장시킴
				room.enterRoom(lobby.getPlayer(id));
				SSPlayerListController playlistController = new SSPlayerListController();
				SSGameController gameController = new SSGameController();
				gameController.addPlayer(id, roomNumber);
				playlistController.getList(null);
				this.getList(null);
				return new Object[] { new Boolean(true), roomNumber, time };
			}
		}
	}

	// 게임 퇴장 함수
	public Object[] exitRoom(String id, int roomNumber) {
		SSLobby lobby = SSLobby.getLobby();
		SSRoom room;
		synchronized (lobby) {
			room = lobby.getRoom(roomNumber);
			if (room == null) {
				return new Object[] { new Boolean(false) };
			} else {
				// 로비에서 id로 사용자를 찾아 방에서 나감
				boolean isRoomExist = room.exitRoom(lobby.getPlayer(id));
				if (isRoomExist) {
					SSGameController gameController = new SSGameController();
					gameController.delPlayer(id, roomNumber);
				}
				SSPlayerListController playlistController = new SSPlayerListController();
				playlistController.getList(null);
				this.getList(null);
				return new Object[] { new Boolean(true), roomNumber,
						new Boolean(isRoomExist) };
			}
		}
	}
}
