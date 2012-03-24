import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class Enlace {

       private No noLocal;

       public Enlace(String path, int id)
       {
    	   leArquivoCI(path,id);
       }
       
       public Enlace(int id)
       {
    	   	leArquivoCI("E:\\Redes II\\Projeto\\Enlace\\src\\acr.txt",id);
    	   	System.out.println("IPs\n" + noLocal.getIP(0) + "\n" + noLocal.getIP(1) + "\n" + noLocal.getIP(2) + "\n" + noLocal.getIP(3)+ "\n" + noLocal.getIP(4)+ "\n" + noLocal.getIP(5));
    	   	System.out.println("Portas\n" + noLocal.getPorta(0) + "\n" + noLocal.getPorta(1) + "\n" + noLocal.getPorta(2) + "\n" + noLocal.getPorta(3)+ "\n" + noLocal.getPorta(4)+ "\n" + noLocal.getPorta(5));
    	   	System.out.println("MTUs\n" + noLocal.getMTU(0) + "\n" + noLocal.getMTU(1) + "\n" + noLocal.getMTU(2) + "\n" + noLocal.getMTU(3)+ "\n" + noLocal.getMTU(4)+ "\n" + noLocal.getMTU(5));
       }

       public void leArquivoCI(String path, int id)
       {
           	BufferedReader br;
           	FileReader fr;
           	String str;
           	String [] ip = new String[6];
           	int [] porta = new int[6];
           	int [] mtu = new int [6];
           	int i,j;
           	boolean flag = false;
           	StringTokenizer tok;

           	for (i = 0; i<6 ;i++)
           		mtu[i] = 0;

           	try {
                   fr = new FileReader(path);
                   br = new BufferedReader(fr);
                   str = br.readLine();
                   while (!str.equals("="))
                   {
                           if (str.equals("/"))
                           {
                        	   flag = true;
                        	   str = br.readLine();
                           }
                                  
                           tok = new StringTokenizer(str, ":");
                           if (!flag)
                           {
                                   while(tok.hasMoreTokens())
                                   {
                                           i = Integer.parseInt(tok.nextToken());
                                           i--;
                                           ip[i] = tok.nextToken();
                                           porta[i] = Integer.parseInt(tok.nextToken());
                                   }
                           }
                           else
                           {
                                   while(tok.hasMoreTokens())
                                   {
                                	   
                                           i = Integer.parseInt(tok.nextToken());
                                           j = Integer.parseInt(tok.nextToken());

                                           if (i == id)
                                                   mtu[--j] = Integer.parseInt(tok.nextToken());
                                           else if (j == id)
                                                            mtu[--i] = Integer.parseInt(tok.nextToken());
                                           		else tok.nextToken();
                                   }
                           }
                           str = br.readLine();
                   }


                   
           	} catch (FileNotFoundException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
           	} catch (IOException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
           	}

           	noLocal = new No(id,ip,porta,mtu);
       }

       public boolean enviarDatagrama(int idDestino, byte [] dados)
       {
           GarbledDatagramSocket socket;
           DatagramPacket p;
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           ObjectOutputStream out;
           Datagrama dtg;
           
           //Gera o código de verificação, e o coloca junto com os dados em um datagrama para envio
           dtg = new Datagrama(dados);
             
            //Verifica se o enlace existe
            if (!noLocal.existeEnlace(idDestino))
            {
            	System.out.println("Enlace não existe\n");
            	return false;
            }
            	
            
            //Verifica se o tamanho do pacote eh menor que o MTU
            
            try {
            	out = new ObjectOutputStream(baos);
            	
				out.writeObject(dtg);
				out.flush();//transforma o datagrama em um vetor de bytes
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            if (baos.toByteArray().length > noLocal.getMTU(idDestino-1))
            {
            	System.out.println("Conteúdo maior que o MTU ::" + noLocal.getMTU(idDestino-1) + ":: " + baos.toByteArray().length + "\n");
            	return false;
            }
            
            //Codigo de verificacao foi gerado na hora que o datagrama foi instanciado
            
            //envia o pacote
            try {
            	//socket = new GarbledDatagramSocket(InetAddress.getByName(noLocal.getIP(noLocal.getIdLocal())),0,0,0,0);
            	socket = new GarbledDatagramSocket(InetAddress.getByName("172.16.13.37"),0,0,0,0);
				p = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, InetAddress.getByName(noLocal.getIP(idDestino)), noLocal.getPorta(idDestino));
				socket.send(p);
            } catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
            
            return true;
            	
       }
       
       public byte [] receberDatagrama (int idorigem) throws IOException
       {
          
           DatagramSocket socket = new DatagramSocket(idorigem, null);
           InputStream in;
           ObjectInputStream ois;
           DatagramPacket dp = null;
           Datagrama dtg;
           boolean verifica_checksum; // booleando que recebe valor do checksum
               
              if (!noLocal.existeEnlace(idorigem)) //caso enlace nao exista retorna null
                  return null;
     
              socket.receive(dp); //soquete recebe datagrama
              
          	  in = new ByteArrayInputStream(dp.getData());
          	  ois = new ObjectInputStream(in);
          	  try {
				dtg = (Datagrama) ois.readObject(); //le datagrama

              verifica_checksum = dtg.solvechecksum(); //verifica se checksum esta correto
              
              if(verifica_checksum)
            	  return dtg.getConteudo(); //se checksum estiver correto retorna conteudo do datagrama
    			} catch (ClassNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} return null;                          
       }
				

}

