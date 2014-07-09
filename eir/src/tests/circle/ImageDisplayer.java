package tests.circle;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class ImageDisplayer
{

	public static void main( final String avg[] ) throws IOException
	{
		ImageDisplayer abc = new ImageDisplayer();
	}

	public ImageDisplayer() throws IOException
	{
		Mandala mandala = new Mandala();
		BufferedImage img = mandala.generateImage();

		ImageIcon icon = new ImageIcon( img );
		JFrame frame = new JFrame();
		frame.setLayout( new FlowLayout() );
//		frame.setSize( img.getWidth(), img.getHeight() );
		JLabel lbl = new JLabel( icon );

		frame.add( lbl );
		frame.validate();
		frame.pack();
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}
