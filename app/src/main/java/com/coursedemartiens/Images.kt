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

class Images(imagesId: IntArray, zone: Zone) : Image() {
    val images: MutableList<Bitmap> = mutableListOf<Bitmap>()

    init {
        for (imageId in imagesId) {
            images.add(Utility.createImage(imageId, zone))
        }
    }

    fun updateImage(imageIndice: Int) {
        image = images[imageIndice]
    }
}