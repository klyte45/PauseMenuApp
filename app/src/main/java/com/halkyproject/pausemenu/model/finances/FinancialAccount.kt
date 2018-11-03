package com.halkyproject.pausemenu.model.finances

import java.io.Serializable
import java.math.BigDecimal

class FinancialAccount : Serializable {
    companion object {
        const val URI_NAME = "finAccount"
    }

    var id: Int? = null
    lateinit var name: String
    var bankNumber: String? = null
    var branch: String? = null
    var number: String? = null
    var balance: BigDecimal? = null
    lateinit var type: AccountType
    lateinit var currency: Currency

    enum class AccountType(val localeEntry: String) {
        SAVINGS("finance.accountType.savings"),
        CURRENT("finance.accountType.current"),
        LOCAL("finance.accountType.local"),
        VIRTUAL("finance.accountType.virtual")
    }


}
/*
{
  "uriName": "finAccount",
  "entityName": "FinancialAccount",
  "sequenceField": "id",
  "keys": ["id"],
  "avoidDefaultCrudService":true,
  "fields": [
      {
          "fieldName": "id",
          "fieldType": "Integer",
          "nullable": false
      },
      {
          "fieldName": "name",
          "fieldType": "String",
          "nullable": false
      },
      {
          "fieldName": "bankNumber",
          "fieldType": "String"
      },
      {
          "fieldName": "branch",
          "fieldType": "String"
      },
      {
          "fieldName": "number",
          "fieldType": "String"
      },
      {
          "fieldName": "balance",
          "fieldType": "Decimal",
          "nullable": false,
          "defaultValue": 0.0
      },
      {
          "fieldName": "type",
          "fieldType": "String"
      },
      {
          "fieldName": "currency",
          "fieldType": "String"
      }
  ]
}*/
