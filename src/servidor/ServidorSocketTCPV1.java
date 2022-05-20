/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.Escuchadores.MensajeClienteEscuchador;
import servidor.ServidorSocket.ServidorSocket;

/**
 *
 * @author Marcelo
 */
public class ServidorSocketTCPV1 {
    
    private static ServidorSocket servidorSocket;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //via propiedades
        try {
            String ruta = System.getProperty("user.dir") + "\\src\\servidor\\Propiedades\\";
            FileInputStream fileInputStream = new FileInputStream(ruta + "Config.properties");
            Properties properties = new Properties();
            properties.load(fileInputStream);
            int puerto = Integer.parseInt(properties.getProperty("puerto"));
            servidorSocket = new ServidorSocket(puerto);
            servidorSocket.Iniciar();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServidorSocketTCPV1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocketTCPV1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //via argumento
//        if (args.length>0) {
//            int puerto = Integer.parseInt(args[0]);
//            ServidorSocket servidorSocket = new ServidorSocket(puerto);
//            servidorSocket.Iniciar();
//        } else {
//            System.out.println("Puerto no ingresado!");
//            System.exit(0);
//        }
    }
    
    public static void AgregarEscuchadorMensajes(MensajeClienteEscuchador mensajeClienteEscuchador) {
        servidorSocket.AgregarEscuchadorMensajes(mensajeClienteEscuchador);
    }
    
}
