package mju.cn.client.main;

import mju.cn.client.gui.SCMainFrame;


public class ClientMain {
	public static void main(String dd[]) throws Exception {

		SCMainFrame frame = new SCMainFrame();
		frame.init("localhost");
	}
}