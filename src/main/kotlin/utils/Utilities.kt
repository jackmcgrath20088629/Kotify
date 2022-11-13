package utils
import models.Playlist
import models.Song

object Utilities {

    // Playlist: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(playlistsToFormat: List<Playlist>): String =
        playlistsToFormat
            .joinToString(separator = "\n") { playlist ->  "$playlist" }

    @JvmStatic
    fun formatSetString(songsToFormat: Set<Song>): String =
        songsToFormat
            .joinToString(separator = "\n") { song ->  "\t$song" }

}
