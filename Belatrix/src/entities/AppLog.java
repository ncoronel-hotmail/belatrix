package entities;

/**
 * 
 * @author ncoronel
 *
 * POJO/Entidad para representar a la tabla app_log
 */
public class AppLog {
	
	private Integer logId; //log_id
	private String logCode; //log_code
	private String logMsg; //log_msg
	
	public AppLog() {
		
	}
	
	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getLogMsg() {
		return logMsg;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}

	public String getLogCode() {
		return logCode;
	}

	public void setLogCode(String logCode) {
		this.logCode = logCode;
	}
}
