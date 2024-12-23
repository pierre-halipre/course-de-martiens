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

class Text(caseW: Int, caseH: Int, casesX: Int, casesY: Int, x: Int, y: Int) :
    Zone(caseW, caseH, casesX, casesY, x, y) {
    var images: Images

    init {
        val imagesId = intArrayOf(
            R.drawable.text_score,
            R.drawable.text_time,
            R.drawable.text_title_1,
            R.drawable.text_title_2
        )
        images = Images(imagesId, this)
    }

    fun drawTitle(canvas: Canvas, paint: Paint) {
        images.updateImage(2)
        images.setCases(0, 2, this)
        images.drawOnZone(this, canvas, paint)
        images.updateImage(3)
        images.setCases(0, 3, this)
        images.drawOnZone(this, canvas, paint)
    }

    fun drawNumbers(canvas: Canvas, paint: Paint) {
        images.updateImage(0)
        images.setCases(0, 0, this)
        images.drawOnZone(this, canvas, paint)
        images.updateImage(1)
        images.setCases(0, 4, this)
        images.drawOnZone(this, canvas, paint)
    }
}