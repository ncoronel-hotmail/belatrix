package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import db.DbConnection;

/**
 * 
 * @author ncoronel
 *
 * Clase estatica que contiene la configuracion de la aplicacion
 */
public class Configuration {

	/** Propiedades cargadas desde app.properties */
	private final static Properties PROPS = getProps();
	
	private final static Properties getProps() {
		Properties props = new Properties();
		
		try (final InputStream stream =
		    DbConnection.class.getClassLoader().getResourceAsStream("app.properties")) {
		    props.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return props;
	}
	
	public static String getDbDriver() {
		return PROPS.get("db_driver").toString();
	}
	
	public static String getDbUrl() {
		return PROPS.get("db_url").toString();
	}
	
	public static String getDbUser() {
		return PROPS.get("db_user").toString();
	}
	
	public static String getDbPass() {
		return PROPS.get("db_pass").toString();
	}
	
	public static String getLogFilePath() {
		return PROPS.get("log_file_path").toString();
	}

}
