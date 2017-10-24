package gt.com.sandiego.bptmovil.app.Models;

/**
 * Created by lramirez on 17/10/2017.
 */

public class Jumbo {
    public int correlativo;
    public int numeroRegistro;
    public int bodega ;
    public int estiba;
    public int plana;
    public double humedad;
    public int color;
    public int colorFecha;
    public long numerodeMovimiento;
    public int tipoDeMovimiento;
    public String columna;
    public int fila;

    public Jumbo() {
    }

    public Jumbo(int correlativo, int numeroRegistro, int bodega, int estiba, int plana, double humedad, int color, int colorFecha, long numerodeMovimiento, int tipoDeMovimiento, String columna, int fila) {
        this.correlativo = correlativo;
        this.numeroRegistro = numeroRegistro;
        this.bodega = bodega;
        this.estiba = estiba;
        this.plana = plana;
        this.humedad = humedad;
        this.color = color;
        this.colorFecha = colorFecha;
        this.numerodeMovimiento = numerodeMovimiento;
        this.tipoDeMovimiento = tipoDeMovimiento;
        this.columna = columna;
        this.fila = fila;
    }

    public int getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(int correlativo) {
        this.correlativo = correlativo;
    }

    public int getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(int numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public int getBodega() {
        return bodega;
    }

    public void setBodega(int bodega) {
        this.bodega = bodega;
    }

    public int getEstiba() {
        return estiba;
    }

    public void setEstiba(int estiba) {
        this.estiba = estiba;
    }

    public int getPlana() {
        return plana;
    }

    public void setPlana(int plana) {
        this.plana = plana;
    }

    public double getHumedad() {
        return humedad;
    }

    public void setHumedad(double humedad) {
        this.humedad = humedad;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColorFecha() {
        return colorFecha;
    }

    public void setColorFecha(int colorFecha) {
        this.colorFecha = colorFecha;
    }

    public long getNumerodeMovimiento() {
        return numerodeMovimiento;
    }

    public void setNumerodeMovimiento(long numerodeMovimiento) {
        this.numerodeMovimiento = numerodeMovimiento;
    }

    public int getTipoDeMovimiento() {
        return tipoDeMovimiento;
    }

    public void setTipoDeMovimiento(int tipoDeMovimiento) {
        this.tipoDeMovimiento = tipoDeMovimiento;
    }

    public String getColumna() {
        return columna;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }
}
