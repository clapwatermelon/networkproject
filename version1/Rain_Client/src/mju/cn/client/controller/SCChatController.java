
package mju.cn.client.controller;

import mju.cn.client.gui.panel.SCLobbyPanel;
import mju.cn.client.network.SCClientStub;
import mju.cn.common.RequestPacket;
import mju.cn.common.ResponsePacket;
import mju.cn.common.RequestPacket.SYNC_TYPE;


public class SCChatController extends SCClientStub {

	// Components
	private SCLobbyPanel m_lobbyPanel; // 로비화면 패널

	// Constructor
	public SCChatController(String ip, SCLobbyPanel lobbyPanel) {
		super(ip);
		m_lobbyPanel = lobbyPanel;
	}

	// Initialization
	public void init(String id) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSChatController");
		packet.setMethodName("init");
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
		packet.setArgs(new Object[] { id });
		this.send(packet);
	}

	// 채팅 전송 함수
	public void chat(String id, String txt) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSChatController");
		packet.setMethodName("chat");
		packet.setSyncType(SYNC_TYPE.ASYNCHRONOUS);
		packet.setArgs(new Object[] { id, txt });
		this.send(packet);
	}

	// 서버측 chat 결과 처리 함수
	private void resultChat(ResponsePacket packet) {
		m_lobbyPanel.getChatPanel().appendText((String) packet.getArgs()[0],
				(String) packet.getArgs()[1]);
	}

	@Override
	public void run() {
		while (m_isConnected) {
			try {
				Object obj = inputStream.readObject();
				ResponsePacket responesPacket = (ResponsePacket) obj;
				if (responesPacket.getResponseType().equals("chat")) {
					// 가입 응답 처리
					this.resultChat(responesPacket);
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