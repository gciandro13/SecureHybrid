package model;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public interface IMyServer {

	public void waitForRequest() throws IOException, NoSuchAlgorithmException, InvalidKeyException, 
	IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, NoSuchPaddingException;
	
	public Object execute();
}
