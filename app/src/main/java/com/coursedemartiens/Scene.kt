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

class Scene(board: Board) {
    var isIntro: Boolean = false
    var isOutro: Boolean = false
    var isDemo: Boolean = true

    var baseMaxSteps: Int = Opponent.maxResetCount
    var cutsceneMaxSteps: Int = 0
    var steps: Int = 0
    var ratio: Float = float(0)
    var scrollingY: Int = 0
    var playerCounterMax: Int = 0
    var placeCounterValue: Int = 0
    var placeCounterMax: Int = 0

    var maxDemoCount: Int = Opponent.maxResetCount / 2
    var demoCount: Int = 0
    val maxDemoCaseXCount: Int = board.casesX
    var demoCaseXCount: Int = 0
    var demoCaseX: Int = 0

    var hasWrongMove: Boolean = false
    var wrongMoveKind: Int = 0

    fun isCutscene(): Boolean {
        val result: Boolean

        if (isIntro || isOutro) {
            result = true
        } else {
            result = false
        }

        return result
    }

    fun updateDemoCount() {
        if (isDemo) {
            if (demoCount == 0) {
                demoCaseXCount = (demoCaseXCount + 1) % maxDemoCaseXCount
            }

            demoCount = (demoCount + maxDemoCount - 1) % maxDemoCount
            demoCaseX = (demoCount + demoCaseXCount) % maxDemoCaseXCount

            val log: String = ("demoCount=" + demoCount
                    + ", maxDemoCount=" + maxDemoCount
                    + ", demoCaseX=" + Test.CASE_NAME(demoCaseX))
            Test.ADD_LOG(Test.SCENE, "updateDemoCount", log)
        }
    }

    fun needWrongMove() {
        if (isDemo && isWrongMove()) {
            val log: String = ("resetCount=" + Opponent.resetCount
                    + ", demoCount=" + demoCount
                    + ", hasWrongMove=" + hasWrongMove)

            hasWrongMove = true
            wrongMoveKind = 0

            Test.ADD_LOG(Test.SCENE, "needWrongMove", log)
        }
    }

    fun isWrongMove(): Boolean {
        val result: Boolean
        val demoResetCount: Int = Opponent.resetCount % maxDemoCount
        val firstDemoCount: Int = demoCount
        val secondDemoCount: Int = (demoCount + ceil(float(maxDemoCount) / 2)) % maxDemoCount

        if (demoResetCount == firstDemoCount || demoResetCount == secondDemoCount) {
            result = true
        } else {
            result = false
        }

        return result
    }

    fun startIntro() {
        steps = 0
        isIntro = true
        isOutro = false
        hasWrongMove = false
        wrongMoveKind = 0
        setCutsceneMaxSteps()

        val log: String = ("cutsceneMaxSteps=" + cutsceneMaxSteps
                + ", demoCaseX=" + Test.CASE_NAME(demoCaseX))
        Test.ADD_LOG(Test.SCENE, "startIntro", log)
    }

    fun startOutro() {
        steps = 0
        isIntro = false
        isOutro = true
        hasWrongMove = false
        wrongMoveKind = 0
        setCutsceneMaxSteps()

        val log: String = ("cutsceneMaxSteps=" + cutsceneMaxSteps
                + ", demoCaseX=" + Test.CASE_NAME(demoCaseX))
        Test.ADD_LOG(Test.SCENE, "startOutro", log)
    }

    fun setCutsceneMaxSteps() {
        cutsceneMaxSteps = baseMaxSteps

        if (isOutro) {
            cutsceneMaxSteps += ceil(float(10) / Utility.frameTime) * Opponent.bumpCount
        }
    }

    fun updateIntro(player: Player, scrolling: Scrolling, board: Board) {
        if (steps == 0) {
            val log: String = ("scrollingY=" + scrollingY
                    + ", placeCounterValue=" + placeCounterValue
                    + ", placeCounterMax=" + placeCounterMax
                    + ", max=" + player.counter.max)
            Test.ADD_LOG(Test.SCENE, "updateIntro", log)

            if (placeCounterMax != 0) {
                player.place.counter.value = placeCounterValue
                player.place.counter.max = placeCounterMax
                placeCounterValue = 0
                placeCounterMax = 0
            } else {
                player.place.counter.value = player.counter.value
                player.place.counter.max = player.counter.max
            }

            scrolling.scrollingY = scrollingY
            scrollingY = 0
            player.setCaseY(board.caseOut, board)
            playerCounterMax = player.counter.max

            Utility.sounds.setPlayEventCutscene()
            Utility.sounds.setPlayEventVacuum()
        }

        checkDemoCaseX(player, board)

        steps++
        setRatio()
        scrolling.updateV()
        scrolling.v = ceil(scrolling.v * ratio)
        player.y = board.y + board.casesY * board.caseH - ceil(board.caseH * ratio)
        player.counter.updateMax(ceil(playerCounterMax / ratio))
        player.place.counter.updateMax(player.counter.max)

        if (steps < cutsceneMaxSteps
            && player.getYMax(board) > board.getYMax()
            && !player.warp.isStarted()
        ) {
            player.warp.start(cutsceneMaxSteps - steps, player)

            val log: String = "warp steps=" + (cutsceneMaxSteps - steps)
            Test.ADD_LOG(Test.SCENE, "updateIntro", log)
        }

        if (steps == cutsceneMaxSteps) {
            val log: String = ("warp=" + player.warp.isStarted()
                    + ", move=" + player.isMove
                    + ", stepsDemoCaseX=" + player.getStepsCaseX(demoCaseX, board)
                    + ", y=" + player.y + ", yLast=" + (board.y + board.caseLast * board.caseH)
                    + ", max=" + player.counter.max
                    + ", lowMax=" + player.lowCounterMax
                    + ", playerCounterMax=" + playerCounterMax)
            Test.ADD_LOG(Test.SCENE, "updateIntro", log)

            cutsceneMaxSteps = 0
            steps = 0
            isIntro = false
            playerCounterMax = 0
            Utility.sounds.setStopEventCutscene()
        }
    }

    fun updateOutro(player: Player, scrolling: Scrolling, board: Board) {
        if (steps == 0) {
            val log: String = "max=" + player.counter.max
            Test.ADD_LOG(Test.SCENE, "updateOutro", log)

            playerCounterMax = player.counter.max
            Utility.sounds.setPlayEventCutscene()
        }

        checkDemoCaseX(player, board)

        steps++
        setRatio()
        scrolling.updateV()
        scrolling.v = ceil(scrolling.v * (1 - ratio))
        player.y = board.y - board.caseH + ceil(board.casesY * board.caseH * (1 - ratio))

        if (steps < cutsceneMaxSteps) {
            player.counter.updateMax(ceil(playerCounterMax / (1 - ratio)))

            if (player.y < board.y && !player.warp.isStarted()) {
                player.warp.start(cutsceneMaxSteps - steps, player)

                val log: String = "warp steps=" + (cutsceneMaxSteps - steps)
                Test.ADD_LOG(Test.SCENE, "updateOutro", log)
            }
        } else {
            player.counter.updateMax(Int.MAX_VALUE)
        }

        player.place.counter.updateMax(player.counter.max)

        if (steps == cutsceneMaxSteps) {
            cutsceneMaxSteps = 0
            steps = 0
            isOutro = false
            scrollingY = scrolling.scrollingY
            playerCounterMax = 0
            placeCounterValue = player.place.counter.value
            placeCounterMax = player.place.counter.max

            var log: String = ("scrollingY=" + scrollingY
                    + ", placeCounterValue=" + placeCounterValue
                    + ", placeCounterMax=" + placeCounterMax)
            Test.ADD_LOG(Test.SCENE, "updateOutro", log)

            log = ("warp=" + player.warp.isStarted()
                    + ", move=" + player.isMove
                    + ", stepsDemoCaseX=" + player.getStepsCaseX(demoCaseX, board)
                    + ", y=" + player.y + ", yIn=" + (board.y + board.caseIn * board.caseH)
                    + ", max=" + player.counter.max
                    + ", lowMax=" + player.lowCounterMax)

            Test.ADD_LOG(Test.SCENE, "updateOutro", log)
        }
    }

    fun checkDemoCaseX(player: Player, board: Board) {
        if (isDemo && !player.isMove && player.getBestCaseX(board) != demoCaseX) {
            val stepsMove: Int = player.getStepsCaseX(demoCaseX, board)
            val stepsAfter: Int = cutsceneMaxSteps - stepsMove - steps - 1

            if (steps >= stepsAfter) {
                player.setMoveEvent(demoCaseX, board)

                val log: String = ("caseX=" + Test.CASE_NAME(demoCaseX)
                        + ", before=" + steps
                        + ", after=" + stepsAfter
                        + ", move=" + stepsMove)
                Test.ADD_LOG(Test.SCENE, "checkDemoCaseX", log)
            }
        }
    }

    fun setRatio() {
        ratio = float(steps) / cutsceneMaxSteps
    }

    fun updateDemo(player: Player, enemy: Enemy, obstacle: Obstacle, board: Board) {
        if (isDemo && !player.isMove) {
            if (hasWrongMove) {
                checkWrongMoveDemo(player, enemy, obstacle, board)
            }

            if (!hasWrongMove) {
                checkMoveDemo(player, enemy, obstacle, board)
            }
        }
    }

    fun checkMoveDemo(player: Player, enemy: Enemy, obstacle: Obstacle, board: Board) {
        val moveDemoCaseX: Int
        val enemyKind: Int = enemy.checkMoveDemo(player, obstacle, board)
        val obstacleKind: Int = obstacle.checkMoveDemo(player, enemy, board)

        if (enemyKind == 1 || obstacleKind == 1 || enemyKind == 2 || obstacleKind == 2) {
            moveDemoCaseX = getMoveDemoCaseX(enemy, obstacle, board)
        } else if (enemyKind == 3 || obstacleKind == 3) {
            moveDemoCaseX = board.caseCenter
        } else {
            moveDemoCaseX = board.caseUnknown
        }

        if (moveDemoCaseX != board.caseUnknown) {
            player.setMoveEvent(moveDemoCaseX, board)

            val log: String = "caseX=" + Test.CASE_NAME(moveDemoCaseX)
            Test.ADD_LOG(Test.DEMO, "checkMoveDemo", log)
        }
    }

    fun getMoveDemoCaseX(enemy: Enemy, obstacle: Obstacle, board: Board): Int {
        val moveDemoCaseX: Int
        val enemyFree: Boolean = enemy.willFree(board)
        val enemyCaseX: Int = enemy.getCaseX(board)
        val obstacleFree: Boolean = obstacle.willFree(board)
        val obstacleCaseX: Int = obstacle.getCaseX(board)

        if (enemyFree && obstacleFree) {
            if (
                (enemyCaseX == board.caseLeft && obstacleCaseX == board.caseCenter)
                || (enemyCaseX == board.caseCenter && obstacleCaseX == board.caseLeft)
            ) {
                moveDemoCaseX = board.caseRight
            } else if (
                (enemyCaseX == board.caseRight && obstacleCaseX == board.caseCenter)
                || (enemyCaseX == board.caseCenter && obstacleCaseX == board.caseRight)
            ) {
                moveDemoCaseX = board.caseLeft
            } else {
                moveDemoCaseX = board.caseCenter
            }
        } else if (enemyFree) {
            if (enemyCaseX != board.caseCenter) {
                moveDemoCaseX = board.caseCenter
            } else {
                moveDemoCaseX = board.caseRight
            }
        } else if (obstacleFree) {
            if (obstacleCaseX != board.caseCenter) {
                moveDemoCaseX = board.caseCenter
            } else {
                moveDemoCaseX = board.caseRight
            }
        } else {
            moveDemoCaseX = board.caseUnknown
        }

        return moveDemoCaseX
    }

    fun checkWrongMoveDemo(player: Player, enemy: Enemy, obstacle: Obstacle, board: Board) {
        var log: String = "kind="
        val previousWrongMoveKind: Int = wrongMoveKind

        val wrongMoveDemoCaseX: Int
        val enemyWrongMoveDemo: Int = enemy.checkWrongMoveDemo(player, board)
        val obstacleWrongMoveDemo: Int = obstacle.checkWrongMoveDemo(player, board)

        if (enemyWrongMoveDemo == 1 || obstacleWrongMoveDemo == 1) {
            hasWrongMove = false
            wrongMoveDemoCaseX = board.caseUnknown
            wrongMoveKind = 1
            log += "1, hasWrongMove=" + hasWrongMove
        } else if (enemyWrongMoveDemo == 2 || obstacleWrongMoveDemo == 2) {
            wrongMoveDemoCaseX = board.caseUnknown
            wrongMoveKind = 2
            log += "2, wait"
        } else if (enemyWrongMoveDemo == 3 || obstacleWrongMoveDemo == 3) {
            val enemyCaseX: Int = enemy.getCaseX(board)
            val obstacleCaseX: Int = obstacle.getCaseX(board)
            wrongMoveKind = 3
            log += "3, move "

            if (enemyWrongMoveDemo == 3 && obstacleWrongMoveDemo == 3) {
                val result: Int = player.compareCaseX(enemyCaseX, obstacleCaseX, board)

                if (result == 1 || (result == 0 && obstacleCaseX == board.caseRight)) {
                    wrongMoveDemoCaseX = obstacleCaseX
                    log += "Obstacle (equal)"
                } else {
                    wrongMoveDemoCaseX = enemyCaseX
                    log += "Enemy (equal)"
                }
            } else if (enemyWrongMoveDemo == 3) {
                wrongMoveDemoCaseX = enemyCaseX
                log += "Enemy"
            } else {
                wrongMoveDemoCaseX = obstacleCaseX
                log += "Obstacle"
            }
        } else {
            wrongMoveDemoCaseX = board.caseUnknown
        }

        if (wrongMoveDemoCaseX != board.caseUnknown) {
            player.setMoveEvent(wrongMoveDemoCaseX, board)

            log += ", caseX=" + Test.CASE_NAME(wrongMoveDemoCaseX)
            Test.ADD_LOG(Test.WRONG, "checkWrongMoveDemo", log)
        } else if (previousWrongMoveKind != wrongMoveKind) {
            Test.ADD_LOG(Test.WRONG, "checkWrongMoveDemo", log)
        }
    }
}