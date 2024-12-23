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

class Background(caseW: Int, caseH: Int, casesX: Int, casesY: Int, x: Int, y: Int) :
    Zone(caseW, caseH, casesX, casesY, x, y) {
    var images: Images

    init {
        val imagesId = intArrayOf(
            R.drawable.background_border_top_left,
            R.drawable.background_border_top,
            R.drawable.background_border_top_right,
            R.drawable.background_border_right,
            R.drawable.background_border_bottom_right,
            R.drawable.background_border_bottom,
            R.drawable.background_border_bottom_left,
            R.drawable.background_border_left,
            R.drawable.background_interior_top_left,
            R.drawable.background_interior_top,
            R.drawable.background_interior_top_right,
            R.drawable.background_interior_right,
            R.drawable.background_interior_bottom_right,
            R.drawable.background_interior_bottom,
            R.drawable.background_interior_bottom_left,
            R.drawable.background_interior_left,
            R.drawable.background_interior_center
        )
        images = Images(imagesId, this)
    }

    fun draw(canvas: Canvas, paint: Paint) {
        var imageIndice: Int

        for (caseX in 0..casesX - 1) {
            for (caseY in 0..casesY - 1) {
                if (caseY == 0) {
                    if (caseX == 0) {
                        imageIndice = 0
                    } else if (caseX == casesX - 1) {
                        imageIndice = 2
                    } else {
                        imageIndice = 1
                    }
                } else if (caseY == casesY - 1) {
                    if (caseX == 0) {
                        imageIndice = 6
                    } else if (caseX == casesX - 1) {
                        imageIndice = 4
                    } else {
                        imageIndice = 5
                    }
                } else if (caseX == 0) {
                    imageIndice = 7
                } else if (caseX == casesX - 1) {
                    imageIndice = 3
                } else if (caseY == 1) {
                    if (caseX == 1 || caseX == casesX / 2) {
                        imageIndice = 8
                    } else if (caseX == casesX - 2 || caseX == casesX / 2 - 1) {
                        imageIndice = 10
                    } else {
                        imageIndice = 9
                    }
                } else if (caseY == casesY - 2) {
                    if (caseX == 1 || caseX == casesX / 2) {
                        imageIndice = 14
                    } else if (caseX == casesX - 2 || caseX == casesX / 2 - 1) {
                        imageIndice = 12
                    } else {
                        imageIndice = 13
                    }
                } else if (caseX == 1 || caseX == casesX / 2) {
                    imageIndice = 15
                } else if (caseX == casesX - 2 || caseX == casesX / 2 - 1) {
                    imageIndice = 11
                } else {
                    imageIndice = 16
                }

                images.updateImage(imageIndice)
                images.setCases(caseX, caseY, this)
                images.draw(canvas, paint)
            }
        }
    }
}