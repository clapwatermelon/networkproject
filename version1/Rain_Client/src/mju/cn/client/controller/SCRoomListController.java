package mju.cn.client.controller;

import java.util.Vector;

import mju.cn.client.gui.SCContentPane;
import mju.cn.client.gui.panel.SCLobbyPanel;
import mju.cn.client.network.SCClientStub;
import mju.cn.common.RequestPacket;
import mju.cn.common.ResponsePacket;
import mju.cn.common.RequestPacket.SYNC_TYPE;


public class SCRoomListController extends SCClientStub {
	
	// Components
	private SCLobbyPanel m_lobbyPanel; // 로비패널

	// Constructor
	public SCRoomListController(String ip, SCLobbyPanel lobbyPanel) {
		super(ip);
		m_lobbyPanel = lobbyPanel;
	}

	// Initialization
	public void init(String id) {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSRoomListController");
		packet.setMethodName("init");
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
		packet.setArgs(new Object[] { id });
		this.send(packet);
	}

	// 리스트 획득 함수
	public void getList() {
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSRoomListController");
		packet.setMethodName("getList");
		packet.setArgs(new Object[] { "Empty String" });
		packet.setSyncType(SYNC_TYPE.ASYNCHRONOUS);

		this.send(packet);
	}

	// 서버측  getList 결과 처리 함수
	@SuppressWarnings("unchecked")
	private void resultGetList(ResponsePacket packet) {
		m_lobbyPanel.getRoomList().clear();
		Vector<Object[]> list = (Vector<Object[]>) (packet.getArgs()[0]);
		for (int i = 0; i < list.size(); i++) {
			m_lobbyPanel.addRoomList((Integer) list.get(i)[0],
					(Integer) list.get(i)[1], (String) list.get(i)[2],
					(Integer) list.get(i)[3], (Boolean) list.get(i)[4]);
		}
		m_lobbyPanel.getRoomListPanel().initMainPanel();
	}

	// 서버측 createRoom 결과 처리 함수
	private void resultCreateRoom(ResponsePacket packet) {
		boolean isCreate = (Boolean) packet.getArgs()[0];

		SCContentPane rootPane = m_lobbyPanel.getContentPane();
		rootPane.hideDialog();
		if (isCreate) {
			rootPane.getGameRoomPanel().getGameController()
					.getPlayerList((Integer) packet.getArgs()[1]);
			this.enterRoom(m_lobbyPanel.getUserId(),
					(Integer) packet.getArgs()[1]);
		} else {
			rootPane.showMessageDialog("방생성 실패했습니다.");
		}
	}

	// 서버측 enterRoom 결과 처리 함수
	private void resultEnterRoom(ResponsePacket packet) {
		boolean isEnter = (Boolean) packet.getArgs()[0];
		int playTime = (int) packet.getArgs()[2];

		SCContentPane rootPane = m_lobbyPanel.getContentPane();
		rootPane.hideDialog();
		if (isEnter) {
			rootPane.getGameRoomPanel().setRoomNumber(
					(Integer) packet.getArgs()[1]);
			rootPane.viewPanel(rootPane.getGameRoomPanel().getClass().getName());
			
			m_lobbyPanel.stopLobbySound(); //로비 sound stop
			m_lobbyPanel.startGameRoomSound(); //game room sound
			
			
		} else {
			rootPane.showMessageDialog("입장하지 못하였습니다.");
		}
	}

	// 서버측 exitRoom 결과 처리 함수
	private void resultExitRoom(ResponsePacket packet) {
		boolean isExit = (Boolean) packet.getArgs()[0];
		SCContentPane rootPane = m_lobbyPanel.getContentPane();
		rootPane.hideDialog();
		if (isExit) {
			rootPane.getGameRoomPanel().initSlot();
			rootPane.viewPanel(rootPane.getLobbyPanel().getClass().getName());
			m_lobbyPanel.stopGameRoomSound(); //game room sound stop
			m_lobbyPanel.startLobbySound(); //로비 sound start
			
		} else {
			rootPane.showMessageDialog("대기룸에 입장하지 못하였습니다.");
		}
	}

	// 방 생성 함수
	public void createRoom(String id, String roomName, String level) {
		SCContentPane rootPane = m_lobbyPanel.getContentPane();
		rootPane.showLoadingDialog("방을 생성 중입니다.");
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSRoomListController");
		packet.setMethodName("createRoom");
		packet.setArgs(new Object[] { id, roomName, level });
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);

		this.send(packet);
	}

	// 방 입장 함수
	public void enterRoom(String id, int roomNumber) {
		SCContentPane rootPane = m_lobbyPanel.getContentPane();
		rootPane.showLoadingDialog("방에 입장 중입니다.");
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSRoomListController");
		packet.setMethodName("enterRoom");
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
		packet.setArgs(new Object[] { id, roomNumber });
		this.send(packet);
	}

	// 방 나가기 함수
	public void exitRoom(String id, int roomNumber) {
		SCContentPane rootPane = m_lobbyPanel.getContentPane();
		rootPane.showLoadingDialog("대기방으로 입장 중입니다.");
		RequestPacket packet = new RequestPacket();
		packet.setClassName("SSRoomListController");
		packet.setMethodName("exitRoom");
		packet.setSyncType(SYNC_TYPE.SYNCHRONOUS);
		packet.setArgs(new Object[] { id, roomNumber });
		this.send(packet);
	}

	@Override
	public void run() {
		while (m_isConnected) {
			try {
				Object obj = inputStream.readObject();
				ResponsePacket responesPacket = (ResponsePacket) obj;
				if (responesPacket.getResponseType().equals("getList")) {
					this.resultGetList(responesPacket);
				} else if (responesPacket.getResponseType().equals("enterRoom")) {
					this.resultEnterRoom(responesPacket);
				} else if (responesPacket.getResponseType()
						.equals("createRoom")) {
					this.resultCreateRoom(responesPacket);
				} else if (responesPacket.getResponseType().equals("exitRoom")) {
					this.resultExitRoom(responesPacket);
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