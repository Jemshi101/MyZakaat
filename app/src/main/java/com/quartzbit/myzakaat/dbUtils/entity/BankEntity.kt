package com.quartzbit.myzakaat.dbUtils.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quartzbit.myzakaat.model.BankBean

/**
 * Created by Jemsheer K D on 14 June, 2018.
 * Package com.quartzbit.myzakaat.dbUtils.entity
 * Project MyZakaat
 */

@Entity(tableName = "bank")
    class BankEntity() {

    constructor(bankBean: BankBean) : this() {

        this.id = bankBean.id
        this.bankName = bankBean.bankName
        this.accountName = bankBean.accountName
        this.accountNumber = bankBean.accountNumber
        this.range = bankBean.range
    }

    @ColumnInfo(name = "id")
    @PrimaryKey()
    var id: String = ""
    var bankName: String = ""
    var accountName: String = ""
    var accountNumber: String = ""
    var range: String = ""


    fun getBean(): BankBean {
        var bankBean: BankBean = BankBean()

        bankBean.id = this.id
        bankBean.bankName = this.bankName
        bankBean.accountName = this.accountName
        bankBean.accountNumber = this.accountNumber
        bankBean.range = this.range

        return bankBean;
    }

}