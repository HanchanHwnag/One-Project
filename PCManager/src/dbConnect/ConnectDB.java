package dbConnect;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {

	public static Connection conn;
	
	static{
		try {
			Class.forName("oracle.jdbc.OracleDriver");

			String url = "jdbc:oracle:thin:@203.236.209.49:1521:xe";

			String user = "hr";
			String password = "1111";
			conn = DriverManager.getConnection(url, user, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ConnectDB() {
		
	
	}
}
