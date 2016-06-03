package mju.cn.server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import mju.cn.common.Constant;
import mju.cn.server.room.SSLobby;

public class SSServerMananger {

	private ServerSocket m_serverSocket;
	private SSLobby m_lobby;

	private boolean isRunning = true;

	public SSServerMananger() {
		m_lobby = SSLobby.getLobby();
		try {
			m_serverSocket = new ServerSocket(Constant.SERVER_PORT, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startServer() {
		System.out.println("서버가 시작되었습니다.");

		while (isRunning) {
			try {
				Socket socket = m_serverSocket.accept();
				SSSocketManager manager = new SSSocketManager(socket, this);
				m_lobby.addSocketManager(manager);
				manager.start();
			} catch (IOException e) {
				e.printStackTrace();
				isRunning = false;
			}
		}
	}

	public SSLobby getLobby() {
		return m_lobby;
	}
}
