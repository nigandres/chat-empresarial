package cliente;

import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
import java.sql.Statement;

//import javax.swing.JOptionPane;

public class chateardb {
	private Statement consulta = null;

	public void insertachat(Connection conexion,String emisor, String receptor, String mensaje) {
		try {
			consulta = conexion.createStatement();
	        consulta.executeUpdate("call insertachat ('"+emisor+"','"+receptor+"','"+mensaje+"')");
//	        consulta.executeUpdate("call insertachat ('"+emisor+"','"+receptor+"',"+mensaje+")");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
