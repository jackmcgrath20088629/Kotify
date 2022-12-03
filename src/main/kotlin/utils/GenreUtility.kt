package utils

object GenreUtility {

    @JvmStatic
    val genres = setOf("Hip-Hop", "R&B", "Pop", "Rock", "Jazz", "Other" )

    @JvmStatic
    fun isValidGenre(genreToCheck: String?): Boolean {
        for (genre in genres) {
            if (genre.equals(genreToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}