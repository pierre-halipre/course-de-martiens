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

abstract class Sprite(loop: Boolean, endless: Boolean) : Image() {
    var loop: Boolean = loop
    var endless: Boolean = endless
    val counter: Counter = Counter()
    var sheet: MutableList<List<Bitmap>> = mutableListOf<List<Bitmap>>()
    var sheetIndice: Int = 0

    fun reinit(counterThreshold: Int) {
        super.reinit()
        counter.reinit(counterThreshold)

        if (endless) {
            counter.start()
        }

        sheetIndice = 0
        setImageFromSheet(0)
    }

    fun addSheet(imagesId: List<Int>, zone: Zone) {
        val images: MutableList<Bitmap> = mutableListOf<Bitmap>()

        for (imageId in imagesId) {
            images.add(Utility.createImage(imageId, zone))
        }

        sheet.add(images)
    }

    fun setImageFromSheet(imageIndice: Int) {
        image = sheet[sheetIndice][imageIndice]
    }

    fun getImageIndice(): Int {
        val imageIndice: Int

        if (sheet[sheetIndice].size > 1) {
            val imagesNumber: Int = sheet[sheetIndice].size

            if (loop) {
                val imagesIndices: Int = imagesNumber - 1
                val spritesNumber: Int = imagesIndices * 2
                val spriteIndice: Int = floor(counter.getRatio() * spritesNumber)

                if (spriteIndice < imagesNumber) {
                    imageIndice = spriteIndice
                } else {
                    imageIndice = imagesIndices - spriteIndice % imagesIndices
                }
            } else {
                imageIndice = floor(counter.getRatio() * imagesNumber)
            }
        } else {
            imageIndice = 0
        }

        return imageIndice
    }

    fun updateImage() {
        setImageFromSheet(getImageIndice())

        counter.update()

        if (endless && !counter.isStarted) {
            counter.start()
        }
    }
}