package com.bavian.nyam.tracker.domain.infrastructure

interface StringDistanceCalculator {
    fun calculate(
        source: CharSequence,
        target: CharSequence,
    ): Int
}
