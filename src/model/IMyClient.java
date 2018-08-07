package model;

import java.io.IOException;

import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public interface IMyClient {
    
	public void sendRequest(String myrequest) throws UnknownHostException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException,
	InvalidKeyException, InvalidKeySpecException, ClassNotFoundException, BadPaddingException, NoSuchPaddingException;
	
    public byte[] generateKeySession();

}
