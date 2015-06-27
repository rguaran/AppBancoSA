/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

/**
 *
 * @author Rita
 */
public class Transaccion {
    int idTransaccion;
    double monto;
    int idSalida;
    int idDestino;
    //String banco;
    int seguro;
    int prestamo;
    String tipo;
    String fecha;

    public Transaccion() {
    }

    public Transaccion(int idTransaccion, double monto, int idSalida, int idDestino, int seguro, int prestamo, String tipo, String fecha) {
        this.idTransaccion = idTransaccion;
        this.monto = monto;
        this.idSalida = idSalida;
        this.idDestino = idDestino;
        this.seguro = seguro;
        this.prestamo = prestamo;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(int idSalida) {
        this.idSalida = idSalida;
    }

    public int getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(int idDestino) {
        this.idDestino = idDestino;
    }

    public int getSeguro() {
        return seguro;
    }

    public void setSeguro(int seguro) {
        this.seguro = seguro;
    }

    public int getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(int prestamo) {
        this.prestamo = prestamo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    

    
    
    
}
