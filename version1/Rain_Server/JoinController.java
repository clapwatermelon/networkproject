package kr.ac.mju.cn.server.controller;

import mju.cn.server.room.BSLobby;


public class JoinController {

	public JoinController() {
	}

	//회원가입
	public Object[] submit(String id, String pw, String name, String avatarName) {
		BSLobby lobby = BSLobby.getLobby();
		boolean valid;
		synchronized(lobby){
			//lobby의 회원가입 메소드가 호출
			valid = lobby.getDBManager().enrollPlayer(id, pw, name, avatarName);
		}
		return new Object[] {valid};
	}
}
