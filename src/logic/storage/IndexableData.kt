package logic.storage

import java.io.Serializable

/**
 * Created by Awlex on 11.01.2018.
 */
abstract class IndexableData : Serializable {

    var id: Int = -1
        set(value) {
            if (field == -1) {
                field = value
            }
        }
}
