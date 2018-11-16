package com.halkyproject.pausemenu.singletons.finances

import com.halkyproject.lifehack.model.finances.MovementSourcePrevision
import com.halkyproject.pausemenu.superclasses.BasicEntityService


object MovementSourcePrevisionService : BasicEntityService<MovementSourcePrevision, MovementSourcePrevisionService.Filter>() {
    override fun getEntityUri(): String {
        return MovementSourcePrevision.URI_NAME
    }

    override fun getClassEntity(): Class<MovementSourcePrevision> {
        return MovementSourcePrevision::class.java
    }

    override fun getClassEntityArray(): Class<Array<MovementSourcePrevision>> {
        return Array<MovementSourcePrevision>::class.java
    }

    override fun getBaseUrl(): String {
        return MovementSourcePrevision.BASE_URL
    }

    class Filter(val movSourceId: Int)
}