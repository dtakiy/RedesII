import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        InputStream in;
        ObjectInputStream ois;
		Enlace e = new Enlace(1);
		try {
	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
	      ObjectOutputStream oos = new ObjectOutputStream(bos);
	      oos.writeObject("123456789");
	      oos.flush();
	      oos.close();
	      
			bos.close();

	      byte [] data = bos.toByteArray();
	      if(e.enviarDatagrama(2, data))
	    	  System.out.println("COCOOOOO");
      	  in = new ByteArrayInputStream(data);
      	  ois = new ObjectInputStream(in);
      	System.out.println("Des" + (String)ois.readObject());
	      
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
	}

}
