package gt.com.sandiego.bptmovil.app.Models;

/**
 * Created by lramirez on 04/09/2017.
 */

public class ProductoEncabezado {
    private String producto;
    private double cantidad;
    private double peso;
    private String bodega;

    public ProductoEncabezado(String producto, double cantidad, double peso, String bodega, String unidadMedida) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.peso = peso;
        this.bodega = bodega;
        this.unidadMedida = unidadMedida;
    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    private String unidadMedida;

    public ProductoEncabezado(String producto, double cantidad, double peso, String unidadMedida) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.peso = peso;
        this.unidadMedida = unidadMedida;
    }

    public ProductoEncabezado() {
    }


}
