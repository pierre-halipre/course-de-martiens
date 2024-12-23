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

class Warp(scrolling: Scrolling) {
    val warpEven: WarpEven = WarpEven(scrolling)
    val warpOdd: WarpOdd = WarpOdd(scrolling)

    fun reinit(character: Character) {
        warpEven.reinit(1)
        warpOdd.reinit(1)
        Utility.sounds.setStopEventWarp(character)
    }

    fun start(counterThreshold: Int, character: Character) {
        warpEven.reinit(counterThreshold)
        warpEven.counter.start()
        warpOdd.reinit(counterThreshold)
        warpOdd.counter.start()

        Utility.sounds.setPlayEventWarp(character)
    }

    fun isStarted(): Boolean {
        val result: Boolean

        if (!warpEven.counter.isStarted && !warpOdd.counter.isStarted) {
            result = false
        } else {
            result = true
        }

        return result
    }

    fun updateImage(character: Character) {
        warpEven.updateImage()
        warpOdd.updateImage()

        if (!isStarted()) {
            Utility.sounds.setStopEventWarp(character)
        }
    }

    fun updateCounterMax(newCounterMax: Int) {
        warpEven.counter.updateMax(newCounterMax)
        warpOdd.counter.updateMax(newCounterMax)
    }

    fun drawOnScrolling(
        character: Character, board: Board, scrolling: Scrolling, canvas: Canvas, paint: Paint
    ) {
        val caseY: Int

        if (character.getYMax(board) > board.getYMax()) {
            caseY = scrolling.casesY - 1
        } else {
            caseY = 0
        }

        val caseXStart: Int = scrolling.toCaseX(character.x)
        val caseXStop: Int = scrolling.toCaseX(character.getXMax(board))

        for (caseX in caseXStart..caseXStop) {
            if (caseX % 2 == 0) {
                warpEven.setCases(caseX, caseY, scrolling)
                warpEven.drawOnZone(scrolling, canvas, paint)
            } else {
                warpOdd.setCases(caseX, caseY, scrolling)
                warpOdd.drawOnZone(scrolling, canvas, paint)
            }
        }
    }
}

class WarpEven(scrolling: Scrolling) : Sprite(false, false) {
    init {
        val imagesId = listOf<Int>(
            R.drawable.warp_even_0,
            R.drawable.warp_even_1,
            R.drawable.warp_even_2,
            R.drawable.warp_even_3,
            R.drawable.warp_even_4,
            R.drawable.warp_even_5,
            R.drawable.warp_even_6,
            R.drawable.warp_even_7,
            R.drawable.warp_even_8
        )
        addSheet(imagesId, scrolling)
    }
}

class WarpOdd(scrolling: Scrolling) : Sprite(false, false) {
    init {
        val imagesId = listOf<Int>(
            R.drawable.warp_odd_0,
            R.drawable.warp_odd_1,
            R.drawable.warp_odd_2,
            R.drawable.warp_odd_3,
            R.drawable.warp_odd_4,
            R.drawable.warp_odd_5,
            R.drawable.warp_odd_6,
            R.drawable.warp_odd_7,
            R.drawable.warp_odd_8
        )
        addSheet(imagesId, scrolling)
    }
}