package utils

import utils.ScannerInput.readNextInt
import java.util.Scanner

object ValidateInput {

    @JvmStatic
    fun readValidGenre(prompt: String?): String {
        print(prompt)
        var input = Scanner(System.`in`).nextLine()
        do {
            if (GenreUtility.isValidGenre(input))
                return input
            else {
                print("Invalid genre $input.  Please use one of the following titles, 'Hip-Hop', 'R&B', 'Pop', 'Rock' or 'Other': ")
                input = Scanner(System.`in`).nextLine()
            }
        } while (true)
    }
}