package controllers

import utils.Utilities.formatListString
import java.util.ArrayList
import models.Playlist
import persistence.Serializer



class PlaylistAPI(serializerType: Serializer) {

    private var playlists = ArrayList<Playlist>()
    private var serializer: Serializer = serializerType

    // ----------------------------------------------
    //  For Managing the id internally in the program
    // ----------------------------------------------
    private var lastId = 0
    private fun getId() = lastId++

    // ----------------------------------------------
    //  CRUD METHODS FOR Playlist ArrayList
    // ----------------------------------------------
    fun add(playlist: Playlist): Boolean {
        playlist.playlistId = getId()
        return playlists.add(playlist)
    }


    fun deletePlaylist(id: Int) = playlists.removeIf { playlist -> playlist.playlistId == id }

    fun updatePlaylist(id: Int, playlist: Playlist?): Boolean {
        // find the playlist object by the index number
        val foundPlaylist = findPlaylist(id)

        // if the playlist exists, use the playlist details passed as parameters to update the found playlist in the ArrayList.
        if ((foundPlaylist != null) && (playlist != null)) {
            foundPlaylist.playlistTitle = playlist.playlistTitle
            foundPlaylist.playlistRating = playlist.playlistRating
            foundPlaylist.playlistGenre = playlist.playlistGenre
            return true
        }

        // if the playlist was not found, return false, indicating that the update was not successful
        return false
    }

    fun archivePlaylist(id: Int): Boolean {
        val foundPlaylist = findPlaylist(id)
        if (( foundPlaylist != null) && (!foundPlaylist.isPlaylistArchived)
            && ( foundPlaylist.checkPlaylistCompletionStatus())) {
              foundPlaylist.isPlaylistArchived = true
              return true
        }
        return false
    }

    // ----------------------------------------------
    //  LISTING METHODS FOR playlist ArrayList
    // ----------------------------------------------
    fun listAllPlaylists() =
        if (playlists.isEmpty()) "No playlists stored"
        else formatListString(playlists)

    fun listPlaylistsBySelectedRating(rating: Int): String =
        if (playlists.isEmpty()) "No playlists stored"
        else {
            val listOfPlaylists = formatListString(playlists.filter { playlist -> playlist.playlistRating == rating })
            if (listOfPlaylists.equals("")) "No playlists with rating: $rating"
            else "${numberOfPlaylistsByRating(rating)} playlists with rating $rating: $listOfPlaylists"
        }
    fun listActivePlaylists() =
        if (numberOfActivePlaylists() == 0) "No active playlists stored"
        else formatListString(playlists.filter { playlist -> !playlist.isPlaylistArchived })

    fun listArchivedPlaylists() =
        if (numberOfArchivedPlaylists() == 0) "No archived playlists stored"
        else formatListString(playlists.filter { playlist -> playlist.isPlaylistArchived })

    // ----------------------------------------------
    //  COUNTING METHODS - PLAYLISTS
    // ----------------------------------------------

    fun numberOfPlaylists() = playlists.size
    fun numberOfArchivedPlaylists(): Int = playlists.count { playlist: Playlist -> playlist.isPlaylistArchived }
    fun numberOfActivePlaylists(): Int = playlists.count { playlist: Playlist -> !playlist.isPlaylistArchived }
    fun numberOfPlaylistsByRating(rating: Int): Int = playlists.count { r: Playlist -> r.playlistRating == rating }

    fun numberOfHipHop() = playlists.count { playlist -> playlist.playlistGenre == "Hip-Hop" }
    fun numberOfPop() = playlists.count { playlist -> playlist.playlistGenre == "Pop" }
    fun numberOfRock() = playlists.count { playlist -> playlist.playlistGenre == "Rock" }
    fun numberOfJazz() = playlists.count { playlist -> playlist.playlistGenre == "Jazz" }
    fun numberOfRnB() = playlists.count { playlist -> playlist.playlistGenre == "R&B" }
    fun numberOfOther() = playlists.count { playlist -> playlist.playlistGenre == "Other" }

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------
    fun findPlaylist(playlistId : Int) =  playlists.find{ playlist -> playlist.playlistId == playlistId }

    fun searchPlaylistsByTitle(searchString: String) =
       formatListString(
            playlists.filter { playlist -> playlist.playlistTitle.contains(searchString, ignoreCase = true) }
        )

    fun searchPlaylistsByGenre(searchString: String) =
        formatListString(
            playlists.filter { playlist -> playlist.playlistGenre.contains(searchString, ignoreCase = true) }
        )
    // ----------------------------------------------
    //  SEARCHING METHODS FOR songS
    // ----------------------------------------------
    fun searchSongByContents(searchString: String): String {
        return if (numberOfPlaylists() == 0) "No playlists stored"
        else {
            var listOfPlaylists = ""
            for (playlist in playlists) {
                for (song in playlist.songs) {
                    if (song.songContents.contains(searchString, ignoreCase = true)) {
                        listOfPlaylists += "${playlist.playlistId}: ${playlist.playlistTitle} \n\t${song}\n"
                    }
                }
            }
            if (listOfPlaylists == "") "No songs found for: $searchString"
            else listOfPlaylists
        }
    }

    // ----------------------------------------------
    //  LISTING METHODS FOR songS
    // ----------------------------------------------
    fun listTodoSongs(): String =
         if (numberOfPlaylists() == 0) "No playlists stored"
         else {
             var listOfTodoSongs = ""
             for (playlist in playlists) {
                 for (song in playlist.songs) {
                     if (!song.isSongComplete) {
                         listOfTodoSongs += playlist.playlistTitle + ": " + song.songContents + "\n"
                     }
                 }
             }
             listOfTodoSongs
         }

    // ----------------------------------------------
    //  COUNTING METHODS FOR SongS
    // ----------------------------------------------
    fun numberOfToDoSongs(): Int {
        var numberOfToDoSongs = 0
        for (playlist in playlists) {
            for (song in playlist.songs) {
                if (!song.isSongComplete) {
                    numberOfToDoSongs++
                }
            }
        }
        return numberOfToDoSongs
    }
    // ----------------------------------------------
    //  LOAD / SAVE
    // ----------------------------------------------
    @Throws(Exception::class)
    fun load() {
        playlists = serializer.read() as ArrayList<Playlist>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(playlists)
    }

}
