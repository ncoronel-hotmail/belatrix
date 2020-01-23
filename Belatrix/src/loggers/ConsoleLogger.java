package loggers;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import interfaces.ILogType;

/**
 * 
 * @author ncoronel
 *
 * Clase para loguear a la consola
 */
public class ConsoleLogger implements ILogType {

	/** Logger de la clase */
	private Logger logger = Logger.getLogger("consoleLogger");
	
	/**
	 * Constructor de la clase
	 */
	public ConsoleLogger() {
		logger.setUseParentHandlers(false); //Deshabilito la consola heredada del padre
		logger.addHandler(new ConsoleHandler()); //Le agrego su propia consola
	}
	
	@Override
	public void log(String msg, Level lvl) {
		this.logger.log(lvl, msg);
	}

}
