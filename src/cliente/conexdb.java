package cliente;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.sql.SQLException;
//import java.sql.Statement;

public class conexdb {

	private String _servidor = "127.0.0.1";
	private String _usuario = "root";
	private String _clave = "1236547890";
	private String _puerto = "3306";
	private static String _basededatos = "capas";
	private String _gestor = "mysql";
	private String _ssl = "useSSL=false";
	private String _url = "jdbc:"+_gestor+"://"+_servidor+":"+_puerto+"/"+_basededatos+"?"+_ssl;
	private Connection conexion = null;
	/*private Statement consulta = null;
	private ResultSet rs = null;*/
	
	public Connection conectar() {
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException ex) {
			    System.out.println("Error al registrar el driver de MySQL: " + ex);
			}
			conexion = DriverManager.getConnection(_url, _usuario, _clave);
		} catch (SQLException e) {
			System.out.println("pos no se "+e);
		}
		return conexion;
	}
}
