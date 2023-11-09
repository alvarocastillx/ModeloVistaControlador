package Modelo

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

/**
 * Clase empleado
 */
@XmlRootElement
class Empleados(DNI:String, Nombre:String, fechanac: String) {
    constructor():this("","","")
    @get:XmlElement(name = "dni")
    var dni = DNI
    @get:XmlElement(name = "nombre")
    var nombre = Nombre
    @get:XmlElement(name = "Fechanac")
    var Fechanac = fechanac
}