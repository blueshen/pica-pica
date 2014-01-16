package com.is.general;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JDesktopPane;
import javax.swing.border.EtchedBorder;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import com.is.filters.JPEGFilter;
import com.is.utils.ImagePanel;

import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;

public class UserInterface extends JFrame {

	private static final long serialVersionUID = 7413631420807225473L;
	private JPanel contentPane;
	private static UserInterface instance = null;
	private JProgressBar progressBar;
	
	final JFileChooser fcf = new JFileChooser();
	final JFileChooser fcd = new JFileChooser();
	
	final JLabel lblNewLabel;
	final JLabel lblNewLabel_1;
	
	final JButton btnStartImageSimilarity;
	final JButton btnSelectImage;
	final JButton btnSelectImagesTo;
	
	private JPanel panel_2;
	private JPanel panel_4;
	private JScrollPane scrollPane_1;
	
	private ImagePanel panel_3;
	
	ImageSimilarity is = ImageSimilarity.getInstance();
	
	File image;
	File directory;
	
	private Thread thread;
	
	int left = 10;
	int top = 10;
	private JTextField textField;
	
	/**
	 * Returns instance of UserInterface
	 * 
	 * @return
	 */
	public static UserInterface getInstance()
	{
		if(instance == null)
		{
			instance = new UserInterface();
		}
		
		return instance;
	}
	
	/**
	 * Set progress bar value
	 * 
	 * @param value
	 */
	public void setProgress(double value)
	{
		progressBar.setValue((int)value);
		progressBar.repaint();
	}
	
	/**
	 * Shows error
	 * 
	 * @param msg
	 */
	public void showError(String msg)
	{
		JOptionPane.showMessageDialog(this, msg, "Error Occurred", JOptionPane.ERROR_MESSAGE);
	}
	
	public void clearUI()
	{
		lblNewLabel.setText("Please select .jpg reference image");
		lblNewLabel_1.setText("Please select directory with images to compare with");
		
		btnStartImageSimilarity.setEnabled(false);
	}
	
	public void clearThumbnails()
	{
		left = 10;
		top = 10;
		
		panel_4.removeAll();
		panel_4.repaint();
		
		progressBar.setValue(0);
	}
	
	/**
	 * Create the frame.
	 */
	protected UserInterface() {
		
		setTitle("ImageSimilarity");
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 731, 503);
		
		contentPane = new JPanel();
		contentPane.setForeground(Color.LIGHT_GRAY);
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblNewLabel = new JLabel("Please select .jpg reference image");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(12, 49, 302, 14);
		panel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Please select directory with images to compare with");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setBounds(336, 49, 356, 14);
		panel.add(lblNewLabel_1);
		
		// Progress bar <3
		progressBar = new JProgressBar();
		progressBar.setForeground(Color.RED);
		progressBar.setBounds(12, 74, 680, 14);
		panel.add(progressBar);
		
		fcf.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fcf.setFileFilter(new JPEGFilter());
		fcd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		// Panel with buttons
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		panel_1.setBounds(12, 5, 680, 33);
		panel.add(panel_1);
		
		btnSelectImage = new JButton("Select Reference Image");
		btnSelectImagesTo = new JButton("Select Images to Compare with");
		btnStartImageSimilarity = new JButton("Calculate Image Similarity");
		
		// Get reference image
		btnSelectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fcf.showOpenDialog(UserInterface.this);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            image = fcf.getSelectedFile();
		            is.setImage(image);
		            lblNewLabel.setText(image.getAbsolutePath());
		            
		            panel_3.create(image, 200, 200);
		            panel_3.repaint();
		            panel_2.repaint();
		        }
		        
		        if(is.ifFiles())
		        {
		        	btnStartImageSimilarity.setEnabled(true);
		        }
		        else
		        {
		        	btnStartImageSimilarity.setEnabled(false);
		        }
			}
		});
		panel_1.add(btnSelectImage);
				
		// Get images to compare with
		btnSelectImagesTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fcd.showOpenDialog(UserInterface.this);
				
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            directory = fcd.getSelectedFile();
		            is.setDirectory(directory);
		            lblNewLabel_1.setText(directory.getAbsolutePath());
		        }
		        
		        if(is.ifFiles())
		        {
		        	btnStartImageSimilarity.setEnabled(true);
		        }
		        else
		        {
		        	btnStartImageSimilarity.setEnabled(false);
		        }
			}
		});
		panel_1.add(btnSelectImagesTo);
		
		// Run ImageSimilarity algorithm
		btnStartImageSimilarity.setEnabled(false);
		btnStartImageSimilarity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				UserInterface.getInstance().clearThumbnails();
				
				thread = new Thread(new Run());
				thread.start();

			}
		});
		panel_1.add(btnStartImageSimilarity);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBackground(Color.DARK_GRAY);
		panel_2.setBounds(12, 99, 220, 344);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		panel_3 = new ImagePanel();
		panel_3.setBackground(Color.DARK_GRAY);
		panel_3.setBounds(10, 11, 200, 200);
		panel_2.add(panel_3);
		
		panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_4.setBackground(Color.DARK_GRAY);
		panel_4.setBounds(242, 99, 450, 344);
		panel.add(panel_4);
		panel_4.setLayout(null);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setToolTipText("test");
		scrollPane_1.setBounds(428, 335, -419, -328);
		panel_4.add(scrollPane_1);
	}
	
	public synchronized void addThumbnail(File thumb, double percent)
	{
		ImagePanel panel_t = new ImagePanel();
		panel_t.setBackground(Color.DARK_GRAY);
		panel_t.setBounds(left, top, 100, 100);
		
		percent = 100 - (double)((percent * 100) / 11041);
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setText(df.format(percent) + "%");
		textField.setBounds(left + 5, top - 5, 50, 20);
		textField.setColumns(10);
		
		panel_4.add(textField);
		panel_4.add(panel_t);
		
		panel_t.create(thumb, 100, 100);
		panel_t.repaint();
		panel_4.repaint();
	
		left += 110;
		top += 0;
		
		if(left % 450 == 0)
		{
			top += 110;
			left = 10;
		}
	}
	
	class Run implements Runnable
	{
		@Override
		public void run() 
		{
			try {
				is.run();
				UserInterface.getInstance().setProgress(100);
			} 
			catch (Exception e)
			{
				UserInterface.getInstance().clearUI();
				return;
			}
			
		}
		
	}
}
