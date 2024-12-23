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

class Scrolling(caseW: Int, caseH: Int, casesX: Int, casesY: Int, x: Int, y: Int) :
    Zone(caseW, caseH, casesX, casesY, x, y) {
    var v: Int = 0
    val minV: Int = floor(float(caseH - 1) / 2)
    val maxV: Int = floor(float(caseH - 1))
    var scrollingY: Int = 0
    var images: Images

    init {
        val imagesId = intArrayOf(
            R.drawable.scrolling_center_0_0,
            R.drawable.scrolling_center_0_1,
            R.drawable.scrolling_center_0_2,
            R.drawable.scrolling_center_0_3,
            R.drawable.scrolling_center_1_0,
            R.drawable.scrolling_center_1_1,
            R.drawable.scrolling_center_1_2,
            R.drawable.scrolling_center_1_3,
            R.drawable.scrolling_center_2_0,
            R.drawable.scrolling_center_2_1,
            R.drawable.scrolling_center_2_2,
            R.drawable.scrolling_center_2_3,
            R.drawable.scrolling_center_3_0,
            R.drawable.scrolling_center_3_1,
            R.drawable.scrolling_center_3_2,
            R.drawable.scrolling_center_3_3,
            R.drawable.scrolling_left_0_0,
            R.drawable.scrolling_left_0_1,
            R.drawable.scrolling_left_0_2,
            R.drawable.scrolling_left_0_3,
            R.drawable.scrolling_right_0_0,
            R.drawable.scrolling_right_0_1,
            R.drawable.scrolling_right_0_2,
            R.drawable.scrolling_right_0_3,
            R.drawable.scrolling_transparency
        )
        images = Images(imagesId, this)
    }

    fun reinit() {
        v = 0
        scrollingY = 0
    }

    fun update() {
        scrollingY = (scrollingY + v) % (2 * caseH)
    }

    fun updateV() {
        v = minV + floor((maxV - minV) * Opponent.getSpeed())
    }

    fun draw(canvas: Canvas, paint: Paint) {
        for (caseX in 0..casesX - 1) {
            for (caseY in 0..casesY - 1) {
                val imageIndice: Int

                if (caseX == 0) {
                    imageIndice = 16 + caseY % 4
                } else if (caseX == casesX - 1) {
                    imageIndice = 20 + caseY % 4
                } else {
                    imageIndice = 4 * ((caseX - 1) % 4) + caseY % 4
                }

                images.updateImage(imageIndice)
                images.setCases(caseX, caseY, this)
                images.y += scrollingY
                images.drawOnZone(this, canvas, paint)

                if (images.getYMax(this) > getYMax()) {
                    images.y -= h
                    images.drawOnZone(this, canvas, paint)
                }
            }
        }
    }

    fun drawTransparency(canvas: Canvas, paint: Paint) {
        for (caseX in 0..casesX - 1) {
            for (caseY in 0..casesY - 1) {
                images.updateImage(24)
                images.setCases(caseX, caseY, this)
                images.drawOnZone(this, canvas, paint)
            }
        }
    }
}