/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.Hilos;

import java.io.IOException;
import java.net.ServerSocket;
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
public class HiloConexiones extends Thread{
    
    private ServerSocket servidor;
    private List<ConexionEscuchador> listaEscuchadorConexiones;
    private List<MensajeClienteEscuchador> listaEscuchadorMensajes;
    private boolean sw;
    
    public HiloConexiones(ServerSocket serverSocket){
        sw = true;
        servidor = serverSocket;
        listaEscuchadorConexiones = new ArrayList<>();
        listaEscuchadorMensajes = new ArrayList<>();
    }
    
    public void AgregarEscuchadorConexiones(ConexionEscuchador conexionEscuchador){
        listaEscuchadorConexiones.add(conexionEscuchador);
    }
    
    public void EliminarEscuchadorConexiones(ConexionEscuchador conexionEscuchador){
        listaEscuchadorConexiones.remove(conexionEscuchador);
    }
    
    public void AgregarEscuchadorMensajes(MensajeClienteEscuchador mensajeClienteEscuchador) {
        listaEscuchadorMensajes.add(mensajeClienteEscuchador);
    }
    
    public void EliminarEscuchadorMensajes(MensajeClienteEscuchador mensajeClienteEscuchador) {
        listaEscuchadorMensajes.remove(mensajeClienteEscuchador);
    }

    @Override
    public void run() {
        //super.run(); //To change body of generated methods, choose Tools | Templates.
        while (sw) {
            try {
                System.out.println("Esperando conexiones...!");
                Socket clienteSocket = servidor.accept();
                System.out.println("Nuevo Cliente Conectado!");
                ClienteEvento clienteEvento = new ClienteEvento(clienteSocket);

                //DespachadorEventoConexion(clienteEvento);
                Despachador.DespacharEventoConexion(clienteEvento, listaEscuchadorConexiones);
                System.out.println("Notificado nuevo cliente conectado!");
                
            } catch (IOException ex) {
                //Logger.getLogger(HiloConexiones.class.getName()).log(Level.SEVERE, null, ex);
                if (servidor.isClosed()) {
                    System.out.println("El Servidor fue Cerrado! " + ex.getMessage());
                    //accion ante servidor fue cerrado
                } else {
                    System.out.println(ex.getMessage());  
                    //accion ante desconexion del ServerSocket
                }
                break;
            }
        }
        Logger.getLogger(this.getName()).info("FIN RUN HILO_CONEXIONES");
    }
    
    public boolean estaCorriendo() {
        return sw;
    }
    
    public void Detener(){
        sw = false;
    }

    public List<ConexionEscuchador> getListaEscuchadorConexiones() {
        return listaEscuchadorConexiones;
    }

    public List<MensajeClienteEscuchador> getListaEscuchadorMensajes() {
        return listaEscuchadorMensajes;
    }
    
}
