package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import system.MyClientSocket;

public class ClientGui {
	
	static MyClientSocket c;
	public static void main(String[] args) throws UnknownHostException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ClassNotFoundException, InvalidKeySpecException {
		
		//preparing client UI
		JFrame f= new JFrame("Client");
		JPanel p= new JPanel();
		JTextField txt = new JTextField(100);
		JButton but = new JButton("Send");
		p.add(but);
		p.add(txt);
		f.add(p);
		f.setSize(1200, 100);
		f.setResizable(true);
		f.setVisible(true);
		
		
		//initialize a ClientSocket
		MyClientSocket cli= new MyClientSocket();
		
		//add an actionListener over the button (Anonymus class)
		but.addActionListener(new ActionListener(){
		      public void actionPerformed( ActionEvent e)
		      {
		         //the button is pressed, try to sendRequest(data) with clientSocket
		       
					try {
						cli.sendRequest(txt.getText());
					} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException
							| InvalidKeySpecException | ClassNotFoundException | BadPaddingException
							| NoSuchPaddingException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		      }
		});
		 
	}

}
