package mx.desarrollo.delegate;


import mx.desarrollo.persistence.dao.ProductoDAO;

public class DelegateProducto {

    private ProductoDAO productoDAO;

    public DelegateProducto() {
        this.productoDAO = new ProductoDAO();
    }

    public void darDeBajaProducto(Long idProducto) {
        if (idProducto == null) {
            throw new IllegalArgumentException("El ID del producto no es válido.");
        }
        // Aquí puedes agregar reglas de negocio adicionales si lo necesitas
        productoDAO.darDeBaja(idProducto);
    }
}
