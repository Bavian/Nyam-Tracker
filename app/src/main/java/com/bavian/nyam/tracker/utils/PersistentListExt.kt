package com.bavian.nyam.tracker.utils

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

inline fun <T> buildPersistentList(builder: MutableList<T>.() -> Unit): PersistentList<T> =
    persistentListOf<T>()
        .builder()
        .apply(builder)
        .build()
