package mju.cn.server.controller;

import mju.cn.server.room.SSLobby;

public class SSJoinController {

	public SSJoinController() {
	}

	// 회원가입 함수
	public Object[] submit(String id, String pw, String name, String avatarName) {
		SSLobby lobby = SSLobby.getLobby();
		boolean valid;
		synchronized(lobby){
			//lobby의 회원가입 메소드가 호출
			valid = lobby.getDBManager().enrollPlayer(id, pw, name, avatarName);
		}
		return new Object[] {valid};
	}
}
