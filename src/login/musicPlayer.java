package login;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;


public class musicPlayer  {

	private JFrame frame;
	private static JPanel panel;
	private static JPanel panel_1;
	private static JPanel panel_2 ;
	private static JPanel panel_3 ;
	private static JPanel panel_4;
	private static String tempPath = null;
	private static String userid = null;
	private static String song = null;
	private static JTextField singup_name;
	private static JTextField signup_id;
	private static JPasswordField signup_password;
	private static JTextField login_id;
	private static JPasswordField login_password;
	private static JPasswordField signup_password_cofirm;
	private static JLabel lblNewLabel_4;
	private static JTextField name_song;
	private static JTextField name_artist;
	private static JTextField name_album;
	private final JFileChooser openFileChooser;
	private static JTable table;
	@SuppressWarnings("rawtypes")
	private static JList list_1;
	@SuppressWarnings("rawtypes")
	private static JList list; 
	private static Connection con;
	private static Statement stmt;
	private static String sql;
	private static ResultSet rs;
	private static Statement stmt1;
	private static String sql1;
	private static ResultSet rs2;
	private static Statement stmt2;
	private static String sql2;
	private static ResultSet rs1;
	private static ResultSet rsn;
	private static PreparedStatement pst;
	private static File file;
	private static AudioInputStream audioInput;
	private static Clip clip;
	private static JLabel searchResult;
	private static JLabel sampleMatched;
	private static JLabel songName;
	private static JLabel offset;
//	private static ;
//	private static ;
	// start the application
	public static void main(String[] args) throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/database_one?serverTimezone=UTC","root","");
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						musicPlayer window = new musicPlayer();
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	
	// creating the application
	public musicPlayer() {
		initialize();
		openFileChooser = new JFileChooser();
		openFileChooser.setCurrentDirectory(new File("c:\\temp"));
		openFileChooser.setFileFilter(new FileNameExtensionFilter("wave file","wav"));
	}
	
	private void processSample() {
		
	}
	private void processSong() {
		WavFile.openFile();
	}
	@SuppressWarnings("unchecked")
	private void updateInfo() {
		try {
			@SuppressWarnings("rawtypes")
			DefaultListModel m = new DefaultListModel();
			stmt1=con.createStatement();
			sql1="Select * from songs";
			rs1=stmt1.executeQuery(sql1);
			table.setModel(DbUtils.resultSetToTableModel(rs1));
			rsn=stmt1.executeQuery(sql1);
			while(rsn.next())
			{
				String name = rsn.getString("song_name");
				m.addElement(name);
			}
			list_1.setModel(m);
			@SuppressWarnings("rawtypes")
			DefaultListModel n = new DefaultListModel();
			stmt2=con.createStatement();
			sql2="Select * from playlist_table where user_id='"+userid+"'";
			rs2=stmt2.executeQuery(sql2);
			while(rs2.next())
			{
				String name = rs2.getString("playlist_name");
				n.addElement(name);
			}
			list.setModel(n);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//initialization
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
				
				panel_4 = new JPanel();
				panel_4.setBounds(492, 316, 492, 244);
				frame.getContentPane().add(panel_4);
				panel_4.setLayout(null);
				panel_4.setVisible(false);
				
				JLabel lblSearchResult = new JLabel("SEARCH RESULT");
				lblSearchResult.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblSearchResult.setHorizontalAlignment(SwingConstants.CENTER);
				lblSearchResult.setBounds(10, 23, 224, 40);
				panel_4.add(lblSearchResult);
				
				searchResult = new JLabel("");
				searchResult.setFont(new Font("SansSerif", Font.BOLD, 10));
				searchResult.setHorizontalAlignment(SwingConstants.CENTER);
				searchResult.setBounds(258, 23, 224, 40);
				panel_4.add(searchResult);
				
				JLabel lblSongName = new JLabel("SONG NAME");
				lblSongName.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblSongName.setHorizontalAlignment(SwingConstants.CENTER);
				lblSongName.setBounds(10, 76, 224, 40);
				panel_4.add(lblSongName);
				
				songName = new JLabel("");
				songName.setFont(new Font("SansSerif", Font.BOLD, 10));
				songName.setHorizontalAlignment(SwingConstants.CENTER);
				songName.setBounds(258, 76, 224, 40);
				panel_4.add(songName);
				
				JLabel lblSamplesMatched = new JLabel("SAMPLES MATCHED");
				lblSamplesMatched.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblSamplesMatched.setHorizontalAlignment(SwingConstants.CENTER);
				lblSamplesMatched.setBounds(10, 127, 224, 40);
				panel_4.add(lblSamplesMatched);
				
				sampleMatched = new JLabel("");
				sampleMatched.setFont(new Font("SansSerif", Font.BOLD, 10));
				sampleMatched.setHorizontalAlignment(SwingConstants.CENTER);
				sampleMatched.setBounds(258, 127, 224, 40);
				panel_4.add(sampleMatched);
				
				JLabel lblOffset = new JLabel("OFFSET");
				lblOffset.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblOffset.setHorizontalAlignment(SwingConstants.CENTER);
				lblOffset.setBounds(10, 178, 224, 40);
				panel_4.add(lblOffset);
				
				offset = new JLabel("");
				offset.setFont(new Font("SansSerif", Font.BOLD, 10));
				offset.setHorizontalAlignment(SwingConstants.CENTER);
				offset.setBounds(258, 178, 224, 40);
				panel_4.add(offset);
		
				
				panel_1 = new JPanel();
				panel_1.setBounds(0, 0, 984, 561);
				frame.getContentPane().add(panel_1);
				panel_1.setLayout(null);
				panel_1.setVisible(false);
				
				JLabel label_15 = new JLabel("");
				JLabel label_16 = new JLabel("");
				JLabel label_17 = new JLabel("");
				
				label_16.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						JOptionPane.showMessageDialog(null,"NEXT");
					}
				});
				
				
				label_17.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						label_17.setVisible(false);
						label_15.setVisible(true);
						clip.stop();
					}
				});
				label_17.setIcon(new ImageIcon("F:\\sdl\\data\\pause.png"));
				label_17.setHorizontalAlignment(SwingConstants.CENTER);
				label_17.setBounds(197, 212, 108, 61);
				panel_1.add(label_17);
				label_17.setVisible(false);
				
				JLabel al_name = new JLabel("ALBUM NAME");
				al_name.setFont(new Font("SansSerif", Font.BOLD, 10));
				al_name.setBounds(266, 116, 200, 34);
				panel_1.add(al_name);
				
				JLabel ar_name = new JLabel("ARTIST NAME");
				ar_name.setFont(new Font("SansSerif", Font.BOLD, 10));
				ar_name.setBounds(266, 71, 200, 34);
				panel_1.add(ar_name);
				
				JLabel s_name = new JLabel("SONG NAME");
				s_name.setFont(new Font("SansSerif", Font.BOLD, 10));
				s_name.setBounds(266, 26, 200, 34);
				panel_1.add(s_name);
				label_16.setHorizontalAlignment(SwingConstants.CENTER);
				label_16.setIcon(new ImageIcon("F:\\sdl\\data\\next-1.png"));
				label_16.setBounds(338, 212, 108, 61);
				panel_1.add(label_16);
				
				
				label_15.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						label_15.setVisible(false);
						label_17.setVisible(true);
						
						file = new File(tempPath);
						try {
							audioInput = AudioSystem.getAudioInputStream(file);
							clip = AudioSystem.getClip();
							clip.open(audioInput);
							clip.start();
							
						} catch(Exception e1)
						{
							e1.printStackTrace();
						}
					}
					
				});
				label_15.setHorizontalAlignment(SwingConstants.CENTER);
				label_15.setIcon(new ImageIcon("F:\\sdl\\data\\play-button.png"));
				label_15.setBounds(197, 212, 108, 61);
				panel_1.add(label_15);
				
				JLabel label_14 = new JLabel("");
				label_14.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						JOptionPane.showMessageDialog(null,"PREVIOUS");
					}
				});
				label_14.setHorizontalAlignment(SwingConstants.CENTER);
				label_14.setIcon(new ImageIcon("F:\\sdl\\data\\back-1.png"));
				label_14.setBounds(46, 212, 108, 61);
				panel_1.add(label_14);
				
				JLabel label_12 = new JLabel("SONGS LIST");
				label_12.setHorizontalAlignment(SwingConstants.CENTER);
				label_12.setFont(new Font("SansSerif", Font.BOLD, 12));
				label_12.setBounds(246, 300, 246, 34);
				panel_1.add(label_12);
				
				JScrollPane scrollPane_1 = new JScrollPane();
				scrollPane_1.setBounds(246, 336, 246, 225);
				panel_1.add(scrollPane_1);
				
				list_1 = new JList();
				list_1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						song=(String) list_1.getSelectedValue();
						try {
							stmt=con.createStatement();
							sql="Select * from songs where song_name='"+song+"'";
							rs=stmt.executeQuery(sql);
							while(rs.next())
							{
								s_name.setText(rs.getString("song_name"));
								ar_name.setText(rs.getString("artist_name"));
								al_name.setText(rs.getString("album_name"));
								tempPath = rs.getString("location");
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						

					}
				});
				scrollPane_1.setViewportView(list_1);
				list_1.setFont(new Font("SansSerif", Font.BOLD, 12));
				list_1.setToolTipText("songs");
				
				JLabel lblNewLabel_7 = new JLabel("PLAYLIST");
				lblNewLabel_7.setFont(new Font("SansSerif", Font.BOLD, 12));
				lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel_7.setBounds(0, 300, 246, 34);
				panel_1.add(lblNewLabel_7);
				
				JScrollPane scrollPane_2 = new JScrollPane();
				scrollPane_2.setBounds(0, 336, 246, 225);
				panel_1.add(scrollPane_2);
				
				list = new JList();
				scrollPane_2.setViewportView(list);
				list.setModel(new AbstractListModel() {
					String[] values = new String[] {};
					public int getSize() {
						return values.length;
					}
					public Object getElementAt(int index) {
						return values[index];
					}
				});
				list.setFont(new Font("SansSerif", Font.BOLD, 12));
				list.setToolTipText("songs");
				
				JLabel label_6 = new JLabel("");
				label_6.setIcon(new ImageIcon("F:\\sdl\\data\\6.jpg"));
				label_6.setBounds(0, 300, 492, 261);
				panel_1.add(label_6);
				
				panel_3 = new JPanel();
				panel_3.setBounds(492, 56, 492, 505);
				panel_1.add(panel_3);
				panel_3.setLayout(null);
				panel_3.setVisible(false);
				
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setBounds(0, 0, 492, 261);
				panel_3.add(scrollPane);
				table = new JTable();
				scrollPane.setViewportView(table);
				table.setModel(new DefaultTableModel(
					new Object[][] {
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
					},
					new String[] {
						"ID", "SONG", "ARTIST", "ALBUM", "PATH", "IMAGE PATH"
					}
				));
				table.getColumnModel().getColumn(0).setResizable(false);
				table.getColumnModel().getColumn(1).setResizable(false);
				table.getColumnModel().getColumn(2).setResizable(false);
				table.getColumnModel().getColumn(3).setResizable(false);
				table.getColumnModel().getColumn(4).setResizable(false);
				table.getColumnModel().getColumn(5).setResizable(false);
				table.setFont(new Font("SansSerif", Font.BOLD, 12));
				
				panel_2 = new JPanel();
				panel_2.setBounds(0, 261, 492, 244);
				panel_3.add(panel_2);
				panel_2.setLayout(null);
				panel_2.setVisible(false);
				
				JLabel lblSongName_1 = new JLabel("SONG NAME:");
				lblSongName_1.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblSongName_1.setBounds(10, 25, 181, 36);
				panel_2.add(lblSongName_1);
				
				JLabel lblArtistName_1 = new JLabel("ARTIST NAME:");
				lblArtistName_1.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblArtistName_1.setBounds(10, 72, 181, 36);
				panel_2.add(lblArtistName_1);
				
				JLabel lblAlbumName_1 = new JLabel("ALBUM NAME:");
				lblAlbumName_1.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblAlbumName_1.setBounds(10, 119, 181, 36);
				panel_2.add(lblAlbumName_1);
				
				name_song = new JTextField();
				name_song.setFont(new Font("SansSerif", Font.BOLD, 10));
				name_song.setBounds(175, 29, 276, 32);
				panel_2.add(name_song);
				name_song.setColumns(10);
				
				name_artist = new JTextField();
				name_artist.setFont(new Font("SansSerif", Font.BOLD, 10));
				name_artist.setColumns(10);
				name_artist.setBounds(175, 72, 276, 32);
				panel_2.add(name_artist);
				
				name_album = new JTextField();
				name_album.setFont(new Font("SansSerif", Font.BOLD, 10));
				name_album.setColumns(10);
				name_album.setBounds(175, 123, 276, 32);
				panel_2.add(name_album);
				
				JButton upload_button = new JButton("UPLOAD");
				upload_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							if(name_song.getText().toString().equals("") || name_artist.getText().toString().equals("") || name_album.getText().toString().equals("") )
							{
								JOptionPane.showMessageDialog(null,"No entry should be empty");
							} else
							{
	
								stmt=con.createStatement();
								sql="Select * from songs where location='"+tempPath+"'";
								rs=stmt.executeQuery(sql);
								if(rs.next())
								{
									JOptionPane.showMessageDialog(null,"Song already exist");
								} else
								{
									PreparedStatement pst=con.prepareStatement("Insert into songs (`song_name`, `artist_name`, `album_name` , `location`) values(?,?,?,?)");
									pst.setString(1, name_song.getText());
									pst.setString(2, name_artist.getText());
									pst.setString(3, name_album.getText());
									pst.setString(4, tempPath);	
									pst.executeUpdate();
									updateInfo();
									processSong();
									JOptionPane.showMessageDialog(null,"Song Uploaded","Success",JOptionPane.INFORMATION_MESSAGE);
									panel_2.setVisible(false);
								}
							}
							
							
						}catch(Exception ex) {
							System.out.print(ex);
						}
					}
				});
				upload_button.setFont(new Font("SansSerif", Font.BOLD, 10));
				upload_button.setForeground(new Color(0, 0, 51));
				upload_button.setBackground(new Color(255, 255, 255));
				upload_button.setBounds(326, 186, 125, 36);
				panel_2.add(upload_button);
				
				JLabel label_7 = new JLabel("");
				label_7.setIcon(new ImageIcon("F:\\sdl\\data\\2.jpg"));
				label_7.setBounds(492, 300, 492, 261);
				panel_1.add(label_7);
				
				JLabel lblNewLabel_6 = new JLabel("");
				lblNewLabel_6.setBounds(0, 185, 984, 114);
				panel_1.add(lblNewLabel_6);
				
				JLabel lblArtist = new JLabel("ARTIST :");
				lblArtist.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblArtist.setBounds(197, 71, 108, 34);
				panel_1.add(lblArtist);
				
				JLabel lblSong = new JLabel("SONG :");
				lblSong.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblSong.setBounds(197, 26, 108, 34);
				panel_1.add(lblSong);
				
				JLabel lblAlbum = new JLabel("ALBUM :");
				lblAlbum.setFont(new Font("SansSerif", Font.BOLD, 10));
				lblAlbum.setBounds(197, 116, 108, 34);
				panel_1.add(lblAlbum);
				
				JLabel lblNewLabel_5 = new JLabel("");
				lblNewLabel_5.setIcon(new ImageIcon("F:\\sdl\\data\\5.jpg"));
				lblNewLabel_5.setBounds(22, 26, 132, 120);
				panel_1.add(lblNewLabel_5);
				
				JButton btnUploadSong = new JButton("UPLOAD SONG");
				btnUploadSong.setBackground(Color.WHITE);
				btnUploadSong.setSelectedIcon(null);
				btnUploadSong.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							int returnValue =openFileChooser.showOpenDialog(btnUploadSong);
							if(returnValue==JFileChooser.APPROVE_OPTION)
							{
								tempPath = openFileChooser.getSelectedFile().getPath();
								panel_2.setVisible(true);
								panel_4.setVisible(false);
							
							}else
							{
								JOptionPane.showMessageDialog(null,"No File Choosen");
							}
							
							
						}catch(Exception ex) {
							System.out.print(ex);
						}
					}
				});
				btnUploadSong.setFont(new Font("SansSerif", Font.BOLD, 15));
				btnUploadSong.setBounds(763, 11, 170, 39);
				panel_1.add(btnUploadSong);
				
				JButton btnUploadSample = new JButton("UPLOAD SAMPLE");
				btnUploadSample.setSelectedIcon(null);
				btnUploadSample.setBackground(Color.WHITE);
				btnUploadSample.setIcon(null);
				btnUploadSample.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int returnValue =openFileChooser.showOpenDialog(btnUploadSong);
						if(returnValue==JFileChooser.APPROVE_OPTION)
						{
							tempPath = openFileChooser.getSelectedFile().getPath();
							panel_4.setVisible(true);
							processSample();
						}else
						{
							JOptionPane.showMessageDialog(null,"No File Choosen");
						}
					}
				});
				btnUploadSample.setFont(new Font("SansSerif", Font.BOLD, 15));
				btnUploadSample.setBounds(542, 11, 170, 39);
				panel_1.add(btnUploadSample);
				
				JLabel label_4 = new JLabel("");
				label_4.setIcon(new ImageIcon("F:\\sdl\\data\\2.jpg"));
				label_4.setBounds(492, 0, 492, 300);
				panel_1.add(label_4);
				
				lblNewLabel_4 = new JLabel("");
				lblNewLabel_4.setIcon(new ImageIcon("F:\\sdl\\data\\2.jpg"));
				lblNewLabel_4.setBounds(0, 0, 492, 185);
				panel_1.add(lblNewLabel_4);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 984, 561);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		signup_password_cofirm = new JPasswordField();
		signup_password_cofirm.setToolTipText("MIN 8 CHARACTER");
		signup_password_cofirm.setForeground(Color.GRAY);
		signup_password_cofirm.setEchoChar('*');
		signup_password_cofirm.setBounds(569, 412, 338, 30);
		panel.add(signup_password_cofirm);
		
		JLabel label_5 = new JLabel("");
		label_5.setIcon(new ImageIcon("F:\\sdl\\data\\4.jpg"));
		label_5.setBackground(Color.BLACK);
		label_5.setForeground(Color.WHITE);
		label_5.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		label_5.setBounds(569, 442, 338, 2);
		panel.add(label_5);
		
		JLabel lblConfirmPassword = new JLabel("CONFIRM PASSWORD");
		lblConfirmPassword.setForeground(Color.WHITE);
		lblConfirmPassword.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblConfirmPassword.setBounds(569, 375, 338, 37);
		panel.add(lblConfirmPassword);
		
		JLabel label_3 = new JLabel("");
		label_3.setIcon(new ImageIcon("F:\\sdl\\data\\4.jpg"));
		label_3.setForeground(Color.WHITE);
		label_3.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		label_3.setBounds(84, 341, 338, 2);
		panel.add(label_3);
		
		login_password = new JPasswordField();
		login_password.setToolTipText("PASSWORD");
		login_password.setEchoChar('*');
		login_password.setForeground(Color.GRAY);
		login_password.setBounds(84, 311, 338, 30);
		panel.add(login_password);
		
		login_id = new JTextField();
		login_id.setToolTipText("USER ID");
		login_id.setForeground(Color.GRAY);
		login_id.setFont(new Font("SansSerif", Font.BOLD, 15));
		login_id.setColumns(10);
		login_id.setBounds(84, 215, 338, 30);
		panel.add(login_id);
		
		JLabel label_2 = new JLabel("");
		label_2.setForeground(Color.WHITE);
		label_2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		label_2.setBounds(84, 245, 338, 2);
		panel.add(label_2);
		
		signup_password = new JPasswordField();
		signup_password.setToolTipText("MIN 8 CHARACTER");
		signup_password.setForeground(Color.GRAY);
		signup_password.setEchoChar('*');
		signup_password.setBounds(569, 322, 338, 30);
		panel.add(signup_password);
		
		singup_name = new JTextField();
		singup_name.setToolTipText("NAME");
		singup_name.setFont(new Font("SansSerif", Font.BOLD, 15));
		singup_name.setForeground(Color.GRAY);
		singup_name.setColumns(10);
		singup_name.setBounds(569, 142, 338, 30);
		panel.add(singup_name);
		
		signup_id = new JTextField();
		signup_id.setToolTipText("E-MAIL");
		signup_id.setFont(new Font("SansSerif", Font.BOLD, 15));
		signup_id.setForeground(Color.GRAY);
		signup_id.setColumns(10);
		signup_id.setBounds(569, 232, 338, 30);
		panel.add(signup_id);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("F:\\sdl\\data\\4.jpg"));
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		label.setBounds(569, 172, 338, 2);
		panel.add(label);
		
		JLabel lblEmail = new JLabel("E-MAIL");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblEmail.setBounds(569, 195, 338, 37);
		panel.add(lblEmail);
		
		JLabel label_11 = new JLabel("");
		label_11.setIcon(new ImageIcon("F:\\sdl\\data\\4.jpg"));
		label_11.setForeground(Color.WHITE);
		label_11.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		label_11.setBounds(569, 352, 338, 2);
		panel.add(label_11);
		
		JLabel label_8 = new JLabel("");
		label_8.setIcon(new ImageIcon("F:\\sdl\\data\\4.jpg"));
		label_8.setForeground(Color.WHITE);
		label_8.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		label_8.setBounds(569, 262, 338, 2);
		panel.add(label_8);
		
		JLabel label_13 = new JLabel("PASSWORD");
		label_13.setForeground(Color.WHITE);
		label_13.setFont(new Font("SansSerif", Font.BOLD, 15));
		label_13.setBounds(569, 285, 338, 37);
		panel.add(label_13);
		
		JLabel lblName = new JLabel("NAME");
		lblName.setForeground(Color.WHITE);
		lblName.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblName.setBounds(569, 105, 338, 37);
		panel.add(lblName);
		
		JButton signup_button = new JButton("SIGN UP");
		signup_button.setBackground(Color.WHITE);
		signup_button.setSelectedIcon(null);
		signup_button.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				try {
					if(signup_id.getText().toString().equals("") || singup_name.getText().toString().equals("") )
					{
						JOptionPane.showMessageDialog(null,"No entry should be empty");
					} else
					{
						stmt=con.createStatement();
						sql="Select * from user where user_id='"+signup_id.getText()+"'";
						rs=stmt.executeQuery(sql);
						if(rs.next())
						{
							JOptionPane.showMessageDialog(null,"USER ID already exist");
						} else
						{
							pst=con.prepareStatement("Insert into user (`user_id`, `user_name`, `password`) values(?,?,?)");
							pst.setString(1, signup_id.getText());
							pst.setString(2, singup_name.getText());
							pst.setString(3, signup_password.getText().toString());
							if(signup_password.getText().toString().equals(signup_password_cofirm.getText().toString() ))
							{
								
								pst.executeUpdate();
								JOptionPane.showMessageDialog(null,"Sucessful Sign up","Success",JOptionPane.INFORMATION_MESSAGE);
								
							} else
							{
								JOptionPane.showMessageDialog(null,"Check Your Information","User Not Registered",JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					
					
				}catch(Exception ex) {
					System.out.print(ex);
				}
			}
		});
		signup_button.setForeground(new Color(255, 0, 51));
		signup_button.setFont(new Font("SansSerif", Font.BOLD, 20));
		signup_button.setBounds(569, 465, 343, 37);
		panel.add(signup_button);
		
		JButton login_button = new JButton("LOGIN");
		login_button.setBackground(Color.WHITE);
		login_button.setSelectedIcon(null);
		login_button.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				try {
					
					stmt=con.createStatement();
					sql="Select * from user where user_id='"+login_id.getText()+"' and password='"+login_password.getText().toString()+"'";
					rs=stmt.executeQuery(sql);
					if(rs.next())
					{
						JOptionPane.showMessageDialog(null,"Login Sucessfully.....");
						userid = login_id.getText();
						updateInfo();
						panel_1.setVisible(true);
						panel.setVisible(false);
						panel_3.setVisible(true);
					} else
					{
						JOptionPane.showMessageDialog(null,"Incorrect information");
					}
				}catch(Exception ex) {
					System.out.print(ex);
				}
			}
		});
		
				login_button.setForeground(new Color(0, 0, 51));
				login_button.setFont(new Font("SansSerif", Font.BOLD, 20));
				login_button.setBounds(84, 389, 343, 37);
				panel.add(login_button);
				
				JLabel lblPassword = new JLabel("PASSWORD");
				lblPassword.setForeground(Color.WHITE);
				lblPassword.setFont(new Font("SansSerif", Font.BOLD, 15));
				lblPassword.setBounds(84, 274, 338, 37);
				panel.add(lblPassword);
				
				JLabel lblUserId = new JLabel("USER ID");
				lblUserId.setFont(new Font("SansSerif", Font.BOLD, 15));
				lblUserId.setForeground(Color.WHITE);
				lblUserId.setBounds(84, 178, 338, 37);
				panel.add(lblUserId);
				
				JLabel lblLogin = new JLabel("LOGIN");
				lblLogin.setFont(new Font("SansSerif", Font.BOLD, 26));
				lblLogin.setForeground(Color.WHITE);
				lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
				lblLogin.setBounds(46, 105, 400, 48);
				panel.add(lblLogin);
				
				JLabel lblNewLabel_3 = new JLabel("REGISTER");
				lblNewLabel_3.setFont(new Font("SansSerif", Font.BOLD, 26));
				lblNewLabel_3.setForeground(Color.WHITE);
				lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel_3.setBounds(538, 55, 400, 62);
				panel.add(lblNewLabel_3);
				
				JLabel lblNewLabel_2 = new JLabel("");
				lblNewLabel_2.setIcon(new ImageIcon("F:\\sdl\\data\\4.jpg"));
				lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel_2.setBounds(491, 31, 2, 500);
				panel.add(lblNewLabel_2);
				
				JLabel label_1 = new JLabel("");
				label_1.setIcon(new ImageIcon("F:\\sdl\\data\\2.jpg"));
				label_1.setBackground(Color.WHITE);
				label_1.setBounds(538, 31, 400, 500);
				panel.add(label_1);
				
				JLabel lblNewLabel_1 = new JLabel("");
				lblNewLabel_1.setIcon(new ImageIcon("F:\\sdl\\data\\3.jpg"));
				lblNewLabel_1.setBackground(Color.WHITE);
				lblNewLabel_1.setBounds(46, 81, 400, 400);
				panel.add(lblNewLabel_1);
				
				JLabel lblNewLabel = new JLabel("");
				lblNewLabel.setIcon(new ImageIcon("F:\\sdl\\data\\back1.jpg"));
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel.setBounds(0, 0, 984, 561);
				panel.add(lblNewLabel);
	}
}
