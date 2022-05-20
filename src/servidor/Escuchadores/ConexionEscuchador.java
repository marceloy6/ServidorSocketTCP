/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.Escuchadores;

import servidor.Cliente.ClienteEvento;

/**
 *
 * @author Marcelo
 */
public interface ConexionEscuchador {
    
    public void onClienteConectado(ClienteEvento cliente);
    
    public void onClienteDesconectado(ClienteEvento cliente);
    
}
