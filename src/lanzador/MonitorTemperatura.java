/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanzador;

import conexiondb.dao.ReglaDAO;
import conexiondb.dao.SensorDAO;
import conexiondb.dao.TemperaturaDAO;
import conexiondb.pojos.Regla;
import conexiondb.pojos.Sensor;
import conexiondb.pojos.Temperatura;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import servidor.Cliente.ClienteEvento;
import servidor.Escuchadores.ConexionEscuchador;
import servidor.Escuchadores.MensajeClienteEscuchador;
import servidor.ServidorSocketTCPV1;

/**
 *
 * @author Marcelo
 */
public class MonitorTemperatura implements MensajeClienteEscuchador, ConexionEscuchador{
    
    static List<Regla> lista = ReglaDAO.LeerReglas();
    static Regla reglacritica;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MonitorTemperatura m = new MonitorTemperatura();
        m.Iniciar(args);
        reglacritica = m.DefinirReglaCritica(4, lista);
        System.out.println("Monitor TEMPERATURA");
    }
    
    public void Iniciar(String[] args) {
        ServidorSocketTCPV1.main(args);
        ServidorSocketTCPV1.AgregarEscuchadorMensajes(this);
        ServidorSocketTCPV1.AgregarEscuchadorConexiones(this);
    }

    @Override
    public void onMensajeRecivido(ClienteEvento clienteEvento, String mensaje) {
        System.err.println("MONITOR TEMPERATURA ESCUCHO EL MENSAJE");
        //leer los valores del MENSAJE
        Temperatura temperatura = LeerObjetoTemperatura(mensaje);

        //mostrar los valores llegados
        System.err.println(mensaje);

        //insertar los valores en la base de datos;
        if (temperatura != null) {
            // Actualizamos los datos del sensor 
            System.out.println("ACTUALIZANDO CLIENTE ID= " + clienteEvento.getId_tr());
            if (SensorDAO.ActualizarSensorPorIdsensor(temperatura)>0) {
                SensorDAO.Depurar(clienteEvento.getId_tr());
            } else {
                SensorDAO.ActualizarSensorPorId(clienteEvento.getId_tr(), temperatura);
            }

            TemperaturaDAO.ingresarTemperatura(temperatura);

            //obtener  la notificacion y correo para cada Temperatura recibida por el ESP8266
            String[] notificacion = ObtenerMensajeNotificacion(lista, temperatura.getTemperatura());
            System.out.println("NOTIFICACION PARA ESTA TEMPERATURA ES: " + notificacion[1] + " PARA: " + notificacion[0]);
            
            //Compara si Temperatura esta en una Regla Critica para enviar un correo electronico
            NotificarSiCumpleRegla(temperatura.getTemperatura(), reglacritica);            
        } else {
            System.err.println("El Mensaje no es Tipo Temperatura!");
        }
    }

    @Override
    public void onMensajeEnviado(Socket clienteSocket, String mensaje) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String[] ObtenerMensajeNotificacion(List<Regla> listaReglas, float t) {
        System.out.println("OBTENIENDO MENSAJE DE NOTIFICACION!");
        String[] res = new String[2];
        for (Regla regla : listaReglas) {
            //RangoFinal seria el tope, al que no puede llegar; porque entonces formaria parte del siguiente rango
            if(t>=regla.getRangoinicial() && t<regla.getRangofinal()){
                res[0] = regla.getDestinatario();
                res[1] = regla.getMensaje();
            }
        }
        return res;
    }
    
    public Regla DefinirReglaCritica(int idregla, List<Regla> listaReglas) {
        Regla reglaf = null;
        for (int i = 0; i < listaReglas.size(); i++) {
            if (listaReglas.get(i).getId()==idregla) {
                reglaf = listaReglas.get(i);
            }
        }
        return reglaf;
    }
    
    public void NotificarSiCumpleRegla(float t, Regla regla) {
        if (regla!=null) {
            if (t >= regla.getRangoinicial() && t < regla.getRangofinal()) {
                System.out.println("REGLA CRITICA!!!");
                System.out.println("Enviando correo a: " + regla.getDestinatario());
                System.out.println("con mensaje: " + regla.getMensaje());
            } else {
                System.out.println("No es regla critica. No se envio ningun correo");
            }
        }
    }
    
    
    public Temperatura LeerObjetoTemperatura(String mensaje) {
        if (mensaje != null && !mensaje.trim().isEmpty()) {
            Temperatura tobjeto;
            try {
                String mac = "ID=";
                String temperatura = "Temp=";
                String humedad = "Hum=";
                String tiempo = "Tiempo=";
                
                int posVMAC = mensaje.lastIndexOf(mac) + mac.length();
                String vmac = mensaje.substring(posVMAC, posVMAC + 17);

                int posVT = mensaje.lastIndexOf(temperatura) + temperatura.length();
                String vt = mensaje.substring(posVT, posVT + 5);

                int posVH = mensaje.lastIndexOf(humedad) + humedad.length();
                String vh = mensaje.substring(posVH, posVH + 5);

                int posVTiempo = mensaje.lastIndexOf(tiempo) + tiempo.length();
                String vtiempo = mensaje.substring(posVTiempo, mensaje.length());

                tobjeto = new Temperatura();
                tobjeto.setTemperatura(Float.parseFloat(vt));
                tobjeto.setHumedad(Float.parseFloat(vh));
                tobjeto.setIdmicrocontrolador(vmac);
                tobjeto.setFechahora(Long.parseLong(vtiempo));
            } catch (NumberFormatException numberFormatException) {
                System.err.println("ERROR al parsear los numeros. " + numberFormatException.getMessage());
                return null;
            } catch (Exception e) {
                System.err.println("String no compatible. " + e.getMessage());
                return null;
            }
            return tobjeto;
        } else
            return null;
    }
    
//    public Sensor ObtenerDatosSensor(String mensaje){
//        if (mensaje != null && !mensaje.trim().isEmpty()) {
//            Sensor tobjeto;
//            try {
//                String mac = "ID=";
//                String temperatura = "Temp=";
//                String humedad = "Hum=";
//                String tiempo = "Tiempo=";
//                
//                int posVMAC = mensaje.lastIndexOf(mac) + mac.length();
//                String vmac = mensaje.substring(posVMAC, posVMAC + 17);
//
//                int posVT = mensaje.lastIndexOf(temperatura) + temperatura.length();
//                String vt = mensaje.substring(posVT, posVT + 5);
//
//                int posVH = mensaje.lastIndexOf(humedad) + humedad.length();
//                String vh = mensaje.substring(posVH, posVH + 5);
//
//                int posVTiempo = mensaje.lastIndexOf(tiempo) + tiempo.length();
//                String vtiempo = mensaje.substring(posVTiempo, mensaje.length());
//
//                tobjeto = new Sensor();
//                tobjeto.setIdsensor(vmac);
//                tobjeto.setTemperatura(Float.parseFloat(vt));
//                tobjeto.setHumedad(Float.parseFloat(vh));
//                tobjeto.setFechalectura(Long.parseLong(vtiempo));
//            } catch (NumberFormatException numberFormatException) {
//                System.err.println("ERROR al parsear los numeros. " + numberFormatException.getMessage());
//                return null;
//            } catch (Exception e) {
//                System.err.println("String no compatible. " + e.getMessage());
//                return null;
//            }
//            return tobjeto;
//        } else
//            return null;
//    }
    
//    private String leerValor(String cadena, int desde) {
//        int i = desde;
//        String res = ""; res.s
//        //ID=3232|Tem=32.2
//        while(i<cadena.length() && i) {
//            i++;
//        }
//        return res;
//    }

    @Override
    public void onClienteConectado(ClienteEvento cliente) {
        //Agregar Sensor a la DB
        //pero cliente evento no tiene ningun valor del tipo Sensor
        Sensor sensor =  new Sensor();
        System.out.println("Insertando en BD a sensor " + Long.valueOf(cliente.hashCode()));
        
        sensor.setIdsensor("nan");
        sensor.setFechaconexion(new Date().getTime());
        sensor.setTemperatura(0);
        sensor.setHumedad(0);
        sensor.setFechalectura(0);
        sensor.setConectado(true);
        SensorDAO.InsertarSensor(sensor, cliente);
    }

    @Override
    public void onClienteDesconectado(ClienteEvento cliente) {
        //Actulizar estado de sensor en DB
        Sensor sensor = SensorDAO.getSensorFromId(cliente.getId_tr());
        if (sensor!=null) {
            System.out.println(this.getClass().getName() + " " + sensor.getIdsensor());
            if (sensor.getIdsensor().equals("nan")) {
                SensorDAO.Eliminar(sensor);
            } else {
                sensor.setConectado(false);
                SensorDAO.ActualizarSensor(sensor);
            }
        } else
            System.out.println("No se pudo Actualizar al Sensor");
    }
    
    
}
