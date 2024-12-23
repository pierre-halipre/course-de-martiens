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

abstract class Zone(caseW: Int, caseH: Int, casesX: Int, casesY: Int, x: Int, y: Int) {
    val caseW: Int = caseW
    val caseH: Int = caseH
    val casesX: Int = casesX
    val casesY: Int = casesY
    val x: Int = x
    val y: Int = y
    val w: Int = caseW * casesX
    val h: Int = caseH * casesY

    fun toCaseX(coordinateX: Int): Int {
        return floor(float(coordinateX - x) / caseW)
    }

    fun toCaseY(coordinateY: Int): Int {
        return floor(float(coordinateY - y) / caseH)
    }

    fun getYMax(): Int {
        return y + h - 1
    }

    fun isInside(coordinateX: Int, coordinateY: Int): Boolean {
        val result: Boolean

        if (
            coordinateX >= x && coordinateX < x + w
            && coordinateY >= y && coordinateY < y + h
        ) {
            result = true
        } else {
            result = false
        }

        return result
    }
}