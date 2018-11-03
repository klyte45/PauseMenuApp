package com.halkyproject.pausemenu.model.finances

class FinancialAccount {
    companion object {
        const val URI_NAME = "finAccount"
    }

    var id: Int? = null
    lateinit var name: String
    var bankNumber: String? = null
    var branch: String? = null
    var number: String? = null
    lateinit var type: AccountType
    lateinit var currency: Currency

    enum class AccountType {
        SAVINGS,
        CURRENT,
        LOCAL,
        VIRTUAL
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
          "fieldName": "type",
          "fieldType": "String"
      },
      {
          "fieldName": "currency",
          "fieldType": "String"
      }
  ]
}*/
