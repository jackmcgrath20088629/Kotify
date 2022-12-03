package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.GenreUtility.genres
import utils.GenreUtility.isValidGenre

internal class GenreUtilityTest {
    @Test
    fun genresReturnsFullCategoriesSet() {
        Assertions.assertEquals(6, genres.size)
        Assertions.assertTrue(genres.contains("Hip-Hop"))
        Assertions.assertTrue(genres.contains("Rock"))
        Assertions.assertTrue(genres.contains("Pop"))
        Assertions.assertTrue(genres.contains("Jazz"))
        Assertions.assertTrue(genres.contains("R&B"))
        Assertions.assertTrue(genres.contains("Other"))
        Assertions.assertFalse(genres.contains(""))
    }

    @Test
    fun isValidGenreTrueWhenGenreExists() {
        Assertions.assertTrue(isValidGenre("Hip-Hop"))
        Assertions.assertTrue(isValidGenre("hip-hop"))
        Assertions.assertTrue(isValidGenre("HIPHOP"))
        Assertions.assertTrue(isValidGenre("hiphop"))
        Assertions.assertTrue(isValidGenre("hip hop"))
        Assertions.assertTrue(isValidGenre("HIP HOP"))
        Assertions.assertTrue(isValidGenre("HipHop"))
        Assertions.assertTrue(isValidGenre("Hiphop"))
        Assertions.assertTrue(isValidGenre("hIPHOP"))
        Assertions.assertTrue(isValidGenre("hIPhOP"))
        Assertions.assertTrue(isValidGenre("ROCK"))
        Assertions.assertTrue(isValidGenre("Rock"))
        Assertions.assertTrue(isValidGenre("rock"))
        Assertions.assertTrue(isValidGenre("rOCK"))
        Assertions.assertTrue(isValidGenre("Pop"))
        Assertions.assertTrue(isValidGenre("pop"))
        Assertions.assertTrue(isValidGenre("POP"))
        Assertions.assertTrue(isValidGenre("pOP"))
        Assertions.assertTrue(isValidGenre("Jazz"))
        Assertions.assertTrue(isValidGenre("JAZZ"))
        Assertions.assertTrue(isValidGenre("jazz"))
        Assertions.assertTrue(isValidGenre("jAZZ"))
        Assertions.assertTrue(isValidGenre("R&B"))
        Assertions.assertTrue(isValidGenre("rnb"))
        Assertions.assertTrue(isValidGenre("Rnb"))
        Assertions.assertTrue(isValidGenre("RnB"))
        Assertions.assertTrue(isValidGenre("r&b"))
        Assertions.assertTrue(isValidGenre("R and B"))
        Assertions.assertTrue(isValidGenre("R & B"))
        Assertions.assertTrue(isValidGenre("R n B"))
        Assertions.assertTrue(isValidGenre("oTHER"))
        Assertions.assertTrue(isValidGenre("Other"))
        Assertions.assertTrue(isValidGenre("other"))
        Assertions.assertTrue(isValidGenre("OTHER"))
    }

    @Test
    fun isValidGenreFalseWhenGenreDoesNotExist() {
        Assertions.assertFalse(isValidGenre("jaz"))
        Assertions.assertFalse(isValidGenre("hipop"))
        Assertions.assertFalse(isValidGenre("rok"))
        Assertions.assertFalse(isValidGenre("hphop"))
        Assertions.assertFalse(isValidGenre("oher"))
        Assertions.assertFalse(isValidGenre("oter"))
        Assertions.assertFalse(isValidGenre("rmb"))
        Assertions.assertFalse(isValidGenre("rb"))
        Assertions.assertFalse(isValidGenre(""))
    }
}