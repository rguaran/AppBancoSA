/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.ArrayList;

/**
 *
 * @author Rita
 */
public class Cuenta {
    int idCuenta;
    Double saldo;
    String fechacreacion;
    ArrayList<Prestamo> prestamos;
    ArrayList<Seguro> seguros;
    ArrayList transacciones;
    

    public Cuenta(int idCuenta, Double saldo, String fechacreacion, ArrayList<Prestamo> prestamos, ArrayList<Seguro> seguros, ArrayList transacciones) {
        this.idCuenta = idCuenta;
        this.saldo = saldo;
        this.fechacreacion = fechacreacion;
        this.prestamos = prestamos;
        this.seguros = seguros;
        this.transacciones = transacciones;
    }


    public ArrayList getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(ArrayList transacciones) {
        this.transacciones = transacciones;
    }

    public Cuenta() {
    }

       public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(String fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public ArrayList<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(ArrayList<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    public ArrayList<Seguro> getSeguros() {
        return seguros;
    }

    public void setSeguros(ArrayList<Seguro> seguros) {
        this.seguros = seguros;
    }
    
}
