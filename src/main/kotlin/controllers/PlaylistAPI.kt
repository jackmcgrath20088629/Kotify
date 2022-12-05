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

    fun downloadPlaylist(id: Int): Boolean {
        val foundPlaylist = findPlaylist(id)
        if (( foundPlaylist != null) && (!foundPlaylist.isPlaylistDownloaded)
            && ( foundPlaylist.checkPlaylistCompletionStatus())) {
              foundPlaylist.isPlaylistDownloaded = true
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
        else formatListString(playlists.filter { playlist -> !playlist.isPlaylistDownloaded })

    fun listDownloadedPlaylists() =
        if (numberOfDownloadedPlaylists() == 0) "No downloaded playlists stored"
        else formatListString(playlists.filter { playlist -> playlist.isPlaylistDownloaded })

    // ----------------------------------------------
    //  COUNTING METHODS - PLAYLISTS
    // ----------------------------------------------

    fun numberOfPlaylists() = playlists.size
    fun numberOfDownloadedPlaylists(): Int = playlists.count { playlist: Playlist -> playlist.isPlaylistDownloaded }
    fun numberOfActivePlaylists(): Int = playlists.count { playlist: Playlist -> !playlist.isPlaylistDownloaded }
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
                    if (song.songTitle.contains(searchString, ignoreCase = true)) {
                        listOfPlaylists += "${playlist.playlistId}: ${playlist.playlistTitle} \n\t${song}\n"
                    }
                }
            }
            if (listOfPlaylists == "") "No songs found for: $searchString"
            else listOfPlaylists
        }
    }

    fun searchSongByArtist(searchString: String): String {
        return if (numberOfPlaylists() == 0) "No playlists stored"
        else {
            var listOfPlaylists = ""
            for (playlist in playlists) {
                for (song in playlist.songs) {
                    if (song.songArtist.contains(searchString, ignoreCase = true)) {
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
    fun listNonFavouriteSongs(): String =
         if (numberOfPlaylists() == 0) "No playlists stored"
         else {
             var listOfNonFavouriteSongs = ""
             for (playlist in playlists) {
                 for (song in playlist.songs) {
                     if (!song.isSongFavoured) {
                         listOfNonFavouriteSongs += playlist.playlistTitle + ": " + song.songTitle + "\n"
                     }
                 }
             }
             listOfNonFavouriteSongs
         }

    fun listAllSongs(): String =
        if (numberOfPlaylists() == 0) "No playlists stored"
        else {
            var listOfAllSongs = ""
            for (playlist in playlists) {
                for (song in playlist.songs) {

                        listOfAllSongs += playlist.playlistTitle + ": " + song.songTitle + " by " + song.songArtist + "\n"

                }
            }
            listOfAllSongs
        }


    // ----------------------------------------------
    //  COUNTING METHODS FOR SongS
    // ----------------------------------------------
    fun numberOfFavouriteSongs(): Int {
        var numberOfFavouriteSongs = 0
        for (playlist in playlists) {
            for (song in playlist.songs) {
                if (!song.isSongFavoured) {
                    numberOfFavouriteSongs++
                }
            }
        }
        return numberOfFavouriteSongs
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
