package cliente;

import cliente.pakage;
import cliente.cliente;

import java.net.InetAddress;
//import java.net.NetworkInterface;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net.SocketException;
import java.net.UnknownHostException;
//import java.util.Enumeration;
import java.awt.event.ActionEvent;

public class login {

	static ServerSocket servsock = null;
	static Socket sockin = null;
	static Socket sockout = null;
//	static DataInputStream datinpstr;
//	static DataOutputStream datoutstr;
	static ObjectOutputStream pakete;
	static ObjectInputStream paqete;

	private Runnable internetprotocol;
	private Thread hilo1;
	private Runnable authorid;
	private Thread hilo2;
	
	protected static JFrame frmLoginCliente;
	private JTextField txbxusuario;
	private JPasswordField psfdcontraseña;
	static JButton btnLogin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					login window = new login();
					window.frmLoginCliente.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoginCliente = new JFrame();
		frmLoginCliente.setTitle("Login Cliente");
		frmLoginCliente.setBounds(100, 100, 549, 388);
		frmLoginCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLoginCliente.getContentPane().setLayout(null);
		
		JLabel lblUsuario = new JLabel("usuario");
		lblUsuario.setBounds(238, 53, 64, 14);
		frmLoginCliente.getContentPane().add(lblUsuario);
		
		JLabel lblContrasea = new JLabel("contrase\u00F1a");
		lblContrasea.setBounds(235, 156, 71, 14);
		frmLoginCliente.getContentPane().add(lblContrasea);
		
		txbxusuario = new JTextField();
		txbxusuario.setBounds(180, 97, 168, 20);
		frmLoginCliente.getContentPane().add(txbxusuario);
		txbxusuario.setColumns(10);
		
		psfdcontraseña = new JPasswordField();
		psfdcontraseña.setBounds(180, 194, 168, 20);
		frmLoginCliente.getContentPane().add(psfdcontraseña);
		
		btnLogin = new JButton("Login");
		btnLogin.setEnabled(false);
		btnLogin.setBounds(217, 261, 89, 23);
		frmLoginCliente.getContentPane().add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enviar();
			}
		});
		internetprotocol = new ipconfig();
		hilo1 = new Thread(internetprotocol);
		hilo1.start();
		authorid = new valid();
		hilo2 = new Thread(authorid);
		hilo2.start();
	}
	private void enviar() {
		try {
			pakage pack = new pakage();
			pack.setNombre(txbxusuario.getText());
			pack.setClave(String.valueOf(psfdcontraseña.getPassword()));
			pack.setIp(ipconfig.ipmaquina);
			pack.setAuth(0);
			sockout = new Socket("192.182.1.2",8989);
			pakete = new ObjectOutputStream(sockout.getOutputStream());
			pakete.writeObject(pack);
			pakete.close();//
			sockout.close();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,"no funciona envio cliente "+e.getMessage());
		}
	}
}

class ipconfig implements Runnable {

	static String ipmaquina;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
		  InetAddress localhost = InetAddress.getLocalHost();
		  InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
		  if (allMyIps != null && allMyIps.length > 1) {
		    for (int i = 0; i < allMyIps.length; i++) {
		    	String stringaux = allMyIps[i].toString();
		    	int intaux = stringaux.indexOf("/")+1;
//				JOptionPane.showMessageDialog(null,intaux+"_"+stringaux);
		    	if(stringaux.substring(intaux,intaux+8).equals("192.182.")) {
		    		ipmaquina = allMyIps[i].toString().substring(intaux);
		    		login.btnLogin.setEnabled(true);
		    	}
		    }
		  }
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null,"error del host "+ e.getMessage());
		}
	}
}

class valid implements Runnable {


	static ServerSocket servsock = null;
	static Socket sockin = null;
	static Socket sockout = null;
//	static DataInputStream datinpstr;
//	static DataOutputStream datoutstr;
	static ObjectOutputStream paqete;
	static ObjectInputStream pakete;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			servsock = new ServerSocket(8787);
			String nombre,ip;
			pakage pack;
			while(true) {
				sockin = servsock.accept();
				pakete = new ObjectInputStream(sockin.getInputStream());
				pack = (pakage) pakete.readObject();
				nombre = pack.getNombre();
				ip = pack.getIp();
//				JOptionPane.showMessageDialog(null,nombre+"_"+clave+"_"+ip+pack.getAuth());
				if(pack.getAuth() == 1) {
//					cliente chat = new cliente();
					cliente chat = new cliente(nombre,ip);
					chat.frmCliente.setVisible(true);
					login.frmLoginCliente.dispose();
				} else {
					JOptionPane.showMessageDialog(null,"usuario o contraseña incorrecta");
				}
				pakete.close();//
				sockin.close();
				
				/*String conversacion = "";
				datinpstr = new DataInputStream(sock.getInputStream());
	//			datoutstr = new DataOutputStream(sock.getOutputStream());
				conversacion = datinpstr.readUTF();
				txarConversacion.append("\n Cliente: "+conversacion);
				sock.close();*/
			}
//			JOptionPane.showMessageDialog(null,"si funciona conexion servidor 1");
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"no funciona conexion servidor "+e.getMessage());
		}
	}
}

/*
		
		try {
		  InetAddress localhost = InetAddress.getLocalHost();
		  System.out.println(" IP Addr: " + localhost.getHostAddress());
		  // Just in case this host has multiple IP addresses....
		  InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
		  if (allMyIps != null && allMyIps.length > 1) {
			  System.out.println(" Full list of IP addresses:");
		    for (int i = 0; i < allMyIps.length; i++) {
		    	String okok = allMyIps[i].toString();
		    	System.out.println("    _" + allMyIps[i]);
		    	System.out.println("    _" + okok.substring(6,14) );
		    }
		  }
		} catch (UnknownHostException e) {
			System.out.println(" (error retrieving server host name)");
		}
//		String semiip = "";
		
		String semiip = "";
		try {
		  InetAddress localhost = InetAddress.getLocalHost();
//		  System.out.println(" IP Addr: " + localhost.getHostAddress());
		  // Just in case this host has multiple IP addresses....
		  InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
		  if (allMyIps != null && allMyIps.length > 1) {
			  System.out.println(" Full list of IP addresses:");
		    for (int i = 0; i < allMyIps.length; i++) {
		    	String aux = allMyIps[i].toString();
		    	if(aux.substring(6,14).equals("192.182.")) {
		    		semiip = allMyIps[i].toString().substring(6);
		    	}
		    }
		  }
		} catch (UnknownHostException e) {
			System.out.println(" (error retrieving server host name)");
		}
//		localhost = hilo1.getName();
		try {
			System.out.println("Full list of Network Interfaces:");
		  for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
		    NetworkInterface intf = en.nextElement();
		    System.out.println("    " + intf.getName() + " " + intf.getDisplayName());
		    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
		    	System.out.println("        " + enumIpAddr.nextElement().toString());
		    }
		  }
		} catch (SocketException e) {
			System.out.println(" (error retrieving network interface list)");
		}
	*/
    /*InetAddress inetAddress;
	try {
		inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Address:- " + inetAddress.getLocalHost());
        System.out.println("IP Address:- " + inetAddress.getHostAddress());
        System.out.println("Host Name:- " + inetAddress.getHostName());
        
        
        
        InetAddress[] addr = InetAddress.getAllByName(null);
        for (int i = 0; i < addr.length; i++)
          System.out.println(addr[i]);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/