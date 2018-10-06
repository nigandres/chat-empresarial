package cliente;

import cliente.paquete;
import cliente.pakage;
import cliente.conexdb;
import cliente.chateardb;
import cliente.usuario;

import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.*;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class servidor implements Runnable {

	private int oculto = 0;

	static ServerSocket servsock = null;
	static Socket sockin = null;
	static Socket sockout = null;
//	static DataInputStream datinpstr;
//	static DataOutputStream datoutstr;
	static ObjectOutputStream paqete;
	static ObjectInputStream pakete;

	private static Connection conexion = null;

	protected JFrame frmServidor;
	private JButton btnEnviar;
	private JTextArea txarMensaje;
	private JTextArea txarConversacion;
	private JScrollPane scrollPane;
	private JTable table;
	protected static DefaultTableModel model;
	private JButton btnRegistrar;
	private JButton btnBackup;
	private JButton btnRestore;
	private JScrollPane scrollPane_1;

	private JPanel panel;
	private JScrollPane scrollPane_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					servidor window = new servidor();
					window.frmServidor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public servidor() {
		initialize();
	}

	public servidor(int oculto) {
		this.oculto = oculto;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmServidor = new JFrame();
		frmServidor.setTitle("Servidor");
		frmServidor.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmServidor.setBounds(100, 100, 745, 484);
		frmServidor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServidor.getContentPane().setLayout(null);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 11, 709, 423);
		frmServidor.getContentPane().add(scrollPane_1);

		if(this.oculto == 2) {
		scrollPane_1.setBounds(10, 11, 709, 246);

		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(603, 369, 116, 65);
		frmServidor.getContentPane().add(btnEnviar);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(136, 268, 583, 90);
		frmServidor.getContentPane().add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 15));
		model = new DefaultTableModel(0, 0);
		model.setColumnIdentifiers(new Object[] { "id", "nombre", "clave", "rol", "ip" });
		table.setModel(model);
		scrollPane.setViewportView(table);

		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setBounds(10, 369, 116, 65);
		frmServidor.getContentPane().add(btnRegistrar);

		btnBackup = new JButton("Backup");
		btnBackup.setBounds(10, 268, 116, 40);
		frmServidor.getContentPane().add(btnBackup);

		btnRestore = new JButton("Restore");
		btnRestore.setBounds(10, 318, 116, 40);
		frmServidor.getContentPane().add(btnRestore);

		txarConversacion = new JTextArea();
		scrollPane_1.setViewportView(txarConversacion);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(136, 369, 457, 65);
		frmServidor.getContentPane().add(scrollPane_2);

		txarMensaje = new JTextArea();
		scrollPane_2.setViewportView(txarMensaje);

		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				usuario user = new usuario();
				user.frame.setVisible(true);
			}
		});

		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enviar();
			}
		});
		btnBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				crearbackup();
			}
		});
		btnRestore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hacerrestore();
			}
		});
		conexdb ok = new conexdb();
		ok.conectar();
		agregarusuarios();
		}
		Thread hilo1 = new Thread(this);
		hilo1.start();
		Runnable autorizacion = new auth();
		Thread hilo2 = new Thread(autorizacion);
		hilo2.start();
		Runnable enlinea = new online();
		Thread hilo3 = new Thread(enlinea);
		hilo3.start();
	}

	protected void agregarusuarios() {
		try {
			if (model.getRowCount() > 0) {
				for (int i = model.getRowCount() - 1; i > -1; i--) {
					model.removeRow(i);
				}
			}
			String id;
			String nombre;
			String clave;
			String rol;
			conexdb kon = new conexdb();
			logindb vent = new logindb();
			conexion = kon.conectar();
			if(vent.vacio(conexion) != 0) {
				ResultSet sulset = vent.seleccionar(conexion);
				while (sulset.next()) {
					id = String.valueOf(sulset.getInt("usuarios.id"));
					nombre = sulset.getString("nombre");
					clave = sulset.getString("clave");
					rol = sulset.getString("roles.tipo");
					model.addRow(new Object[] { id, nombre, clave, rol, });
				}
				sulset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected static void agregarusuario() {
		try {
			String id = null;
			String nombre = null;
			String clave = null;
			String rol = null;
			conexdb kon = new conexdb();
			logindb vent = new logindb();
			conexion = kon.conectar();
			ResultSet sulset = vent.seleccionar(conexion);
			while (sulset.next()) {
				id = String.valueOf(sulset.getInt("usuarios.id"));
				nombre = sulset.getString("nombre");
				clave = sulset.getString("clave");
				rol = sulset.getString("roles.tipo");
			}
			model.addRow(new Object[] { id, nombre, clave, rol, });
			sulset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void enviar() {
		int row;
		String ip;
		try {
			row = table.getSelectedRow();
			try {
				ip = table.getModel().getValueAt(row, 4).toString();
//				JOptionPane.showMessageDialog(null,table.getModel().getValueAt(row, 4).toString());
				try {
					txarConversacion.append("\n Admin: " + txarMensaje.getText());
					paquete pack = new paquete();
					pack.setEmisor("Admin");
//					pack.setRceptor("todos");
					pack.setIp(ip);
					pack.setMensaje(txarMensaje.getText());
					sockout = new Socket(pack.getIp(), 9090);
					paqete = new ObjectOutputStream(sockout.getOutputStream());
					paqete.writeObject(pack);
					paqete.close();//
					sockout.close();
					txarMensaje.setText("");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "no funciona envio de servidor " + e.getMessage());
				}
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "no hay ip para ese usuario");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, "no seleciono usuario");
		}
		/*
		 * try { sockin = new Socket("192.168.1.3",9898); datoutstr = new
		 * DataOutputStream(sockin.getOutputStream()); String mensaje = ""; mensaje =
		 * txarMensaje.getText().trim(); datoutstr.writeUTF(mensaje); datoutstr.close();
		 * txarMensaje.setText("");
		 * JOptionPane.showMessageDialog(null,"si funciona envio servidor "); }
		 * catch(Exception e) {
		 * JOptionPane.showMessageDialog(null,"no funciona envio servidor "+e.getMessage
		 * ()); }
		 * 
		 * try { txarConversacion.append("\n "+txarMensaje.getText()); paquete pack =
		 * new paquete(); pack.setNick("NICO"); pack.setIp("192.168.1.2");
		 * pack.setMensaje(txarMensaje.getText()); sockout = new
		 * Socket(pack.getIp(),9090); ObjectOutputStream pakete = new
		 * ObjectOutputStream(sockout.getOutputStream()); pakete.writeObject(pack);
		 * pakete.close();// sockout.close(); txarMensaje.setText(""); } catch(Exception
		 * e) {
		 * JOptionPane.showMessageDialog(null,"no funciona envio cliente "+e.getMessage(
		 * )); }
		 */
	}

	public void preparar(String _emisor, String _receptor, String _mensaje, String _ip, String _direccion) {
		txarConversacion.append(_emisor + ": " + _mensaje + " para:" + _receptor + " con ip " + _ip + "¶\n");
		for (int i = model.getRowCount() - 1; i > -1; i--) {
			if (String.valueOf(_emisor).equals(model.getValueAt(i, 1).toString())) {
				model.setValueAt(String.valueOf(_direccion), i, 4);
			}
		}
	}

	public void almacenachat(String _emisor, String _receptor, String _mensaje) {
		conexdb kon = new conexdb();
		chateardb chat = new chateardb();
		conexion = kon.conectar();
		chat.insertachat(conexion, _emisor, _receptor, _mensaje);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			servsock = new ServerSocket(9898);
			String emisor, receptor, ip, direccion, mensaje;
			String ipdeprueba = null;
			paquete pack;
			while (true) {
				sockin = servsock.accept();
				pakete = new ObjectInputStream(sockin.getInputStream());
				pack = (paquete) pakete.readObject();
				emisor = pack.getEmisor();
				receptor = pack.getReceptor();/////////////////////////////////////////
				ip = pack.getIp();
				direccion = pack.getDireccion();
				mensaje = pack.getMensaje();
				almacenachat(emisor, receptor, mensaje);
				preparar(emisor, receptor, mensaje, ip, direccion);
				// accion
				for (int i = servidor.model.getRowCount() - 1; i > -1; i--) {
					if (String.valueOf(receptor).equals(servidor.model.getValueAt(i, 1).toString())) {
//			    		servidor.model.setValueAt(String.valueOf(direccion), i, 4);
						ipdeprueba = servidor.model.getValueAt(i, 4).toString();
						sockout = new Socket(ipdeprueba, 9090);
//						sockout = new Socket(ip,9090);
//						sockout = new Socket("192.168.1.3",9090);
						paqete = new ObjectOutputStream(sockout.getOutputStream());
						paqete.writeObject(pack);
						paqete.close();//
						pakete.close();//
						sockin.close();
						break;
					}
				}

				/*
				 * String conversacion = ""; datinpstr = new
				 * DataInputStream(sock.getInputStream()); // datoutstr = new
				 * DataOutputStream(sock.getOutputStream()); conversacion = datinpstr.readUTF();
				 * txarConversacion.append("\n Cliente: "+conversacion); sock.close();
				 */
			}
//			JOptionPane.showMessageDialog(null,"si funciona conexion servidor 1");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "no funciona conexion servidor pggo " + e.getMessage());
		}
	}

	public void crearbackup() {
		String ruta = null;
		JFileChooser emergente = new JFileChooser();
		 emergente.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		 int badera = emergente.showOpenDialog(this.panel);
		 if (badera == JFileChooser.APPROVE_OPTION) {
			 ruta = emergente.getSelectedFile().getAbsolutePath();
		 }
		try {
			Process p = Runtime.getRuntime()
					.exec("C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump -u root -p1236547890 capas");

			InputStream is = p.getInputStream();
			FileOutputStream fos = new FileOutputStream(ruta+"\\respaldo.sql");
			byte[] buffer = new byte[1000];
			int leido = is.read(buffer);
			while (leido > 0) {
				fos.write(buffer, 0, leido);
				leido = is.read(buffer);
			}
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		 /*String ruta = null; JFileChooser emergente = new JFileChooser();
		 emergente.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); int badera =
		 emergente.showOpenDialog(this.panel); if (badera ==
		 JFileChooser.APPROVE_OPTION) { ruta =
		 emergente.getSelectedFile().getAbsolutePath(); }
		 JOptionPane.showMessageDialog(null,ruta); String respaldo = null; respaldo =
		 txarConversacion.getText(); String[] mensajes =
		 respaldo.split(Pattern.quote("¶")); // Split on period. PrintWriter writer;
		 try { // 
			 writer = new PrintWriter("C:\\Users\\andre\\Desktop\\2client1server\\backup.txt","UTF-8");
			 writer = new PrintWriter(ruta+"\\backup.txt", "UTF-8"); 
		 for(int i = 0;i<mensajes.length-1;i++) { writer.println(mensajes[i]+"¶"); }
		 writer.close(); System.out.println(respaldo);
		 } catch (FileNotFoundException | UnsupportedEncodingException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace(); 
		 }*/
		

//		try {
//			Runtime.getRuntime().exec("C:\\Windows\\System32\\cmd.exe /K start C:\\Users\\andre\\Desktop\\2client1server\\encr.exe");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void hacerrestore() {
		String ruta = null;
		JFileChooser emergente = new JFileChooser();
		int badera = emergente.showOpenDialog(this.panel);
		if (badera == JFileChooser.APPROVE_OPTION) {
			ruta = emergente.getSelectedFile().getAbsolutePath();
		}
		try {
			Process p = Runtime.getRuntime()
					.exec("C:/Program Files/MySQL/MySQL Server 5.5/bin/mysql -u root -p1236547890 capas");

			OutputStream os = p.getOutputStream();
			FileInputStream fis = new FileInputStream(ruta);
			byte[] buffer = new byte[1000];

			int leido = fis.read(buffer);
			while (leido > 0) {
				os.write(buffer, 0, leido);
				leido = fis.read(buffer);
			}

			os.flush();
			os.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

//		try {
////			Runtime.getRuntime().exec("C:\\Windows\\System32\\cmd.exe /K start cmd.exe");
//			Runtime.getRuntime().exec("C:\\Windows\\System32\\cmd.exe /K start C:\\Users\\andre\\Desktop\\2client1server\\decr.exe");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			Process p = app.exec("cmd /o"+"C:\\Users\\andre\\Desktop\\2 client 1 server\\encr.exe");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		try {
//			ProcessBuilder p = new ProcessBuilder();
//			p.command("cmd.exe","/c","C:\\Users\\andre\\Desktop\\ejecutable.java");
//			p.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		
		
		
		
		
		/*String ruta = null;
		JFileChooser emergente = new JFileChooser();
		int badera = emergente.showOpenDialog(this.panel);
		if (badera == JFileChooser.APPROVE_OPTION) {
			ruta = emergente.getSelectedFile().getAbsolutePath();
		}*/
		
		
		
		
		
		
		
		
//		FileInputStream reader;
//		try {
//			reader = new FileInputStream("C:\\Users\\andre\\Desktop\\2 client 1 server\\backup.txt");
//			//Construct BufferedReader from InputStreamReader
//			BufferedReader br = new BufferedReader(new InputStreamReader(reader));
//		 
//			String line = null;
//			try {
//				while ((line = br.readLine()) != null) {
//					System.out.println(line);
//				}
//				 
//				br.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// Metodo 2
		
		
		
		
		
		
		
		/*try {
			String content = new String(Files.readAllBytes(Paths.get(ruta))).replaceAll("(\r\n|Â)", "");
			txarConversacion.setText(content);
			System.out.println(content);
//			String embed = content.replaceAll("¶", "¶\n");
//			txarConversacion.setText(embed);
//			System.out.println(embed);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}

class auth implements Runnable {

	static ServerSocket servsock = null;
	static Socket sockin = null;
	static Socket sockout = null;
//	static DataInputStream datinpstr;
//	static DataOutputStream datoutstr;
	static ObjectOutputStream paqete;
	static ObjectInputStream pakete;

	private Connection conexion = null;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			servsock = new ServerSocket(8989);
			String nombre, clave, ip;
			pakage pack;
			while (true) {
				sockin = servsock.accept();
				pakete = new ObjectInputStream(sockin.getInputStream());
				pack = (pakage) pakete.readObject();
				nombre = pack.getNombre();
				clave = pack.getClave();
				ip = pack.getIp();
//				JOptionPane.showMessageDialog(null,nombre+"_"+clave+"_"+ip);
				conexdb kon = new conexdb();
				logindb vent = new logindb();
				conexion = kon.conectar();
				int sulset = vent.seleccionar(conexion, nombre, clave);
				pack.setAuth(sulset);
				sockout = new Socket(ip, 8787);
				paqete = new ObjectOutputStream(sockout.getOutputStream());
				paqete.writeObject(pack);
				paqete.close();//
				pakete.close();//
				sockin.close();

				/*
				 * String conversacion = ""; datinpstr = new
				 * DataInputStream(sock.getInputStream()); // datoutstr = new
				 * DataOutputStream(sock.getOutputStream()); conversacion = datinpstr.readUTF();
				 * txarConversacion.append("\n Cliente: "+conversacion); sock.close();
				 */
			}
//			JOptionPane.showMessageDialog(null,"si funciona conexion servidor 1");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "no funciona conexion servidor " + e.getMessage());
		}
	}
}

class online implements Runnable {

	static ServerSocket servsock = null;
	static Socket sockin = null;
//	static DataInputStream datinpstr;
//	static DataOutputStream datoutstr;
	static ObjectInputStream pakete;

//	private Connection conexion = null;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			servsock = new ServerSocket(7878);
			String emisor, direccion;
//			String emisor,receptor,ip,direccion,mensaje;
			paquete pack;
			while (true) {
				sockin = servsock.accept();
				pakete = new ObjectInputStream(sockin.getInputStream());
				pack = (paquete) pakete.readObject();
				emisor = pack.getEmisor();// a quien yo busco
//				receptor = pack.getReceptor();/////////////////////////////////////////
//				ip = pack.getIp();
				direccion = pack.getDireccion();// ip de quie busco
//				mensaje = pack.getMensaje();
				// acciones
				for (int i = servidor.model.getRowCount() - 1; i > -1; i--) {
					if (String.valueOf(emisor).equals(servidor.model.getValueAt(i, 1).toString())) {
						servidor.model.setValueAt(String.valueOf(direccion), i, 4);
					}
				}
				pakete.close();
				sockin.close();

				/*
				 * String conversacion = ""; datinpstr = new
				 * DataInputStream(sock.getInputStream()); // datoutstr = new
				 * DataOutputStream(sock.getOutputStream()); conversacion = datinpstr.readUTF();
				 * txarConversacion.append("\n Cliente: "+conversacion); sock.close();
				 */
			}
//			JOptionPane.showMessageDialog(null,"si funciona conexion servidor 1");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "no funciona conexion servidor " + e.getMessage());
		}
	}
}