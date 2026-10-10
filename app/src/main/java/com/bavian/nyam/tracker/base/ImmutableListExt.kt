package com.bavian.nyam.tracker.base

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

inline fun <T, R> Iterable<T>.mapAsImmutableList(mapper: (T) -> R): ImmutableList<R> =
    persistentListOf<R>()
        .builder()
        .also { builder ->
            for (item in this) {
                builder.add(mapper(item))
            }
        }.build()
