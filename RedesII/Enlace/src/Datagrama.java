import java.io.Serializable;

public class Datagrama implements Serializable{

       /**
        *
        */
       private static final long serialVersionUID = 42L;

       private int checksum = 0;
       private byte [] conteudo;

       
       public Datagrama(byte [] data)
       {
    	   conteudo = data;
    	   generateCheckSum();
    	   
       }

       public int getChecksum() {
               return checksum;
       }
       public void setChecksum(int checksum) {
               this.checksum = checksum;
       }
       public byte [] getConteudo() {
               return conteudo;
       }
       public void setConteudo(byte [] conteudo) {
               this.conteudo = conteudo;
       }
       
       private void generateCheckSum() {
           
           //Calculate CheckSum
    	   int sum = 0;
    	   
           for(int i = 0 ; i < conteudo.length; i++){
          
              sum =+ (int)conteudo[i];
           }
                 checksum = sum % 10000;
               
               
       }
      
       public boolean solvechecksum(){
       //Solve Checksum
    	   
    	   int sum = 0;
    	   
           for(int j = 0 ; j < conteudo.length; j++){
          
              sum =+ (int)conteudo[j];
           }
           sum = sum % 1000;
          
           if(checksum != sum)
           return false;
            
             else
               return true;
       }
      
 
	
}
