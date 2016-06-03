package mju.cn.server.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

import mju.cn.common.ResponsePacket;
import mju.cn.common.RequestPacket;
import mju.cn.common.RequestPacket.SYNC_TYPE;
import mju.cn.server.room.SSLobby;


public class SSSocketManager extends Thread implements Runnable {

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private String m_managerName;

	private boolean isConnected = true;

	private SSServerMananger parent;

	public SSSocketManager(Socket socket, SSServerMananger parent) {
		this.parent = parent;

		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(Object obj) {
		try {
			outputStream.writeObject(obj);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initPlayer(String id) {
		SSLobby lobby = SSLobby.getLobby();
		synchronized (lobby) {
			lobby.moveManager(id, this);
		}
	}

	@Override
	public void run() {

		while (isConnected) {
			try {
				Object obj = inputStream.readObject();
				RequestPacket requestPacket = (RequestPacket) obj;
				System.out.println("server get " + requestPacket.getMethodName());
				if (m_managerName == null) {
					m_managerName = requestPacket.getClassName();
				}

				if (requestPacket.getMethodName().equals("init")) {
					this.initPlayer((String) requestPacket.getArgs()[0]);
					continue;
				}

				Class<?> clz = ClassLoader.getSystemClassLoader().loadClass(
						"mju.cn.server.controller." + m_managerName);
				Object classObj = clz.newInstance();
				Method[] methods = clz.getMethods();
				Method targetMethod = null;

				for (Method iteratorMethod : methods) {
					if (iteratorMethod.getName().equals(
							requestPacket.getMethodName())) {
						targetMethod = iteratorMethod;
					}
				}

				if (requestPacket.getSyncType() == SYNC_TYPE.SYNCHRONOUS) {
					Object[] returnValue = (Object[]) targetMethod.invoke(
							classObj, requestPacket.getArgs());
					ResponsePacket responsePacket = new ResponsePacket(
							requestPacket.getMethodName(), returnValue);
					outputStream.writeObject(responsePacket);
				} else {
					targetMethod.invoke(classObj, requestPacket.getArgs());
				}
			} catch (Exception e) {
			System.out.println("클라이언트 접속 종료 : " + m_managerName + " : "
						+ e.getMessage());
//				e.printStackTrace();
				parent.getLobby().removeSocketManager(this);
			isConnected = false;
			}
		}

	}

	public String getSocketManagerName() {
		return m_managerName;
	}
}
