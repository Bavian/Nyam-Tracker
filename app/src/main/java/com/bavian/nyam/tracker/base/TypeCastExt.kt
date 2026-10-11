package com.bavian.nyam.tracker.base

inline fun <reified T> Any.castTo() = this as T

inline fun <reified T> Any.safeCastTo() = this as? T
