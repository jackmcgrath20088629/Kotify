package models

data class Song (var songId: Int = 0,
                 var songTitle : String,
                 var songArtist : String,
                 var isSongComplete: Boolean = false) {

    override fun toString(): String {
        (isSongComplete)
            return "$songId: $songTitle by $songArtist"
    }


}