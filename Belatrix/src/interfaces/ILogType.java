package interfaces;

import java.util.logging.Level;

/**
 * 
 * @author ncoronel
 * 
 * Interface para representar los tipos de Log.
 */
public interface ILogType {
	
	/**
	 * Loguea el mensaje especificado en el nivel indicado.
	 * @param msg: El mensaje a loguear
	 * @param lvl: El nivel para el objeto Logger
	 * @throws Exception
	 */
	public void log(String msg, Level lvl);
	
}
