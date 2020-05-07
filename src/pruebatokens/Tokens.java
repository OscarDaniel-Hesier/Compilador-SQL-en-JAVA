/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebatokens;

import java.util.Hashtable;

/**
 *
 * @author Hesier
 */
public class Tokens
{
    //### Atributos de la clase ####
   private StringBuffer tok;    //Para guardar el lexema del token
   private char car;            //Almacena un caracter de la expresionSQL
   public String token;         //Para que el token este disponible como String y no como StringBuffer
   public int intTipoToken;         //Guarda el tipo de token en forma numerica
   public String strTipoToken = new String();       //Guarda el Tipo de token en forma de cadena 
    public String strInstruccionSQL;        //Guarda la cadena SQL que se esta analizando
 
   //Tipos de token en formato numerico
   public final int     FIN_DE_ARCHIVO      =  -1;
   public final int     NUMERO_ENTERO       =  2;
   public final int     DELIMITADOR         =  3;
   public final int  SALTO_DE_LINEA     =  4;
   public final int  NINGUNO            =  5;
   public final int  IDENTIFICADOR      =  6;
   public final int  PALABRA_RESERVADA      =  7;
   public final int  VARIABLE             =  8;
   public final int  PARENTESIS         =  9;
   public final int     NUMERO_DECIMAL      =  10;
   public final int     ASTERISCO           =  11;
   public final int     ERROR                   =  12;
   public final int     OPERADOR                =   13;
 
    //indice  para recorrer caracter por caracter la expresionSQL
   private int indexExpSQL;     
 
   //Arreglo de palabras clave
   private static String palabras_clave[]  = {"create","insert","delete","select","database","from","where","and","upam","","into","values",",","update","by","between"};
   private static String palabras[]  =       {"create","insert","delete","select","database","from","where","and","upam","","into","values",",","update","by","between"};
   String  indice[];
   int contador, tamanio;
         
 
     Tokens(String cadSQLAParsear)
     {
         strInstruccionSQL = cadSQLAParsear+";";       
         //Inicializar con por default los atributos
         car = ' ';
         indexExpSQL = 0;      //Iniciar analisis en el primer caracter de la cadena
         tok  =  new StringBuffer();
          token = "";
         leerCaracter();
     }
     
//Constructor vacio
    Tokens()
    {
 
    }
 
    //Metodo que lee el siguiente token de la cadena SQL
    public void obtenerToken()
    {
        tok = new StringBuffer();       // Reinicializar las variables para poder leer otro token
        token = new String();       // Reinicializar variable para leer otro token  
        intTipoToken = NINGUNO;                 //Por defecto no es ningun tipo de token
        strTipoToken = "ninguno";               
        token = "";
 
        // SALTARSE ESPACIOS EN BLANCO Y TABULADORES  
        for(;; leerCaracter())
         {
            if( (char)car == ' ' || (char)car == '\t' )
               continue;
            else
               break;
         }
        
            //Comprobar si es un operador relacional
        switch(car)
        {
           case '<':
              if(leerCaracter('>'))
              {
                 tok.append('<');
                 tok.append('>');
              }
              else if( leerCaracter('=') )
              {
                 tok.append('<');
                 tok.append('=');
              }
              else
                 tok.append('<');
              break;
 
           case '>':
              if(leerCaracter('='))
              {
                 tok.append('>');
                 tok.append('=');
              }
              else
                 tok.append('>');
              break;
 
           case '!':                        //Operador No
              if(leerCaracter('='))
              {
                 tok.append('!');
                 tok.append('=');
              }
              else
                 tok.append('!');
              break;
 
        }      //Fin comprobbar si es operador relacional
     
         //Si la variable tiene algo se leyo un operador relacional
       if(tok.length()>0)
       {
          intTipoToken = OPERADOR;
          strTipoToken = "operador";
             token = tok.toString().toLowerCase();
          leerCaracter();
          return;
       }
 
            //Comprobar si es un operador aritmetico
       String delims = new String("+-=");
       int d = delims.indexOf(car);
       if( d >= 0 )
       {
          tok.append(car);
          intTipoToken = OPERADOR;           //Es un OPERADOR (aritmetico)
          strTipoToken = "operador";
             token = tok.toString().toLowerCase();
          leerCaracter();
          return;
       }
            //comprobar si es un delimitador
       delims = new String(",.;");
       d = delims.indexOf(car);
       if( d >= 0 )
       {
          tok.append(car);
          intTipoToken = DELIMITADOR;           //Es un delimitador punto o coma
          strTipoToken = "delimitador";
             token = tok.toString().toLowerCase();
          leerCaracter();
          return;
       }
 
            //Comprobar si son parentesis
       String parentesis = new String("()");
       int p = parentesis.indexOf(car);
       if( p >= 0 )
       {
          tok.append(car);
          intTipoToken = PARENTESIS;           //Es un parentesis
          strTipoToken= "parentesis";
             token = tok.toString().toLowerCase();
          leerCaracter();
          return;
       }
 
        // Comprobar si es una cadena
        // La cadena, se guarda sin comillas 
       if( (char)car == '\'' )
       {
           leerCaracter();  // Saltarse la comilla de apertura 
          while((char)car != '\'')
          {
              tok.append(car);
              leerCaracter();
          }
          leerCaracter();           // Saltarse la comilla de cierre
 
          intTipoToken = VARIABLE;
          strTipoToken= "char";
             token = tok.toString().toLowerCase();
          return;
       }
       
       
       
        
       //Comprobar Si es una identificador de usuario o una palabra clave de SQL
       if(Character.isLetter((char)car))
       {
          do
          {
             tok.append(car);
             leerCaracter();
          } while(Character.isLetterOrDigit( (char)car) || (char)car == '-' || (char)car == '_' || (char)car == '.' );
 
          
          
          
          
          
           if( contarPuntos(tok)>1 )
           {
              mensaje_error("El identificador \'" +tok.toString()+ "\' tiene demasiados puntos");
              intTipoToken = ERROR;
              strTipoToken = "error";
                  token = tok.toString().toLowerCase();
              return;
           }
            
             //comprobar si es una palabra clave, sino, entonces es un identificador
          if( buscar_keyword( tok.toString().toLowerCase() ) )
          {
             intTipoToken = PALABRA_RESERVADA;
             strTipoToken = "keyword";
                 token = tok.toString().toLowerCase();
          }
          else
          {
             intTipoToken = IDENTIFICADOR;
             strTipoToken = "identificador";
                 token = tok.toString().toLowerCase();
          }
          
          
          // if( palabras_clave.equals(PALABRA_RESERVADA) )
//{System.out.println("hay un error de sintaxis                      <-- ERROR !");
//} 
          
        return;      
    }
        
            //Si es un numero (entero o decimal)
       if(Character.isDigit((char)car))
       {      
          do
          {
             tok.append(car);
             leerCaracter();
          } while(Character.isDigit( (char)car) || (char)car == '.' );
           
                //Comprobar si es un entero
          if(contarPuntos(tok) == 0 )
          {
              if(esEntero(tok.toString()))
              {
                intTipoToken = NUMERO_ENTERO;
                strTipoToken = "int";
                    token = tok.toString().toLowerCase();
              }               
          }     //Comprobar si es un decimal
          else if(contarPuntos(tok) == 1)
          {
              if(esDecimal(tok.toString()))
              {
                intTipoToken = NUMERO_DECIMAL;
                strTipoToken = "decimal";
                    token = tok.toString().toLowerCase();
              }               
              else
              {
                  //Informar al usuario del error de sintaxis en el decimal
                  mensaje_error("El token \'" +tok.toString()+ "\' parece un decimal pero tiene un error de sintaxis");
                  intTipoToken = ERROR;
                  strTipoToken = "error";
                      token = tok.toString().toLowerCase();
              }
              
              
          }
          else if(contarPuntos(tok) > 1 )             //Si tiene mas de un punto
          {
              //Informar a usuario que un decimal no puede tener mas de 1 caracter punto
              mensaje_error(" Error de sintaxis El token \'" +tok.toString()+ "\' tiene mas de un punto");
              intTipoToken = ERROR;
              strTipoToken = "error";
                 token = tok.toString().toLowerCase();
          }
          return;
       }          //Fin de validar si es un numero
        
            //Comprobar si es asterisco
       if((char)car == '*' )
       {
          tok.append(car);
          intTipoToken = ASTERISCO;
          strTipoToken = "asterisco";
            token = tok.toString().toLowerCase();
          leerCaracter();
          return;
       }
  }          //Fin del metodo obtenerToken
 
   //Devuelve true si en una cadena que llega todos son numeros, false en caso contrario
    public boolean esEntero(String cad)
    {
        for(int i = 0; i<cad.length(); i++)
            if( !Character.isDigit(cad.charAt(i)) )
                return false;
 
        return true;
    }
 
    //Devuelve true si la cadena que llega tiene la sintaxis de un decimal
    public boolean esDecimal(String cad)
    {
        boolean hayPunto=false;
        StringBuffer parteEntera = new StringBuffer();
        StringBuffer parteDecimal = new StringBuffer();
        int i=0, posicionDelPunto;
 
        for( i=0;i<cad.length(); i++ )
            if ( cad.charAt(i) == '.')                          //Detectar si hay un punto decimal en la cadena
                hayPunto=true;
        if(hayPunto)                                            //Si hay punto guardar la posicion donde se encuentra el carater punto
            posicionDelPunto=cad.indexOf('.');                  //(si la cadena tiene varios puntos, detecta donde esta el primero).
        else
            return false;                                       //Si no hay punto; no es decimal
 
        if( posicionDelPunto == 0 || posicionDelPunto == cad.length()-1  )    //Si el punto esta al principio o al final no es un decimal
            return false;
 
        for( i=0;i<posicionDelPunto; i++ )
            parteEntera.append(cad.charAt(i)) ;                 //Guardar la parte entera en una variable
 
        for(i = 0; i<parteEntera.length(); i++)
            if( ! Character.isDigit(parteEntera.charAt(i)) )    //Si alguno de los caracteres de la parte entera
                return false;                                   //no son digitos no es decimal
 
        for( i=posicionDelPunto+1;i<cad.length(); i++ )
            parteDecimal.append(cad.charAt(i));                 //Guardar la parte decimal en una variable
 
        for(i = 0; i<parteDecimal.length(); i++)
            if( ! Character.isDigit(parteDecimal.charAt(i)) )   //Si alguno de los caracteres de la parte decimal no es un digito no es decimal
                return false;                                   //Incluye el caso en el que la cadena tenga dos o mas puntos
 
        return true;                                            //Si paso todas las pruebas anterior, la cadena es un Numero decimal
    }
     
   //Lee un caracter de la cadenaSQL
   void leerCaracter()  throws StringIndexOutOfBoundsException
   {     
       
            car = strInstruccionSQL.charAt(indexExpSQL);        /* leer siguiente caracter */
                   
            //Verificar si se a sobrepasado el indice maximo de caracteres en la expresion SQL
            if( indexExpSQL < strInstruccionSQL.length() )
               indexExpSQL++;
            else
               indexExpSQL = strInstruccionSQL.length()-1;
   }
 
    // lee un caracter x adelantado de la cadena SQL, el caracter que llega lo compara con
    //el caracter leido y si son iguales devuelve true, si son diferentes devuelve false
   boolean  leerCaracter(char c)
   {
      leerCaracter();
      if( (char)car != (char)c )
      {
          indexExpSQL--;
          return false;
          
      }   
 else
              {
               System.out.println("hay un error");
              }

//********************************** 
      car = ' ';
      return true;
              
   }
   
   //Cuenta el numero de caracteres punto que tiene una cadena especificada
   public int contarPuntos(StringBuffer cad)
   {
       int contador = 0;
       for(int i=0;i<cad.length();i++)
       {
           if(cad.charAt(i)=='.')
               contador++;
       }
       return contador;
   }
    
   //Devuelve true si la cadena que llega es una palabra clave, false en caso contrario
   //busca en la tabla de palabras clave la cadena que llega
   static boolean buscar_keyword(String identificador)
   {
      for(int i = 0;i< palabras_clave.length;i++)
      {
         if( palabras_clave[i].equals(identificador) )
               return true;
      }
              //create","insert","delete","select","set","from","where","and","or","not","into","values","group","having","by","between"};
  
            if(! palabras_clave.equals(palabras) )
{System.out.println("hay un error de sintaxis           <-- ERROR !");
} 
            
      return false;
   }
    
    // Muestra un msg de error en el area de msgs del parser 
   public void mensaje_error(String strMsg)
   {
       System.out.print("\n" + strMsg );
   }   
 
}       //Fin de la clase Tokens.java