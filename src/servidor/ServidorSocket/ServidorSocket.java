/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.ServidorSocket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.Cliente.ClienteEvento;
import servidor.Escuchadores.ConexionEscuchador;
import servidor.Escuchadores.MensajeClienteEscuchador;
import servidor.Hilos.HiloConexiones;
import servidor.Hilos.HiloMensajesCliente;

/**
 *
 * @author Marcelo
 */
public class ServidorSocket implements ConexionEscuchador, MensajeClienteEscuchador{
    
    private int puerto;
    private ServerSocket servidor;
    private List<ClienteEvento> listaClientesConectados;
    private HiloConexiones hiloConexiones;
    private PrintWriter flujosalida;
    
    public ServidorSocket(){
        servidor = null;
    }
    
    public ServidorSocket(int puerto){
        try {
            this.puerto = puerto;
            this.servidor = new ServerSocket(puerto);
            listaClientesConectados = new ArrayList<>();
            hiloConexiones = new HiloConexiones(servidor);
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Iniciar() {
        System.out.println("Servidor Iniciado en puerto: " + puerto);
        hiloConexiones.AgregarEscuchadorConexiones(this);
        hiloConexiones.AgregarEscuchadorMensajes(this);
        hiloConexiones.start();
        
        System.out.println("Tamanho lista EscuchadorConexiones = " + hiloConexiones.getListaEscuchadorConexiones().size());
        System.out.println("Tamanho lista EscuchadorMensajes = " + hiloConexiones.getListaEscuchadorMensajes().size());
        System.out.println("Tamanho lista de clientes conectados = " + listaClientesConectados.size());
    }
    
    public void Detener() {
        if (!hiloConexiones.estaCorriendo()) {
            return;
        }
        //deteniendo los hilosMensajesCliente de cada Cliente Conectado
        for (ClienteEvento ce : listaClientesConectados) {
            System.out.println("Deteniendo HILOS de Lista de Escuchadores!");
            ce.getHiloDatos().Detener();
        }
        System.out.println("Deteniendo Hilo de Conexiones!!!");
        hiloConexiones.EliminarEscuchadorMensajes(this);
        hiloConexiones.EliminarEscuchadorConexiones(this);
        hiloConexiones.Detener();
        try {
            servidor.close();
            System.out.println("SERVIDOR CERRADO!");
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onClienteConectado(ClienteEvento clienteEvento) {
        System.out.println("OnClienteConectado*****");
        System.out.println(clienteEvento.getSocketCliente().getRemoteSocketAddress().toString());
        EnviarMensaje(clienteEvento);
        clienteEvento.setId(clienteEvento.hashCode());
        
        clienteEvento.setHiloDatos(new HiloMensajesCliente(clienteEvento, hiloConexiones.getListaEscuchadorConexiones(), hiloConexiones.getListaEscuchadorMensajes()));
            //clienteEvento.getHiloDatos().agregarEscuchadorMensajes(this);
//        hiloConexiones.AgregarEscuchadorMensajes(this);
        clienteEvento.getHiloDatos().start();
        listaClientesConectados.add(clienteEvento);
        
        System.out.println("Tamanho lista de clientes conectados = " + listaClientesConectados.size());
        System.out.println("Tamanho lista EscuchadorDeMensajes = " + hiloConexiones.getListaEscuchadorMensajes().size());
    }

    @Override
    public void onClienteDesconectado(ClienteEvento cliente) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("onClienteDesconectado*****");
        System.out.println("ID de cliente desconectado: " + cliente.getId());
        cliente.getHiloDatos().Detener();
            //cliente.getHiloDatos().eliminarEscuchadorMensajes(this);
        //hiloConexiones.EliminarEscuchadorMensajes(this);
        
        listaClientesConectados.remove(cliente);
        System.out.println("Borrado Cliente ID: " + cliente.getId());
        System.out.println("Tamanho lista de clientes conectados = " + listaClientesConectados.size());
    }

    
    @Override
    public void onMensajeRecivido(ClienteEvento clienteEvento, String mensaje) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("*****onMensajeRecibido " + this.getClass().getName());
        System.out.println("cliente " + clienteEvento.getId() + " dice: " + mensaje);
    }

    @Override
    public void onMensajeEnviado(Socket clienteSocket, String mensaje) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void EnviarMensajeACliente(int idcliente, String mensaje) {
        
    }
    
    public void EnviarMensaje(ClienteEvento clienteEvento) {
        try {
            flujosalida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clienteEvento.getSocketCliente().getOutputStream())), true);
            String mensajecliente = String.valueOf(new Date().getTime());
            System.out.println(mensajecliente);
            System.out.println(mensajecliente.length());
            flujosalida.println(mensajecliente);
        } catch (IOException ex) {
//            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR> " + ex.getMessage());
        } finally {
//            flujosalida.close();
        }
        
    }
    
    public void AgregarEscuchadorMensajes(MensajeClienteEscuchador mensajeClienteEscuchador) {
        hiloConexiones.AgregarEscuchadorMensajes(mensajeClienteEscuchador);
        System.out.println("Agregado Nuevo escuchador de mensajes!");
        System.out.println("Tamanho lista EscuchadorMensajes = " + hiloConexiones.getListaEscuchadorMensajes().size());
    }
    
    public void EliminarEscuchadorMensajes(MensajeClienteEscuchador mensajeClienteEscuchador) {
        hiloConexiones.EliminarEscuchadorMensajes(mensajeClienteEscuchador);
    }
    
    public void AgregarEscuchadorConexiones(ConexionEscuchador conexionEscuchador) {
        hiloConexiones.AgregarEscuchadorConexiones(conexionEscuchador);
        System.out.println("Agregado Nuevo escuchador de conexiones!");
        System.out.println("Tamanho lista EscuchadorConexiones = " + hiloConexiones.getListaEscuchadorConexiones().size());
    }
    
    public void EliminarEscuchadorConexiones(ConexionEscuchador conexionEscuchador) {
        hiloConexiones.EliminarEscuchadorConexiones(conexionEscuchador);
    }
    
}
