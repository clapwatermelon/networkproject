package mju.cn.server.db;

import java.sql.*;

public class DBManager {

	public DBManager() {
	}

	// 돈을 DB에 저장
	public void updateExp(String id, int exp) {
		if (hasId(id)) {
			HSQLManager.openConnection();
			HSQLManager.update("update players set exp = " + exp
					+ " where uid = '" + id + "'");
			HSQLManager.shutdown();
		} else
			System.out.println("id없음");
	}

	// 검색한 아이디가 존재 하는지 여부
	public boolean hasId(String id) { // ID중복여부 검사
		HSQLManager.openConnection();
		ResultSet rs = HSQLManager
				.query("select count(*)FROM players WHERE UID = '" + id + "'");
		try {
			while (rs.next())
				if (rs.getInt(1) == 0) {
					rs.close();
					return false;
				} else {
					rs.close();
					return true;
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HSQLManager.shutdown();
		return false;
	}

	// 레코드를 가져온다.
	public Object[] getRecord(String id) {
		HSQLManager.openConnection();
		ResultSet rs;
		Object[] returnValue = new Object[4];
		rs = HSQLManager
				.query("SELECT NAME,EXP,LEVEL,AVATAR FROM players WHERE UID = '"
						+ id + "'");
		try {
			while (rs.next()) {
				returnValue[0] = rs.getObject(1);
				returnValue[1] = rs.getInt(2);
				returnValue[2] = rs.getObject(3);
				returnValue[3] = rs.getObject(4);
			}
			rs.close();
		} catch (SQLException e) {
		}
		HSQLManager.shutdown();

		return returnValue;
	}

	// 가입을 한다.
	public boolean enrollPlayer(String id, String pw, String name, String avatar) { // 회원가입
																					// 실패시
																					// false리턴
		if (hasId(id)) {
			return false;
		} else {
			HSQLManager.openConnection();
			// 디폴트값 존재함
			HSQLManager
					.update("insert into players(UID,PW,NAME,EXP,LEVEL,AVATAR) values ('"
							+ id
							+ "','"
							+ pw
							+ "','"
							+ name
							+ "',0,1,'"
							+ avatar + "')");
			HSQLManager.shutdown();
			return true;
		}

	}

	// 합당한 플레이어접근인가?
	public boolean isValidPlayer(String uid, String pw) { // 비밀번호 확인
		ResultSet rs;
		try {
			if (hasId(uid)) {
				HSQLManager.openConnection();
				rs = HSQLManager.query("SELECT PW FROM players WHERE UID = '"
						+ uid + "'");
				while (rs.next()) {
					if (rs.getObject(1).equals(pw)) {
						return true;
					} else {
						return false;
					}
				}
				rs.close();
				HSQLManager.shutdown();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
