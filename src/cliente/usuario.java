package cliente;

import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class usuario {
	
	private static Connection conexion = null;
	@SuppressWarnings("unused")
	private int id_rol;

	protected JFrame frame;
	private JComboBox<ComboItem> comboBox;
	private JTextField txbxUser;
	private JPasswordField psfdclave;
	private JButton btnRegistrar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					usuario window = new usuario();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public usuario() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(300, 200, 449, 354);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		comboBox = new JComboBox<ComboItem>();
		comboBox.setBounds(118, 166, 204, 31);
		frame.getContentPane().add(comboBox);
		
		JLabel label = new JLabel("usuario");
		label.setBounds(98, 36, 51, 14);
		frame.getContentPane().add(label);
		
		txbxUser = new JTextField();
		txbxUser.setColumns(10);
		txbxUser.setBounds(39, 80, 168, 20);
		frame.getContentPane().add(txbxUser);
		
		JLabel label_1 = new JLabel("contrase\u00F1a");
		label_1.setBounds(287, 36, 66, 14);
		frame.getContentPane().add(label_1);
		
		psfdclave = new JPasswordField();
		psfdclave.setBounds(236, 80, 168, 20);
		frame.getContentPane().add(psfdclave);
		
		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setBounds(169, 242, 106, 40);
		frame.getContentPane().add(btnRegistrar);
		
		cargaroles();
		comboBox.addItemListener(new ItemListener() {
			/*public void itemStateChanged(ItemEvent arg0) {
				JOptionPane.showMessageDialog(null,"seleccione una fila");
			}*/
			public void itemStateChanged(ItemEvent e) {
			    if (e.getStateChange() == ItemEvent.SELECTED) {
					id_rol = Integer.parseInt(comboBox.getItemAt(comboBox.getSelectedIndex()).getValue());
			        //Do any operations you need to do when an item is selected.
			    } else if(e.getStateChange() == ItemEvent.DESELECTED){
			        //Do any operations you need to do when an item is de-selected.
			    }
			}
		});
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexdb kon = new conexdb();
				logindb vent = new logindb();
				conexion = kon.conectar();
				vent.insertaruser(conexion,txbxUser.getText(),String.valueOf(psfdclave.getPassword()),id_rol);
				frame.dispose();
				servidor.agregarusuario();
			}
		});
	}
	public void cargaroles() {
		try {
			String id;
			String tipo;
			conexdb kon = new conexdb();
			logindb vent = new logindb();
			conexion = kon.conectar();
			ResultSet sulset = vent.seleccionarroles(conexion);
			while (sulset.next()){
				id = String.valueOf(sulset.getInt("id"));
				tipo = sulset.getString("tipo");
				comboBox.addItem(new ComboItem(tipo, id));
			}
			sulset.close();
			id_rol = Integer.parseInt(comboBox.getItemAt(comboBox.getSelectedIndex()).getValue());
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
class ComboItem {
    private String key;
    private String value;
    public ComboItem(String key, String value)
    {
        this.key = key;
        this.value = value;
    }
    @Override
    public String toString()
    {
        return key;
    }
    public String getKey()
    {
        return key;
    }
    public String getValue()
    {
        return value;
    }
}