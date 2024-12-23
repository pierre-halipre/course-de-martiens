/*
 * Copyright 2024 Pierre Halipré
 *
 * This file is part of Course de martiens.
 *
 * Course de martiens is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Course de martiens is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Course de martiens.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.coursedemartiens

class Place(board: Board) : Sprite(false, true) {
    init {
        var imagesId = listOf<Int>(
            R.drawable.place_0,
            R.drawable.place_1,
            R.drawable.place_2,
            R.drawable.place_3,
            R.drawable.place_4,
            R.drawable.place_5,
            R.drawable.place_6,
            R.drawable.place_7
        )
        addSheet(imagesId, board)
        imagesId = listOf<Int>(
            R.drawable.place_move_0,
            R.drawable.place_move_1,
            R.drawable.place_move_2,
            R.drawable.place_move_3,
            R.drawable.place_move_4,
            R.drawable.place_move_5,
            R.drawable.place_move_6,
            R.drawable.place_move_7
        )
        addSheet(imagesId, board)
    }
}