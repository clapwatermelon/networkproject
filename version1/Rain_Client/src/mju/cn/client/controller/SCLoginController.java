package mju.cn.client.controller;

import mju.cn.client.gui.SCContentPane;
import mju.cn.client.gui.panel.SCLoginPanel;
import mju.cn.client.network.SCClientStub;
import mju.cn.common.RequestPacket;
import mju.cn.common.ResponsePacket;
import mju.cn.common.RequestPacket.SYNC_TYPE;

public class SCLoginController extends SCClientStub {
	// Components
	SCLoginPanel m_loginPanel; // 로그인패널

	// Constructor
	public SCLoginController(String ip, SCLoginPanel loginPanel) {
		super(ip);
		m_loginPanel = loginPanel;
	}

	// 로그인 함수
	public void login(String id, String pw) {
		m_loginPanel.getContentPane().showLoadingDialog("로그인 중 입니다.");
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSLoginController");
		packet.setMethodName("login");
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
		packet.setArgs(new Object[] { id, pw });
		this.send(packet);
	}

	// 서버측 login 결과 처리 함수
	private void resultLogin(ResponsePacket packet) {
		boolean success = (Boolean) packet.getArgs()[0];
		SCContentPane parent = m_loginPanel.getContentPane();
		if (success) {

			parent.getLobbyPanel().initManager((String) packet.getArgs()[1]);
			parent.getLobbyPanel().initUserInfo((String) packet.getArgs()[1],
					(String) packet.getArgs()[2], (String) packet.getArgs()[3],
					(Integer) packet.getArgs()[4]);
			parent.viewPanel(parent.getLobbyPanel().getClass().getName());
			parent.hideDialog();
			parent.getLobbyPanel().getPlayerListController().getList();
			parent.getLobbyPanel().getRoomListController().getList();
			parent.getLoginPanel().playSound("stopbgm");; //로그인bgm끄기
			parent.getLobbyPanel().startLobbySound(); //로비 bgm
			m_loginPanel.getContentPane().showMessageDialog("로그인에 성공 하였습니다.");
		} else {
			m_loginPanel.getContentPane().hideDialog();
			m_loginPanel.getContentPane().showMessageDialog("로그인에 실패 하였습니다.");
		}

	}

	@Override
	public void run() {

		while (m_isConnected) {
			try {
				Object obj = inputStream.readObject();
				ResponsePacket responesPacket = (ResponsePacket) obj;
				if (responesPacket.getResponseType().equals("login")) {
					// 로그인 응답 처리
					this.resultLogin(responesPacket);
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