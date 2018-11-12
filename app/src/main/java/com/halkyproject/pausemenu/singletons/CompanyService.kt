package com.halkyproject.pausemenu.singletons

import com.halkyproject.lifehack.model.Company
import com.halkyproject.pausemenu.superclasses.BasicEntityService


object CompanyService : BasicEntityService<Company, CompanyService.CompanyFilter>() {
    override fun getEntityUri(): String {
        return Company.URI_NAME
    }

    override fun getClassEntity(): Class<Company> {
        return Company::class.java
    }

    override fun getClassEntityArray(): Class<Array<Company>> {
        return Array<Company>::class.java
    }

    override fun getBaseUrl(): String {
        return Company.BASE_URL
    }

    class CompanyFilter
}