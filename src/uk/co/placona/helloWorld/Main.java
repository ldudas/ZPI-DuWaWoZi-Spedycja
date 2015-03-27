package uk.co.placona.helloWorld;

import java.util.ArrayList;

import exceptions.DatabaseConnectionExeption;

public class Main {
	/*******************KORZYSTANIE Z KLASY DATABASECONNECTOR**************
	 **********************************************************************/
	public static void main(String[] args){
	String query = "SELECT * FROM Miasta";
	int numberOfResultColumns = 3;
	
	DatabaseConnector databaseConnector = new DatabaseConnector();
	try 
	{
		ArrayList<ArrayList<Object>> result = databaseConnector.getResultOfMySqlQuery(query,numberOfResultColumns);
		
		for(int i=0;i<result.size();i++)
		{
			// Tu implementowac dzialanie na zwroconych krotkach
			// Nalezy najpier zrzutowac obiekt czyli np.
			// (String) result.get(i).get(0)
			// (int) result.get(i).get(1) itp.
			// Lub tez skorzystac z petli pod spodem gdy w kazdej
			// kolumnie sa obiekty tego samego typu.
			for(int j=0;j<numberOfResultColumns;j++)
			{
				System.out.print( result.get(i).get(j).toString() + " ");
			}
			System.out.println();
		}
	} 
	catch (DatabaseConnectionExeption e) 
	{
		e.printStackTrace();
	}
	/***********************************************************************
	 ***********************************************************************/
}}
