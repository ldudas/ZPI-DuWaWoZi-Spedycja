package database;

import java.io.File;
import java.io.IOException;

import net.ucanaccess.jdbc.JackcessOpenerInterface;
import com.healthmarketscience.jackcess.CryptCodecProvider;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;


public class CryptCodecOpener implements JackcessOpenerInterface 
{
 	public Database open(File file,String password) throws IOException
 	{
 		DatabaseBuilder dbd =new DatabaseBuilder(file);
 		dbd.setAutoSync(false);
 		dbd.setCodecProvider(new CryptCodecProvider(password));
 		dbd.setReadOnly(false);
 		return dbd.open();
 	}

}


