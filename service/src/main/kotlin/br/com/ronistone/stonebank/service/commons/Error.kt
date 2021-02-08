package br.com.ronistone.stonebank.service.commons

class Error(
    val message: String,
    val code: Long
) {

    companion object {
        val DEPOSIT_NOT_POSITIVE = Error(
            "deposit cannot be negative",
            4001
        )
        val WITHDRAW_NOT_POSITIVE = Error(
            "withdraw cannot be negative",
            4002
        )
        val TRANSFER_NOT_POSITIVE = Error(
            "transfer cannot be negative",
            4003
        )
        val ACCOUNT_NOT_FOUND = Error(
            "Account not found",
            4004
        )

        val DOCUMENT_INVALID = Error(
            "document invalid",
            4009
        )

        val ACCOUNT_RECEIVER_NOT_FOUND = Error(
            "Account of receiver not found",
            4104
        )

        val TRANSACTIONS_NOT_FOUND = Error(
            "Transactions not found", 4014
        )

        val TRANSACTIONS_INVALID = Error(
            "Transactions invalid", 4015
        )

        val INSUFFICIENT_FUNDS = Error(
            "insufficient funds", 4000
        )

        val INVALID_AMOUNT = Error(
            "invalid amount", 4005
        )

        val INVALID_CUSTOMER_INFORMATIONS = Error(
            "invalid customer informations", 4006
        )

        val CUSTOMER_ALREADY_HAS_AN_ACCOUNT = Error(
            "custome already has an account", 4007
        )
    }

    override fun toString(): String {
        return "Error(message='$message', code=$code)"
    }


}