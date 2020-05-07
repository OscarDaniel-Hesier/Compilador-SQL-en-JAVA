/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebatokens;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Hesier
 */
public class PruebaTokens   
{
   //Tipo de token que puede haber en la cadena
   public final int     FIN_DE_ARCHIVO  =  -1;
   public final int     NUMERO_ENTERO   =   2;
   public final int     DELIMITADOR     =   3;//== SIGNO
   public final int     SALTO_DE_LINEA  =   4;
   public final int     NINGUNO         =   5;
   public final int     IDENTIFICADOR   =   6;
   public final int     PALABRA_RESERVADA   =   7;
   public final int     VARIABLE          =   8;
   public final int     PARENTESIS      =  9;
   public final int     NUMERO_DECIMAL   =   10;
   public final int     ASTERISCO   =       11;
   public final int     ERROR   =       12;
      
   //El constructor espera recibir una cadena sql de la cual se quieren extraer sus tokens
   private void analizarCadenaSQL(String cadSQL)
   {
      Tokens tokens = new Tokens(cadSQL);
 
//      System.out.println("\n\n");
      //Este bucle imprime los tokens que se van leyendo y el tipo de token que se leyo de la cadena SQL
      tokens.obtenerToken();        //Obtener el primer token de la cadena SQL
      while( ! tokens.token.equals(";") )
      {
         switch(tokens.intTipoToken)
         {
            case NUMERO_ENTERO:
               System.out.println(tokens.token+" \t\t\tNUMERO_ENTERO");
               break;
            case NUMERO_DECIMAL:
               System.out.println(tokens.token+" \t\t\tNUMERO_DECIMAL");
               break;
            case PALABRA_RESERVADA:
               System.out.println(tokens.token+" \t\t\tPALABRA_RESERVADA");
               break;
            case IDENTIFICADOR:
               System.out.println(tokens.token+" \t\t\tIDENTIFICADOR");
               break;
            case VARIABLE:
               System.out.println(tokens.token+" \t\t\tVARIABLE");
               break;
            case PARENTESIS:
               System.out.println(tokens.token+" \t\t\tPARENTESIS");
               break;
            case DELIMITADOR:
               System.out.println(tokens.token+" \t\t\tSIGNO");
               break;
            case ASTERISCO:
               System.out.println(tokens.token+" \t\t\tASTERISCO");
               break;
            case ERROR:
               System.out.println(tokens.token + " \t\t\t<-- ERROR !");
               break;
         }
            tokens.obtenerToken();
        }
   }
 
   public static void main(String args[]) throws FileNotFoundException, IOException
   {
          
        String texto= ("hesier.txt");
        BufferedReader br = new BufferedReader (new FileReader ("hesier.txt"));
         texto = br.readLine();
        
        PruebaTokens prueba1 = new PruebaTokens();
        prueba1.analizarCadenaSQL(texto);
        
        /* hash.funcionHash(("hesier.txt"), hash.arreglo);
        hash.mostrar();*/
        
        
   }
}