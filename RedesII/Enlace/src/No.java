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
                       return ips[no];
               }

               public void setIP(String ip, int no)
               {
                       ips[no] = ip;
               }

               public int getPorta(int no)
               {
                       return portas[no];
               }

               public void setPorta(int porta, int no)
               {
                       portas[no] = porta;
               }

               public int getMTU(int no)
               {
                       return mtus[no];
               }

               public void setMTU(int mtu, int no)
               {
                       mtus[no] = mtu;
               }

               public void setIdLocal(int idLocal) {
                       this.idLocal = idLocal;
               }

               public int getIdLocal() {
                       return idLocal;
               }

               public boolean existeEnlace(int idNo)
               {
                       if (mtus[idNo] != 0)
                               return false;

                       return true;
               }



}

