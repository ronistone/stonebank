package br.com.ronistone.stonebank.application.controller

import br.com.ronistone.stonebank.service.commons.Error
import br.com.ronistone.stonebank.service.commons.ValidationException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class ErrorHandlerController {

    val logger: Logger = LogManager.getLogger(this.javaClass)

    @ExceptionHandler(value = [ValidationException::class])
    @ResponseBody
    fun validationException(validationException: ValidationException) : ResponseEntity<List<Error>> {
        logger.warn("Handling ValidationException", validationException)

        return ResponseEntity.badRequest().body(validationException.errors)
    }

}