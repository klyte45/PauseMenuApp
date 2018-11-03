package com.halkyproject.pausemenu.model.finances

import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class MovementSource {
    var id: Int? = null
    lateinit var name: String
    lateinit var frequency: Frequency
    private var refDay: Int? = null
    private var refMonth: Int? = null
    private var values: List<Value> = ArrayList()

    private val inAccountId: Int? = null
    private val outAccountId: Int? = null


    class Value {
        lateinit var startDate: Date
        lateinit var value: BigDecimal
        lateinit var currency: Currency
    }

    enum class Frequency {
        MONTHLY,
        WEEKLY,
        FIFTHLY,
        DAILY,
        YEARLY
    }
}
/*
{
  "uriName": "movSource",
  "entityName": "Movement Source",
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
          "fieldName": "frequency",
          "fieldType": "String",
          "nullable": false
      },
      {
          "fieldName": "refDay",
          "fieldType": "Integer"
      },
      {
          "fieldName": "refMonth",
          "fieldType": "Integer"
      },
      {
          "fieldName": "values",
          "fieldType": "Document",
          "nullable": false,
          "documentFields": [
              {
                  "fieldName": "startDate",
                  "fieldType": "Timestamp"
              },
              {
                  "fieldName": "value",
                  "fieldType": "Decimal"
              },
              {
                  "fieldName": "currency",
                  "fieldType": "String"
              }
          ]
      },
      {
          "fieldName": "inAccountId",
          "fieldType": "Integer"
      },
      {
          "fieldName": "outAccountId",
          "fieldType": "Integer"
      }
  ]
}*/
