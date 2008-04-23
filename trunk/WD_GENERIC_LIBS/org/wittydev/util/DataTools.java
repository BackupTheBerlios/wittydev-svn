package org.wittydev.util;
import java.lang.reflect.Array;

import org.wittydev.logging.LoggingService;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class DataTools {

    public DataTools() {
    }


    public static byte[] reorderByteArray( byte[] dumb ){
        return null;
    }
    /***
    *  Il metodo seguente permette di decomprimere un array di byte compressi
    *  col metodo compressColsIndex, in un altro array di byte!
    *
    */
    public static byte[] uncompressByteArray( byte[] dumb ){
      int bigger=-1;
      int newLen=0;

      //if (dumb==null|| (dumb.length==1) return colsIndex;

      for (int i=0; i<dumb.length; i++){
          for (int j=0; j<7; j++)
            if ( (dumb[i] & (byte)Math.pow(2, j ))>0 )
              newLen++;
      }

      byte[] colsIndex=new byte [newLen];
      int k=0;
      for (int i=0; i<dumb.length; i++){
          for (int j=0; j<7; j++)
            if ( (dumb[i] & (byte)Math.pow(2, j ))>0 )
              colsIndex[k++]=(byte)(i*7+j);
      }
      return colsIndex;
  }

  /***
   *  Il metodo seguente permette di comprimere un array di byte,
   *  in un altro array di byte!
   *  la compressione inoltre ha il seguente effetto:
   *    - riordina l'array
   *    - toglie gli eventuali doppioni
   *  L' dea e' la seguente:
   *    l'array di byte { 0,2,4,5,6 } diventa {117} cioe': 01110101
   *  l'ottavo bit dii ogni ellemento dell'array di byte risultante
   *  e' impostato a 0 (zero) se e' l'ultimo dell'indice, a 1 (uno)
   *  altrimenti
   */
  public  static byte[] compressByteArray( byte[] colsIndex ){
      int bigger=-1;
      for ( int i=0; i<colsIndex.length; i++)
        if (bigger<colsIndex[i])bigger=colsIndex[i];

      bigger+=1;

      int newLen=(bigger/7)+((bigger%7>0)?1:0);
      byte [] compB= new byte[newLen];
      for ( int i= compB.length - 2; i>=0; i--){
        compB[i] = (byte)(compB[i] | (byte)(128));  // 2^7
      }
      for ( int i=0;i<colsIndex.length; i++){
        byte dummy=colsIndex[i];
        compB[dummy/7] = (byte)(compB[dummy/7] | (byte)(Math.pow(2,(dummy%7))) );
      }
      return compB;
  }


   /**
    * Dati due array di indici "colsIndex" e "newColsIndex",
    * ed un array di valori "values" relativo
    * al primo array di indici (colsIndex), questo metodo
    * retituisce un nuovo array di valori compatibili con
    * il secondo array di indici "newColsIndex".
    * Se un indice e' presente piu' in "colsIndex",
    * viene preso solo il valore di quello con indice maggiore
    *
    *
    */
   public static Object[] reorderArrayObject(
                          byte[] actualOrder, byte[] newOrder,
                          Object[] values ){

        Object[] valuesD=new Object[newOrder.length];
       // Se un indice e' presente piu' in "colsIndex",
       // viene preso solo il valore di quello con indice minore
        for ( int i=0; i<actualOrder.length; i++){
          for (int j=0; j<newOrder.length; j++)
            if (newOrder[j]==actualOrder[i]) {
                valuesD[j]=values[i];
                break;
            }
        }

        return valuesD;
    }

    /**
     * Trasforma in Stringa un qualunque array, di oggetti (Object[], String[], ...)
     * o di primitivi (int[])!
     * Se il parametro in entrata e' null restituisce null!
     * Se parametro in entrata on e' un Array restituisce arr.toString()
     * Il metodo e' ricorsivo, gestisce dunque anche gli array di array (Object[][],...)!
     * Gli elmenti dell'array che non sono al loro volta array vengono tramutati in stringa
     * tramite il metodo toString();
     */
    public static String arrayToString( Object arr ){
        if (arr==null) {
          return  null;
        }else if  ( !arr.getClass().isArray() ){
            return arr.toString();
        }else {
          String a="{";
          for ( int i=0; i<Array.getLength(arr); i++){
           try{
                Object result_0=Array.get(arr, i) ;
                if (result_0!=null && result_0.getClass().isArray() )
                    a+=arrayToString(result_0);
                else
                    a+=result_0;
            }catch(Exception e){
                a+="[#err# toString:"+e.getMessage()+"]";
                //LoggingService.getDefaultLogger().logWarning( DataTools.class, e);
            }

            a+=( (i<Array.getLength(arr)-1)?",":"" );
          }
          a+="}";
          return a;
        }

    }

    static public Object normalizeObjectsType(Object dummy, Class targetClass) throws Exception{
        if ( dummy==null ) return null;
        if ( targetClass==null ) return dummy;

        if ( dummy!=null ){
            // Se cMDClasses[i] e' null o e' uguale alla classe dell'oggetto da convertire
            // o ne e' una sua superclasse, non eseguo nessuna trasgformazione
            if (    targetClass != dummy.getClass() &&
                    !targetClass.isAssignableFrom( dummy.getClass() ) ){

                // Se la cMDClasses[i] e' la classe astratta Number, allora, creo un double
                if ( Number.class == targetClass ){ // && dummy[i] instanceof String ){
                    //it.imiweb.util.DataTools.
                    try {
                        dummy = new Double(dummy.toString());
                    }catch(Exception e){
                        throw e;
                    }

                }else if ( Integer.class == targetClass || Long.class == targetClass ){
                    try {
                        if ( Integer.class == targetClass )
                            dummy = new Integer(new Double(dummy.toString()).intValue());
                        else
                            dummy = new Long(new Double(dummy.toString()).longValue());
                    }catch(Exception e){
                        throw e;
                    }

                }else if ( String.class == targetClass  ){
                // Se la cMDClasses[i] e' la classe finale String, allora la conversione e' diretta
                    dummy = dummy.toString();
                }else  {
                    // se cMDClassesStringConstructor[i] non e' null lo usoper la conversione
                    try {
                        java.lang.reflect.Constructor classConst= targetClass.getConstructor(new Class[]{String.class});
                        dummy = classConst
                                        .newInstance(  new Object[]{ dummy.toString() } );
                    }catch(Exception ex){
                        throw ex;
                    }
                }
            }
        }

        return dummy;
    }



    public static void main (String[] aa){
        LoggingService.getDefaultLogger().logDebug(DataTools.class, arrayToString(
                        new int[][]{
                            new int[]{1,2,3},
                            new int[]{4,5,6},
                            new int[]{7,8,9}
                            }));

        LoggingService.getDefaultLogger().logDebug(DataTools.class, arrayToString(
                        new Object[]{"AAAA",
                                    new String[]{"aaa", "ccc"},
                                    "BBB"}));
    }
}