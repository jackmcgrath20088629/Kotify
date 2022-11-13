import controllers.PlaylistAPI
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import kotlin.system.exitProcess
import models.Playlist
import models.Song

private val playlistAPI = PlaylistAPI()

fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addPlaylist()
            2 -> listPlaylists()
            3 -> updatePlaylist()
            4 -> deletePlaylist()
            5 -> archivePlaylist()
            6 -> addSongToPlaylist()
            7 -> updateSongContentsInPlaylist()
            8 -> deleteASong()
            9 -> markSongStatus()
            10 -> searchPlaylists()
            15 -> searchSongs()
            //16 -> listToDoSongs()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |                 Welcome to Kotify                 |
         > -----------------------------------------------------  
         > | PLAYLIST MENU                                     |
         > |   1) Add a Playlist                               |
         > |   2) List Playlists                               |
         > |   3) Update a Playlist                            |
         > |   4) Delete a Playlist                            |
         > |   5) Archive a Playlist                           |
         > -----------------------------------------------------  
         > | SONG MENU                                         | 
         > |   6) Add song to a playlist                       |
         > |   7) Update song contents on a playlist           |
         > |   8) Delete song from a playlist                  |
         > -----------------------------------------------------  
         > | REPORT MENU FOR PLAYLISTS                         | 
         > |   10) Search for all songs (by song title)        |
         > |   11) List                                        |
         > |   12) .....                                       |
         > |   13) .....                                       |
         > |   14) .....                                       |
         > -----------------------------------------------------  
         > | REPORT MENU FOR SONGS                             |                                
         > |   15) Search for all songs (by song description)  |
         > |   16) List TODO songs                             |
         > -----------------------------------------------------  
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)

//------------------------------------
//playlist MENU
//------------------------------------
fun addPlaylist() {
    val playlistTitle = readNextLine("Enter playlists title: ")
    val playlistPriority = readNextInt("Enter priority: ")
    val playlistGenre = readNextLine("Enter a genre for the playlist: ")
    val isAdded = playlistAPI.add(Playlist(playlistTitle = playlistTitle, playlistPriority = playlistPriority, playlistCategory = playlistGenre))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listPlaylists() {
    if (playlistAPI.numberOfPlaylists() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL playlists          |
                  > |   2) View ACTIVE playlists       |
                  > |   3) View ARCHIVED playlists     |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllPlaylists()
            2 -> listActivePlaylists()
            3 -> listArchivedPlaylists()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No playlists stored")
    }
}

fun listAllPlaylists() = println(playlistAPI.listAllPlaylists())
fun listActivePlaylists() = println(playlistAPI.listActivePlaylists())
fun listArchivedPlaylists() = println(playlistAPI.listArchivedPlaylists())

fun updatePlaylist() {
    listPlaylists()
    if (playlistAPI.numberOfPlaylists() > 0) {
        // only ask the user to choose the Playlist if Playlists exist
        val id = readNextInt("Enter the id of the Playlist to update: ")
        if (playlistAPI.findPlaylist(id) != null) {
            val playlistTitle = readNextLine("Enter a title for the Playlist: ")
            val playlistPriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val playlistCategory = readNextLine("Enter a category for the Playlist: ")

            // pass the index of the Playlist and the new Playlist details to playlistAPI for updating and check for success.
            if (playlistAPI.update(id, Playlist(0, playlistTitle, playlistPriority, playlistCategory, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no playlists for this index number")
        }
    }
}

fun deletePlaylist() {
    listPlaylists()
    if (playlistAPI.numberOfPlaylists() > 0) {
        // only ask the user to choose the Playlist to delete if Playlists exist
        val id = readNextInt("Enter the id of the Playlist to delete: ")
        // pass the index of the Playlist to playlistAPI for deleting and check for success.
        val playlistToDelete = playlistAPI.delete(id)
        if (playlistToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun archivePlaylist() {
    listActivePlaylists()
    if (playlistAPI.numberOfActivePlaylists() > 0) {
        // only ask the user to choose the Playlist to archive if active Playlists exist
        val id = readNextInt("Enter the id of the playlist to archive: ")
        // pass the index of the Playlist to playlistAPI for archiving and check for success.
        if (playlistAPI.archivePlaylist(id)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

//-------------------------------------------
//Song MENU (only available for active Playlists)
//-------------------------------------------
private fun addSongToPlaylist() {
    val playlist: Playlist? = askUserToChooseActivePlaylist()
    if (playlist != null) {
        if (playlist.addSong(Song(songContents = readNextLine("\t Song Contents: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

fun updateSongContentsInPlaylist() {
    val playlist: Playlist? = askUserToChooseActivePlaylist()
    if (playlist != null) {
        val song: Song? = askUserToChooseSong(playlist)
        if (song != null) {
            val newContents = readNextLine("Enter new contents: ")
            if (playlist.update(song.songId, Song(songContents = newContents))) {
                println("Song contents updated")
            } else {
                println("Song contents NOT updated")
            }
        } else {
            println("Invalid Song Id")
        }
    }
}

fun deleteASong() {
    val playlist: Playlist? = askUserToChooseActivePlaylist()
    if (playlist != null) {
        val song: Song? = askUserToChooseSong(playlist)
        if (song != null) {
            val isDeleted = playlist.delete(song.songId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

fun markSongStatus() {
    val playlist: Playlist? = askUserToChooseActivePlaylist()
    if (playlist != null) {
        val song: Song? = askUserToChooseSong(playlist)
        if (song != null) {
            var changeStatus = 'X'
            if (song.isSongComplete) {
                changeStatus = readNextChar("The song is currently complete...do you want to mark it as TODO?")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    song.isSongComplete = false
            }
            else {
                changeStatus = readNextChar("The song is currently TODO...do you want to mark it as Complete?")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    song.isSongComplete = true
            }
        }
    }
}

//------------------------------------
//playlist REPORTS MENU
//------------------------------------
fun searchPlaylists() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = playlistAPI.searchPlaylistsByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No playlists found")
    } else {
        println(searchResults)
    }
}

//------------------------------------
//Song REPORTS MENU
//------------------------------------
fun searchSongs() {
    val searchContents = readNextLine("Enter the song contents to search by: ")
    val searchResults = playlistAPI.searchSongByContents(searchContents)
    if (searchResults.isEmpty()) {
        println("No songs found")
    } else {
        println(searchResults)
    }
}

fun listToDoSongs(){
    if (playlistAPI.numberOfToDoSongs() > 0) {
        println("Total TODO Songs: ${playlistAPI.numberOfToDoSongs()}")
    }
    println(playlistAPI.listTodoSongs())
}


//------------------------------------
// Exit App
//------------------------------------
fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}

//------------------------------------
//HELPER FUNCTIONS
//------------------------------------

private fun askUserToChooseActivePlaylist(): Playlist? {
    listActivePlaylists()
    if (playlistAPI.numberOfActivePlaylists() > 0) {
        val playlist = playlistAPI.findPlaylist(readNextInt("\nEnter the id of the playlist: "))
        if (playlist != null) {
            if (playlist.isPlaylistArchived) {
                println("Playlist is NOT Active, it is Archived")
            } else {
                return playlist //chosen playlist is active
            }
        } else {
            println("Playlist id is not valid")
        }
    }
    return null //selected playlist is not active
}

private fun askUserToChooseSong(playlist: Playlist): Song? {
    if (playlist.numberOfSongs() > 0) {
        print(playlist.listSongs())
        return playlist.findOne(readNextInt("\nEnter the id of the Song: "))
    }
    else{
        println ("No Songs for chosen playlist")
        return null
    }
}
