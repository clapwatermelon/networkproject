package mju.cn.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class HSQLManager {

	private static Connection conn;

	public HSQLManager() {
	}

	public static void openConnection() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			conn = DriverManager.getConnection("jdbc:hsqldb:file:save/sdb",
					"sa", "");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void shutdown() {
		try {
			if (!conn.isClosed()) {
				Statement st = conn.createStatement();
				st.execute("SHUTDOWN");
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet query(String expression) {
		Statement st = null;
		try {
			st = conn.createStatement();
			return st.executeQuery(expression); // run the query
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static ResultSet query(PreparedStatement pstmt) {

		try {
			return pstmt.executeQuery(); // run the query
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static PreparedStatement getPreparedStatement(String pstmt) {
		try {
			return conn.prepareStatement(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void createTable(String tableName, String[] columnNames,
			String[] columnTypes) {
		Statement st = null;
		String expression = "CREATE TABLE " + tableName
				+ " ( id INTEGER IDENTITY";
		for (int i = 0; i < columnNames.length; i++) {
			expression += ", " + columnNames[i] + " " + columnTypes[i];
		}
		expression += ")";
		try {
			st = conn.createStatement();
			st.executeUpdate(expression); // run the query
		} catch (SQLException e) {
			System.out.println("db error : " + expression);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// use for SQL commands CREATE, DROP, INSERT and UPDATE
	public static int update(PreparedStatement psmt) {

		try {
			return psmt.executeUpdate(); // run the query
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("db error : " + psmt);
		} finally {
			try {
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public static void update(String expression) {

		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(expression); // run the query
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("db error : " + expression);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void printDump(ResultSet rs) {

		ResultSetMetaData meta;
		try {
			meta = rs.getMetaData();
			int colmax = meta.getColumnCount();
			int i;
			Object o = null;
			for (int index = 0; index < colmax; index++) {
				System.out.print(meta.getColumnName(index + 1) + " ");
			}
			System.out.println("");

			for (; rs.next();) {
				for (i = 0; i < colmax; ++i) {
					o = rs.getObject(i + 1);
					System.out.print(o.toString() + " ");
				}
				System.out.println(" ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
