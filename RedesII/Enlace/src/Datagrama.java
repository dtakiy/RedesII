import java.io.Serializable;

public class Datagrama implements Serializable{

       /**
        *
        */
       private static final long serialVersionUID = 42L;

       private int checksum = 0;
       private byte [] conteudo;
       private byte [] quadro;

       
       public Datagrama(byte [] data, boolean t)
       {
    	   if(t)
    	   {
    		   conteudo = data;
    	   	   generateCheckSum();
    	   	   montaQuadro();
    	   }
    	   else
    	   {
    		   quadro = data;
    		   desmontaQuadro();
    		   
    		   
    	   }
    	   
       }
       
       

    private void montaQuadro() {
    	quadro =  new byte [conteudo.length + 4];
		System.arraycopy( intToByteArray(checksum),0 ,quadro, 0, 4);
		System.arraycopy( conteudo,0 ,quadro, 4, conteudo.length);
		
	}
    
    private void desmontaQuadro()
    {
    	byte [] cs = new byte[4];
    	
    	conteudo =  new byte [quadro.length - 4];
    	System.arraycopy(quadro,0,cs,0,4);
    	System.arraycopy(quadro,4,conteudo,0,conteudo.length);
    	
    	checksum = byteArrayToInt(cs);
    	
    }

    
    private  byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
    
    private int byteArrayToInt(byte [] b) {
        return (b[0] << 24)
                + ((b[1] & 0xFF) << 16)
                + ((b[2] & 0xFF) << 8)
                + (b[3] & 0xFF);
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
       
       public byte [] getQuadro() {
           return quadro;
	   }
	   public void setQuadro(byte [] quadro) {
	           this.quadro = quadro;
	   }
       
       private void generateCheckSum() {
           
           //Calculate CheckSum
    	   int sum = 0;
    	   
           for(int i = 0 ; i < conteudo.length; i++){
          
              sum += (int)conteudo[i];
           }
                 checksum = sum % 10000;
               
               
       }
      
       public boolean solvechecksum(){
       //Solve Checksum
    	   
    	   int sum = 0;
    	   
    	   
           for(int j = 0 ; j < conteudo.length; j++){
          
              sum += (int)conteudo[j];
           }
          sum = sum % 10000;
           
          
           if(checksum != sum)
           return false;
           
               return true;
       }
      
 
	
}
