/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanzador;

/**
 *
 * @author Marcelo
 */
public class AppTrato {
    //PROTOCOLO DEL SENSOR TEMPERATURA
    public static final String IDMICRO = "ID=";
    public static final String TEMPERATURA = "Temp=";
    public static final String HUMEDAD = "Hum=";
    public static final String TIEMPO = "Tiempo=";
    
    public static abstract class VarUtil {
        private static StringBuilder valor_sb = null;
        public static StringBuilder getValorSB(){
            if (valor_sb==null) {
                valor_sb = new StringBuilder();
            }
            return valor_sb;
        }
    }
}
