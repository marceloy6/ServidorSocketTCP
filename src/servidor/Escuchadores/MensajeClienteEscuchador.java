/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.Escuchadores;

import java.net.Socket;
import servidor.Cliente.ClienteEvento;

/**
 *
 * @author Marcelo
 */
public interface MensajeClienteEscuchador {
    
    public void onMensajeRecivido(ClienteEvento clienteEvento, String mensaje);
    
    public void onMensajeEnviado(Socket clienteSocket, String mensaje);
            
}
