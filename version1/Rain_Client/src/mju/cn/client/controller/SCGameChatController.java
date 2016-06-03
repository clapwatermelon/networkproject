
package mju.cn.client.controller;

import mju.cn.client.gui.panel.SCGameRoomPanel;
import mju.cn.client.network.SCClientStub;
import mju.cn.common.RequestPacket;
import mju.cn.common.RequestPacket.SYNC_TYPE;
import mju.cn.common.ResponsePacket;


public class SCGameChatController extends SCClientStub {
	// Components
	private SCGameRoomPanel m_gameRoomPanel; 

	// Constructor
	public SCGameChatController(String ip, SCGameRoomPanel gameRoomPanel) {
		super(ip);
		m_gameRoomPanel = gameRoomPanel;
	}

	// Initialization
	public void init(String id) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSGameChatController");
		packet.setMethodName("init");
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
		packet.setArgs(new Object[] { id });
		this.send(packet);
	}

	public void gameChat(int roomNumber, String id, String txt) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSGameChatController");
		packet.setMethodName("gameChat");
		packet.setSyncType(SYNC_TYPE.ASYNCHRONOUS);
		packet.setArgs(new Object[] { roomNumber, id, txt });
		this.send(packet);
	}

	private void resultChat(ResponsePacket packet) {
		m_gameRoomPanel.getGameChatPanel().appendText((String) packet.getArgs()[0], (String) packet.getArgs()[1]);
	}

	@Override
	public void run() {
		while (m_isConnected) {
			try {
				Object obj = inputStream.readObject();
				ResponsePacket responesPacket = (ResponsePacket) obj;
				if (responesPacket.getResponseType().equals("gameChat")) {
					this.resultChat(responesPacket);
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
				m_isConnected = false;
			}
		}
	}
}