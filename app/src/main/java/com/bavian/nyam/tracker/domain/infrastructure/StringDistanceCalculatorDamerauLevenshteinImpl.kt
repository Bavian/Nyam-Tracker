package com.bavian.nyam.tracker.domain.infrastructure

class StringDistanceCalculatorDamerauLevenshteinImpl : StringDistanceCalculator {
    override fun calculate(
        source: CharSequence,
        target: CharSequence,
    ): Int {
        val sourceLength = source.length
        val targetLength = target.length
        if (sourceLength == 0) return targetLength
        if (targetLength == 0) return sourceLength

        val dist = Array(sourceLength + 2) { IntArray(targetLength + 2) }
        val maxDist = sourceLength + targetLength
        dist[0][0] = maxDist
        for (i in 0..sourceLength) {
            dist[i + 1][0] = maxDist
            dist[i + 1][1] = i
        }
        for (j in 0..targetLength) {
            dist[0][j + 1] = maxDist
            dist[1][j + 1] = j
        }

        val da = HashMap<Char, Int>()
        for (i in 1..sourceLength) {
            var db = 0
            for (j in 1..targetLength) {
                val k = da[target[j - 1]] ?: 0
                val l = db

                val cost =
                    if (source[i - 1] == target[j - 1]) {
                        db = j
                        0
                    } else {
                        1
                    }

                dist[i + 1][j + 1] =
                    minOf(
                        dist[i][j] + cost,
                        dist[i + 1][j] + 1,
                        dist[i][j + 1] + 1,
                        dist[k][l] + (i - k - 1) + 1 + (j - l - 1),
                    )
            }
            da[source[i - 1]] = i
        }
        return dist[sourceLength + 1][targetLength + 1]
    }
}
