/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.Cliente;

import java.net.Socket;
import servidor.Hilos.HiloMensajesCliente;

/**
 *
 * @author Marcelo
 */
public class ClienteEvento {
    
    private int id;
    private long id_tr;
    private HiloMensajesCliente hiloDatos;
    private Socket socketCliente;
    
    public ClienteEvento(Socket socketCliente) {
        this.socketCliente = socketCliente;
    }

    public ClienteEvento(Socket conexion, HiloMensajesCliente hiloMensajesCliente) {
        this.socketCliente = conexion;
        this.hiloDatos = hiloMensajesCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getSocketCliente() {
        return socketCliente;
    }

    public void setSocketCliente(Socket socketCliente) {
        this.socketCliente = socketCliente;
    }

    public HiloMensajesCliente getHiloDatos() {
        return hiloDatos;
    }

    public void setHiloDatos(HiloMensajesCliente hiloDatos) {
        this.hiloDatos = hiloDatos;
    }

    public long getId_tr() {
        return id_tr;
    }

    public void setId_tr(long id_tr) {
        this.id_tr = id_tr;
    }
    
}
