package br.com.ronistone.stonebank.service.commons

import java.lang.RuntimeException

class ValidationException: RuntimeException {

    val errors: MutableList<Error> = mutableListOf()

    constructor(error: Error) : super() {
        addError(error)
    }

    fun addError(error: Error) {
        errors.add(error)
    }

}
