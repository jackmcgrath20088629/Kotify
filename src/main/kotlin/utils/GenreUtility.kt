package utils

object GenreUtility {

    @JvmStatic
    val categories = setOf("Hip-Hop", "R&B", "Pop", "Rock", "Jazz", "Other" )

    @JvmStatic
    fun isValidGenre(genreToCheck: String?): Boolean {
        for (genre in categories) {
            if (genre.equals(genreToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}