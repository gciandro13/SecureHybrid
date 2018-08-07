package system;






import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import model.IMyServer;

public class MyServerSocket implements IMyServer {

	String str;
	public MyServerSocket() throws NoSuchAlgorithmException, IOException{
	
	}

	
    @Override
	public void waitForRequest() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {
		
		//Server generates its own keys (Public and Private key)
		KeyPairGenerator keysgen= KeyPairGenerator.getInstance("RSA");
		keysgen.initialize(1024);
		KeyPair keys=keysgen.generateKeyPair();
		
		PublicKey kpub=keys.getPublic();
		PrivateKey kpriv=keys.getPrivate();
		
		//wait the client's connection
		ServerSocket socket = new ServerSocket(8900);
		Socket sS = socket.accept();
		
		
		//Send Public Key to client as ObjectOutputStream
		InputStream in = sS.getInputStream();
		OutputStream out = sS.getOutputStream();
		ObjectOutputStream outp= new ObjectOutputStream(out);
		outp.writeObject(kpub);
		
		
		//Prepare to read first message from client: k cyphred (Asimmetric Phase)
		ObjectInputStream inp = new ObjectInputStream(in);
		byte[] keysessioncypher =(byte[]) inp.readObject();
	    
		//First PDU received: send ACK
	    String s= "ok";
		outp.writeObject(s);
		
	    //Prepare to read second message from client: data cyphred with k (Simmetric Phase)
	    byte[] datacypher = (byte[]) inp.readObject();
	   
	    
	   //Decrypt k with server private key , and then decrypt data with k
	    byte[] keysessionbytes ;
	    Cipher cipher = Cipher.getInstance("RSA");
	    cipher.init(Cipher.DECRYPT_MODE, kpriv);
	    keysessionbytes=cipher.doFinal(keysessioncypher);
	    
	    SecretKey secretkey= new SecretKeySpec(keysessionbytes, "AES");
	    
	    byte[] data;
	    Cipher cipher2= Cipher.getInstance("AES/ECB/PKCS5Padding");
	    cipher2.init(Cipher.DECRYPT_MODE, secretkey);
	    data=cipher2.doFinal(datacypher); 
	    
	    //set global String str to plaintext: that's the plaintext of the client
	    String plaintext= new String(data, "UTF-8"); 
	    str=plaintext;
	    
	    //close NET object
	    outp.close();
	    out.close();
	    inp.close();
	    in.close();
	    socket.close();
	}
	
	
	@Override
	public Object execute() {
		//do some stuff with data decrypted(for example, store on a secure DB)
		//return, global str modified with client data
		return this.str;
	}



}
