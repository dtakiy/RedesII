import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Adulterador extends Thread{
	
	byte [] data;
	boolean tipo;
	String st;
	
	public Adulterador(String str)
	{
		st= str;
		tipo = true;
	}
	
	public Adulterador()
	{
		tipo = false;
	}	
	
	
	public void envia(String s)
	{

			Enlace e = new Enlace(2);

			
			try {
		      ByteArrayOutputStream bos = new ByteArrayOutputStream();
		      ObjectOutputStream oos = new ObjectOutputStream(bos);
		      
		      oos.writeObject(s);
		      oos.flush();
		      oos.close();
		      
				bos.close();

		      data = bos.toByteArray();
		      if(e.enviarDatagrama(3, data))
		    	  System.out.println("ENVIOU "+data.length +" bytes");

		      
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	public void recebe()
	{
	        InputStream in;
	        ObjectInputStream ois;
	        Enlace e = new Enlace(2);
	        
	        try {
				data = e.receberDatagrama(1);
		      	  in = new ByteArrayInputStream(data);
		      	  ois = new ObjectInputStream(in);
		      	System.out.println("RECEBEU " + (String)ois.readObject());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
	        

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if(tipo)
		{
			System.out.println("Thread enviando iniciada: " + st);
			envia(st);
		}
		else
		{
			System.out.println("Thread recebendo iniciada");
			recebe();
			
		}
	}

}
