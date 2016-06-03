package mju.cn.server.controller;

import mju.cn.common.ResponsePacket;
import mju.cn.server.network.SSSocketManager;
import mju.cn.server.player.SSPlayer;
import mju.cn.server.room.SSLobby;
import mju.cn.server.room.SSRoom;

public class SSGameChatController {

	public SSGameChatController() {
	}

	public void gameChat(int roomNumber, String id, String txt) {
		System.out.println(txt);
		SSLobby lobby = SSLobby.getLobby();
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("gameChat");
		packet.setArgs(new Object[] { id, txt });

		synchronized (lobby) {
			SSRoom room = lobby.getRoom(roomNumber);
			for (SSPlayer player : room.getPlayerList()) {
				SSSocketManager manager = player
						.getSocketManager("SSGameChatController");
				if (manager != null) {
					manager.send(packet);
				}
			}
		}
	}
}