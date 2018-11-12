package com.halkyproject.pausemenu.singletons.finances

import com.halkyproject.lifehack.model.finances.MovementSource
import com.halkyproject.pausemenu.superclasses.BasicEntityService


object MovementSourceService : BasicEntityService<MovementSource, MovementSourceService.MovementSourceFilter>() {
    override fun getEntityUri(): String {
        return MovementSource.URI_NAME
    }

    override fun getClassEntity(): Class<MovementSource> {
        return MovementSource::class.java
    }

    override fun getClassEntityArray(): Class<Array<MovementSource>> {
        return Array<MovementSource>::class.java
    }

    override fun getBaseUrl(): String {
        return MovementSource.BASE_URL
    }

    class MovementSourceFilter(val _active: Boolean?)
}