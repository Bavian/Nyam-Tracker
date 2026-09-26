package com.bavian.nyam.tracker.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource

@Immutable
sealed interface StringSource {
    @Composable
    fun get(): String

    data class Constant(
        val value: String,
    ) : StringSource {
        @Composable
        override fun get(): String = value
    }

    data class Resource(
        val resId: Int,
        val formatArgs: List<Any>,
    ) : StringSource {
        constructor(resId: Int, vararg formatArgs: Any) : this(resId, formatArgs.toList())

        @Composable
        override fun get(): String = stringResource(resId)
    }

    companion object {
        val Empty: StringSource = Constant("")
    }
}

inline val String.stringSource: StringSource
    get() = StringSource.Constant(this)
