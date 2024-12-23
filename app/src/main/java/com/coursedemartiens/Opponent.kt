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

abstract class Opponent(scrolling: Scrolling) : Character(scrolling) {
    companion object {
        var maxResetCount: Int = 2 * Utility.reflexSteps
        var resetCount: Int = 0
        var outCount: Int = 0
        var bumpCount: Int = 0
        var speedCount: Int = 0
        var sheetsIndice: Int = 0

        fun reinit() {
            maxResetCount = 2 * Utility.reflexSteps
            resetCount = 0
            outCount = 0
            bumpCount = 0
            speedCount = 0
            sheetsIndice = 0
        }

        fun updateResetCount() {
            resetCount++
        }

        fun isMaxResetCount(): Boolean {
            val result: Boolean

            if (resetCount < maxResetCount) {
                result = false
            } else {
                result = true
            }

            return result
        }

        fun updateOutCount() {
            outCount++
        }

        fun isMaxOutCount(): Boolean {
            val result: Boolean

            if (outCount < maxResetCount) {
                result = false
            } else {
                result = true
            }

            return result
        }

        fun updateBumpCount() {
            bumpCount++
        }

        fun getScore(): Float {
            return float(outCount - bumpCount) / maxResetCount
        }

        fun updateSpeedCount() {
            speedCount++
        }

        fun getSpeed(): Float {
            return float(speedCount) / (maxResetCount - 2)
        }

        fun updateSheetIndice(opponent: Opponent) {
            opponent.sheetIndice = 2 * sheetsIndice
            sheetsIndice = (sheetsIndice + 1) % 3
        }

        fun getLog(): String {
            val log: String = (Test.LINE_LOG("Opponent", "getLog")
                    + "resetCount=" + resetCount
                    + ", maxResetCount=" + maxResetCount
                    + ", outCount=" + outCount
                    + ", bumpCount=" + bumpCount
                    + ", speedCount=" + speedCount
                    + ", speed=" + Test.FLOAT(getSpeed())
                    + ", score=" + Test.FLOAT(getScore()))

            return log
        }
    }

    var minV: Int = 0
    var maxV: Int = 0
    var canReset: Boolean = false
    var cameOut: Boolean = false

    override fun reinit(board: Board) {
        loop = true
        endless = true
        super.reinit(board)
        setCaseY(board.caseOut, board)
        setCaseX(board.caseUnknown, board)
        canReset = false
        cameOut = false
    }

    override fun update(board: Board) {
        super.update(board)
        y += v

        if (needEnteringWarp(board)) {
            warp.start(getStepsEntering(board), this)
        } else if (needLeavingWarp(board)) {
            warp.start(getStepsLeaving(board), this)
        }

        if (isOut(board)) {
            val log: String = name + ", out"
            Test.ADD_LOG(Test.SPEED, "update", log)
        }
    }

    override fun updateCounterMax() {
        if (!isBump) {
            super.updateCounterMax()
        }
    }

    override fun drawOnBoardAndScrolling(
        board: Board, scrolling: Scrolling, canvas: Canvas, paint: Paint
    ) {
        if (isFree(board) && (!isBump || counter.value % 2 == 0)) {
            super.drawOnZone(board, canvas, paint)
        }

        if (warp.isStarted()) {
            warp.drawOnScrolling(this, board, scrolling, canvas, paint)
        }
    }

    fun reset(player: Player, opponent: Opponent, board: Board) {
        prereset(player, board)
        resetCaseX(player, opponent, board)

        if (!checkCaseX(player, opponent, board)) {
            correctCaseX(board)
        }

        val log: String = name + ", speed=" + Opponent.getSpeed()
        Test.ADD_LOG(Test.SPEED, "reset", log)
    }

    fun prereset(player: Player, board: Board) {
        setCaseY(board.caseIn, board)
        loop = true
        endless = true
        Opponent.updateSheetIndice(this)
        isBump = false
        bumpCounterMax = player.bumpCounterMax
        counter.reinit(getCounterMax())
        counter.start()
        warp.reinit(this)
        updateV()
        update(board)
    }

    fun resetCaseX(player: Player, opponent: Opponent, board: Board) {
        var log: String = name + ", "

        val availableCasesX: MutableList<Int> = mutableListOf<Int>()

        for (availableCaseX in 0..board.casesX - 1) {
            if (
                (!opponent.isFree(board) || opponent.getCaseX(board) != availableCaseX)
                && getCaseX(board) != availableCaseX
            ) {
                availableCasesX.add(availableCaseX)

                if (!opponent.isFree(board) && opponent.getCaseX(board) == availableCaseX) {
                    log += "(" + opponent.name + " not free=" + Test.CASE_NAME(availableCaseX) + ") "
                }
            } else if (opponent.isFree(board) && opponent.getCaseX(board) == availableCaseX) {
                log += "(not " + opponent.name + "=" + Test.CASE_NAME(availableCaseX) + ") "
            } else if (getCaseX(board) == availableCaseX) {
                log += "(not previous=" + Test.CASE_NAME(availableCaseX) + ") "
            }
        }

        val caseXPlayer: Int

        if (player.isMove) {
            caseXPlayer = player.moveEventCaseX
        } else {
            caseXPlayer = player.getCaseX(board)
        }

        var caseX: Int = board.caseUnknown

        if (caseXPlayer in availableCasesX) {
            caseX = caseXPlayer
            log += "(player) "
        } else {
            log += "(other) "

            for (availableCaseX in availableCasesX) {
                if (caseX == board.caseUnknown) {
                    caseX = availableCaseX
                } else {
                    val result: Int = player.compareCaseX(availableCaseX, caseX, board)

                    if (result == -1 || (result == 0 && availableCaseX >= caseX)) {
                        caseX = availableCaseX
                    }

                    if (result == 0) {
                        log += "(equal) "
                    }
                }
            }
        }

        setCaseX(caseX, board)

        log += "caseX=" + Test.CASE_NAME(caseX)
        Test.ADD_LOG(Test.CASE, "resetCaseX", log)
    }

    fun checkCaseX(player: Player, opponent: Opponent, board: Board): Boolean {
        val result: Boolean

        val opponentWillFree: Boolean = opponent.willFree(board)
        val caseX: Int = getCaseX(board)
        val opponentCaseX: Int = opponent.getCaseX(board)
        val playerCaseX: Int
        val playerMove: Int
        val playerMoveOne: Int = player.getStepsMoveOne(board)
        val playerMoveTwo: Int = player.getStepsMoveTwo(board)
        val lastCaseY: Int = getStepsLastCaseY(board)
        val opponentLastCaseY: Int = opponent.getStepsLastCaseY(board)
        val opponentFree: Int

        var log: String = name + ", "

        if (opponent.isBump && opponent.getStepsBump() < opponent.getStepsOut(board)) {
            opponentFree = opponent.getStepsBump() - 1
            log += "bump"
        } else {
            opponentFree = opponent.getStepsOut(board) - 1
            log += "out"
        }

        if (player.isMove) {
            playerCaseX = player.moveEventCaseX
            playerMove = player.getStepsCaseX(player.moveEventCaseX, board)
            log += ", move"
        } else {
            playerCaseX = player.getCaseX(board)
            playerMove = 0
        }

        val opponentBeforeLastCaseY: Int

        if (opponent.isBump) {
            opponentBeforeLastCaseY = 0
        } else {
            opponentBeforeLastCaseY = opponentLastCaseY - playerMove
        }

        val opponentAfterLastCaseY: Int = opponentLastCaseY - playerMoveTwo - playerMove
        val beforeLastCaseY: Int = lastCaseY - playerMoveOne - playerMove
        val afterFree: Int = lastCaseY - playerMoveOne - opponentFree

        if (opponentWillFree && caseX == playerCaseX && opponentCaseX == board.caseCenter) {
            log += ", check : "

            if (
                beforeLastCaseY >= 0
                && ((opponentBeforeLastCaseY >= 0 && afterFree >= 0) || opponentAfterLastCaseY >= 0)
            ) {
                result = true
                log += "ok"
            } else {
                result = false

                log += "ko"
            }

            log += (", before=" + beforeLastCaseY
                    + ", opponent before last=" + opponentBeforeLastCaseY
                    + ", after free=" + afterFree
                    + ", opponent after last=" + opponentAfterLastCaseY)
            Test.ADD_LOG(Test.PATTERN, "checkCaseX", log)
        } else {
            result = true
        }

        return result
    }

    fun correctCaseX(board: Board) {
        val caseX: Int

        if (getCaseX(board) == board.caseRight) {
            caseX = board.caseLeft
        } else {
            caseX = board.caseRight
        }

        setCaseX(caseX, board)
    }

    fun updateV() {
        v = minV + floor((maxV - minV) * Opponent.getSpeed())
    }

    fun isFree(board: Board): Boolean {
        val result: Boolean

        if ((isBump && !isDisappearing()) || isOut(board)) {
            result = false
        } else {
            result = true
        }

        return result
    }

    fun willFree(board: Board): Boolean {
        val result: Boolean

        if (!isFree(board) || getStepsOut(board) == 1 || getStepsBump() == 1) {
            result = false
        } else {
            result = true
        }

        return result
    }

    fun isOut(board: Board): Boolean {
        val result: Boolean

        if (y > board.getYMax()) {
            result = true
        } else {
            result = false
        }

        return result
    }

    fun isDisappearing(): Boolean {
        val result: Boolean

        if (isBump && counter.isStarted) {
            result = true
        } else {
            result = false
        }

        return result
    }

    fun getStepsOut(board: Board): Int {
        val steps: Int

        if (isOut(board)) {
            steps = 0
        } else {
            steps = getSteps(board.getYMax() + 1 - y)
        }

        return steps
    }

    fun getStepsLastCaseY(board: Board): Int {
        val steps: Int
        val lastY: Int = board.y + (board.casesY - 1) * board.caseH

        if (getYMax(board) >= lastY) {
            steps = 0
        } else {
            steps = getSteps(lastY - getYMax(board))
        }

        return steps
    }

    fun getStepsBump(): Int {
        val steps: Int

        if (isDisappearing()) {
            steps = counter.max - counter.value
        } else {
            steps = 0
        }

        return steps
    }

    fun setBump(board: Board) {
        loop = false
        endless = false
        sheetIndice++
        isBump = true
        counter.reinit(bumpCounterMax)
        counter.start()

        if (warp.isStarted()) {
            updateWarp(board)
        }
    }

    fun needEnteringWarp(board: Board): Boolean {
        val result: Boolean

        if (isFree(board) && !warp.isStarted() && y < board.y && getYMax(board) >= board.y) {
            result = true
        } else {
            result = false
        }

        return result
    }

    fun needLeavingWarp(board: Board): Boolean {
        val result: Boolean

        if (
            isFree(board) && !warp.isStarted()
            && y <= board.getYMax() && getYMax(board) > board.getYMax()
        ) {
            result = true
        } else {
            result = false
        }

        return result
    }

    fun updateWarp(board: Board) {
        val counterNewMax: Int

        if (getYMax(board) > board.getYMax()) {
            counterNewMax = getStepsLeaving(board)
        } else {
            counterNewMax = getStepsEntering(board)
        }

        var log: String = name + ", "
        log += "before max=" + warp.warpEven.counter.max + ", value=" + warp.warpEven.counter.value
        warp.updateCounterMax(counterNewMax)
        log += ", after max=" + warp.warpEven.counter.max + ", value=" + warp.warpEven.counter.value
        Test.ADD_LOG(Test.WARP, "updateWarp", log)
    }

    fun getStepsEntering(board: Board): Int {
        return getSteps(board.y - y)
    }

    fun getStepsLeaving(board: Board): Int {
        val steps: Int

        var log: String = name + ", "

        if (isDisappearing() && counter.max - counter.value < getSteps(board.getYMax() + 1 - y)) {
            steps = counter.max - counter.value
            log += "bump=" + steps + ", out=" + getSteps(board.getYMax() + 1 - y)
        } else {
            steps = getSteps(board.getYMax() + 1 - y)
            log += "out=" + steps
        }

        Test.ADD_LOG(Test.WARP, "getStepsLeaving", log)

        return steps
    }

    fun checkMoveDemo(player: Player, opponent: Opponent, board: Board): Int {
        val kind: Int

        val playerCaseX: Int = player.getBestCaseX(board)
        val playerMoveOne: Int = player.getStepsMoveOne(board)
        val playerMoveTwo: Int = player.getStepsMoveTwo(board)

        val free: Boolean = willFree(board)
        val caseX: Int = getCaseX(board)
        val lastCaseY: Int = getStepsLastCaseY(board)

        val opponentFree: Boolean = opponent.willFree(board)
        val opponentCaseX: Int = opponent.getCaseX(board)
        val opponentLastCaseY: Int = opponent.getStepsLastCaseY(board)
        val opponentCan: Boolean = (opponentFree
                && opponentCaseX == playerCaseX
                && opponentCaseX != board.caseCenter
                && opponentLastCaseY >= playerMoveOne)

        var log: String = name + ", kind="

        if (opponentCan && free && caseX == board.caseCenter && lastCaseY == playerMoveTwo) {
            kind = 1
            log += "1, both"
        } else if (free && caseX == playerCaseX && lastCaseY <= playerMoveOne) {
            if (caseX == board.caseCenter) {
                kind = 2
                log += "2, alone"
            } else {
                kind = 3
                log += "3, just"
            }

            if (lastCaseY < playerMoveOne) {
                log += ", bump"
            }
        } else {
            kind = 0
        }

        if (kind != 0) {
            Test.ADD_LOG(Test.DEMO, "checkMoveDemo", log)
        }

        return kind
    }

    fun checkWrongMoveDemo(player: Player, board: Board): Int {
        val kind: Int

        val playerMove: Int = player.getStepsCaseX(getCaseX(board), board)

        if (player.getCaseX(board) == getCaseX(board)) {
            if (player.isOn(this, board)) {
                kind = 1
            } else if (getStepsLastCaseY(board) < player.getStepsMoveOne(board)) {
                kind = 2
            } else {
                kind = 0
            }
        } else if (getStepsLastCaseY(board) <= playerMove && getStepsOut(board) > playerMove) {
            kind = 3
        } else {
            kind = 0
        }

        return kind
    }

    fun performUpdate(board: Board, scene: Scene) {
        if (!isOut(board)) {
            update(board)

            if (isOut(board)) {
                cameOut = true
                Opponent.updateOutCount()

                if (!Opponent.isMaxResetCount()) {
                    scene.needWrongMove()
                    Opponent.updateResetCount()
                    canReset = true
                }

                if (isBump) {
                    Opponent.updateBumpCount()
                } else if (canReset) {
                    Opponent.updateSpeedCount()
                }
            }
        } else if (!Opponent.isMaxResetCount() && Opponent.outCount == 0) {
            scene.needWrongMove()
            Opponent.updateResetCount()
            canReset = true
        }
    }

    fun performReset(player: Player, opponent: Opponent, board: Board) {
        if (canReset) {
            canReset = false
            reset(player, opponent, board)
            Egg.switchOpponent(this)
        }
    }

    fun performOut(player: Player, opponent: Opponent, scrolling: Scrolling) {
        if (cameOut) {
            cameOut = false
            player.updateCounterMax()
            updateCounterMax()
            opponent.updateCounterMax()
            scrolling.updateV()
        }
    }

    fun getLog(board: Board): String {
        val log: String = (Test.LINE_LOG(name, "getLog")
                + "out   =" + Test.NUMBER(getStepsOut(board))
                + ", last  =" + Test.NUMBER(getStepsLastCaseY(board))
                + ", bump  =" + Test.NUMBER(getStepsBump())
                + ", case  =" + Test.CASE_NAME(getCaseX(board)))

        return log
    }
}