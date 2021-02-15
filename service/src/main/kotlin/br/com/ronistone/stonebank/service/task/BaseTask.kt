package br.com.ronistone.stonebank.service.task

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

abstract class BaseTask: JavaDelegate {

    protected val logger: Logger = LogManager.getLogger(this::class.java)

    override fun execute(delegate: DelegateExecution) {
        logger.info("Executing Task ${this::class.java}")
        proccess(delegate)
        logger.info("Finishing Task ${this::class.java}")
    }


    abstract fun proccess(delegate: DelegateExecution)
}