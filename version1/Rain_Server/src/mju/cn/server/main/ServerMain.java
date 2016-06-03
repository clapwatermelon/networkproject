package mju.cn.server.main;

import mju.cn.server.db.SQLiteManager;
import mju.cn.server.network.SSServerMananger;

public class ServerMain {
	public static void main(String dd[]) {
		  SQLiteManager s=new SQLiteManager();
	      
//	      s.createTable();
//	      s.InsertWord();
//	      s.InsertDB();
//	      ---------------------------여기까지는 한번만 선언해주면되
		SSServerMananger con = new SSServerMananger();
		con.startServer();
	}
}
