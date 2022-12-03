package controllers

import models.Playlist
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals

class PlaylistAPITest {

    private var Summer: Playlist? = null
    private var Winter: Playlist? = null
    private var Easter: Playlist? = null
    private var Chritmas: Playlist? = null
    private var Halloween: Playlist? = null
    private var populatedPlaylists: PlaylistAPI? = PlaylistAPI(XMLSerializer(File("playlists.xml")))
    private var emptyPlaylists: PlaylistAPI? = PlaylistAPI(XMLSerializer(File("playlists.xml")))

    @BeforeEach
    fun setup() {
        Summer = Playlist(0, "Summer Holiday", 2, "Hip-Hop",false)
        Winter = Playlist(1, "Winter Holiday", 3, "Other",false)
        Easter = Playlist(2, "Easter Holiday", 4, "Jazz", true)
        Chritmas = Playlist(3, "Christmas Holiday", 5, "R&B", false)
        Halloween = Playlist(4, "Halloween Party", 1, "Pop", true)

        // adding 5 Playlist to the playlists api
        populatedPlaylists!!.add(Summer!!)
        populatedPlaylists!!.add(Winter!!)
        populatedPlaylists!!.add(Easter!!)
        populatedPlaylists!!.add(Chritmas!!)
        populatedPlaylists!!.add(Halloween!!)
    }

    @AfterEach
    fun tearDown() {
        Summer = null
        Winter = null
        Easter = null
        Chritmas = null
        Halloween = null
        populatedPlaylists = null
        emptyPlaylists = null
    }

    @Nested
    inner class AddPlaylists {
        @Test
        fun `adding a Playlist to a populated list adds to ArrayList`() {
            val newPlaylist = Playlist(5, "Spring Playlist", 5, "Other", false)
            assertEquals(5, populatedPlaylists!!.numberOfPlaylists())
            assertTrue(populatedPlaylists!!.add(newPlaylist))
            assertEquals(6, populatedPlaylists!!.numberOfPlaylists())
            assertEquals(newPlaylist, populatedPlaylists!!.findPlaylist(populatedPlaylists!!.numberOfPlaylists() - 1))
        }

        @Test
        fun `adding a Playlist to an empty list adds to ArrayList`() {
            val newPlaylist = Playlist(5, "Spring Playlist", 5, "Other", false)
            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())
            assertTrue(emptyPlaylists!!.add(newPlaylist))
            assertEquals(1, emptyPlaylists!!.numberOfPlaylists())
            assertEquals(newPlaylist, emptyPlaylists!!.findPlaylist(emptyPlaylists!!.numberOfPlaylists() - 1))
        }
    }

    @Nested
    inner class ListPlaylists {

        @Test
        fun `listAllPlaylists returns No Playlists Stored message when ArrayList is empty`() {
            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())
            assertTrue(emptyPlaylists!!.listAllPlaylists().lowercase().contains("no playlists"))
        }

        @Test
        fun `listAllPlaylists returns Playlists when ArrayList has playlists stored`() {
            assertEquals(5, populatedPlaylists!!.numberOfPlaylists())
            val playlistsString = populatedPlaylists!!.listAllPlaylists().lowercase()
            assertTrue(playlistsString.contains("learning kotlin"))
            assertTrue(playlistsString.contains("code app"))
            assertTrue(playlistsString.contains("test app"))
            assertTrue(playlistsString.contains("Halloween"))
            assertTrue(playlistsString.contains("summer holiday"))
        }

        @Test
        fun `listActivePlaylists returns no active playlists stored when ArrayList is empty`() {
            assertEquals(0, emptyPlaylists!!.numberOfActivePlaylists())
            assertTrue(
                emptyPlaylists!!.listActivePlaylists().lowercase().contains("no active playlists")
            )
        }

        @Test
        fun `listActivePlaylists returns active playlists when ArrayList has active playlists stored`() {
            assertEquals(3, populatedPlaylists!!.numberOfActivePlaylists())
            val activePlaylistsString = populatedPlaylists!!.listActivePlaylists().lowercase()
            assertTrue(activePlaylistsString.contains("learning kotlin"))
            assertFalse(activePlaylistsString.contains("code app"))
            assertTrue(activePlaylistsString.contains("summer holiday"))
            assertTrue(activePlaylistsString.contains("test app"))
            assertFalse(activePlaylistsString.contains("Halloween"))
        }

        @Test
        fun `listArchivedPlaylists returns no archived playlists when ArrayList is empty`() {
            assertEquals(0, emptyPlaylists!!.numberOfArchivedPlaylists())
            assertTrue(
                emptyPlaylists!!.listArchivedPlaylists().lowercase().contains("no archived playlists")
            )
        }

        @Test
        fun `listArchivedPlaylists returns archived playlists when ArrayList has archived playlists stored`() {
            assertEquals(2, populatedPlaylists!!.numberOfArchivedPlaylists())
            val archivedPlaylistsString = populatedPlaylists!!.listArchivedPlaylists().lowercase()
            assertFalse(archivedPlaylistsString.contains("learning kotlin"))
            assertTrue(archivedPlaylistsString.contains("code app"))
            assertFalse(archivedPlaylistsString.contains("summer holiday"))
            assertFalse(archivedPlaylistsString.contains("test app"))
            assertTrue(archivedPlaylistsString.contains("Halloween"))
        }

        @Test
        fun `listPlaylistsBySelectedRating returns No Playlists when ArrayList is empty`() {
            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())
            assertTrue(
                emptyPlaylists!!.listPlaylistsBySelectedRating(1).lowercase().contains("no playlists")
            )
        }

        @Test
        fun `listPlaylistsBySelectedRating returns no playlists when no playlists of that priority exist`() {
            // Rating 1 (1 playlist), 2 (none), 3 (1 playlist). 4 (2 playlists), 5 (1 playlist)
            assertEquals(5, populatedPlaylists!!.numberOfPlaylists())
            val priority2String = populatedPlaylists!!.listPlaylistsBySelectedRating(2).lowercase()
            assertTrue(priority2String.contains("no playlists"))
            assertTrue(priority2String.contains("2"))
        }

        @Test
        fun `listPlaylistsBySelectedRating returns all playlists that match that priority when playlists of that priority exist`() {
            // Rating 1 (1 playlist), 2 (none), 3 (1 playlist). 4 (2 playlists), 5 (1 playlist)
            assertEquals(5, populatedPlaylists!!.numberOfPlaylists())
            val priority1String = populatedPlaylists!!.listPlaylistsBySelectedRating(1).lowercase()
            assertTrue(priority1String.contains("1 playlist"))
            assertTrue(priority1String.contains("priority 1"))
            assertTrue(priority1String.contains("summer holiday"))
            assertFalse(priority1String.contains("Halloween"))
            assertFalse(priority1String.contains("learning kotlin"))
            assertFalse(priority1String.contains("code app"))
            assertFalse(priority1String.contains("test app"))

            val priority4String = populatedPlaylists!!.listPlaylistsBySelectedRating(4).lowercase()
            assertTrue(priority4String.contains("2 playlist"))
            assertTrue(priority4String.contains("priority 4"))
            assertFalse(priority4String.contains("Halloween"))
            assertTrue(priority4String.contains("code app"))
            assertTrue(priority4String.contains("test app"))
            assertFalse(priority4String.contains("learning kotlin"))
            assertFalse(priority4String.contains("summer holiday"))
        }


    }

    @Nested
    inner class DeletePlaylists {

        @Test
        fun `deleting a Playlist that does not exist, returns null`() {
            assertNull(emptyPlaylists!!.deletePlaylist(0))
            assertNull(populatedPlaylists!!.deletePlaylist(-1))
            assertNull(populatedPlaylists!!.deletePlaylist(5))
        }

      /*  @Test
        fun `deleting a playlist that exists delete and returns deleted object`() {
            assertEquals(5, populatedPlaylists!!.numberOfPlaylists())
            assertEquals(Halloween, populatedPlaylists!!.deletePlaylist(4))
            assertEquals(4, populatedPlaylists!!.numberOfPlaylists())
            assertEquals(Summer, populatedPlaylists!!.deletePlaylist(0))
            assertEquals(3, populatedPlaylists!!.numberOfPlaylists())
        } */
    }

    @Nested
    inner class UpdatePlaylists {
        @Test
        fun `updating a playlist that does not exist returns false`() {
            assertFalse(populatedPlaylists!!.updatePlaylist(6, Playlist(6, "Autumn Playlist", 3, "Other", false)))
            assertFalse(populatedPlaylists!!.updatePlaylist(-1, Playlist(6, "Autumn Playlist", 3, "Other", false)))
            assertFalse(emptyPlaylists!!.updatePlaylist(0, Playlist(6, "Autumn Playlist", 3, "Other", false)))
        }

        @Test
        fun `updating a playlist that exists returns true and updates`() {
            // check playlist 5 exists and check the contents
            assertEquals(Halloween, populatedPlaylists!!.findPlaylist(4))
            assertEquals("Swim - Pool", populatedPlaylists!!.findPlaylist(4)!!.playlistTitle)
            assertEquals(3, populatedPlaylists!!.findPlaylist(4)!!.playlistRating)
            assertEquals("Hobby", populatedPlaylists!!.findPlaylist(4)!!.playlistGenre)

            // update playlist 5 with new information and ensure contents updated successfully
            assertTrue(populatedPlaylists!!.updatePlaylist(4, Playlist(4, "Halloween Party", 1, "Pop",  false)))
            assertEquals("Updating Playlist", populatedPlaylists!!.findPlaylist(4)!!.playlistTitle)
            assertEquals(2, populatedPlaylists!!.findPlaylist(4)!!.playlistRating)
            assertEquals("College", populatedPlaylists!!.findPlaylist(4)!!.playlistGenre)
        }
    }

    @Nested
    inner class ArchivePlaylists {
        @Test
        fun `archiving a playlist that does not exist returns false`() {
            assertFalse(populatedPlaylists!!.archivePlaylist(6))
            assertFalse(populatedPlaylists!!.archivePlaylist(-1))
            assertFalse(emptyPlaylists!!.archivePlaylist(0))
        }

        @Test
        fun `archiving an already archived playlist returns false`() {
            assertTrue(populatedPlaylists!!.findPlaylist(2)!!.isPlaylistArchived)
            assertFalse(populatedPlaylists!!.archivePlaylist(2))
        }

        @Test
        fun `archiving an active playlist that exists returns true and archives`() {
            assertFalse(populatedPlaylists!!.findPlaylist(1)!!.isPlaylistArchived)
            assertTrue(populatedPlaylists!!.archivePlaylist(1))
            assertTrue(populatedPlaylists!!.findPlaylist(1)!!.isPlaylistArchived)
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty playlists.XML file.
            val storingPlaylists = PlaylistAPI(XMLSerializer(File("playlists.xml")))
            storingPlaylists.store()

            // Loading the empty playlists.xml file into a new object
            val loadedPlaylists = PlaylistAPI(XMLSerializer(File("playlists.xml")))
            loadedPlaylists.load()

            // Comparing the source of the playlists (storingPlaylists) with the XML loaded playlists (loadedPlaylists)
            assertEquals(0, storingPlaylists.numberOfPlaylists())
            assertEquals(0, loadedPlaylists.numberOfPlaylists())
            assertEquals(storingPlaylists.numberOfPlaylists(), loadedPlaylists.numberOfPlaylists())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 playlists to the playlists.XML file.
            val storingPlaylists = PlaylistAPI(XMLSerializer(File("playlists.xml")))
            storingPlaylists.add(Chritmas!!)
            storingPlaylists.add(Halloween!!)
            storingPlaylists.add(Winter!!)
            storingPlaylists.store()

            // Loading playlists.xml into a different collection
            val loadedPlaylists = PlaylistAPI(XMLSerializer(File("playlists.xml")))
            loadedPlaylists.load()

            // Comparing the source of the playlists (storingPlaylists) with the XML loaded playlists (loadedPlaylists)
            assertEquals(3, storingPlaylists.numberOfPlaylists())
            assertEquals(3, loadedPlaylists.numberOfPlaylists())
            assertEquals(storingPlaylists.numberOfPlaylists(), loadedPlaylists.numberOfPlaylists())
            assertEquals(storingPlaylists.findPlaylist(0), loadedPlaylists.findPlaylist(0))
            assertEquals(storingPlaylists.findPlaylist(1), loadedPlaylists.findPlaylist(1))
            assertEquals(storingPlaylists.findPlaylist(2), loadedPlaylists.findPlaylist(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty playlists.json file.
            val storingPlaylists = PlaylistAPI(JSONSerializer(File("playlists.json")))
            storingPlaylists.store()

            // Loading the empty playlists.json file into a new object
            val loadedPlaylists = PlaylistAPI(JSONSerializer(File("playlists.json")))
            loadedPlaylists.load()

            // Comparing the source of the playlists (storingPlaylists) with the json loaded playlists (loadedPlaylists)
            assertEquals(0, storingPlaylists.numberOfPlaylists())
            assertEquals(0, loadedPlaylists.numberOfPlaylists())
            assertEquals(storingPlaylists.numberOfPlaylists(), loadedPlaylists.numberOfPlaylists())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 playlists to the playlists.json file.
            val storingPlaylists = PlaylistAPI(JSONSerializer(File("playlists.json")))
            storingPlaylists.add(Chritmas!!)
            storingPlaylists.add(Halloween!!)
            storingPlaylists.add(Winter!!)
            storingPlaylists.store()

            // Loading playlists.json into a different collection
            val loadedPlaylists = PlaylistAPI(JSONSerializer(File("playlists.json")))
            loadedPlaylists.load()

            // Comparing the source of the playlists (storingPlaylists) with the json loaded playlists (loadedPlaylists)
            assertEquals(3, storingPlaylists.numberOfPlaylists())
            assertEquals(3, loadedPlaylists.numberOfPlaylists())
            assertEquals(storingPlaylists.numberOfPlaylists(), loadedPlaylists.numberOfPlaylists())
            assertEquals(storingPlaylists.findPlaylist(0), loadedPlaylists.findPlaylist(0))
            assertEquals(storingPlaylists.findPlaylist(1), loadedPlaylists.findPlaylist(1))
            assertEquals(storingPlaylists.findPlaylist(2), loadedPlaylists.findPlaylist(2))
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfPlaylistsCalculatedCorrectly() {
            assertEquals(5, populatedPlaylists!!.numberOfPlaylists())
            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())
        }

        @Test
        fun numberOfArchivedPlaylistsCalculatedCorrectly() {
            assertEquals(2, populatedPlaylists!!.numberOfArchivedPlaylists())
            assertEquals(0, emptyPlaylists!!.numberOfArchivedPlaylists())
        }

        @Test
        fun numberOfActivePlaylistsCalculatedCorrectly() {
            assertEquals(3, populatedPlaylists!!.numberOfActivePlaylists())
            assertEquals(0, emptyPlaylists!!.numberOfActivePlaylists())
        }

        @Test
        fun numberOfPlaylistsByRatingCalculatedCorrectly() {
            assertEquals(1, populatedPlaylists!!.numberOfPlaylistsByRating(1))
            assertEquals(0, populatedPlaylists!!.numberOfPlaylistsByRating(2))
            assertEquals(1, populatedPlaylists!!.numberOfPlaylistsByRating(3))
            assertEquals(2, populatedPlaylists!!.numberOfPlaylistsByRating(4))
            assertEquals(1, populatedPlaylists!!.numberOfPlaylistsByRating(5))
            assertEquals(0, emptyPlaylists!!.numberOfPlaylistsByRating(1))
        }
    }

    @Nested
    inner class SearchMethods {

      /*  @Test
        fun `search playlists by title returns no playlists when no playlists with that title exist`() {
            // Searching a populated collection for a title that doesn't exist.
            assertEquals(5, populatedPlaylists!!.numberOfPlaylists())
            val searchResults = populatedPlaylists!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            // Searching an empty collection
            assertEquals(0, emptyPlaylists!!.numberOfPlaylists())
            assertTrue(emptyPlaylists!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search playlists by title returns playlists when playlists with that title exist`() {
            assertEquals(5, populatedPlaylists!!.numberOfPlaylists())

            // Searching a populated collection for a full title that exists (case matches exactly)
            var searchResults = populatedPlaylists!!.searchByTitle("Code App")
            assertTrue(searchResults.contains("Code App"))
            assertFalse(searchResults.contains("Test App"))

            // Searching a populated collection for a partial title that exists (case matches exactly)
            searchResults = populatedPlaylists!!.searchByTitle("App")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))

            // Searching a populated collection for a partial title that exists (case doesn't match)
            searchResults = populatedPlaylists!!.searchByTitle("aPp")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))
        }*/
    }
}