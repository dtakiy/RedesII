import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    	   	//leArquivoCI("E:\\Redes II\\Projeto\\Enlace\\src\\acr.txt",id);
    	   	leArquivoCI("/tmp/default/Área de trabalho/Enlace/src/acr.txt",id);
    	   	//System.out.println("IPs\n" + noLocal.getIP(1) + "\n" + noLocal.getIP(2) + "\n" + noLocal.getIP(3) + "\n" + noLocal.getIP(4)+ "\n" + noLocal.getIP(5)+ "\n" + noLocal.getIP(6));
    	   	//System.out.println("Portas\n" + noLocal.getPorta(1) + "\n" + noLocal.getPorta(2) + "\n" + noLocal.getPorta(3) + "\n" + noLocal.getPorta(4)+ "\n" + noLocal.getPorta(5)+ "\n" + noLocal.getPorta(6));
    	   	//System.out.println("MTUs\n" + noLocal.getMTU(1) + "\n" + noLocal.getMTU(2) + "\n" + noLocal.getMTU(3) + "\n" + noLocal.getMTU(4)+ "\n" + noLocal.getMTU(5)+ "\n" + noLocal.getMTU(6));
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
           Datagrama dtg;
           
           //Gera o c�digo de verifica��o, e o coloca junto com os dados em um quadro para envio
           dtg = new Datagrama(dados,true);
           
            //Verifica se o enlace existe
            if (!noLocal.existeEnlace(idDestino))
            {
            	System.out.println("Enlace n�o existe\n");
            	return false;
            }
            	
            
            //Verifica se o tamanho do pacote eh menor que o MTU
            
            
            if (dtg.getQuadro().length > noLocal.getMTU(idDestino))
            {
            	System.out.println("Conte�do maior que o MTU ::" + noLocal.getMTU(idDestino) + ":: " + dtg.getQuadro().length + "\n");
            	return false;
            }
            
            
            //Codigo de verificacao foi gerado na hora que o datagrama foi instanciado
            
            //envia o pacote
            try {
            	socket = new GarbledDatagramSocket(InetAddress.getByName(noLocal.getIP(noLocal.getIdLocal())),noLocal.getPorta(noLocal.getIdLocal()),0,0,100);
            	//socket = new GarbledDatagramSocket(InetAddress.getByName("172.16.12.225"),0,0,0,0);

            	
				p = new DatagramPacket(dtg.getQuadro(), dtg.getQuadro().length, InetAddress.getByName(noLocal.getIP(idDestino)), noLocal.getPorta(idDestino));
				System.out.println("Enviando para o endereço "+noLocal.getIP(idDestino)+":"+noLocal.getPorta(idDestino));
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
            socket.close();
            return true;
            	
       } 
       
       public byte [] receberDatagrama (int idorigem) throws IOException
       {
    	   System.out.println("recebendo no endereço "+noLocal.getIP(noLocal.getIdLocal())+":"+noLocal.getPorta(noLocal.getIdLocal()));
          DatagramSocket socket = new DatagramSocket(noLocal.getPorta(noLocal.getIdLocal()),InetAddress.getByName(noLocal.getIP(noLocal.getIdLocal())));
          //DatagramSocket socket = new DatagramSocket(500,InetAddress.getByName("172.16.12.225"));
          byte [] data = new byte[noLocal.getMTU(idorigem)];
           DatagramPacket dp = new DatagramPacket(data,data.length);
           Datagrama dtg;
           boolean verifica_checksum; // booleano que recebe valor do checksum
               
              if (!noLocal.existeEnlace(idorigem)) //caso enlace nao exista retorna null
                  return null;
              
              socket.receive(dp); //soquete recebe datagrama
              
              dtg = new Datagrama(dp.getData(),false);
              verifica_checksum = dtg.solvechecksum(); //verifica se checksum esta correto
              socket.close();
              if(verifica_checksum)
            	  return dtg.getConteudo(); //se checksum estiver correto retorna conteudo do datagrama
              System.out.println("Checksum dos dados recebidos: "+dtg.getChecksum());
              return null;                          
       }
				

}

