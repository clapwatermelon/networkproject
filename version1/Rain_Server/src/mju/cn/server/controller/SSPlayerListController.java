package mju.cn.server.controller;

import java.util.Vector;

import mju.cn.common.ResponsePacket;
import mju.cn.server.network.SSSocketManager;
import mju.cn.server.player.SSPlayer;
import mju.cn.server.room.SSLobby;

public class SSPlayerListController {

	public SSPlayerListController() {

	}

	// 플레이어 목록 호출 함수
	public Object[] getPlayerList() {
		SSLobby lobby = SSLobby.getLobby();
		Vector<Object[]> list = new Vector<Object[]>();
		synchronized (lobby) {
			for (SSPlayer player : lobby.getWaitPlayerList()) {
				list.add(new Object[] { player.getId(), player.getName(),
						player.getAvatar(), player.getExp() });
			}
		}
		return new Object[] { list };
	}

	public void getList(String empty) {
		ResponsePacket packet = new ResponsePacket();
		packet.setResponseType("getList");
		packet.setArgs(this.getPlayerList());
		SSLobby lobby = SSLobby.getLobby();
		synchronized (lobby) {
			for (SSPlayer player : lobby.getWaitPlayerList()) {
				SSSocketManager manager = player
						.getSocketManager("SSPlayerListController");
				if (manager != null) {
					manager.send(packet);
				}
			}
		}
	}
}
