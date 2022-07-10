/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexiondb.dao;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import conexiondb.pojos.Regla;
import java.util.List;

/**
 *
 * @author Marcelo
 */
public class ReglaDAO {
    private static Session session = null;
    
    public static List<Regla> LeerReglas() {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Regla");
        List<Regla> lista = query.list();
        session.getTransaction().commit();
        session.close();
        return lista;
    }
    
}
