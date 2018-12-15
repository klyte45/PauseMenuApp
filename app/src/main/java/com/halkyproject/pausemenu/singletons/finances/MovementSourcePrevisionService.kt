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

    fun listAllActivePrevisions(): List<MovementSourcePrevision> {
        return MovementSourcePrevisionService.search(MovementSourcePrevisionService.Filter(null, MovementSourcePrevisionService.MovSourceFilter(true)))
    }

    class Filter(val movSourceId: Int? = null, val movSource: MovSourceFilter? = null)

    class MovSourceFilter(val _active: Boolean? = null)
}