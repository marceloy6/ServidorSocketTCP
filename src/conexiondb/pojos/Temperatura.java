package conexiondb.pojos;
// Generated Jun 10, 2022 7:35:30 AM by Hibernate Tools 4.3.1



/**
 * Temperatura generated by hbm2java
 */
public class Temperatura  implements java.io.Serializable {


     private Long id;
     private String idmicrocontrolador;
     private long fechahora;
     private float temperatura;
     private float humedad;

    public Temperatura() {
    }

    public Temperatura(String idmicrocontrolador, long fechahora, float temperatura, float humedad) {
       this.idmicrocontrolador = idmicrocontrolador;
       this.fechahora = fechahora;
       this.temperatura = temperatura;
       this.humedad = humedad;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public String getIdmicrocontrolador() {
        return this.idmicrocontrolador;
    }
    
    public void setIdmicrocontrolador(String idmicrocontrolador) {
        this.idmicrocontrolador = idmicrocontrolador;
    }
    public long getFechahora() {
        return this.fechahora;
    }
    
    public void setFechahora(long fechahora) {
        this.fechahora = fechahora;
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




}


