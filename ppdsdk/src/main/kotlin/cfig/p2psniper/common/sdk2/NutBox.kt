package cfig.p2psniper.common.sdk2

import java.util.*
import kotlin.collections.HashMap

class NutBox<K, V>() {
    private var recentIds = LinkedList<K>()
    private val recentIdMaxSize = 2000
    private val recentObjs: HashMap<K, V> = hashMapOf()

    //Guarded by: recentIdMaxSize
    fun updateCacheLocked(inMap: HashMap<K, V>): List<V> {
        val newList: MutableList<V> = mutableListOf()
        synchronized(recentIdMaxSize) {
            inMap
                    .filter { !recentIds.contains(it.key) }
                    .forEach {
                        recentIds.addLast(it.key)
                        newList.add(it.value)
                        recentObjs.put(it.key, it.value)
                    }
            //reduce
            for (i in 1..(recentIds.size - recentIdMaxSize)) {
                recentIds.removeFirst()
            }
        }

        return newList
    }

}
