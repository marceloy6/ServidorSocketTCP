/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexiondb.dao;

import conexiondb.pojos.Temperatura;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Marcelo
 */
public class TemperaturaDAO {

    private static Session session = null;

    public static void ingresarTemperatura(Temperatura t) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(t);
            tx.commit();
        } catch (HibernateException hibernateException) {
            tx.rollback();
            System.out.println(hibernateException.getMessage());
        } finally {
            session.close();
        }
    }

}
