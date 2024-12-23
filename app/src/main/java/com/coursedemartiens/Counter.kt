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

class Counter {
    var max: Int = 1
    var value: Int = 0
    var isStarted: Boolean = false

    fun reinit(newMax: Int) {
        max = newMax
        value = 0
        isStarted = false
    }

    fun start() {
        value = 0
        isStarted = true
    }

    fun update() {
        if (isStarted) {
            value++

            if (value == max) {
                isStarted = false
            }
        }
    }

    fun getRatio(): Float {
        var ratio: Float = float(value) / max

        if (ratio >= float(1)) {
            val log: String = "ratio>=1" + ", ratio=" + ratio + ", minRatio=" + Utility.minRatio
            Test.ADD_LOG(Test.SPRITE, "getRatio", log)

            ratio = Utility.minRatio
        }

        return ratio
    }

    fun updateMax(newMax: Int) {
        val maxRatio: Float = float(newMax) / max
        value = round(value * maxRatio)

        if (value >= newMax) {
            val log: String = ("value>=newMax"
                    + ", value=" + value + ", max=" + max + ", newMax=" + newMax)
            Test.ADD_LOG(Test.SPRITE, "updateMax", log)

            value = newMax - 1
        }

        max = newMax
    }
}