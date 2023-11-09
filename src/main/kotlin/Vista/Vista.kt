package Vista

import Modelo.Empleados
import Modelo.GestorBDD
import java.lang.NumberFormatException

/**
 * @author: Álvaro Castilla
 * Clase dedicada a la interfaz gráfica
 */
class Vista() {
    var controlador = 0
    var gestor = GestorBDD()

    /**
     * Función que se ejecuta ininterrumpidamente mostrando el menú
     */
    fun menu() {
        println(
            """
            Menu:
            1- Mostrar empleados
            2- Agregar empleado
            3- Modificar empleado
            4- Eliminar empleado
            5- Archivo XML de un empleado
            6- Salir
        """.trimIndent()
        )
        try {
            controlador = readln().toInt()
        }
        catch (e: NumberFormatException) {
            println("Número introducido incorrecto.")
        }
        Controlador.ClassControlador().controlador(controlador)

    }

    /**
     * Función para imprimir empleados en pantalla
     * @param listaDeEmpleados -> lista de los empleados guardados en BDD
     */
    fun imprimirEmpleados(listaDeEmpleados: MutableList<Empleados>) {
        println("Empleados encontrados: ${listaDeEmpleados.size}")
        println("____________________________")
        for (i in listaDeEmpleados) {
            println("""
               Empleado actual: ${listaDeEmpleados.indexOf(i)+1}
               DNI: ${i.dni}
               Nombre: ${i.nombre}
               Fecha de nacimiento: ${i.Fechanac}
               ________________________________
            """.trimIndent())
        }
    }

    /**
     * Función que muestra un menú para agregar un empleado
     */
    fun menuAgregar() {
        println("Introduzca DNI")
        val DNI = readln().toUpperCase()
        //true duplicado, false no duplicado
        var comprobacionDNI = gestor.comprobarDNI(DNI)
        if (comprobacionDNI) {
            println("DNI duplicado. no se ha podido agregar.")
        }
        else {
            println("Introduzca nombre del cliente")
            val nombreempleado = readln().toUpperCase()
            println("Introduzca fecha de nacimiento")
            val fechanac = readln().toUpperCase()
            gestor.agregarEmpleado(DNI,nombreempleado,fechanac)
        }

    }

    /**
     * Función que muestra un menú para modificar empleado
     */
    fun menuModificar() {
        println("Introduzca DNI del empleado que quiere modificar")
        val DNI = readln().toUpperCase()
        println("Introduzca nombre a modificar")
        val nuevonombre = readln().toUpperCase()
        println("Introduzca fecha de nacimiento a modificar")
        val nuevafecha = readln().toUpperCase()
        gestor.modificarEmpleado(DNI,nuevonombre,nuevafecha)
    }
    /**
     * Función que muestra un menú para eliminar empleado
     */
    fun menuEliminar() {
        println("Introduzca DNI del empleado que quiere eliminar")
        val DNI = readln().toUpperCase()
        gestor.eliminarEmpleado(DNI)
    }
    /**
     * Función que muestra un menú para crear XML de un empleado
     */
    fun menuXML() {
        gestor.recuperarEmpleados()
        println("Introduzca el DNI del empleado del que desea crear XML")
        var DNI = readln().toUpperCase()
        gestor.crearXML(DNI)
    }

    /**
     * Función para mostrar mensajes al usuario
     * @param: mensaje -> numero de mensaje para mostrar
     */
    fun mensajesAMostrar(mensaje:Int) {
        when (mensaje) {
            1-> {
                println("No se ha podido agregar el empleado")
            }
            2-> {
                println("No se ha podido modificar el empleado")
            }
            3 -> {
                println("Empleado eliminado")
            }
            4 -> {
                println("No se ha encontrado al empleado. No se ha podido eliminar.")
            }
            5 -> {
                println("No se ha podido eliminar el empleado")
            }
            6 -> {
                println("XML creado correctamente.")
            }
            7 -> {
                println("Empleado agregado correctamente.")
            }
            8 -> {
                println("Empleado modificado correctamente.")
            }
        }
    }
}