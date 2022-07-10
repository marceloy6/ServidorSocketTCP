/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanzador;

import conexiondb.pojos.Temperatura;

/**
 *
 * @author Marcelo
 */
public class LectorUtil {
    
    public static void LeerObjetoTemperatura(String mensaje, Temperatura temperatura) {
        if (EsValido(mensaje)) {
            try {
                StringBuilder valor = AppTrato.VarUtil.getValorSB();
                
                LeerValor(mensaje, mensaje.lastIndexOf(AppTrato.IDMICRO) + AppTrato.IDMICRO.length(), valor);
                temperatura.setIdmicrocontrolador(valor.toString());
                
                LeerValor(mensaje, mensaje.lastIndexOf(AppTrato.TEMPERATURA) + AppTrato.TEMPERATURA.length(), valor);
                temperatura.setTemperatura(Float.parseFloat(valor.toString()));
                
                LeerValor(mensaje, mensaje.lastIndexOf(AppTrato.HUMEDAD) + AppTrato.HUMEDAD.length(), valor);
                temperatura.setHumedad(Float.parseFloat(valor.toString()));
                
                LeerValor(mensaje, mensaje.lastIndexOf(AppTrato.TIEMPO) + AppTrato.TIEMPO.length(), valor);
                temperatura.setFechahora(Long.parseLong(valor.toString()));
            } catch (NumberFormatException numberFormatException) {
                System.err.println("ERROR al parsear los numeros. " + numberFormatException.getMessage());
                temperatura.setId(-1L);
            } 
        } else {
            System.err.println("Mensaje no es compatible con tipo TEMPERATURA.");
            temperatura.setId(-1L);
        }
    }
    
    private static boolean EsValido(String mensaje){
        return (mensaje!=null && !mensaje.trim().isEmpty() && 
                mensaje.contains(AppTrato.IDMICRO) && 
                mensaje.contains(AppTrato.TEMPERATURA) && 
                mensaje.contains(AppTrato.HUMEDAD) && 
                mensaje.contains(AppTrato.TIEMPO));
    }
    
    private static void LeerValor(String mensaje, int desde, StringBuilder ref_valor){
        int i = desde;
        ref_valor.setLength(0);
        while(i<mensaje.length() && mensaje.charAt(i)!='|'){
            i++;
        }
        ref_valor.append(mensaje.substring(desde, i));
    }
}
