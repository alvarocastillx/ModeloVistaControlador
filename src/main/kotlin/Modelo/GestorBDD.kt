package Modelo

import Controlador.ClassControlador
import Vista.Vista
import java.sql.Connection
import java.sql.DriverManager
import jakarta.xml.bind.*
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.sql.Date
import java.time.LocalDateTime


/*
CREATE TABLE EMPLEADOS(
DNI VARCHAR2(10) PRIMARY KEY,
NOMBRE VARCHAR2(40),
FECHA_NAC VARCHAR2(40));
 */

// INSERT INTO EMPLEADOS (DNI,NOMBRE,FECHA_NAC) VALUES ('0000A','Álvaro Castilla','13/01/2003');
val jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe"
var connection: Connection = DriverManager.getConnection(jdbcUrl,"ADA","ADA")
lateinit var listaDeEmpleados : MutableList<Empleados>
var printWriter : PrintWriter = PrintWriter(File("./logs/gestorlog.txt"))
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
        printWriter.println("$date: Empleados recuperados")

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
            printWriter.println("$date: Empleado agregado: DNI:$DNI, Nombre:$nombreempleado, Fecha de nacimiento:$fechanac")

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
            println(DNI)
            Vista().mensajesAMostrar(8)

            //
            printWriter.println("$date: Empleado modificado: DNI:$DNI, Nuevo nombre:$nuevonombre, Nueva fecha de nacimiento:$nuevafecha")
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
                printWriter.println("$date: Empleado eliminado -> DNI: $DNI")
            }
            else {
                Vista().mensajesAMostrar(4)
                printWriter.println("$date: Intento de eliminación de empleado -> ERR: No se ha encontrado al empleado. No se ha podido eliminar.")
            }
        }
        catch (e: Exception) {
            Vista().mensajesAMostrar(5)
            printWriter.println("$date: Intento de eliminación de empleado -> ERR: No se ha podido eliminar el empleado")
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
                printWriter.println("$date: Archivo XML creado del empleado: $DNI")
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
        }
       return duplicado
        /*
        for (i in listaDeEmpleados) {
            if (i.dni == dni) {
                duplicado = true
                //
                printWriter.println("$date: Intento fallido de agregación de empleado. DNI repetido.")
            }
        }
        return duplicado

         */


    }

    /**
     * Función para cerrar la conexión con la BDD
     */
    fun cerrarBDD() {
        connection.close()
        printWriter.close()
    }
}