package shared.database;

import java.io.File;
import java.io.IOException;

import net.ucanaccess.jdbc.JackcessOpenerInterface;
import com.healthmarketscience.jackcess.CryptCodecProvider;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;


public class CryptCodecOpener implements JackcessOpenerInterface 
{
	/**
	 * Pozwala otworzyć plik lokalnej bazy danych 
	 * zapisanej w formacie accdb zabezpieczonej szyfrowanym hasłem.
	 * @param file - scieżka do pliku lokalnej bazy
	 * @param password - hasło do lokalnej bazy w postaci tekstowej
	 * @return obiekt Database 
	 * @author Kamil Zimny
	 */
 	public Database open(File file,String password) throws IOException
 	{
 		DatabaseBuilder dbd =new DatabaseBuilder(file);
 		dbd.setAutoSync(false);
 		dbd.setCodecProvider(new CryptCodecProvider(password));
 		dbd.setReadOnly(false);
 		return dbd.open();
 	}

}


