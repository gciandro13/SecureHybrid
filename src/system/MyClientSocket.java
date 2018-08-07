package system;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import model.IMyClient;

public class MyClientSocket implements IMyClient {
	
	SecretKey secretkey;
	
	public MyClientSocket() throws UnknownHostException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, ClassNotFoundException{
		
		
	}


	@Override
	public void sendRequest(String string) throws UnknownHostException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, ClassNotFoundException, BadPaddingException, NoSuchPaddingException {
		
		//Client connect on socket Server: get Stream and an objectInputStream
		Socket clisock= new Socket("127.0.0.1", 8900);
		InputStream in = clisock.getInputStream();
		OutputStream out = clisock.getOutputStream();
		ObjectInputStream inp= new ObjectInputStream(in);
		
		//Read Public Server Key 
		PublicKey serverpubkey= (PublicKey)inp.readObject();
		
		
	    //generate K (Method:generate random and assign it to secretKey for Cypher Simmetric)
		byte[] bytes= generateKeySession();
		
	    //encrypt k(random bytes) with Public Server Key (First phase: Asimmethric Encrypt)
		
	    byte[] cipherText = null;
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE , serverpubkey);
		cipherText = cipher.doFinal(bytes);
	    
		//send cripted k on ObjectOutputStream
	    ObjectOutputStream outp= new ObjectOutputStream(out);
	    outp.writeObject(cipherText);
	    
	    //wait for Server's ack
	    String s= (String)inp.readObject();
	    if(!s.equals("ok")){ 
	    	System.out.println("Invalid ACK: Problem with passing msg" +s);
	    }
	    
		//encrypt data with SecretKey (Second phase: Simmetric Cypher)
	    byte[] cipherText2 = null;
	    Cipher cipher2= Cipher.getInstance("AES/ECB/PKCS5Padding"); //TODO: check AES
	    cipher2.init(Cipher.ENCRYPT_MODE, secretkey);
	    cipherText2= cipher2.doFinal(string.getBytes());
	    
	    //send crypted data
	    outp.writeObject(cipherText2);
	    
	    //close NET object
	    inp.close();
	    in.close();
	    outp.close();
	    out.close();
	    clisock.close();
	}
		


	@Override
	public byte[] generateKeySession() {
		//generate Key random: it will be the secretKey for the second phase of communication
		SecureRandom keySession=new SecureRandom();
		
		byte bytes[] = new byte[16];
	    keySession.nextBytes(bytes); 
		
	    
	    secretkey= new SecretKeySpec(bytes, "AES");
		return bytes;
	}

}
