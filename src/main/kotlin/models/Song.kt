package models

data class Song (var songId: Int = 0, var songContents : String, var isSongComplete: Boolean = false){

    override fun toString(): String {
        //TODO Lift Return out in labs
        if (isSongComplete)
            return "$songId: $songContents (Complete)"
         else
            return "$songId: $songContents (TODO)"
    }

}