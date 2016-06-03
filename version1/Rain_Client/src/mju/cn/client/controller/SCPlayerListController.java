package mju.cn.client.controller;

import java.util.Vector;

import mju.cn.client.gui.panel.SCLobbyPanel;
import mju.cn.client.network.SCClientStub;
import mju.cn.common.RequestPacket;
import mju.cn.common.ResponsePacket;
import mju.cn.common.RequestPacket.SYNC_TYPE;

public class SCPlayerListController extends SCClientStub {

	// Components
	private SCLobbyPanel m_lobbyPanel; // 로비 패널

	// Constructor
	public SCPlayerListController(String ip, SCLobbyPanel lobbyPanel) {
		super(ip);
		m_lobbyPanel = lobbyPanel;
	}

	// Initialization
	public void init(String id) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSPlayerListController");
		packet.setMethodName("init");
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
		packet.setArgs(new Object[] { id });
		this.send(packet);
	}

	// 리스트 획득 함수
	public void getList() {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSPlayerListController");
		packet.setMethodName("getList");
		packet.setArgs(new Object[] { "Empty String" });
		packet.setSyncType(SYNC_TYPE.ASYNCHRONOUS);

		this.send(packet);
	}

	// 서버측 getList 결과 처리 함수
	@SuppressWarnings("unchecked")
	private void resultGetList(ResponsePacket packet) {
		m_lobbyPanel.getPlayerList().clear();
		Vector<Object[]> list = (Vector<Object[]>) (packet.getArgs()[0]);
		for (int i = 0; i < list.size(); i++) {
			m_lobbyPanel.addPlayerList((String) list.get(i)[0],
					(String) list.get(i)[1], (String) list.get(i)[2],
					(Integer) list.get(i)[3]);

		}
		m_lobbyPanel.getPlayListPanel().initMainPanel();

	}

	@Override
	public void run() {
		while (m_isConnected) {
			try {
				Object obj = inputStream.readObject();
				ResponsePacket responesPacket = (ResponsePacket) obj;
				if (responesPacket.getResponseType().equals("getList")) {
					// 가입 응답 처리
					this.resultGetList(responesPacket);
				} else {
					// 모르는 패킷은 버린다.
				}
			} catch (Exception e) {
				e.printStackTrace();
				m_isConnected = false;
			}
		}
	}

}