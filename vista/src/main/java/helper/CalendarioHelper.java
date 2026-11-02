package helper;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import mx.desarrollo.persistence.persistence.HibernateUtil;

@WebListener
public class CalendarioHelper implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        try {
            HibernateUtil.getEntityManagerFactory();
        } catch (Throwable e) {
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        HibernateUtil.close();
    }
}
