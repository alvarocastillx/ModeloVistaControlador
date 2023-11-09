package Controlador

import Modelo.Empleados
import Modelo.GestorBDD
import Vista.Vista

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
                Vista().menu()
            }
            2 -> {
                Vista().menuAgregar()
                Vista().menu()
            }
            3 -> {
                Vista().menuModificar()
                Vista().menu()
            }
            4 -> {
                Vista().menuEliminar()
                Vista().menu()
            }
            5 -> {
                Vista().menuXML()
                Vista().menu()
            }
            else -> {
                gestor.cerrarBDD()
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
}