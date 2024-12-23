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

import android.graphics.Bitmap

class Egg {
    companion object {
        var egg: Boolean = false
        var time1: Long = long(0)
        var time2: Long = long(0)

        fun set(player: Player, enemy: Enemy, obstacle: Obstacle, text: Text, board: Board) {
            var imagesId = listOf<Int>(
                R.drawable.egg_player_0,
                R.drawable.egg_player_1,
                R.drawable.egg_player_2
            )
            player.addSheet(imagesId, board)

            imagesId = listOf<Int>(
                R.drawable.egg_opponent_0_0,
                R.drawable.egg_opponent_0_1,
                R.drawable.egg_opponent_0_2,
                R.drawable.egg_opponent_0_3,
            )
            enemy.addSheet(imagesId, board)
            obstacle.addSheet(imagesId, board)
            imagesId = listOf<Int>(
                R.drawable.egg_opponent_bump_0,
            )
            enemy.addSheet(imagesId, board)
            obstacle.addSheet(imagesId, board)

            imagesId = listOf<Int>(
                R.drawable.egg_opponent_1_0,
                R.drawable.egg_opponent_1_1,
                R.drawable.egg_opponent_1_2,
                R.drawable.egg_opponent_1_3,
            )
            enemy.addSheet(imagesId, board)
            obstacle.addSheet(imagesId, board)
            imagesId = listOf<Int>(
                R.drawable.egg_opponent_bump_1,
            )
            enemy.addSheet(imagesId, board)
            obstacle.addSheet(imagesId, board)

            imagesId = listOf<Int>(
                R.drawable.egg_opponent_2_0,
                R.drawable.egg_opponent_2_1,
                R.drawable.egg_opponent_2_2,
                R.drawable.egg_opponent_2_3,
            )
            enemy.addSheet(imagesId, board)
            obstacle.addSheet(imagesId, board)
            imagesId = listOf<Int>(
                R.drawable.egg_opponent_bump_2,
            )
            enemy.addSheet(imagesId, board)
            obstacle.addSheet(imagesId, board)

            text.images.images.add(Utility.createImage(R.drawable.text_egg_title_1, text))
            text.images.images.add(Utility.createImage(R.drawable.text_egg_title_2, text))
        }

        fun check(player: Player, enemy: Enemy, obstacle: Obstacle, text: Text) {
            val time: Long = System.currentTimeMillis()

            var log: String = ""

            if (time1 == long(0)
                || (time2 == long(0) && time - time1 > Utility.reflexTime)
                || (time2 != long(0) && time - time2 > Utility.reflexTime)
            ) {
                log += "time1 "

                if (time1 == long(0)) {
                    log += "(first)"
                } else if (time2 == long(0)) {
                    log += "(fail after time1>" + (time - time1) + ")"
                } else {
                    log += "(fail after time2>" + (time - time2) + ")"
                }

                time1 = time
                time2 = long(0)
            } else if (time2 == long(0)) {
                time2 = time

                log += "set time2 (after time1<" + (time - time1) + ")"
            } else {
                egg = !egg
                switch(player, enemy, obstacle)
                switchText(text)

                log += "set egg=" + egg + " (after time2<" + (time - time2) + ")"

                time1 = long(0)
                time2 = long(0)
            }

            Test.PRINT_DEBUG("Egg.check", log)
        }

        fun switch(player: Player, enemy: Enemy, obstacle: Obstacle) {
            switchPlayer(player)
            switchOpponent(enemy)
            switchOpponent(obstacle)
        }

        fun switchPlayer(player: Player) {
            if (egg) {
                player.sheetIndice = 1
            } else {
                player.sheetIndice = 0
            }

            player.setImageFromSheet(player.getImageIndice())
        }

        fun switchOpponent(opponent: Opponent) {
            if (egg) {
                if (opponent.sheetIndice < 6) {
                    opponent.sheetIndice += 6
                }
            } else {
                if (opponent.sheetIndice >= 6) {
                    opponent.sheetIndice -= 6
                }
            }

            opponent.setImageFromSheet(opponent.getImageIndice())
        }

        fun switchText(text: Text) {
            val previousTitle1: Bitmap = text.images.images.removeAt(2)
            val previousTitle2: Bitmap = text.images.images.removeAt(2)
            text.images.images.add(previousTitle1)
            text.images.images.add(previousTitle2)
        }
    }
}