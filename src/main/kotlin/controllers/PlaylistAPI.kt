package controllers

import utils.Utilities.formatListString
import java.util.ArrayList
import models.Playlist
import persistence.Serializer


class PlaylistAPI() {

    private var playlists = ArrayList<Playlist>()

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

    fun delete(id: Int) = playlists.removeIf { playlist -> playlist.playlistId == id }

    fun update(id: Int, playlist: Playlist?): Boolean {
        // find the playlist object by the index number
        val foundPlaylist = findPlaylist(id)

        // if the playlist exists, use the playlist details passed as parameters to update the found playlist in the ArrayList.
        if ((foundPlaylist != null) && (playlist != null)) {
            foundPlaylist.playlistTitle = playlist.playlistTitle
            foundPlaylist.playlistPriority = playlist.playlistPriority
            foundPlaylist.playlistCategory = playlist.playlistCategory
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

    fun listActivePlaylists() =
        if (numberOfActivePlaylists() == 0) "No active playlists stored"
        else formatListString(playlists.filter { playlist -> !playlist.isPlaylistArchived })

    fun listArchivedPlaylists() =
        if (numberOfArchivedPlaylists() == 0) "No archived playlists stored"
        else formatListString(playlists.filter { playlist -> playlist.isPlaylistArchived })

    // ----------------------------------------------
    //  COUNTING METHODS FOR Playlist ArrayList
    // ----------------------------------------------
    fun numberOfPlaylists() = playlists.size
    fun numberOfArchivedPlaylists(): Int = playlists.count { playlist: Playlist -> playlist.isPlaylistArchived }
    fun numberOfActivePlaylists(): Int = playlists.count { playlist: Playlist -> !playlist.isPlaylistArchived }

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------
    fun findPlaylist(playlistId : Int) =  playlists.find{ playlist -> playlist.playlistId == playlistId }

    fun searchPlaylistsByTitle(searchString: String) =
       formatListString(
            playlists.filter { playlist -> playlist.playlistTitle.contains(searchString, ignoreCase = true) }
        )

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

    @Throws(Exception::class)
    fun load() {
        playlists = serializer.read() as ArrayList<Playlist>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(playlists)
    }

}
