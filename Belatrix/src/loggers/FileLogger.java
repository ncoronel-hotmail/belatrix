package loggers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import interfaces.ILogType;

/**
 * 
 * @author ncoronel
 *
 * Clase para loguear a un archivo
 */
public class FileLogger implements ILogType {

	/** Logger de la clase */
	private Logger logger = Logger.getLogger("fileLogger");
	
	/**
	 * Constructor de la clase
	 * @param path: El path del archivo donde se va a loguear
	 */
	public FileLogger(String path) {
		logger.setUseParentHandlers(false); //Deshabilito la consola del padre
		try {
			FileHandler fh = new FileHandler(path, true); //Par�metro true para que haga append, ya contempla si existe o no
			fh.setFormatter(new SimpleFormatter()); //Loguea como en la consola, si no lo hace como xml
			logger.addHandler(fh);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void log(String msg, Level lvl) {
		this.logger.log(lvl, msg);
	}

}
