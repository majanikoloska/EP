package ep.rest

import java.io.Serializable

data class Car(

    var id_avto: Int = 0,
    var marka: String = "",
    var cena: Int = 0,
    var aktiven: Int = 0,
    var opis: String = "") : Serializable