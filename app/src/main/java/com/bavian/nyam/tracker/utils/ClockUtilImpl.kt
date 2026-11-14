package com.bavian.nyam.tracker.utils

import kotlin.time.Clock

class ClockUtilImpl : ClockUtil {
    override val epoch: Long get() = Clock.System.now().toEpochMilliseconds()
}
