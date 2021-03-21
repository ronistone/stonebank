package br.com.ronistone.stonebank.commons

import org.mockito.Mockito

class Utils {

    companion object {
        fun <T> any(type: Class<T>): T {
            Mockito.any(type)
            return null as T
        }
        fun <T> eq(type: T): T {
            Mockito.eq(type)
            return null as T
        }
    }

}