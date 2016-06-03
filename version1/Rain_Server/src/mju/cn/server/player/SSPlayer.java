package mju.cn.server.player;

import java.util.Collection;
import java.util.HashMap;

import mju.cn.server.db.DBManager;
import mju.cn.server.network.SSSocketManager;
import mju.cn.server.room.SSRoom;

public class SSPlayer{
	// Attributes
	private SSRoom m_room; // 플레이어가 속한 방
	private String m_id; // 플레이어 ID
	private String m_name; // 플레이어 이름
	private String m_avatar; // 플레이어 아바타 식별자
	private int m_exp; // 경험치
	
	// Components
	private DBManager m_dbManager; // 경험치 저장을위한 메니져
	private HashMap<String, SSSocketManager> m_socketManager; // 플레이어에게 해당되는 소켓메니져
	
	// Constructor
	public SSPlayer(String id, String name, String avatar, int exp){
		m_id = id;
		m_name = name;
		m_avatar = avatar;
		m_exp = exp;
		m_socketManager = new HashMap<String, SSSocketManager>();
		m_dbManager = new DBManager();
		m_room = null;
		
	}
	
	// Initialization
	public void initInfo(){
		
	}
	
	// 소켓메니져 등록 함수
	public void addSocketManager(String managerName, SSSocketManager manager){
		m_socketManager.put(managerName, manager);
	}
	
	// 경험치 정보 저장 함수
	public void updateExp() {
		m_dbManager.updateExp(m_id, m_exp);
	}
	
	// 경험치 정보 저장 함수
	public void earnExp(int exp){
		m_exp += exp;
		updateExp();
	}
	
	// Getters and Setters
	public void removeSocketManager(SSSocketManager manager) {
		Collection<SSSocketManager> socketManager = m_socketManager.values();
		socketManager.remove(manager);
	}

	public SSSocketManager getSocketManager(String managerName) {
		return m_socketManager.get(managerName);
	}

	public String getId() {
		return m_id;
	}

	public void setId(String mId) {
		m_id = mId;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String mName) {
		m_name = mName;
	}

	public String getAvatar() {
		return m_avatar;
	}

	public void setAvatar(String mAvatar) {
		m_avatar = mAvatar;
	}

	public int getExp() {
		return m_exp;
	}

	public void setExp(int Exp) {
		m_exp = Exp;
	}

	public int getManagerNumber() {
		return m_socketManager.size();
	}

	public SSRoom getRoom() {
		return m_room;
	}

	public void setRoom(SSRoom room) {
		m_room = room;
	}
}
