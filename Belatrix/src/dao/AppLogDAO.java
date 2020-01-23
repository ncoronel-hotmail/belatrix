package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DbConnection;
import entities.AppLog;

/**
 * 
 * @author ncoronel
 *
 * DAO para operaciones en la tabla app_log
 */
public class AppLogDAO {

	public AppLogDAO() {

	}

	/**
	 * Inserta un nuevo registro en la tabla app_log
	 * @param appLog: entidad a insertar
	 * @throws Exception: si fallo la insercion o no se inserto nada
	 */
	public void insertLog(AppLog appLog) throws Exception {
		PreparedStatement pstmt;
		try {
			pstmt = DbConnection.getConnection().prepareStatement("INSERT INTO APP_LOG VALUES(?,?,?)", 1);
			pstmt.setInt(1, 0);
			pstmt.setString(2, appLog.getLogCode());
			pstmt.setString(3, appLog.getLogMsg());

			pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				appLog.setLogId(rs.getInt(1));
			}else {
				throw new Exception("Insert not executed");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DbConnection.closeConnection();
		}
	}

}
