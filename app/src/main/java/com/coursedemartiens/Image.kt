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
import android.graphics.Canvas
import android.graphics.Paint

open class Image {
    var x: Int = 0
    var y: Int = 0
    lateinit var image: Bitmap

    open fun reinit() {
        x = 0
        y = 0
    }

    fun getCaseX(zone: Zone): Int {
        return (x - zone.x) / zone.caseW
    }

    fun getCaseY(zone: Zone): Int {
        return (y - zone.y) / zone.caseH
    }

    fun setCaseX(caseX: Int, zone: Zone) {
        x = zone.x + caseX * zone.caseW
    }

    fun setCaseY(caseY: Int, zone: Zone) {
        y = zone.y + caseY * zone.caseH
    }

    fun setCases(caseX: Int, caseY: Int, zone: Zone) {
        setCaseX(caseX, zone)
        setCaseY(caseY, zone)
    }

    fun getXMax(zone: Zone): Int {
        return x + zone.caseW - 1
    }

    fun getYMax(zone: Zone): Int {
        return y + zone.caseH - 1
    }

    open fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(image, float(x), float(y), paint)
    }

    fun drawOnZone(zone: Zone, canvas: Canvas, paint: Paint) {
        if (y < zone.y && getYMax(zone) >= zone.y) {
            val caseY: Int = zone.caseH - 1 - (getYMax(zone) - zone.y)
            val caseHeight: Int = getYMax(zone) - zone.y + 1
            val caseCutted: Bitmap = Bitmap.createBitmap(image, 0, caseY, zone.caseW, caseHeight)
            canvas.drawBitmap(caseCutted, float(x), float(zone.y), paint)
        } else if (getYMax(zone) > zone.getYMax() && y <= zone.getYMax()) {
            val caseY: Int = 0
            val caseHeight: Int = zone.caseH - (getYMax(zone) - zone.getYMax())
            val caseCutted: Bitmap = Bitmap.createBitmap(image, 0, caseY, zone.caseW, caseHeight)
            canvas.drawBitmap(caseCutted, float(x), float(y), paint)
        } else if (getYMax(zone) >= zone.y && y <= zone.getYMax()) {
            draw(canvas, paint)
        }
    }
}