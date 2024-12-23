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

class Obstacle(enemy: Enemy, board: Board, scrolling: Scrolling) : Opponent(scrolling) {
    init {
        name = "Obstacle"
        minV = floor(float(enemy.minV) * 9 / 10)
        maxV = floor(float(enemy.maxV) * 9 / 10)

        var imagesId: List<Int> = listOf<Int>(
            R.drawable.obstacle_0_0,
            R.drawable.obstacle_0_1,
            R.drawable.obstacle_0_2,
            R.drawable.obstacle_0_3,
            R.drawable.obstacle_0_4,
            R.drawable.obstacle_0_5,
            R.drawable.obstacle_0_6,
            R.drawable.obstacle_0_7
        )
        addSheet(imagesId, board)
        imagesId = listOf<Int>(
            R.drawable.obstacle_bump_0_0,
            R.drawable.obstacle_bump_0_1,
            R.drawable.obstacle_bump_0_2,
            R.drawable.obstacle_bump_0_3,
            R.drawable.obstacle_bump_0_4,
            R.drawable.obstacle_bump_0_5,
            R.drawable.obstacle_bump_0_6,
            R.drawable.obstacle_bump_0_7,
            R.drawable.obstacle_bump_0_8
        )
        addSheet(imagesId, board)

        imagesId = listOf<Int>(
            R.drawable.obstacle_1_0,
            R.drawable.obstacle_1_1,
            R.drawable.obstacle_1_2,
            R.drawable.obstacle_1_3,
            R.drawable.obstacle_1_4,
            R.drawable.obstacle_1_5,
            R.drawable.obstacle_1_6,
            R.drawable.obstacle_1_7
        )
        addSheet(imagesId, board)
        imagesId = listOf<Int>(
            R.drawable.obstacle_bump_1_0,
            R.drawable.obstacle_bump_1_1,
            R.drawable.obstacle_bump_1_2,
            R.drawable.obstacle_bump_1_3,
            R.drawable.obstacle_bump_1_4,
            R.drawable.obstacle_bump_1_5,
            R.drawable.obstacle_bump_1_6,
            R.drawable.obstacle_bump_1_7,
            R.drawable.obstacle_bump_1_8
        )
        addSheet(imagesId, board)

        imagesId = listOf<Int>(
            R.drawable.obstacle_2_0,
            R.drawable.obstacle_2_1,
            R.drawable.obstacle_2_2,
            R.drawable.obstacle_2_3,
            R.drawable.obstacle_2_4,
            R.drawable.obstacle_2_5,
            R.drawable.obstacle_2_6,
            R.drawable.obstacle_2_7
        )
        addSheet(imagesId, board)
        imagesId = listOf<Int>(
            R.drawable.obstacle_bump_2_0,
            R.drawable.obstacle_bump_2_1,
            R.drawable.obstacle_bump_2_2,
            R.drawable.obstacle_bump_2_3,
            R.drawable.obstacle_bump_2_4,
            R.drawable.obstacle_bump_2_5,
            R.drawable.obstacle_bump_2_6,
            R.drawable.obstacle_bump_2_7,
            R.drawable.obstacle_bump_2_8
        )
        addSheet(imagesId, board)
    }
}