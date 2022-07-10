/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexiondb.dao;

import conexiondb.pojos.Sensor;
import conexiondb.pojos.Temperatura;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import servidor.Cliente.ClienteEvento;
/**
 *
 * @author Marcelo
 */
public class SensorDAO {
    private static Session session =  null;
    
    public static void InsertarSensor(Sensor sensor, ClienteEvento clienteEvento) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(sensor);
            clienteEvento.setId_tr(sensor.getId());
            tx.commit();
        } catch (HibernateException hibernateException) {
            tx.rollback();
            System.out.println(hibernateException.getMessage());
        } finally {
            session.close();
        }
    }
    
    public static boolean ActualizarSensor(Sensor sensor) {
        if (!session.isOpen()) {
            session =  HibernateUtil.getSessionFactory().openSession();
        }
        Transaction tx = session.beginTransaction();
        try {
            System.out.println("FechaLectura = " + sensor.getFechalectura());
            session.update(sensor);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
            return false;
        } finally {
            session.close();
        }
    }
    
    public static int ActualizarSensorPorId(long id, Temperatura t) {
        if (!session.isOpen()) {
            session =  HibernateUtil.getSessionFactory().openSession();
        }
        session.beginTransaction();
        int rowCount = -1;
        try {
            String hql = "UPDATE Sensor se SET "
                    + " se.idsensor = :vidsensor , "
                    + " se.temperatura = :vtemperatura , "
                    + " se.humedad = :vhumedad , "
                    + " se.fechalectura = :vfechalectura "
                    + " WHERE se.id = :vid";
            Query query = session.createQuery(hql);
            query.setParameter("vidsensor", t.getIdmicrocontrolador());
            query.setParameter("vtemperatura", t.getTemperatura());
            query.setParameter("vhumedad", t.getHumedad());
            query.setParameter("vfechalectura", t.getFechahora());
            query.setParameter("vid", id);
            rowCount = query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        System.out.println("Filas afectadas: " + rowCount);
        return rowCount;
    }
    
    public static int ActualizarSensorPorIdsensor(Temperatura t) {
        if (!session.isOpen()) {
            session =  HibernateUtil.getSessionFactory().openSession();
        }
        session.beginTransaction();
        int rowCount = -1;
        try {
            String hql = "UPDATE Sensor se SET "
                    + " se.temperatura = :vtemperatura , "
                    + " se.humedad = :vhumedad , "
                    + " se.fechalectura = :vfechalectura , "
                    + " se.conectado = true "
                    + " WHERE se.idsensor = :vidsensor";
            Query query = session.createQuery(hql);
            query.setParameter("vtemperatura", t.getTemperatura());
            query.setParameter("vhumedad", t.getHumedad());
            query.setParameter("vfechalectura", t.getFechahora());
            query.setParameter("vidsensor", t.getIdmicrocontrolador());
            rowCount = query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        System.out.println("Filas afectadas: " + rowCount);
        return rowCount;
    }
    
    public static Sensor getSensorFromId(long id) {
        if (!session.isOpen()) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        session.beginTransaction();
        Sensor sensor = null;
        try {
            sensor = (Sensor) session.get(Sensor.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        return sensor;
    }
    
    public static int Depurar(long id) {
        if (!session.isOpen()) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        session.beginTransaction();
        int rowCount = -1;
        try {
            String hql = "delete from Sensor "
                    + " where id = :vid and "
                    + " idsensor = :vidsensor";
            Query query = session.createQuery(hql);
            query.setParameter("vid", id);
            query.setString("vidsensor", "nan");
            rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        return rowCount;
    }
    
    public static void Eliminar(Sensor sensor){
        if (!session.isOpen()) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        Transaction tx = session.beginTransaction();
        try {
            session.delete(sensor);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }
}
