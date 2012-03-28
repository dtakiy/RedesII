public class No {

               private int idLocal;

               private String [] ips;
               private int [] portas;
               private int [] mtus;

               public No(int id)
               {
                       setIdLocal(id);
                       ips = new String[6];
                       portas = new int[6];
                       mtus = new int [6];
               }

               public No(int id, String [] ip, int [] porta, int [] mtu)
               {
                       setIdLocal(id);
                       ips = ip;
                       portas = porta;
                       mtus = mtu;
               }


               public String getIP(int no)
               {
                       return ips[no-1];
               }

               public void setIP(String ip, int no)
               {
                       ips[no-1] = ip;
               }

               public int getPorta(int no)
               {
                       return portas[no-1];
               }

               public void setPorta(int porta, int no)
               {
                       portas[no-1] = porta;
               }

               public int getMTU(int no)
               {
                       return mtus[no-1];
               }

               public void setMTU(int mtu, int no)
               {
                       mtus[no-1] = mtu;
               }

               public void setIdLocal(int idLocal) {
                       this.idLocal = idLocal;
               }

               public int getIdLocal() {
                       return idLocal;
               }

               public boolean existeEnlace(int idNo)
               {
                       if (mtus[idNo-1] == 0)
                               return false;

                       return true;
               }



}

