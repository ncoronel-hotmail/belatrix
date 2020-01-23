package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import config.Configuration;

/**
 * 
 * @author ncoronel
 * 
 * Clase estatica para manejar la conexion a la base de datos
 */
public class DbConnection {
	
	/** La conexion a la base de datos */
	private static Connection con = null;
	
	/**
	 * Genera la conexion a la base de datos
	 * @return La conexion a la base de datos
	 */
	public static Connection getConnection() {
		if (con == null) {
			try {
				Class.forName(Configuration.getDbDriver());
				con = DriverManager.getConnection(Configuration.getDbUrl(), Configuration.getDbUser(), Configuration.getDbPass()); 
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return con;
	}
	
	/**
	 * Cierra la conexion con la base de datos
	 */
	public static void closeConnection() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		con = null;
	}
	
}
