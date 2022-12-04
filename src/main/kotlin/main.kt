import controllers.PlaylistAPI
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import kotlin.system.exitProcess
import models.Playlist
import models.Song
//Imports for text styles and colors by https://github.com/ajalt/mordant
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import mu.KotlinLogging
import persistence.JSONSerializer
import java.io.File
import utils.ValidateInput.readValidGenre
import utils.ValidateInput.readValidRating


//private val playlistAPI = PlaylistAPI()

private val logger = KotlinLogging.logger {}
// private val playlistAPI = PlaylistAPI(XMLSerializer(File("playlists.xml")))
private val playlistAPI = PlaylistAPI(JSONSerializer(File("playlists.json")))

fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> playlistMenu()
            2 -> songMenu()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

val style = black on (rgb("#1DB954")).bg

fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |                 Welcome to Kotify                 |
         > -----------------------------------------------------  
         > |   1) PLAYLIST MENU                                |
         > |   2) SONGS MENU                                   |
	     > -----------------------------------------------------
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)

//----------------------------------//
//PLAYLIST MENUS                    //
//----------------------------------//
fun playlistMenu() {
        val option = readNextInt(
            """
                  > ------------------------------------
                  > |          PLAYLIST MENU           |
                  > ------------------------------------
                  > |   1) Create a playlist           |
                  > |   2) Update a playlist           |
                  > |   3) Delete a playlist           |
                  > |   4) View playlists              |
                  > |   5) Download a playlist         |
                  > ------------------------------------
                  >     0) Back                        |
                  > ------------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> addPlaylist()
            2 -> updatePlaylist()
            3 -> deletePlaylist()
            4 -> viewPlaylistMenu()
            5 -> downloadPlaylist()
            0 -> mainMenu()
            else -> println("Invalid option entered: $option")
        }
    }
fun viewPlaylistMenu() {
    if (playlistAPI.numberOfPlaylists() > 0) {
        val option = readNextInt(
            """
                  > ------------------------------------
                  > |          PLAYLIST MENU           |
                  > ------------------------------------ 
                  > |   1) Search for a playlist       | 
                  > |   2) View all Playlists          |
                  > |   3) View all downloaded         |
                  > |   4) View playlists by rating    |
                  > |   5) View playlist by genre      |
                  > ------------------------------------
                  > |   6) Count all playlists         |
                  > |   7) Count by genre              |
                  > |   8) Count by playlist rating    |
                  > |   9) Count downloaded playlists  |
                  > ------------------------------------
                  > |    0) Back                       |
                  > ------------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> searchPlaylistsByTitle() //search for a playlist
            2 -> listAllPlaylists() // list all
            3 -> listDownloadedPlaylists() // view by status of downloaded or not (downloaded)
            4 -> listRating() //view playlists by rating (rating)
            5 -> searchPlaylistsByGenre() //search for a playlist by genre
            //Counts
            6 -> countAllPlaylists() //counts all playlists created
            7 -> listGenres() //counts playlists per genre
            8 -> countByRating() //count playlist by rating
           // 9 -> countDownloaded() //counts amount of playlists that are downloaded
            0 -> playlistMenu() //back
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No playlists stored")
    }
}

//----------------------------------//
//SONG MENUS                        //
//----------------------------------//
fun songMenu() {
        val option = readNextInt(
            """
                  > ------------------------------------
                  > |           SONG MENU              |
                  > ------------------------------------
                  > |   1) Add song a playlist         |
                  > |   2) Update a songs information  |
                  > |   3) Delete a song               |
                  > |   4) View songs                  |
                  > ------------------------------------
                  > |    0) Back                       |
                  > ------------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> addSongToPlaylist() //adds a song
           // 2 -> updateSong() //updates the contents of a song
           // 3 -> deleteSong() //deletes a song
           // 4 -> viewSongMenu() //view menu of songs for listing and counting
            0 -> mainMenu()
            else -> println("Invalid option entered: $option")
        }
    }
fun viewSongMenu() {
    if (playlistAPI.numberOfPlaylists() > 0) {
        val option = readNextInt(
            """
                  > ------------------------------------
                  > |           SONG MENU              |
                  > ------------------------------------
                  > |   1) Search for a Song           | 
                  > |   2) View all songs              |
                  > |   3) View all downloaded         |
                  > |   4) View song by artist         |
                  > |   5) View songs by genre         |
                  > ------------------------------------
                  > |    0) Back                       |
                  > ------------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
           // 1 -> () //Search for a song
           // 2 -> () // list all songs
           // 3 -> ()
           // 4 -> () //view song by artist
           // 5 -> () //view songs by genre
            0 -> songMenu()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No playlists stored")
    }
}

//----------------------------------//
//PLAYLIST FEATURES                 //
//----------------------------------//

fun addPlaylist() {
    val playlistTitle = readNextLine("Enter playlists title: ")
    val playlistRating = readValidRating("Enter numerical rating  (from ☆ - ☆☆☆☆☆): ")
    val playlistGenre = readValidGenre("Enter a genre for the playlist: ")
    val isAdded = playlistAPI.add(Playlist(playlistTitle = playlistTitle, playlistRating = playlistRating, playlistGenre = playlistGenre))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listRating() {
    if (playlistAPI.numberOfPlaylists() > 0) {
        val option = readNextInt(
            """
                  > ------------------------------------------------------
                  > |   Please enter a rating number (1 to 5)            |
                  > |   1) - ☆                                           |
                  > |   2) - ☆☆                                          |
                  > |   3) - ☆☆☆                                        |
                  > |   4) - ☆☆☆☆                                       |
                  > |   5) - ☆☆☆☆☆                                      |
                  > ------------------------------------------------------
         > ==>> """.trimMargin(">")
            )

        when (option) {
            1 -> println(playlistAPI.listPlaylistsBySelectedRating(1))
            2 -> println(playlistAPI.listPlaylistsBySelectedRating(2))
            3 -> println(playlistAPI.listPlaylistsBySelectedRating(3))
            4 -> println(playlistAPI.listPlaylistsBySelectedRating(4))
            5 -> println(playlistAPI.listPlaylistsBySelectedRating(5))

            else -> println("Invalid option entered: " + option)
        }
    } else {
        println("Option Invalid - No playlists stored")
    }
}

fun listAllPlaylists() = println(playlistAPI.listAllPlaylists())
fun listActivePlaylists() = println(playlistAPI.listActivePlaylists()) //needed for downloaded
fun listDownloadedPlaylists() = println(playlistAPI.listDownloadedPlaylists()) //downloaded

//Counting
fun countAllPlaylists() = println(playlistAPI.numberOfPlaylists())
fun countAllHipHop() = println(playlistAPI.numberOfHipHop())
fun countAllPop() = println(playlistAPI.numberOfPop())
fun countAllRock() = println(playlistAPI.numberOfRock())
fun countAllJazz() = println(playlistAPI.numberOfJazz())
fun countAllRnB() = println(playlistAPI.numberOfRnB())
fun countAllOther() = println(playlistAPI.numberOfOther())

fun countByRating() {
    if (playlistAPI.numberOfPlaylists() > 0) {
        val option = readNextInt(
            """
                  > ------------------------------------------------------
                  > |   Please enter a rating number (1 to 5)            |
                  > |   1) - ☆                                           |
                  > |   2) - ☆☆                                          |
                  > |   3) - ☆☆☆                                        |
                  > |   4) - ☆☆☆☆                                       |
                  > |   5) - ☆☆☆☆☆                                      |
                  > ------------------------------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> println(playlistAPI.numberOfPlaylistsByRating(1))
            2 -> println(playlistAPI.numberOfPlaylistsByRating(2))
            3 -> println(playlistAPI.numberOfPlaylistsByRating(3))
            4 -> println(playlistAPI.numberOfPlaylistsByRating(4))
            5 -> println(playlistAPI.numberOfPlaylistsByRating(5))


            else -> println("Invalid option entered: " + option)
        }
    } else {
        println("Option Invalid - No playlists stored under this rating")
    }
}

fun listGenres() {
    if (playlistAPI.numberOfPlaylists() > 0) {
        val option = readNextInt(
            """
                  > ------------------------------------------
                  > |   Please select a genre                |
                  > |   1) - Hip-Hop                         |
                  > |   2) - Pop                             |
                  > |   3) - Rock                            |
                  > |   4) - Jazz                            |
                  > |   5) - R&B                             |
                  > |   6) - Other                           |
                  > ------------------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> println(countAllHipHop())
            2 -> println(countAllPop())
            3 -> println(countAllRock())
            4 -> println(countAllJazz())
            5 -> println(countAllRnB())
            6 -> println(countAllOther())

            else -> println("Invalid option entered: " + option)
        }
    } else {
        println("Option Invalid - No playlists stored under this genre")
    }
}

fun updatePlaylist() {
    listAllPlaylists()
    if (playlistAPI.numberOfPlaylists() > 0) {
        // only ask the user to choose the Playlist if Playlists exist
        val id = readNextInt("Enter the ID of the Playlist to update: ")
        if (playlistAPI.findPlaylist(id) != null) {
            val playlistTitle = readNextLine("Enter a title for the Playlist: ")
            val playlistRating = readValidRating("Enter a rating (☆ - ☆☆☆☆☆): ")
            val playlistGenre = readValidGenre("Enter a genre for the Playlist: ")

            // pass the index of the Playlist and the new Playlist details to playlistAPI for updating and check for success.
            if (playlistAPI.updatePlaylist(id, Playlist(0, playlistTitle, playlistRating, playlistGenre, false))){
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
    listAllPlaylists()
    if (playlistAPI.numberOfPlaylists() > 0) {
        // only ask the user to choose the Playlist to delete if Playlists exist
        val id = readNextInt("Enter the id of the Playlist to delete: ")
        // pass the index of the Playlist to playlistAPI for deleting and check for success.
        val playlistToDelete = playlistAPI.deletePlaylist(id)
        if (playlistToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}


fun downloadPlaylist() { //download
    listActivePlaylists()
    if (playlistAPI.numberOfActivePlaylists() > 0) {
        // only ask the user to choose the Playlist to download if active Playlists exist
        val id = readNextInt("Enter the id of the playlist to download: ")
        // pass the index of the Playlist to playlistAPI for downloading and check for success.
        if (playlistAPI.downloadPlaylist(id)) {
            println("Download Successful!")
        } else {
            println("Download NOT Successful")
        }
    }
}

//-------------------------------------------
//Song MENU (only available for active Playlists)
//-------------------------------------------
private fun addSongToPlaylist() {
    val playlist: Playlist? = askUserToChooseActivePlaylist()
    if (playlist != null) {
        if (playlist.addSong(Song(songTitle = readNextLine("\t Song Title: "), songArtist = readNextLine("\t Song Artist: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

fun updatesongTitleInPlaylist() {
    val playlist: Playlist? = askUserToChooseActivePlaylist()
    if (playlist != null) {
        val song: Song? = askUserToChooseSong(playlist)
        if (song != null) {
            val newTitle = readNextLine("Enter new title: ")
            val newArtist = readNextLine("Enter new artist: ")
            if (playlist.update(song.songId, Song(songTitle = newTitle, songArtist = newArtist))) {
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
fun searchPlaylistsByTitle() {
    val searchTitle = readNextLine("Enter the title of desired playlist: ")
    val searchResults = playlistAPI.searchPlaylistsByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No playlists found")
    } else {
        println(searchResults)
    }
}

fun searchPlaylistsByGenre() {
    val searchGenre = readNextLine("Enter the genre you wish to search for: ")
    val searchResults = playlistAPI.searchPlaylistsByGenre(searchGenre)
    if (searchResults.isEmpty()) {
        println("No playlists found under this Genre.")
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
            if (playlist.isPlaylistDownloaded) {
                println("Playlist is NOT Active, it is Downloaded")
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

