package models

import utils.Utilities

data class Playlist(var playlistId: Int = 0,
                    var playlistTitle: String,
                    var playlistPriority: Int,
                    var playlistCategory: String,
                    var isPlaylistArchived: Boolean = false,
                    var songs : MutableSet<Song> = mutableSetOf())
{
    private var lastSongId = 0
    private fun getSongId() = lastSongId++

    fun addSong(song: Song) : Boolean {
        song.songId = getSongId()
        return songs.add(song)
    }

    fun numberOfSongs() = songs.size

    fun findOne(id: Int): Song?{
        return songs.find{ song -> song.songId == id }
    }

    fun delete(id: Int): Boolean {
        return songs.removeIf { song -> song.songId == id}
    }

    fun update(id: Int, newSong : Song): Boolean {
        val foundSong = findOne(id)

        //if the object exists, use the details passed in the newSong parameter to
        //update the found object in the Set
        if (foundSong != null){
            foundSong.songContents = newSong.songContents
            foundSong.isSongComplete = newSong.isSongComplete
            return true
        }

        //if the object was not found, return false, indicating that the update was not successful
        return false
    }

    fun checkPlaylistCompletionStatus(): Boolean {
        if (songs.isNotEmpty()) {
            for (song in songs) {
                if (!song.isSongComplete) {
                    return false
               }
            }
        }
        return true //a playlist with empty songs can be archived, or all songs are complete
    }

    fun listSongs() =
         if (songs.isEmpty())  "\tNO SongS ADDED"
         else  Utilities.formatSetString(songs)

    override fun toString(): String {
        val archived = if (isPlaylistArchived) 'Y' else 'N'
        return "$playlistId: $playlistTitle, Priority($playlistPriority), Category($playlistCategory), Archived($archived) \n${listSongs()}"
    }

}