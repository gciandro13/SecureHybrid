package view;

import java.io.IOException;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import system.MyServerSocket;

public class ServerGui {
	
	static MyServerSocket s;
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException  {
		
		//preparing UI for server user
		JFrame f = new JFrame("Server");
		JPanel p = new JPanel();
		JTextField txt = new JTextField(100);
	    p.add(txt);
		f.add(p);
		f.setSize(1200, 100);
		f.setResizable(true);
		f.setVisible(true);
		
		//initialize a ServerSocket
		s = new MyServerSocket();
		//startServerforRequest
		s.waitForRequest();
		//execute the request: show client's data
		String k = (String)s.execute();
		txt.setText(k);
	}

}
