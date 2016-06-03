package mju.cn.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import mju.cn.common.Constant;


public class SCClientStub extends Thread {

	protected Socket m_Socket;
	protected ObjectOutputStream outputStream;
	protected ObjectInputStream inputStream;
	protected boolean m_isConnected = true;

	public SCClientStub(String ip) {

		try {
			m_Socket = new Socket(ip, Constant.SERVER_PORT);
			outputStream = new ObjectOutputStream(m_Socket.getOutputStream());
			inputStream = new ObjectInputStream(m_Socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(Object obj) {
		try {
			outputStream.writeObject(obj);
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
