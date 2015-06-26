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
public class Administracion {
    
    public Administracion(){}
    
    public String getCadenaEtiquetas(String cadena, String etiqueta){
        int pos = cadena.indexOf(etiqueta);
        int lon = etiqueta.length();
        String cierre = "</"+etiqueta.substring(1, etiqueta.length());
        int fin = cadena.indexOf(cierre);
        return cadena.substring(pos + lon, fin);
    }
    
    public int finEtiqueta(String cadena, String etiqueta){
        int fin =0;
        String cierre = "</"+etiqueta.substring(1, etiqueta.length());
        int pos = cadena.indexOf(cierre);
        fin = pos + cierre.length();
        return fin;
    }
    
    public ArrayList<String> getLista(String cadena, String etiqueta){
        int tamanio = Integer.parseInt(getCadenaEtiquetas(cadena,"<tamanio>"));
        ArrayList<String> list = new ArrayList<String>();
        String sub = cadena.substring(finEtiqueta(cadena,"<tamanio>"), cadena.length());
        String id;
        for( int i= 1; i<= tamanio; i++ ){
            id = getCadenaEtiquetas(sub, etiqueta);
            list.add(id);
            sub = sub.substring(finEtiqueta(sub, etiqueta), sub.length());
        }
        return list;
    }
    
}
