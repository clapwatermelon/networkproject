package mju.cn.server.controller;

import mju.cn.common.ResponsePacket;
import mju.cn.server.player.SSPlayer;
import mju.cn.server.room.SSLobby;

public class SSChatController {

	public SSChatController() {
	}

	// 채팅 메소드
	public void chat(String id, String txt) {
		SSLobby lobby = SSLobby.getLobby();
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("chat");
		packet.setArgs(new Object[] { id, txt });
		synchronized (lobby) {
			for (SSPlayer player : lobby.getWaitPlayerList()) {
				player.getSocketManager("SSChatController").send(packet);
			}
		}
	}
}