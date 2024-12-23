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

abstract class Character(scrolling: Scrolling) : Sprite(true, true) {
    lateinit var name: String
    val lowCounterMax: Int = 2 * Utility.reflexSteps
    val highCounterMax: Int = Utility.reflexSteps
    var v: Int = 0
    var bumpCounterMax: Int = 0
    var isBump: Boolean = false
    val warp: Warp = Warp(scrolling)

    open fun reinit(board: Board) {
        super.reinit(lowCounterMax)
        v = 0
        isBump = false
        warp.reinit(this)
    }

    open fun update(board: Board) {
        if (counter.isStarted) {
            super.updateImage()
        }

        if (warp.isStarted()) {
            warp.updateImage(this)
        }
    }

    fun getSteps(coordinate: Int): Int {
        return ceil(float(abs(coordinate)) / v)
    }

    fun getCounterMax(): Int {
        return highCounterMax + ceil((lowCounterMax - highCounterMax) * (1 - Opponent.getSpeed()))
    }

    open fun updateCounterMax() {
        counter.updateMax(getCounterMax())
    }

    abstract fun drawOnBoardAndScrolling(
        board: Board, scrolling: Scrolling, canvas: Canvas, paint: Paint
    )

    fun logPosition(board: Board): String {
        val log: String = (Test.LINE_LOG(name, "logPosition")
                + "xMin=" + Test.NUMBER(x - board.x)
                + ", xMax=" + Test.NUMBER(getXMax(board) - board.x)
                + ", yMin=" + Test.NUMBER(y - board.y)
                + ", yMax=" + Test.NUMBER(getYMax(board) - board.y))

        return log
    }
}