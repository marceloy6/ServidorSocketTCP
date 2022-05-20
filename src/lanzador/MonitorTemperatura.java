/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanzador;

import conexiondb.dao.ReglaDAO;
import conexiondb.dao.TemperaturaDAO;
import conexiondb.pojos.Regla;
import conexiondb.pojos.Temperatura;
import java.net.Socket;
import java.util.List;
import servidor.Cliente.ClienteEvento;
import servidor.Escuchadores.MensajeClienteEscuchador;
import servidor.ServidorSocketTCPV1;

/**
 *
 * @author Marcelo
 */
public class MonitorTemperatura implements MensajeClienteEscuchador{
    
    static List<Regla> lista = ReglaDAO.LeerReglas();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServidorSocketTCPV1.main(args);
        ServidorSocketTCPV1.AgregarEscuchadorMensajes(new MonitorTemperatura());
//        MonitorTemperatura m = new MonitorTemperatura();
//        m.Iniciar(args);
//        m.PonerAEscuchar();
        System.out.println("Monitor TEMPERATURA");
    }
    
    public void Iniciar(String[] args) {
        ServidorSocketTCPV1.main(args);
    }
    
    public void PonerAEscuchar() {
        ServidorSocketTCPV1.AgregarEscuchadorMensajes(this);
    }

    @Override
    public void onMensajeRecivido(ClienteEvento clienteEvento, String mensaje) {
        System.err.println("MONITOR TEMPERATURA ESCUCHO EL MENSAJE");
        if (mensaje!=null || !mensaje.trim().isEmpty()) {
            //Leer los valores y parsearlos
            Temperatura temperatura = LeerObjetoTemperatura(mensaje);
            
            //mostrar los valores llagados
            System.err.println(mensaje);

            //insertar los valores en la base de datos;
            TemperaturaDAO.ingresarTemperatura(temperatura);
            
            //comparar con las reglas de la BD para enviar un correo electronico
            String[] notificacion = ObtenerMensajeNotificacion(lista, temperatura.getTemperatura());
            System.err.println("ENVIANDO UN CORREO a: " + notificacion[0]);
            System.err.println("CON MENSAJE: " + notificacion[1]);
        }
    }

    @Override
    public void onMensajeEnviado(Socket clienteSocket, String mensaje) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    String[] ObtenerMensajeNotificacion(List<Regla> listaReglas, float t) {
        String[] res = new String[2];
        for (Regla regla : listaReglas) {
            if(t>=regla.getRangoinicial() && t<=regla.getRangofinal()){
                res[0] = regla.getDestinatario();
                res[1] = regla.getMensaje();
            }
        }
        return res;
    }
    
    
    public Temperatura LeerObjetoTemperatura(String mensaje) {
        String temperatura = "temperatura=";
        String humedad = "humedad=";
        String mac = "mac=";
        String tiempo = "tiempo=";
        
        int posVT = mensaje.lastIndexOf(temperatura) + temperatura.length();
        String vt = mensaje.substring(posVT, posVT+5);
        
        int posVH = mensaje.lastIndexOf(humedad) + humedad.length();
        String vh = mensaje.substring(posVH, posVH+5);
        
        int posVMAC = mensaje.lastIndexOf(mac) + mac.length();
        String vmac = mensaje.substring(posVMAC, posVMAC+17);
        
        int posVTiempo = mensaje.lastIndexOf(tiempo) + tiempo.length();
        String vtiempo = mensaje.substring(posVTiempo, mensaje.length());

        Temperatura tobjeto = new Temperatura();
        tobjeto.setTemperatura(Float.parseFloat(vt));
        tobjeto.setHumedad(Float.parseFloat(vh));
        tobjeto.setIdmicrocontrolador(vmac);
        tobjeto.setFechahora(Long.parseLong(vtiempo));
        return tobjeto;
    }
}
