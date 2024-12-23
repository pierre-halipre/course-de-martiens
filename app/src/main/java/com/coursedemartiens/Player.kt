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

class Player(board: Board, scrolling: Scrolling) : Character(scrolling) {
    val initialV: Int = ceil((board.casesX - 1) * float(board.caseW) / Utility.reflexSteps)
    var hasMoveEvent: Boolean = false
    var moveEventCaseX: Int = board.caseUnknown
    var isMove: Boolean = false
    var nextX: Int = x
    var previousX: Int = x
    val bump: Bump = Bump(board)
    var hasBump: Boolean = false
    val place: Place = Place(board)

    init {
        name = "Player"
        v = initialV
        bumpCounterMax = getStepsMoveTwo(board) + 1
        val imagesId = listOf<Int>(
            R.drawable.player_0,
            R.drawable.player_1,
            R.drawable.player_2,
            R.drawable.player_3,
            R.drawable.player_4
        )
        addSheet(imagesId, board)
    }

    override fun reinit(board: Board) {
        super.reinit(board)
        v = initialV
        setCaseX(board.caseCenter, board)
        setCaseY(board.caseOut, board)
        hasMoveEvent = false
        moveEventCaseX = board.caseUnknown
        isMove = false
        nextX = x
        previousX = x
        bump.reinit(bumpCounterMax)
        hasBump = false
        place.reinit(counter.max)
    }

    override fun update(board: Board) {
        super.update(board)
        place.updateImage()

        if (isMove) {
            updateMove(board)
        }

        if (isBump) {
            bump.updateImage()

            if (!bump.counter.isStarted) {
                isBump = false
                Utility.sounds.setStopEventBump()
            }
        }
    }

    override fun updateCounterMax() {
        super.updateCounterMax()
        place.counter.updateMax(counter.max)
    }

    override fun drawOnBoardAndScrolling(
        board: Board, scrolling: Scrolling, canvas: Canvas, paint: Paint
    ) {
        if (!isBump || bump.counter.value % 2 == 1) {
            super.drawOnZone(board, canvas, paint)
        }

        if (isBump) {
            bump.x = x
            bump.y = y
            bump.drawOnZone(board, canvas, paint)
        }

        if (warp.isStarted()) {
            warp.drawOnScrolling(this, board, scrolling, canvas, paint)
        }
    }

    fun updateMove(board: Board) {
        val sign: Int

        if (previousX == nextX) {
            sign = 0
        } else if (previousX < nextX) {
            sign = 1
        } else {
            sign = -1
        }

        x += sign * v

        if (
            previousX == nextX
            || (previousX < nextX && x >= nextX)
            || (previousX > nextX && x <= nextX)
        ) {
            val log: String = "stop move, moveEventCaseX=" + Test.CASE_NAME(moveEventCaseX)
            Test.ADD_LOG(Test.MOVE, "updateMove", log)

            x = nextX
            previousX = x
            moveEventCaseX = board.caseUnknown
            isMove = false
            Utility.sounds.setStopEventMove()
        }
    }

    fun setMoveEvent(caseX: Int, board: Board) {
        var log: String = ("hasMoveEvent=" + hasMoveEvent
                + ", moveEventCaseX=" + Test.CASE_NAME(moveEventCaseX)
                + ", caseX=" + Test.CASE_NAME(caseX)
                + ", isMove=" + isMove
                + ", bestCaseX=" + Test.CASE_NAME(getBestCaseX(board))
                + ", ")

        val canSet: Boolean = moveEventCaseX != caseX

        if (
            (!hasMoveEvent || canSet)
            && ((!isMove && (getBestCaseX(board) != caseX || hasMoveEvent)) || (isMove && canSet))
        ) {
            hasMoveEvent = true
            moveEventCaseX = caseX

            log += "set"
        } else {
            log += "not set"
        }

        Test.ADD_LOG(Test.MOVE, "setMoveEvent", log)
    }

    fun updateMoveEvent(board: Board) {
        if (hasMoveEvent) {
            hasMoveEvent = false
            previousX = x
            nextX = board.toX(moveEventCaseX)

            if (!isMove && previousX != nextX) {
                Utility.sounds.setPlayEventMove()
            }

            isMove = true

            val log: String = ("start move"
                    + ", previousX=" + x
                    + ", nextX=" + nextX
                    + ", moveEventCaseX=" + Test.CASE_NAME(moveEventCaseX))
            Test.ADD_LOG(Test.MOVE, "updateMoveEvent", log)
        }
    }

    fun getBestCaseX(zone: Zone): Int {
        return zone.toCaseX(x + round(float(zone.caseW - 1) / 2))
    }

    fun getStepsMoveOne(board: Board): Int {
        return getSteps(board.caseW)
    }

    fun getStepsMoveTwo(board: Board): Int {
        return getSteps(2 * board.caseW)
    }

    fun getStepsCaseX(caseX: Int, board: Board): Int {
        return getSteps(board.x + caseX * board.caseW - x)
    }

    fun compareCaseX(caseX1: Int, caseX2: Int, board: Board): Int {
        val result: Int
        val x1: Int = abs(x - board.x - caseX1 * board.caseW)
        val x2: Int = abs(x - board.x - caseX2 * board.caseW)

        if (x1 > x2) {
            result = 1
        } else if (x1 < x2) {
            result = -1
        } else {
            result = 0
        }

        return result
    }

    fun checkBump(opponent: Opponent, board: Board) {
        if (isOn(opponent, board)) {
            opponent.setBump(board)
            hasBump = true

            val log: String = (opponent.name
                    + "\n" + logPosition(board) + "\n" + opponent.logPosition(board))
            Test.ADD_LOG(Test.TEST, "checkBump", log)
        }
    }

    fun isOn(opponent: Opponent, board: Board): Boolean {
        val result: Boolean

        if (
            x <= opponent.getXMax(board) && getXMax(board) >= opponent.x
            && y <= opponent.getYMax(board) && getYMax(board) >= opponent.y
        ) {
            result = true
        } else {
            result = false
        }

        return result
    }

    fun drawPlaceOnBoard(board: Board, canvas: Canvas, paint: Paint) {
        for (caseX in 0..board.casesX - 1) {
            if ((isMove && moveEventCaseX == caseX) || (!isMove && getBestCaseX(board) == caseX)) {
                place.sheetIndice = 1
            } else {
                place.sheetIndice = 0
            }

            place.setImageFromSheet(place.getImageIndice())
            place.setCases(caseX, board.caseLast, board)
            place.drawOnZone(board, canvas, paint)
        }
    }

    fun performUpdate(board: Board) {
        // il faudrait mettre à jour bump ici pour qu'il envoit un stopEvent au cas ou, donc
        // ununmute à move et un bump.hasStopEvent, donc le setPlayEventMove ne sera pas muted car
        // bump a un stopEvent
        updateMoveEvent(board)
        update(board)
    }

    fun performBump(enemy: Enemy, obstacle: Obstacle, board: Board) {
        if (!isBump) {
            if (!enemy.isBump) {
                checkBump(enemy, board)
            }

            if (!obstacle.isBump) {
                checkBump(obstacle, board)
            }

            if (hasBump) {
                hasBump = false
                isBump = true
                bump.counter.start()
                Utility.sounds.setPlayEventBump()
            }
        }
    }

    fun getLog(board: Board): String {
        val log: String = (Test.LINE_LOG(name, "getLog")
                + "left  =" + Test.NUMBER(getStepsCaseX(board.caseLeft, board))
                + ", center=" + Test.NUMBER(getStepsCaseX(board.caseCenter, board))
                + ", right =" + Test.NUMBER(getStepsCaseX(board.caseRight, board))
                + ", case  =" + Test.CASE_NAME(getBestCaseX(board)))

        return log
    }
}