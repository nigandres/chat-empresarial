package cliente;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
import java.sql.Statement;

public class logindb {
	private Statement consulta = null;
	private ResultSet rs = null;
	private int existe = 0;

	public int seleccionar(Connection conexion,String nombre,String clave) {
		try {
			consulta = conexion.createStatement();
			rs = consulta.executeQuery("select count(*) as exist from usuarios where nombre = '"+nombre+"' and clave = md5('"+clave+"')");
			
			while (rs.next()){
				existe = rs.getInt("exist");
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return existe;
	}
	public int seleccionarrol(Connection conexion,String nombre,String clave) {
		try {
			consulta = conexion.createStatement();
			rs = consulta.executeQuery("select count(*) as exist from usuarios where nombre = '"+nombre+"' and clave = md5('"+clave+"') and id_rol = 1");

			while (rs.next()){
				existe = rs.getInt("exist");
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return existe;
	}
	public ResultSet seleccionar(Connection conexion) {
		try {
			consulta = conexion.createStatement();
			rs = consulta.executeQuery("select * from usuarios inner join roles on usuarios.id_rol = roles.id");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	public ResultSet seleccionarroles(Connection conexion) {
		try {
			consulta = conexion.createStatement();
			rs = consulta.executeQuery("select * from roles");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	public void insertaruser(Connection conexion,String nombre, String clave, int rol) {
		try {
			consulta = conexion.createStatement();
	        consulta.executeUpdate("insert into usuarios values(null,'"+nombre+"',md5('"+clave+"'),"+rol+")");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public int vacio(Connection conexion) {
		int numeritos = 0;
		try {
			consulta = conexion.createStatement();
			ResultSet sulset = rs = consulta.executeQuery("SELECT COUNT(*) as numtablas FROM information_schema.tables WHERE table_schema = 'capas';");
			while (sulset.next()) {
				numeritos = sulset.getInt("numtablas");
			}
			sulset.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return numeritos;
	}
}
