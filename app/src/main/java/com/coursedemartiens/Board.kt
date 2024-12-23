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

class Board(caseW: Int, caseH: Int, casesX: Int, casesY: Int, x: Int, y: Int) :
    Zone(caseW, caseH, casesX, casesY, x, y) {
    val caseLeft: Int = 0
    val caseCenter: Int = 1
    val caseRight: Int = 2
    val caseUnknown: Int = 3
    val caseIn: Int = -1
    val caseLast: Int = 3
    val caseOut: Int = 4

    fun toX(caseX: Int): Int {
        return x + caseW * caseX
    }

    fun isInsidePlace(yEvent: Int): Boolean {
        val result: Boolean

        if (yEvent >= y + caseLast * caseH && yEvent < y + caseOut * caseH) {
            result = true
        } else {
            result = false
        }

        return result
    }
}