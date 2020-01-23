package loggers;

import java.util.logging.Level;

import dao.AppLogDAO;
import entities.AppLog;
import interfaces.ILogType;

/**
 * 
 * @author ncoronel
 *
 * Clase para loguear a una base de datos
 */
public class DatabaseLogger implements ILogType {

	/**
	 * Atributo que se setea a true si la operacion de la base de datos fue exitosa
	 */
	private boolean ok;
	
	public DatabaseLogger() {
		this.ok = false;
	}
	
	/**
	 * Loguear a la base
	 */
	@Override
	public void log(String msg, Level lvl) {
		AppLog appLog = new AppLog();
		appLog.setLogCode(lvl.getName());
		appLog.setLogMsg(msg);
		
		AppLogDAO dao = new AppLogDAO();
		try {
			dao.insertLog(appLog);
			this.ok = appLog.getLogId() != null; //Si se inserto, contiene el id generado en la base
		} catch (Exception e) {
			e.printStackTrace();
			this.ok = false;
		}
	}

	public boolean isOk() {
		return ok;
	}

}
