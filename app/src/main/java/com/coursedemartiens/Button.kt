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

class Button(caseW: Int, caseH: Int, casesX: Int, casesY: Int, x: Int, y: Int) :
    Zone(caseW, caseH, casesX, casesY, x, y) {
    var images: Images
    var gamePlay: Boolean = false
    var gameMenu: Boolean = true
    var gameOver: Boolean = true
    var gameQuit: Boolean = false

    init {
        val imagesId = intArrayOf(
            R.drawable.button_play,
            R.drawable.button_replay,
            R.drawable.button_pause,
            R.drawable.button_resume,
            R.drawable.button_stop,
            R.drawable.button_quit
        )
        images = Images(imagesId, this)
    }

    fun checkGamePlay(caseX: Int, caseY: Int) {
        if (caseX == 0 && caseY == 0) {
            if (gamePlay) {
                gamePlay = false
            } else {
                gamePlay = true
            }
        }
    }

    fun checkGameQuit(caseX: Int, caseY: Int) {
        if (caseX == 1 && caseY == 0) {
            if (!gameMenu) {
                gameMenu = true
            } else {
                gameQuit = true
            }
        }
    }

    fun willGameQuit(xEvent: Int, yEvent: Int): Boolean {
        val result: Boolean

        if (gameMenu && toCaseX(xEvent) == 1 && toCaseY(yEvent) == 0) {
            result = true
        } else {
            result = false
        }

        return result
    }

    fun draw(canvas: Canvas, paint: Paint) {
        var imageIndice: Int

        if (!gameMenu) {
            if (!gameOver) {
                if (gamePlay) {
                    imageIndice = 2
                } else {
                    imageIndice = 3
                }
            } else {
                imageIndice = 1
            }
        } else {
            imageIndice = 0
        }

        images.updateImage(imageIndice)
        images.setCases(0, 0, this)
        images.draw(canvas, paint)

        if (!gameMenu) {
            imageIndice = 4
        } else {
            imageIndice = 5
        }

        images.updateImage(imageIndice)
        images.setCases(1, 0, this)
        images.draw(canvas, paint)
    }
}