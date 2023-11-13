package Modelo

import Controlador.ClassControlador
import Vista.Vista
import java.sql.Connection
import java.sql.DriverManager
import jakarta.xml.bind.*
import java.io.File
import java.io.PrintWriter
import java.time.LocalDateTime


/*
CREATE TABLE EMPLEADOS(
DNI VARCHAR2(10) PRIMARY KEY,
NOMBRE VARCHAR2(40),
FECHA_NAC VARCHAR2(40));
 */

// INSERT INTO EMPLEADOS (DNI,NOMBRE,FECHA_NAC) VALUES ('0000A','Álvaro Castilla','13/01/2003');
//cd C:\Users\AlvaroPC\IdeaProjects\SQL\PersistenciaConSQL\FacturaciónSql\ModeloVistaControlador1tabla\out\artifacts\ModeloVistaControlador1tabla_jar
//java -jar ModeloVistaControlador1tabla.jar

val jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe"
var connection: Connection = DriverManager.getConnection(jdbcUrl,"ADA","ADA")
lateinit var listaDeEmpleados : MutableList<Empleados>
var printWriterGestor : PrintWriter = PrintWriter(File("C:\\Users\\AlvaroPC\\IdeaProjects\\SQL\\PersistenciaConSQL\\FacturaciónSql\\ModeloVistaControlador1tabla\\logs\\gestorlog.txt"))
lateinit var date : LocalDateTime


/**
 * @author Álvaro Castilla
 * Clase que se encarga de gestionar la BDD
 */
class GestorBDD {
    /**
     * Init para cargar en una lista los empleados de la BDD
     */
    init {
        Class.forName("oracle.jdbc.driver.OracleDriver")
        val resultTabla = connection.createStatement().executeQuery("select * from EMPLEADOS order by DNI asc")
        listaDeEmpleados = mutableListOf<Empleados>()


        while (resultTabla.next()) {
            val DNI: String = resultTabla.getString("DNI")
            val nombre: String = resultTabla.getString("NOMBRE")
            val fechanac: String = resultTabla.getString("FECHA_NAC")

            var objetoActual = Empleados(DNI,nombre,fechanac)
            listaDeEmpleados.add(objetoActual)
        }
        date = LocalDateTime.now()
    }

    /**
     * Función para imprimir empleados en pantalla
     */
    fun recuperarEmpleados() {
        ClassControlador().comunicarEmpleadosVista(listaDeEmpleados)
        //
         escribirLog("Empleados recuperados")

    }

    /**
     * Función para agregar empleado
     * @param: DNI -> dni del empleado que se desea agregar
     * @param: nombreempleado -> nombre del empleado que se desea agregar
     * @param: fecha_nac -> fecha de nacimiento del empleado que desea agregar
     */
    fun agregarEmpleado(DNI: String,nombreempleado:String,fechanac:String) {
        try {
            connection.prepareStatement("INSERT INTO EMPLEADOS (DNI,NOMBRE,FECHA_NAC) VALUES ('$DNI','$nombreempleado','$fechanac')").execute()
            Vista().mensajesAMostrar(7)
            //
            escribirLog("Empleado agregado: DNI:$DNI, Nombre:$nombreempleado, Fecha de nacimiento:$fechanac")

        }
        catch (e: Exception) {
            Vista().mensajesAMostrar(1)
        }
    }
    /**
     * Función para modificar empleado
     * @param: DNI -> dni del empleado que se desea modificar
     * @param: nuevonombre -> nuevo nombre del empleado a modificar
     * @param: fecha_nac -> nueva fecha de nacimiento a modificar
     */
    fun modificarEmpleado(DNI: String,nuevonombre:String,nuevafecha:String) {
        try {
            connection.createStatement().executeQuery("UPDATE EMPLEADOS SET NOMBRE = '$nuevonombre', FECHA_NAC = '$nuevafecha' WHERE DNI = '$DNI'")
            Vista().mensajesAMostrar(8)

            //
            escribirLog("Empleado modificado: DNI:$DNI, Nuevo nombre:$nuevonombre, Nueva fecha de nacimiento:$nuevafecha")
        }
        catch (e: Exception) {
            Vista().mensajesAMostrar(2)
        }
    }
    /**
     * Función para eliminar empleado
     * @param: DNI -> dni del empleado que se desea eliminar
     */
    fun eliminarEmpleado(DNI:String) {
        try {
            val sql =  "DELETE FROM EMPLEADOS WHERE DNI = '$DNI'"
            var statement = connection.prepareStatement(sql)
            var lineasEliminadas = statement.executeUpdate()
            if (lineasEliminadas>0) {
                Vista().mensajesAMostrar(3)
                escribirLog("Empleado eliminado -> DNI: $DNI")
            }
            else {
                Vista().mensajesAMostrar(4)
                escribirLog("Intento de eliminación de empleado -> ERR: No se ha encontrado al empleado. No se ha podido eliminar.")
            }
        }
        catch (e: Exception) {
            Vista().mensajesAMostrar(5)
            escribirLog("Intento de eliminación de empleado -> ERR: No se ha podido eliminar el empleado")
        }
    }

    /**
     * Función para crear el xml de un empleado
     * @param: DNI -> DNI del empleado del que se quiere crear XML
     */
    fun crearXML(DNI: String) {
        val context = JAXBContext.newInstance(Empleados::class.java)

        //Crea un marshaller para serializar
        var marshaller = context.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        val archivo = File("./empleado.xml")
        for (i in listaDeEmpleados) {
            if (i.dni == DNI) {
                marshaller.marshal(i,archivo)
                Vista().mensajesAMostrar(6)
                escribirLog("Archivo XML creado del empleado: $DNI")
            }
        }
    }

    /**
     * Función para comprobar DNI introducido.
     * @param: dni -> dni introducido.
     */
    fun comprobarDNI(dni:String):Boolean {
        var duplicado = false

        var busqueda = connection.createStatement().executeQuery("SELECT * FROM EMPLEADOS WHERE DNI = '$dni'")

        if (busqueda.next()) {
            duplicado = true
            escribirLog("Intento fallido de agregación de empleado. DNI repetido.")
        }
       return duplicado

    }

    /**
     * Funcion para escribir log del gestor BDD
     * @param: msj -> Mensaje que se escribe en el log
     */
    fun escribirLog(msj:String) {
        printWriterGestor.println("$date -> $msj")
        printWriterGestor.flush()

    }
    /**
     * Función para cerrar la conexión con la BDD
     */
    fun cerrarBDD() {
        connection.close()
        printWriterGestor.close()
    }
}