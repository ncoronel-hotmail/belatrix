package jobs;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import config.Configuration;
import constants.LogConstant;
import interfaces.ILogType;
import loggers.ConsoleLogger;
import loggers.DatabaseLogger;
import loggers.FileLogger;

/**
 * 
 * @author ncoronel
 *
 * Clase de metodos estaticos para loguear mensajes
 */
public class JobLogger {
	
	/** Mapa que contiene el tipo de log como clave y la implementacion del tipo correspondiente como valor */
	private static final Map<Integer, ILogType> logTypes = getLogTypes();
	
	/** Mapa que contiene el tipo de mensaje como clave y el objeto Level correspondiente como valor */
	private static final Map<Integer, Level> logLevels = getLogLevels();
	
	/** Set que contiene los tipos donde se loguearan los mensajes */
	private static Set<ILogType> currentLogTypes = new LinkedHashSet<>();
	
	/** Set con los tipos de mensajes que va a aceptar el logger */
	private static Set<Integer> msgSettings;
	
	/** Set con los tipos de logs que va a aceptar el logger */
	private static Set<Integer> typeLogSettings;
	
	/**
	 * Loguea un mensaje de tipo Message
	 * @param message: Mensaje a loguear
	 * @throws Exception: Tomada del metodo validateLog
	 */
	public static void logMessage(String message) throws Exception {
		log(message, LogConstant.MSG_MESSAGE, "MESSAGE");
	}
	
	/**
	 * Loguea un mensaje de tipo Warning
	 * @param message: Mensaje a loguear
	 * @throws Exception: Tomada del metodo validateLog
	 */
	public static void logWarning(String message) throws Exception {
		log(message, LogConstant.MSG_WARNING, "WARNING");
	}
	
	/**
	 * Loguea un mensaje de tipo Error
	 * @param message: Mensaje a loguear
	 * @throws Exception: Tomada del metodo validateLog
	 */
	public static void logError(String message) throws Exception {
		log(message, LogConstant.MSG_ERROR, "ERROR");
	}
	
	private static void log(String message, Integer msgType, String logMsgType) throws Exception{
		validateLog(message, msgType, logMsgType);
		
		//Se llama al metodo log de la clases correspondientes
		currentLogTypes.forEach((i) -> i.log(message, logLevels.get(msgType)));
	}
	
	/**
	 * Valida que el mensaje y los seteos del JobLogger sean correctos
	 * @param message: mensaje a loguear
	 * @param msgType: tipo numerico de mensaje a loguear
	 * @param logMsgType: el nombre del tipo de mensaje a loguear
	 * @throws Exception:
	 *    1. Cuando no hay tipos de log seteados.
	 *    2. Cuando no hay tipos de mensaje seteados.
	 *    3. Cuando el mensaje esta vacio.
	 */
	private static void validateLog(String message, Integer msgType, String logMsgType) throws Exception{
		if (currentLogTypes.size() == 0) {
			throw new Exception ("Invalid configuration");
		}
		
		if (msgSettings == null || !msgSettings.contains(msgType)) {
			throw new Exception (logMsgType + " messages must be configured");
		}
		
		if (message == null || message.trim().length() == 0) {
			throw new Exception("Message cannot be empty");
		}
		
	}
	
	/**
	 * Crea un mapa con todos los tipos de logs disponibles.
	 * @return: Mapa completo con todos los tipos de logs disponibles.
	 */
	private static final Map<Integer, ILogType> getLogTypes(){
		Map<Integer, ILogType> methods = new HashMap<>();
		
		ILogType fileLogger = new FileLogger(Configuration.getLogFilePath());
		
		methods.put(LogConstant.LOG_FILE,fileLogger );
		methods.put(LogConstant.LOG_CONSOLE, new ConsoleLogger());
		methods.put(LogConstant.LOG_DATABASE, new DatabaseLogger());
		
		return methods;
	}
	
	/**
	 * Crea un mapa con los niveles de log aceptados.
	 * @return; Mapa de los niveles de log aceptados.
	 */
	private static final Map<Integer, Level> getLogLevels(){
		Map<Integer, Level> levels = new HashMap<>();
		
		levels.put(LogConstant.MSG_MESSAGE, Level.INFO);
		levels.put(LogConstant.MSG_WARNING, Level.WARNING);
		levels.put(LogConstant.MSG_ERROR, Level.SEVERE);
		
		return levels;
	}
	
	/**
	 * Setter para especificar que tipos de mensajes se van a loguear
	 * @param msgSettings: Set con los tipos de mensajes que se quieren loguear.
	 */
	public static void setMsgSettings(Set<Integer> msgSettings) {
		JobLogger.msgSettings = msgSettings;
	}

	/**
	 * Setter para especificar que tipos de log se van a utilizar
	 * @param typeSettings: Set con los tipos de log que se quieren utilizar.
	 */
	public static void setTypeLogSettings(Set<Integer> typeLogSettings) {
		
			currentLogTypes = new LinkedHashSet<>(); 
		
			JobLogger.typeLogSettings = typeLogSettings;

		
		
		//Se llena el set de los tipos de log que se van a utilizar con los que se especificaron por parametro
		if (JobLogger.typeLogSettings != null && JobLogger.typeLogSettings.size() > 0) {
			JobLogger.typeLogSettings.forEach((i) -> {
				if (logTypes.containsKey(i)) {
					currentLogTypes.add(logTypes.get(i));
				}
			});
		}
	}
	
	public static boolean getDatabaseLoggerStatus() {
		if (currentLogTypes == null || !currentLogTypes.contains(logTypes.get(LogConstant.LOG_DATABASE))) {
			return false;
		}
		
		DatabaseLogger dbl = (DatabaseLogger) logTypes.get(LogConstant.LOG_DATABASE);
		return dbl.isOk();
	}
	
}
