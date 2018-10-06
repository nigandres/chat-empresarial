package cliente;

import java.io.Serializable;

@SuppressWarnings("serial")
public class paquete implements Serializable {
	private String emisor,receptor,ip,direccion,mensaje;
	public String getEmisor() {
		return this.emisor;
	}
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
	public String getReceptor() {
		return this.receptor;
	}
	public void setRceptor(String receptor) {
		this.receptor = receptor;
	}
	public String getIp() {
		return this.ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDireccion() {
		return this.direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getMensaje() {
		return this.mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}