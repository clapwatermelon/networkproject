package mju.cn.server.controller;

import mju.cn.server.player.SSPlayer;
import mju.cn.server.room.SSLobby;

public class SSLoginController {

	public SSLoginController() {
	}

	// 로그인 함수
	public Object[] login(String id, String pw) {
		SSLobby lobby = SSLobby.getLobby();
		String name = null;
		String avatar = null;
		int exp = 0;
		boolean dBValid = false;
		boolean loginValid = true;
		boolean valid = false;
		synchronized (lobby) {
			// 유효한 id,pw인지 검사함
			dBValid = lobby.getDBManager().isValidPlayer(id, pw);
			for (SSPlayer player : lobby.getWaitPlayerList()) {
				if (player.getId().equals(id)) {
					loginValid = false;
				}
			}
			// DB의 유효여부와 현재 접속자인지 여부 검사
			if (dBValid && loginValid) {
				Object[] data = lobby.getDBManager().getRecord(id);
				name = (String) data[0];
				avatar = (String) data[3];
				exp = (Integer) data[1];
				lobby.createPlayer(id);
				valid = true;
			}
		}

		return new Object[] { new Boolean(valid), id, name, avatar, exp };
	}

}
