/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.ServidorSocket;

import java.util.List;
import servidor.Cliente.ClienteEvento;
import servidor.Escuchadores.ConexionEscuchador;
import servidor.Escuchadores.MensajeClienteEscuchador;

/**
 *
 * @author Marcelo
 */
public class Despachador {
    
    public static void DespacharEventoConexion(ClienteEvento clienteEvento, List<ConexionEscuchador> listaEscuchadores) {
        for (ConexionEscuchador escuchador : listaEscuchadores) {
            escuchador.onClienteConectado(clienteEvento);
        }
    }
    
    public static void DespacharEventoDesconexion(ClienteEvento clienteEvento, List<ConexionEscuchador> listaEscuchadores) {
        for (ConexionEscuchador escuchador : listaEscuchadores) {
            escuchador.onClienteDesconectado(clienteEvento);
        }
    }
    
    public static void DespacharEventoMensajeRecibido(ClienteEvento clienteEvento, String mensaje, List<MensajeClienteEscuchador> listaEscuchadores) {
        for (MensajeClienteEscuchador escuchador : listaEscuchadores) {
            escuchador.onMensajeRecivido(clienteEvento, mensaje);
        }
    }
    
}
