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

import android.graphics.Canvas
import android.graphics.Paint

class Number(caseW: Int, caseH: Int, casesX: Int, casesY: Int, x: Int, y: Int) :
    Zone(caseW, caseH, casesX, casesY, x, y) {
    var images: Images

    init {
        val imagesId = intArrayOf(
            R.drawable.number_0,
            R.drawable.number_1,
            R.drawable.number_2,
            R.drawable.number_3,
            R.drawable.number_4,
            R.drawable.number_5,
            R.drawable.number_6,
            R.drawable.number_7,
            R.drawable.number_8,
            R.drawable.number_9,
            R.drawable.number_minute,
            R.drawable.number_second,
            R.drawable.number_space,
            R.drawable.number_slash,
        )
        images = Images(imagesId, this)
    }

    fun draw(canvas: Canvas, paint: Paint) {
        val gameScore: String = getGameScore()

        for (caseX in 0..6) {
            var imageIndice: Int

            if (caseX == 0) {
                imageIndice = gameScore[0].digitToInt()
            } else if (caseX == 1) {
                imageIndice = gameScore[1].digitToInt()
            } else if (caseX == 2) {
                imageIndice = 12
            } else if (caseX == 3) {
                imageIndice = 13
            } else if (caseX == 4) {
                imageIndice = 12
            } else if (caseX == 5) {
                imageIndice = gameScore[5].digitToInt()
            } else {
                imageIndice = gameScore[6].digitToInt()
            }

            images.updateImage(imageIndice)
            images.setCases(caseX, 1, this)
            images.x += floor(float(caseW * 5) / 2)
            images.drawOnZone(this, canvas, paint)
        }

        val gameTime: String = getGameTime()

        for (caseX in 0..6) {
            var imageIndice: Int

            if (caseX == 0) {
                imageIndice = gameTime[0].digitToInt()
            } else if (caseX == 1) {
                imageIndice = 10
            } else if (caseX == 2) {
                imageIndice = gameTime[2].digitToInt()
            } else if (caseX == 3) {
                imageIndice = gameTime[3].digitToInt()
            } else if (caseX == 5) {
                imageIndice = gameTime[5].digitToInt()
            } else if (caseX == 6) {
                imageIndice = gameTime[6].digitToInt()
            } else {
                imageIndice = 11
            }

            images.updateImage(imageIndice)
            images.setCases(caseX, 5, this)
            images.x += floor(float(caseW * 5) / 2)
            images.drawOnZone(this, canvas, paint)
        }
    }

    fun getGameScore(): String {
        return String.format("%02d", floor(Opponent.getScore() * 10)) + " / 10"
    }

    fun getGameTime(): String {
        val gameTime: Int = Utility.gameTicks * Utility.frameTime
        val minutes: String
        val seconds: String
        val centiseconds: String

        if (gameTime >= 10 * 60 * 1000) {
            minutes = "9"
            seconds = "59"
            centiseconds = "99"
        } else {
            minutes = String.format("%1d", gameTime / 60000)
            seconds = String.format("%02d", (gameTime % 60000) / 1000)
            centiseconds = String.format("%02d", (gameTime % 1000) / 10)
        }

        return minutes + "'" + seconds + "\"" + centiseconds
    }
}