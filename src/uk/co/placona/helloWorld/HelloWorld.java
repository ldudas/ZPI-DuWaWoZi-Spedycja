package uk.co.placona.helloWorld;


public class HelloWorld 
{

	/**
	 * This method return a string value with "Hello World"
	 * @return string = "Hello World"
	 */
	public  String sayHello() {
		return "Hello World";
	}
		

	public static void main(String [] args)
	{
		HelloWorld newHello = new HelloWorld();
		System.out.println(newHello.sayHello());
	}

}
