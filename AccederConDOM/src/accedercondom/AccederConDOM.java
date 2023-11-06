/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package accedercondom;

import java.io.File;

/**
 *
 * @author maget
 */
public class AccederConDOM {

    
    public static void main(String[] args) {
        AccesoDOM acces=new AccesoDOM();
        File f=new File("Libros.xml");
        
        acces.abrirXMLaDOM(f);
        
        acces.recorreDOMyMuestra();
        
        acces.insertarLibroEnDOM("Cochee", "Empresa", "2022");
        
        acces.deleteNode("");
        
        acces.guardarDOM("LibrosDeDOM.xml");
    }
    
}
