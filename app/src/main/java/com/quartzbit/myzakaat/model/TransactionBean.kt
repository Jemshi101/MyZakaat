package com.quartzbit.myzakaat.model

/**
 * Created by Jemsheer K D on 11 June, 2018.
 * Package com.quartzbit.myzakaat.model
 * Project MyZakaat
 */
class TransactionBean : BaseBean() {

    var id: Int = 0
    var dateString: String = ""
    var date: Long = 0
    var description: String = ""
    var reference: String = ""
    var debit: Int = 0
    var credit: Int = 0
    var balance: Int = 0
    var bankCut: Int = 0
    var realBalance: Int = 0
    var interest: Int = 0
    var lowestAmount: Int = 0

    fun formatDate(date: String): String {
        val index = if (!date.contains("\n(")) date.indexOf("(") else date.indexOf("\n(")
        return if (index == -1) date else date.substring(index + 1)
    }


}