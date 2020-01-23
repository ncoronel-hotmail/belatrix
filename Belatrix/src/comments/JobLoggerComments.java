package comments;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobLoggerComments {
	
	 
	private static boolean logToFile;
	private static boolean logToConsole;
	private static boolean logMessage;
	private static boolean logWarning;
	private static boolean logError;
	private static boolean logToDatabase;
	private boolean initialized;//1-Se declara varias que nunca se usa.
	private static Map dbParams;
	private static Logger logger;

	public JobLoggerComments(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) {
		logger = Logger.getLogger("MyLog");
		
		logError = logErrorParam;
		logMessage = logMessageParam;
		logWarning = logWarningParam;
		
		logToDatabase = logToDatabaseParam;
		logToFile = logToFileParam;
		logToConsole = logToConsoleParam;
		
		dbParams = dbParamsMap;
	} 
	/**
	 * 2- No haria falta crear un constructor ya que todos los metodos son static se podrian pasar los valores sin instancia la clase.
	 * 3- Los parametros de la base de datos seria mejor leerlo de una archivo .properties seria mas seguro tomarlo de esa fuente y se puede reutilizar en diferente lugares.
	 * 4- Los metodos comienzan con minuscula.
	 * 5-Esto se podria separar en 3 metodos , ya que un mensaje no puede ser las 3 cosas al mismo tiempo es decir no puede ser se message y warning y error.Ademas el metodo es muy extenso y realiza varias acciones, un metodo solo deberia realizar una accion.
	 * Ejemplo logMessage, logWarning etc.
	 * 
	 * 
	 */
	
	public static void LogMessage(String messageText, boolean message, boolean warning, boolean error)
			throws Exception {
		messageText.trim();/**
						 *	6-Se realiza un trim del parametro messageText en el caso que el valor que se envia sea null fallaria.
						 * 7-Tambien se hace un length de messageText en el caso de ser null tambien fallaria.
						 * 8-Se realiza el messageText.trim() y no se guarda en una variable este valor se perderia.
						 * 
						 */
		if (messageText == null || messageText.length() == 0) {
			return;
		}
		if (!logToConsole && !logToFile && !logToDatabase) {
			throw new Exception("Invalid configuration");
		}
		if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
			throw new Exception("Error or Warning or Message must be specified");
		}
		

		
		
		Connection connection = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", dbParams.get("userName"));
		connectionProps.put("password", dbParams.get("password"));
		connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
				+ ":" + dbParams.get("portNumber") + "/", connectionProps);
		int t = 0;
		/**
		 9-No se utiza un nombre descriptivo para la variable "t"
		*/
		if (message && logMessage) {
			t = 1;
		}
		if (error && logError) {
			t = 2;
		}
		if (warning && logWarning) {
			t = 3;
		}
		
		/**
		 *10- Utilizaria el Prepared Statement esto evita los 
		 * ataques por inject sql y ademas se ejecutan mas rapido ya que la
		 * consulta son precompilada y siempre es el mismo plan de ejecucion.
		 */
		Statement stmt = connection.createStatement();
		/**
		 *11- Antes de hacer el Statement deberia validarse que connection no es null, que los datos de la conexion 
		 * a la base de datos sean los correctos.
		 */
		
		String l = null;/**
		--12 No se utiza un nombre descriptivo para la variable "l"
		*/
		
		File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt");
	
		
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		
		
		/**
		 * //--13- No haria falta comprobar si el existe con createNewFile y despues Instanciar FileHandler
		 * Solo se instancia logFile que despues no se usa mas, Ya creando Filehandler es suficiente.
		 */
		FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
		
		ConsoleHandler ch = new ConsoleHandler();
		
		/**
		 * 14-Se crea instancia de FileHandler , consoleHanDler y Connection que solo se usara una, u otra 
		 * dependiendo de la bandera logToFile, logToConsole y logToDatabase. Esto lleva a un consumo innecesario
		 * de recursos. Solo deberia instanciarse solamente la que se utilizara.
		 */
		
		/**
		 * 15- Se podria sacar afuera en una funcion que devuelva la variable l,
		 * Se repite los mismo if cuando se inicializa el valor de la variable "t", esto podria utificarse para no repertir codigo. 
		 */
		if (error && logError) {
			l = l + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}
		if (warning && logWarning) {
			l = l + "warning " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}
		if (message && logMessage) {
			l = l + "message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
		}
		
		/**
		 * 16- Se asigna a la variable "l"el formato del mensaje que se logueare que despues no la utiliza en ninguna
		 * de las diferentes formas de loguear, FileHandler, ConsoleHandler o Statement.
		 */
		
		
		if (logToFile) { /**
		 17-Con los if solamente realiza comprobacion innecesarias, en el caso de cumplirse en la primera condicion 
		 seguira preguntando en los if siguiente, se podria reemplazar por un if -else if - else
		*/	logger.addHandler(fh);
			logger.log(Level.INFO, messageText);
		}
		if (logToConsole) {
			logger.addHandler(ch);
			logger.log(Level.INFO, messageText);
		}
		
		/**
		 * 18- Sin importar el nivel de logueo, siempre loguea nivel Info logger.log(Level.INFO, messageText) tanto para console y FileHandler.

		 */
		
		if (logToDatabase) {
			/**
			 * 19- Nunca cierra la conexion ni el Statement, deberia tener un stmt.close();
			 * y connection.close();
			 */
			stmt.executeUpdate("insert into Log_Values('" + message + "', " + String.valueOf(t) + ")");
			/**
			 * 20-Se inserta en la base de datos message que es un tipo boolean no tiene sentido. Se deberia guardar
			 * la variable l que tiene el formato del mensaje que se guardara y en Level a loguear.
			 * 
			 * 21-La nomenclatura de la tabla es incorrecta, se encuentra en mayuscula, plural y no es descriptiva.
			 */
		}
		
		/**
		 * 22- Esta clase contiene funcionalidad tanto de console, FileHandler y database. Esto se deberia separar en clases
		 * Separadas y cada uno aplicar la logica correspondientes asi seria mas facil de mantener y seria mas escalable
		 *
23-La clase no contiene ningun comentario, alguna persona sin conocimiento podria leer la clase y no entender de que trataria.
*/
		
		
	}
}