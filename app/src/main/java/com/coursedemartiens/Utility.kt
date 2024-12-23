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

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.system.exitProcess

class Utility {
    companion object {
        lateinit var resources: Resources
        lateinit var sounds: Sounds
        var minRatio: Float = float(0)
        val unitCaseW: Int = Resources.getSystem().displayMetrics.widthPixels / 14
        val unitCaseH: Int = Resources.getSystem().displayMetrics.heightPixels / 24
        val x: Int = (Resources.getSystem().displayMetrics.widthPixels - 14 * unitCaseW) / 2
        val y: Int = (Resources.getSystem().displayMetrics.heightPixels - 24 * unitCaseH) / 2
        const val frameTime: Int = 40
        const val reflexTime: Int = 750
        var previousFrameDate: Long = 0
        var previousGameDate: Long = 0
        val reflexSteps: Int = ceil(float(reflexTime) / frameTime)
        var gameTicks: Int = 0

        fun init(context: Context) {
            resources = context.resources
            sounds = Sounds()

            var minValue: Int = Int.MAX_VALUE

            while (float(minValue) / Int.MAX_VALUE >= float(1)) {
                minValue--
            }

            minRatio = float(minValue) / Int.MAX_VALUE
        }

        fun free() {
            sounds.free()
        }

        fun notifyGame(): Boolean {
            val result: Boolean
            val currentDate: Long = System.currentTimeMillis()

            if (currentDate - previousGameDate >= frameTime) {
                result = true
                previousGameDate = currentDate
            } else {
                result = false
            }

            return result
        }

        fun updateGameTicks() {
            gameTicks++
        }

        fun resetGameTicks() {
            gameTicks = 0
        }

        fun resetPreviousDate() {
            previousFrameDate = System.currentTimeMillis()
            previousGameDate = System.currentTimeMillis()
        }

        fun createImage(imageId: Int, zone: Zone): Bitmap {
            val resource: Bitmap = BitmapFactory.decodeResource(resources, imageId)
            val image: Bitmap = Bitmap.createScaledBitmap(
                resource, zone.caseW, zone.caseH, false
            )

            return image
        }
    }
}

fun exit() {
    exitProcess(0)
}

fun int(number: Float): Int {
    return number.toInt()
}

fun long(number: Int): Long {
    return number.toLong()
}

fun float(number: Int): Float {
    return number.toFloat()
}

fun floor(number: Float): Int {
    return kotlin.math.floor(number).toInt()
}

fun ceil(number: Float): Int {
    return kotlin.math.ceil(number).toInt()
}

fun round(number: Float): Int {
    return kotlin.math.round(number).toInt()
}

fun abs(number: Int): Int {
    return kotlin.math.abs(number)
}