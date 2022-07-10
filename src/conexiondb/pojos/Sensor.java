package conexiondb.pojos;
// Generated Jun 10, 2022 7:35:30 AM by Hibernate Tools 4.3.1



/**
 * Sensor generated by hbm2java
 */
public class Sensor  implements java.io.Serializable {


     private Long id;
     private String idsensor;
     private long fechaconexion;
     private float temperatura;
     private float humedad;
     private long fechalectura;
     private boolean conectado;

    public Sensor() {
    }

    public Sensor(String idsensor, long fechaconexion, float temperatura, float humedad, long fechalectura, boolean conectado) {
       this.idsensor = idsensor;
       this.fechaconexion = fechaconexion;
       this.temperatura = temperatura;
       this.humedad = humedad;
       this.fechalectura = fechalectura;
       this.conectado = conectado;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public String getIdsensor() {
        return this.idsensor;
    }
    
    public void setIdsensor(String idsensor) {
        this.idsensor = idsensor;
    }
    public long getFechaconexion() {
        return this.fechaconexion;
    }
    
    public void setFechaconexion(long fechaconexion) {
        this.fechaconexion = fechaconexion;
    }
    public float getTemperatura() {
        return this.temperatura;
    }
    
    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }
    public float getHumedad() {
        return this.humedad;
    }
    
    public void setHumedad(float humedad) {
        this.humedad = humedad;
    }
    public long getFechalectura() {
        return this.fechalectura;
    }
    
    public void setFechalectura(long fechalectura) {
        this.fechalectura = fechalectura;
    }
    public boolean isConectado() {
        return this.conectado;
    }
    
    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }




}

