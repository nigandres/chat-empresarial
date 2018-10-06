package cliente;

import java.io.Serializable;

@SuppressWarnings("serial")
public class pakage implements Serializable {
	private String nombre,clave,ip;
	private int auth;
	public String getNombre() {
		return this.nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getClave() {
		return this.clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getIp() {
		return this.ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getAuth() {
		return this.auth;
	}
	public void setAuth(int auth) {
		this.auth = auth;
	}
}