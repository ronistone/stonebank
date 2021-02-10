package br.com.ronistone.stonebank.domain

enum class TransactionStatus {

    COMPLETED,
    CREATED,
    PROCESSING,
    ERROR_TO_CREATE,
    ERROR,
    FAIL

}