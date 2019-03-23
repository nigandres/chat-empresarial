package cliente;

import cliente.servidor;
import cliente.conexdb;
import cliente.logindb;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class loguear {
	
	private Connection conexion = null;

	private static JFrame frmLoginServidor;
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
					loguear window = new loguear();
					window.frmLoginServidor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public loguear() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoginServidor = new JFrame();
		frmLoginServidor.setTitle("Login Servidor");
		frmLoginServidor.setBounds(100, 100, 549, 388);
		frmLoginServidor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLoginServidor.getContentPane().setLayout(null);
		
		JLabel lblUsuario = new JLabel("USUARIO");
		lblUsuario.setBounds(238, 53, 64, 14);
		frmLoginServidor.getContentPane().add(lblUsuario);
		
		JLabel lblContrasea = new JLabel("contrase\u00F1a");
		lblContrasea.setBounds(235, 156, 71, 14);
		frmLoginServidor.getContentPane().add(lblContrasea);
		
		txbxusuario = new JTextField();
		txbxusuario.setBounds(180, 97, 168, 20);
		frmLoginServidor.getContentPane().add(txbxusuario);
		txbxusuario.setColumns(10);
		
		psfdcontraseña = new JPasswordField();
		psfdcontraseña.setBounds(180, 194, 168, 20);
		frmLoginServidor.getContentPane().add(psfdcontraseña);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(217, 261, 89, 23);
		frmLoginServidor.getContentPane().add(btnLogin);
		
		JButton btnRestaurar = new JButton("Restaurar");
		btnRestaurar.setBounds(411, 315, 112, 23);
		frmLoginServidor.getContentPane().add(btnRestaurar);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enviar();
			}
		});
		btnRestaurar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//existe pero es admin
				servidor chat = new servidor(2);
				chat.frmServidor.setVisible(true);
				loguear.frmLoginServidor.dispose();
			}
		});
	}
	private void enviar() {
		int sulset = 0;
		String nombre = txbxusuario.getText();
		String clave = String.valueOf(psfdcontraseña.getPassword());
		try {
			conexdb kon = new conexdb();
			logindb vent = new logindb();
			conexion = kon.conectar();
			sulset += vent.seleccionar(conexion,nombre,clave);
			sulset += vent.seleccionarrol(conexion,nombre,clave);
			if(sulset == 1) {//existe pero es user
				servidor chat = new servidor(1);
				chat.frmServidor.setVisible(true);
				loguear.frmLoginServidor.dispose();
			} else if(sulset == 2) {//existe pero es admin
				servidor chat = new servidor(2);
				chat.frmServidor.setVisible(true);
				loguear.frmLoginServidor.dispose();
			} else {
				JOptionPane.showMessageDialog(null,"usuario o contraseña incorrecta");
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,"no funciona envio log "+e.getMessage());
		}
	}
}