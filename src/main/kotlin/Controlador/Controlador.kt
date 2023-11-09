package Controlador

import Modelo.Empleados
import Modelo.GestorBDD
import Vista.Vista
import java.io.File
import java.io.PrintWriter
import java.time.LocalDateTime


var printWriterCntrl : PrintWriter = PrintWriter(File("./logs/gestorcntrl.txt"))
lateinit var date : LocalDateTime

/**
 * @author Álvaro Castilla
 * Clase controlador
 */
class ClassControlador() {
    var finalizado = false
    var gestor = GestorBDD()

    /**
     * Funcion case que controla el menú
     */
    fun controlador(controlador:Int) {
        when (controlador) {
            1 -> {
                gestor.recuperarEmpleados()
                escribirLog("Accedido a recuperar empleados")
                Vista().menu()
            }
            2 -> {
                Vista().menuAgregar()
                escribirLog("Accedido a menu agregar")
                Vista().menu()
            }
            3 -> {
                Vista().menuModificar()
                escribirLog("Accedido a menu modificar")
                Vista().menu()
            }
            4 -> {
                Vista().menuEliminar()
                escribirLog("Accedido a menu eliminar")
                Vista().menu()
            }
            5 -> {
                Vista().menuXML()
                escribirLog("Accedido a menu XML")
                Vista().menu()
            }
            else -> {
                gestor.cerrarBDD()
                printWriterCntrl.close()
                finalizado = true
            }
        }
    }

    /**
     * Función para imprimir empleados en vista
     */
    fun comunicarEmpleadosVista(listaDeEmpleados: MutableList<Empleados>) {
        Vista().imprimirEmpleados(Modelo.listaDeEmpleados)
    }

    fun escribirLog(msj:String) {
        date = LocalDateTime.now()
        printWriterCntrl.println("$date: $msj")
    }
}