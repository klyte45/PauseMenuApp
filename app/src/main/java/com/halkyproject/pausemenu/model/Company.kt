package com.halkyproject.pausemenu.model

import com.google.common.base.Enums
import com.halkyproject.pausemenu.enum.Country
import java.io.Serializable


class Company : Serializable {
    var id: Int? = null
    lateinit var mainName: String
    lateinit var realName: String
    lateinit var cityDisplayName: String
    var latitude: Double? = null
    var longitude: Double? = null
    lateinit var documentNumber: String
    lateinit var country: String


    fun getCountryEnum(): Country {
        return try {
            Enums.getIfPresent(Country::class.java, country).get()
        } catch (e: Exception) {
            Country.ZA
        }
    }
}
/*
{
  "uriName": "company",
  "entityName": "Empresa",
  "sequenceField": "id",
  "keys": ["id"],
  "fields": [
  {
      "fieldName": "id",
      "fieldType": "Integer"
  },
  {
      "fieldName": "mainName",
      "fieldType": "String",
      "nullable": false
  },
  {
      "fieldName": "realName",
      "fieldType": "String",
      "nullable": false
  },
  {
      "fieldName": "cityDisplayName",
      "fieldType": "String",
      "nullable": false
  },
  {
      "fieldName": "latitude",
      "fieldType": "Decimal",
      "nullable": true
  },
  {
      "fieldName": "longitude",
      "fieldType": "Decimal",
      "nullable": true
  },
  {
      "fieldName": "documentNumber",
      "fieldType": "String",
      "nullable": false
  },
  {
      "fieldName": "country",
      "fieldType": "String",
      "nullable": false,
      "minLength":2,
      "maxLength":2,
      "defaultValue": "BR"
  }
  ]
}*/
