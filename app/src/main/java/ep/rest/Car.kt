package ep.rest

import java.io.Serializable

class Car : Serializable {

    internal var id: Int = 0
    internal var marka: String = ""
    internal var cena: Int = 0
    internal var slika: String = ""
    internal var aktiven: Int = 0

}
