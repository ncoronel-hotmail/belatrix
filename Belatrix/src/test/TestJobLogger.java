package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import constants.LogConstant;
import jobs.JobLogger;

/**
 * 
 * @author ncoronel
 *
 * JUnit para testear la clase JobLogger
 * La version es JUnit 5 o "Juniper"
 */
@TestMethodOrder(OrderAnnotation.class)
class TestJobLogger {

	/**
	 * Este metodo se ejecuta antes de cada uno de los tests
	 */
	@BeforeEach
	void resetParams() {
		JobLogger.setMsgSettings(null);
		JobLogger.setTypeLogSettings(null);
	}
	
	//Test sin especificar un tipo de log valido
	@Order(1)
	@Test
	void testNoLogTypes() {
		setSpecificParams(LogConstant.MSG_ERROR, 99);
		Exception e = assertThrows(Exception.class, () -> JobLogger.logError("Test NoLogTypes"));
		assertEquals("Invalid configuration", e.getMessage());
	}
	
	//Test sin especificar un tipo de mensaje valido
	@Test
	@Order(2)
	void testNoMsgTypes() {
		setSpecificParams(99, LogConstant.LOG_CONSOLE);
		Exception e = assertThrows(Exception.class, () -> JobLogger.logError("Test NoLogTypes"));
		assertEquals("ERROR messages must be configured", e.getMessage());
	}
	
	//Test para mensaje nulo
	@Test
	@Order(3)

	void testNullMessage() {
		setSpecificParams(LogConstant.MSG_ERROR, LogConstant.LOG_CONSOLE);
		Exception e = assertThrows(Exception.class, () -> JobLogger.logError(null));
		assertEquals("Message cannot be empty", e.getMessage());
	}
	
	//Test para mensaje vacio
	@Test
	@Order(4)

	void testEmptyMessage() {
		setSpecificParams(LogConstant.MSG_ERROR, LogConstant.LOG_CONSOLE);
		Exception e = assertThrows(Exception.class, () -> JobLogger.logError("      "));
		assertEquals("Message cannot be empty", e.getMessage());
	}
	
	//Test para log en consola, pasa el test si no tira excepcion (verificar la consola)
	@Test
	@Order(5)

	void testConsole() {
		setSpecificParams(LogConstant.MSG_MESSAGE, LogConstant.LOG_CONSOLE);
		assertDoesNotThrow(() -> JobLogger.logMessage("Test Message Console"));
	}
	
	//Test para log en archivo (verificar el archivo especificado en el log_file_path de app.properties)
	//El test pasa si no hubo excepciones
	@Test
	@Order(6)

	void testFile() {
		setSpecificParams(LogConstant.MSG_ERROR, LogConstant.LOG_FILE);
		assertDoesNotThrow(() -> JobLogger.logError("Test Error File"));
	}
	
	//Test para verificar log en base de datos (pasa si se genero la primary key, verificar tambien en base)
	@Test
	@Order(7)

	void testDatabaseLog() throws Exception {
		setSpecificParams(LogConstant.MSG_WARNING, LogConstant.LOG_DATABASE);

		JobLogger.logWarning("Database test case Warning");
		assertEquals(true, JobLogger.getDatabaseLoggerStatus());
	}

	//Test para verificar todos los tipos de mensajes y de logs
	@Test
	@Order(8)

	void testAllLogs() throws Exception {
		setFullParams();
		
		assertDoesNotThrow(() -> JobLogger.logMessage("Test Message"));
		assertEquals(true, JobLogger.getDatabaseLoggerStatus());
		
		assertDoesNotThrow(() -> JobLogger.logWarning("Test Warning"));
		assertEquals(true, JobLogger.getDatabaseLoggerStatus());
		
		assertDoesNotThrow(() -> JobLogger.logError("Test Error"));
		assertEquals(true, JobLogger.getDatabaseLoggerStatus());
	}
	
	
	//Metodo para especificar un parametro especifico de mensaje y log
	private void setSpecificParams(Integer msgType, Integer logType) {
		Integer[] msgTypesParams = {msgType};
		Integer[] logTypesParams = {logType};
		JobLogger.setMsgSettings(new HashSet<Integer>(Arrays.asList(msgTypesParams)));
		JobLogger.setTypeLogSettings(new HashSet<Integer>(Arrays.asList(logTypesParams)));
	}
	
	//Metodo para setear los parametros con todos los tipos de mensajes y de logs
	private void setFullParams() {
		Integer[] msgParams = {LogConstant.MSG_MESSAGE, LogConstant.MSG_WARNING, LogConstant.MSG_ERROR};
		Integer[] logTypesParams = {LogConstant.LOG_CONSOLE, LogConstant.LOG_FILE, LogConstant.LOG_DATABASE};
		JobLogger.setMsgSettings(new HashSet<Integer>(Arrays.asList(msgParams)));
		JobLogger.setTypeLogSettings(new HashSet<Integer>(Arrays.asList(logTypesParams)));
	}
}
