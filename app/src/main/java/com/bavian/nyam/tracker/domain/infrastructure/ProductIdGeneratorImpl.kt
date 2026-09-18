package com.bavian.nyam.tracker.domain.infrastructure

import java.util.UUID

class ProductIdGeneratorImpl : ProductIdGenerator {
    override fun generateId(): String = UUID.randomUUID().toString()
}
