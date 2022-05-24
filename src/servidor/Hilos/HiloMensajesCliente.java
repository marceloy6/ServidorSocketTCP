/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.Hilos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.Cliente.ClienteEvento;
import servidor.Escuchadores.ConexionEscuchador;
import servidor.Escuchadores.MensajeClienteEscuchador;
import servidor.ServidorSocket.Despachador;

/**
 *
 * @author Marcelo
 */
public class HiloMensajesCliente extends Thread{
    
    private BufferedReader flujoentradacliente;
    private boolean sw;
    private List<MensajeClienteEscuchador> listaEscuchadorMensajes;
    private List<ConexionEscuchador> listaEscuchadorConexiones;
    private ClienteEvento clienteEvento;
    
    public HiloMensajesCliente(ClienteEvento clienteEvento, List<ConexionEscuchador> listaEscuchadorConexiones, List<MensajeClienteEscuchador> listaEscuchadorMensajes) {
        try {
            this.clienteEvento = clienteEvento;
            this.sw = true;
                //this.listaEscuchadorMensajes = new ArrayList<>();
            this.listaEscuchadorMensajes = listaEscuchadorMensajes;
            this.listaEscuchadorConexiones = listaEscuchadorConexiones;
            this.flujoentradacliente = new BufferedReader(new InputStreamReader(clienteEvento.getSocketCliente().getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(HiloMensajesCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        //super.run(); //To change body of generated methods, choose Tools | Templates.
        while (sw) {            
            try {
                System.out.println("Listo para recibir mensajes del Cliente");
                String mensajecliente = flujoentradacliente.readLine();
                
                //DespachadorEventoMensajeRecibido(socketCliente, mensajecliente);
                Despachador.DespacharEventoMensajeRecibido(clienteEvento, mensajecliente, listaEscuchadorMensajes);
            } catch (IOException ex) {
                //Logger.getLogger(HiloMensajesCliente.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Socket Cliente desconectado. ERROR: " + ex.getMessage());
                sw = false;
            }
        }
        Logger.getLogger(this.getName()).info("FIN RUN HILO_ESCUCHADOR_MENSAJES");
        Despachador.DespacharEventoDesconexion(clienteEvento, listaEscuchadorConexiones);
        try {
            flujoentradacliente.close();
        } catch (IOException ex) {
            Logger.getLogger(HiloMensajesCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Detener() {
        try {
            clienteEvento.getSocketCliente().close();
            sw = false;
        } catch (IOException ex) {
//            Logger.getLogger(HiloMensajesCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
