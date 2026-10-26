import mx.desarrollo.entity.Administrador;
import mx.desarrollo.persistence.dao.AdministradorDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

public class testDAO {

    public static void main(String[] args) {
        AdministradorDAO administradorDAO = new AdministradorDAO(HibernateUtil.getEntityManager());



        for (Administrador administrador : administradorDAO.findAll()) {
            System.out.println(administrador + "|| id [" + administrador.getId()+ "]");
        }
    }
}
