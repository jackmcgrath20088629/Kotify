package models

data class Song (var songId: Int = 0,
                 var songTitle : String,
                 var songArtist : String,
                 var isSongFavoured: Boolean = false) {

    override fun toString(): String {
        if (isSongFavoured)
            return "$songId: $songTitle by $songArtist (Favourited)"
        else
            return "$songId: $songTitle by $songArtist"
    }


}

