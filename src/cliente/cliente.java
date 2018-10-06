package cliente;

import cliente.paquete;

import java.net.*;
import java.io.*;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

public class cliente implements Runnable {
	
	private String usuario = "";
	private String direccion = "";

	static ServerSocket servsock = null;
	static Socket sockin = null;
	static Socket sockout = null;
//	static DataInputStream datinpstr;
//	static DataOutputStream datoutstr;
	static ObjectOutputStream pakete;
	static ObjectInputStream paqete;
	
	protected JFrame frmCliente;
	private JButton btnEnviar;
	private JTextArea txarMensaje;
	private JTextArea txarConversacion;
	private JTextField txbxDe;
	private JTextField txbxIp;
	private JScrollPane scrollPane;
	private JTextField txbxPara;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cliente window = new cliente();
					window.frmCliente.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public cliente() {
		initialize();
	}
	public cliente(String usuario,String direccion) {
		this.usuario = usuario;
		this.direccion = direccion;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCliente = new JFrame();
		frmCliente.setTitle("Cliente");
		frmCliente.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmCliente.setBounds(100, 100, 745, 484);
		frmCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCliente.getContentPane().setLayout(null);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(603, 369, 116, 65);
		frmCliente.getContentPane().add(btnEnviar);
		
		JLabel lblNick = new JLabel("De");
		lblNick.setBounds(10, 14, 46, 14);
		frmCliente.getContentPane().add(lblNick);
		
		txbxDe = new JTextField();
		txbxDe.setEnabled(false);
		txbxDe.setBounds(66, 11, 137, 20);
		txbxDe.setText(usuario);
		frmCliente.getContentPane().add(txbxDe);
		txbxDe.setColumns(10);
		
		JLabel lblIpDestinatario = new JLabel("I.P. Destinatario");
		lblIpDestinatario.setBounds(416, 14, 100, 14);
		lblIpDestinatario.setVisible(false);
		frmCliente.getContentPane().add(lblIpDestinatario);
		
		txbxIp = new JTextField();
		txbxIp.setBounds(526, 11, 193, 20);
		txbxIp.setVisible(false);
		frmCliente.getContentPane().add(txbxIp);
		txbxIp.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 39, 709, 319);
		frmCliente.getContentPane().add(scrollPane);
		
		txarConversacion = new JTextArea();
		scrollPane.setViewportView(txarConversacion);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 369, 583, 65);
		frmCliente.getContentPane().add(scrollPane_1);
		
		txarMensaje = new JTextArea();
		scrollPane_1.setViewportView(txarMensaje);
		
		JLabel lblDestinarario = new JLabel("Para");
		lblDestinarario.setBounds(213, 14, 46, 14);
		frmCliente.getContentPane().add(lblDestinarario);
		
		txbxPara = new JTextField();
		txbxPara.setText("");
		txbxPara.setColumns(10);
		txbxPara.setBounds(269, 11, 137, 20);
		frmCliente.getContentPane().add(txbxPara);
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enviar();
			}
		});
		Thread hilo1 = new Thread(this);
		hilo1.start();
		activo();
	}
	private void enviar() {
		try {
			txarConversacion.append("\n "+txbxDe.getText()+": "+txarMensaje.getText());
			paquete pack = new paquete();
			pack.setEmisor(txbxDe.getText());
			pack.setRceptor(txbxPara.getText());
			pack.setIp(txbxIp.getText());
			pack.setDireccion(this.direccion);
			pack.setMensaje(txarMensaje.getText());
//			sockout = new Socket(pack.getIp(),9898);
			sockout = new Socket("192.182.1.2",9898);
			pakete = new ObjectOutputStream(sockout.getOutputStream());
			pakete.writeObject(pack);
			pakete.close();//
			sockout.close();
			txarMensaje.setText("");
			
			/*datoutstr = new DataOutputStream(sock.getOutputStream());
			String mensaje = "";
			mensaje = txarMensaje.getText().trim();
			datoutstr.writeUTF(mensaje);
			datoutstr.close();
			txarMensaje.setText("");
			JOptionPane.showMessageDialog(null,"si funciona envio cliente ");*/
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,"no funciona envio cliente "+e.getMessage());
		}
	}
	private void activo() {
		try {
			paquete pack = new paquete();
			pack.setEmisor(txbxDe.getText());
			pack.setRceptor(txbxPara.getText());
			pack.setIp(txbxIp.getText());
			pack.setDireccion(this.direccion);
			pack.setMensaje(txarMensaje.getText());
			sockout = new Socket("192.182.1.2",7878);
			pakete = new ObjectOutputStream(sockout.getOutputStream());
			pakete.writeObject(pack);
			pakete.close();//
			sockout.close();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,"no funciona envio cliente "+e.getMessage());
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			servsock = new ServerSocket(9090);
			String nick,mensaje;
			paquete pack;
			while(true) {
				sockin = servsock.accept();
				paqete = new ObjectInputStream(sockin.getInputStream());
				pack = (paquete) paqete.readObject();
				nick = pack.getEmisor();
				nick = pack.getReceptor();
				mensaje = pack.getMensaje();
				txarConversacion.append("\n "+nick+": "+mensaje);
				paqete.close();//
				sockin.close();
			}
//			JOptionPane.showMessageDialog(null,"si funciona conexion servidor 1");
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"no funciona conexion servidor "+e.getMessage());
		}
	}
}
