/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package accedercondom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;//for Document
import org.w3c.dom.Document;
import java.util.*;
import java.io.*;//clase File
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class AccesoDOM {
    //Creamos la variable doc que proviene de Document
    Document doc;
    public int abrirXMLaDOM (File f){
        try{
            System.out.println("Abriendo archivo XML file y generando DOM....");

            //creamos nuevo objeto DocumentBuilder al que apunta la variable factory

            DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();

            //ignorar comentarios y espacios blancos
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            //DocumentBuilder tiene el método parse que es el que genera DOM en memoria

            DocumentBuilder builder=factory.newDocumentBuilder();
            //Se utiliza el parse para analizar el documento XML
            doc=builder.parse(f);
            System.out.println("DOM creado con éxito.");
            return 0;//si el método funciona
        }catch(Exception e){
            System.out.println(e);
            return -1;//En caso de no funcionar un salir de la ejecucion
        }
    }
    
    
    public void recorreDOMyMuestra() {
        String[] datos=new String[3];//lo usamos para almacenar la información de cada libro
        Node nodo=null;
        Node root=doc.getFirstChild();
        NodeList nodelist=root.getChildNodes(); //(1)Ver dibujo del árbol
        //recorrer el árbol DOM. El 1er nivel de nodos hijos del raíz
        for(int i=0;i<nodelist.getLength();i++){
            nodo=nodelist.item(i);//node toma el valor de los hijos de raíz
            if(nodo.getNodeType()==Node.ELEMENT_NODE){//miramos nodos de tipo Element
                Node ntemp=null;
                int contador=1;
                //sacamos el valor del atributo publicado
                datos[0]=nodo.getAttributes().item(0).getNodeValue();
                //sacamos los valores de los hijos de nodo, Titulo y Autor
                NodeList nl2=nodo.getChildNodes();//obtenemos lista de hijos (2)
                for(int j=0;j<nl2.getLength();j++){//iteramos en esa lista
                    ntemp=nl2.item(j);
                    if(ntemp.getNodeType()==Node.ELEMENT_NODE){

                    if(ntemp.getNodeType()==Node.ELEMENT_NODE){

                        //para conseguir el texto de titulo y autor, se
                        //puedo hacer con getNodeValue(), también con
                        //getTextContent() si es ELEMENT
                        datos[contador]=ntemp.getTextContent();
                    // también datos[contador]=ntemp.getChildNodes().item(0).getNodeValue();

                        contador++;

                    }
                    }
                //el array de String datos[] tiene los valores que necesitamos
            System.out.println(datos[0]+"--"+datos[2]+"--"+datos[1]);
                }//
            }
        }
    }
    public int insertarLibroEnDOM(String titulo, String autor, String fecha) {
        try {
            System.out.println("Añadir libro al árbol DOM:" + titulo + ";" + autor + ";" + fecha);
            //crea los nodos=>los añade al padre desde las hojas a la raíz
            //CREATE TITULO con el texto en medio
            Node ntitulo = doc.createElement("Titulo");//crea etiquetas <Titulo>...</Titulo>
            Node ntitulo_text = doc.createTextNode(titulo);//crea el nodo texto para el Titulo
            ntitulo.appendChild(ntitulo_text);//añade el titulo a las etiquetas=><Titulo>titulo</Titulo>
            //Node nautor=doc.createElement("Autor").appendChild(doc.createTextNode(autor));//one line doesn't work
            //CREA AUTOR
            Node nautor = doc.createElement("Autor");
            Node nautor_text = doc.createTextNode(autor);
            nautor.appendChild(nautor_text);
            //CREA LIBRO, con atributo y nodos Título y Autor 
            Node nLibro = doc.createElement("Libro");
            ((Element) nLibro).setAttribute("publicado", fecha);
            nLibro.appendChild(ntitulo);
            nLibro.appendChild(nautor);
            //APPEND LIBRO TO THE ROOT

            nLibro.appendChild(doc.createTextNode("\n"));//para insertar saltos de línea

            Node raiz = doc.getFirstChild();//tb. doc.getChildNodes().item(0)
            raiz.appendChild(nLibro);
            System.out.println("Libro insertado en DOM.");
            return 0;
            
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }

    public int deleteNode(String tit) {
        System.out.println("Buscando el Libro " + tit + " para borrarlo");
        try {
            Node raiz = doc.getDocumentElement(); //Para saber la raiz del XML
            NodeList nl1 = doc.getElementsByTagName("Titulo");//Almacenamos los nodos con el nombre indicado
            Node n1;//La utilizaremos para almacenar info. sobre el nodo en el que nos encontremos
            for (int i = 0; i < nl1.getLength(); i++) {
                n1 = nl1.item(i); //Sirve para recorrer los nodos a traves del bucle
                if (n1.getNodeType() == Node.ELEMENT_NODE) {//Para comprobar si el nodo es de tipo ELEMENT_NODE
                    if (n1.getChildNodes().item(0).getNodeValue().equals(tit)) {
                        System.out.println("Borrando el nodo <Libro> con título " + tit);
                        //borra el nodo de manera completa
                        n1.getParentNode().getParentNode().removeChild(n1.getParentNode());
                    }

                }
            }
            System.out.println("Nodo borrado");
            //Guardar el arbol DOM en un nuevo archivo para mantener nuestro archivo original
            //guardarDOMcomoArchivo("LibrosBorrados.xml");

            return 0;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return -1;
        }
    }
    
    void guardarDOM(String nuevoArchivo) {
        try {
            Source src = new DOMSource(doc); // Definimos el origen
            StreamResult rst = new StreamResult(new File(nuevoArchivo)); // Definimos el resultado
            // Declaramos el Transformer que tiene el método .transform() que necesitamos.
            //Lo utilizamos para transformar la fuentes de datos de un domcuento DOM o XML a en lo que deseemos
            //como un archivo, etc
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // Opción para indentar el archivo
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(src, (javax.xml.transform.Result) rst);
            System.out.println("Archivo creado del DOM con éxito\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
