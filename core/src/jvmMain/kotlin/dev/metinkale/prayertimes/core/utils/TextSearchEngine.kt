package dev.metinkale.prayertimes.core.utils

internal object TextSearchEngine {

    private fun calculateSearchScore(
        normalizedQueries: Collection<String>,
        normalizedNames: Collection<String>
    ) =
        normalizedQueries.sumOf { lhs ->
            normalizedNames.indexOf(lhs).let {
                if (it < 0) 0
                else maxOf(10 - it, 1) * 10
            }
        }


    fun <T> search(
        list: Sequence<T>,
        normalizedTextGetter: (T) -> Collection<String>,
        priority: (T) -> Int = { 0 },
        query: String
    ): T? {
        val words = query.normalize().split(' ')
        val bestEntries: MutableList<T> = mutableListOf()
        var bestScore = 0
        for (entry in list) {
            val normalizedNames = normalizedTextGetter(entry)
            val score = calculateSearchScore(words, normalizedNames)
            if (score > bestScore) {
                bestEntries.clear()
                bestEntries.add(entry)
                bestScore = score
            } else if (score == bestScore && score > 0) {
                bestEntries.add(entry)
            }
        }

        return bestEntries.minByOrNull { priority(it) }
    }


}