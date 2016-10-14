import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDB {
	
	public static void main(String[] args) {
		try {
			final String JDBC_DRIVER = "com.mysql.jdbc.Driver";   
			Class.forName(JDBC_DRIVER);
			
			final String DB_URL = "jdbc:mysql://localhost/SnookerResult";
			final String USER = "root";
			final String PASS = "";
			final String URL = "jdbc:mysql://localhost/SnookerResult;";

			
			DriverManager.getConnection(URL, USER, PASS);
			System.out.println("Connected!");
			

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
