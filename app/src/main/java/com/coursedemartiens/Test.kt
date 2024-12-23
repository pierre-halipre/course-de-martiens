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

class Test {
    companion object {
        val TEST: String = "TEST"
        var testNumber: Int = 0
        val CASE: String = "CASE"
        val testCases: Int = 28
        val SPRITE: String = "SPRITE"
        val testSprites: Int = 45
        val WARP: String = "WARP"
        val testWarps: Int = 48
        val BUMP: String = "BUMP"
        val testBumps: Int = 54
        val PATTERN: String = "PATTERN"
        val testPatterns: Int = 112
        val DEMO: String = "DEMO"
        val testDemos: Int = 132
        val WRONG: String = "WRONG"
        val testWrongs: Int = 176
        val SPEED: String = "SPEED"
        val testSpeeds: Int = 196
        val SOUND: String = "SOUND"
        val testSounds: Int = 224
        val SCENE: String = "SCENE"
        val testScenes: Int = 248
        val MOVE: String = "MOVE"
        val testMoves: Int = 260
        val DEBUG: String = "DEBUG"

        var isTest: Boolean = false
        var testStop: Boolean = false
        var testLog: String = ""
        var previousNeedReinit: Boolean = false
        var previousIsDemo: Boolean = false

        lateinit var player: Player
        lateinit var enemy: Enemy
        lateinit var obstacle: Obstacle
        lateinit var board: Board
        lateinit var scrolling: Scrolling
        lateinit var scene: Scene
        lateinit var button: Button
        lateinit var screen: Screen
        lateinit var sounds: Sounds

        val ENEMY: Int = 0
        val OBSTACLE: Int = 1

        var LAST: Int = 0
        var OUT: Int = 0
        var IN: Int = 0
        var CENTER: Int = 0
        var LEFT: Int = 0
        var RIGHT: Int = 0
        var NONE: Int = 0
        var MOVE_ONE: Int = 0
        var MOVE_TWO: Int = 0

        fun SET(test: Boolean) {
            isTest = test
        }

        fun INIT(
            player: Player,
            enemy: Enemy,
            obstacle: Obstacle,
            board: Board,
            scrolling: Scrolling,
            scene: Scene,
            button: Button,
            screen: Screen
        ) {
            if (IS(TEST)) {
                this.player = player
                this.enemy = enemy
                this.obstacle = obstacle
                this.board = board
                this.scrolling = scrolling
                this.scene = scene
                this.button = button
                this.screen = screen
                this.sounds = Utility.sounds

                this.CENTER = board.caseCenter
                this.LEFT = board.caseLeft
                this.RIGHT = board.caseRight
                this.NONE = board.caseUnknown
                this.LAST = board.caseLast
                this.OUT = board.caseOut
                this.IN = board.caseIn
                this.MOVE_ONE = player.getStepsMoveOne(board)
                this.MOVE_TWO = player.getStepsMoveTwo(board)

                Opponent.outCount = -1
                this.scene.isDemo = false

                ADD_TITLE_LOG(DEBUG, "begin")
                PRINT_LOG(DEBUG)
            }
        }

        fun IS(kind: String): Boolean {
            val result: Boolean

            if (!isTest) {
                result = false
            } else if (kind == DEBUG) {
                result = true
            } else if (testNumber > testMoves) {
                result = false
            } else if (kind == TEST) {
                result = true
            } else if (kind == CASE && testNumber > 0 && testNumber <= testCases) {
                result = true
            } else if (kind == SPRITE && testNumber > testCases && testNumber <= testSprites) {
                result = true
            } else if (kind == WARP && testNumber > testSprites && testNumber <= testWarps) {
                result = true
            } else if (kind == BUMP && testNumber > testWarps && testNumber <= testBumps) {
                result = true
            } else if (kind == PATTERN && testNumber > testBumps && testNumber <= testPatterns) {
                result = true
            } else if (kind == DEMO && testNumber > testPatterns && testNumber <= testDemos) {
                result = true
            } else if (kind == WRONG && testNumber > testDemos && testNumber <= testWrongs) {
                result = true
            } else if (kind == SPEED && testNumber > testWrongs && testNumber <= testSpeeds) {
                result = true
            } else if (kind == SOUND && testNumber > testSpeeds && testNumber <= testSounds) {
                result = true
            } else if (kind == SCENE && testNumber > testSounds && testNumber <= testScenes) {
                result = true
            } else if (kind == MOVE && testNumber > testScenes && testNumber <= testMoves) {
                result = true
            } else {
                result = false
            }

            return result
        }

        fun PRINT(log: String, kind: String) {
            if (IS(kind)) {
                println(log)
            }
        }

        fun PRINT_LOG(kind: String) {
            if (testLog != "") {
                PRINT(testLog, kind)
                testLog = ""
            }
        }

        fun PRINT_DEBUG(function: String, log: String) {
            PRINT(LINE_LOG(DEBUG, function) + log, DEBUG)
        }

        fun LINE_LOG(kind: String, function: String): String {
            return STRING(kind) + " : " + function + " -> "
        }

        fun ADD_TITLE_LOG(kind: String, log: String) {
            val title: String = ("*".repeat(100) + "\n"
                    + LINE_LOG(STRING(TEST + "#" + testNumber), STRING(kind)) + log
                    + "\n" + "*".repeat(100))

            if (testLog != "") {
                testLog = title + "\n" + testLog
            } else {
                testLog = title
            }
        }

        fun ADD_LOG(kind: String, function: String, log: String) {
            if (IS(kind)) {
                if (testLog != "") {
                    testLog += "\n"
                }

                testLog += LINE_LOG(kind, function) + log
            }
        }

        fun CASE_X_PLAYER(caseX: Int, steps: Int = 0, x: Int = 0) {
            player.setCaseX(caseX, board)
            player.x += steps * player.v + x
        }

        fun MOVE_PLAYER(caseX: Int) {
            player.setMoveEvent(caseX, board)
            player.updateMoveEvent(board)
        }

        fun OPPONENT(who: Int): Opponent {
            val opponent: Opponent

            if (who == ENEMY) {
                opponent = enemy
            } else {
                opponent = obstacle
            }

            return opponent
        }

        fun CASE_X(who: Int, caseX: Int) {
            val opponent: Opponent = OPPONENT(who)
            opponent.setCaseX(caseX, board)
        }

        fun CASE_X_ENEMY(caseX: Int) {
            CASE_X(ENEMY, caseX)
        }

        fun CASE_X_OBSTACLE(caseX: Int) {
            CASE_X(OBSTACLE, caseX)
        }

        fun CASE_Y(who: Int, where: Int = IN, steps: Int = 0, y: Int = 0) {
            val opponent: Opponent = OPPONENT(who)
            opponent.setCaseY(board.caseIn, board)

            if (where == OUT) {
                opponent.y += opponent.getStepsOut(board) * opponent.v
            } else if (where == LAST) {
                opponent.y += opponent.getStepsLastCaseY(board) * opponent.v
            }

            opponent.y += steps * opponent.v + y
        }

        fun CASE_Y_ENEMY(where: Int = IN, steps: Int = 0, y: Int = 0) {
            CASE_Y(ENEMY, where, steps, y)
        }

        fun CASE_Y_OBSTACLE(where: Int = IN, steps: Int = 0, y: Int = 0) {
            CASE_Y(OBSTACLE, where, steps, y)
        }

        fun TO_CASE_Y(who: Int, where: Int): Int {
            val opponent: Opponent = OPPONENT(who)
            val steps: Int

            if (where == LAST) {
                steps = opponent.getStepsLastCaseY(board)
            } else if (where == OUT) {
                steps = opponent.getStepsOut(board)
            } else {
                steps = 0
            }

            return steps
        }

        fun BUMP_OPPONENT(who: Int, where: Int = IN, steps: Int = 0) {
            val opponent: Opponent = OPPONENT(who)

            if (where == LAST) {
                opponent.bumpCounterMax = opponent.getStepsLastCaseY(board) + steps
            } else if (where == OUT) {
                opponent.bumpCounterMax = opponent.getStepsOut(board) + steps
            } else {
                opponent.bumpCounterMax = steps
            }

            opponent.setBump(board)

            if (opponent.bumpCounterMax <= 0) {
                opponent.counter.isStarted = false
            }
        }

        fun PRERESET_CHARACTERS() {
            enemy.prereset(player, board)
            enemy.updateCounterMax()
            enemy.warp.reinit(enemy)
            obstacle.prereset(player, board)
            obstacle.updateCounterMax()
            obstacle.warp.reinit(enemy)
            player.reinit(board)
            player.updateCounterMax()
            player.warp.reinit(player)
            player.setCaseY(board.caseLast, board)
            scrolling.reinit()
            scrolling.updateV()
        }

        fun RESET_SCENE() {
            scene.isIntro = false
            scene.isOutro = false
            scene.isDemo = true

            scene.baseMaxSteps = Opponent.maxResetCount
            scene.cutsceneMaxSteps = 0
            scene.steps = 0
            scene.ratio = float(0)
            scene.scrollingY = 0
            scene.playerCounterMax = 0
            scene.placeCounterValue = 0
            scene.placeCounterMax = 0

            scene.maxDemoCount = Opponent.maxResetCount / 2
            scene.demoCount = 0
            scene.demoCaseXCount = 0
            scene.demoCaseX = 0

            scene.hasWrongMove = false
            scene.wrongMoveKind = 0
        }

        fun TEST_CASE() {
            val log: String
            Opponent.reinit()
            RESET_SCENE()
            scene.isDemo = false
            PRERESET_CHARACTERS()

            if (testNumber == 1) {
                log = "Obstacle : center (left, Enemy left, Player left)"
                CASE_X_OBSTACLE(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_X_PLAYER(LEFT)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 2) {
                log = "Obstacle : right (center, Enemy left, Player left)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(LEFT)
                CASE_X_PLAYER(LEFT)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 3) {
                log = "Obstacle : center (right, Enemy left, Player left)"
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_X_PLAYER(LEFT)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 4) {
                log = "Obstacle : equal right (center, Enemy center, Player center)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(CENTER)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 5) {
                log = "Obstacle : right (left, Enemy center, Player center)"
                CASE_X_OBSTACLE(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(CENTER)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 6) {
                log = "Obstacle : left (right, Enemy center, Player center)"
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(CENTER)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 7) {
                log = "Obstacle : right (center, Enemy out right, Player right)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT)
                CASE_X_PLAYER(RIGHT)
                obstacle.resetCaseX(player, enemy, board)
                Opponent.outCount++
            } else if (testNumber == 8) {
                log = "Obstacle : right (center, Enemy bump right, Player right)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(RIGHT)
                BUMP_OPPONENT(ENEMY, IN, 0)
                CASE_X_PLAYER(RIGHT)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 9) {
                log = "Obstacle : right (left, Enemy center, Player left move right)"
                CASE_X_OBSTACLE(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 10) {
                log = "Obstacle : right (left, Enemy center, Player center move right)"
                CASE_X_OBSTACLE(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 11) {
                log = "Obstacle : center (right, Enemy left, Player left move center)"
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(CENTER)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 12) {
                log = "Obstacle : right (center, Enemy left, Player left move center)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(LEFT)
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(CENTER)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 13) {
                log = "Obstacle : right (center, Enemy left, Player center)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(LEFT)
                CASE_X_PLAYER(CENTER)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 14) {
                log = "Obstacle : center (left, Enemy right, Player left)"
                CASE_X_OBSTACLE(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_X_PLAYER(LEFT)
                obstacle.resetCaseX(player, enemy, board)
            } else if (testNumber == 15) {
                log = "Enemy : center (right, Obstacle right, Player right)"
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(RIGHT)
                CASE_X_PLAYER(RIGHT)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 16) {
                log = "Enemy : left (center, Obstacle right, Player right)"
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(RIGHT)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 17) {
                log = "Enemy : center (left, Obstacle right, Player right)"
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_X_PLAYER(RIGHT)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 18) {
                log = "Enemy : equal right (center, Obstacle center, Player center)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(CENTER)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 19) {
                log = "Enemy : left (right, Obstacle center, Player center)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_X_PLAYER(CENTER)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 20) {
                log = "Enemy : right (left, Obstacle center, Player center)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(LEFT)
                CASE_X_PLAYER(CENTER)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 21) {
                log = "Enemy : left (center, Obstacle out left, Player left)"
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(OUT)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(LEFT)
                enemy.resetCaseX(player, obstacle, board)
                Opponent.outCount++
            } else if (testNumber == 22) {
                log = "Enemy : left (center, Obstacle bump left, Player left)"
                CASE_X_OBSTACLE(LEFT)
                BUMP_OPPONENT(OBSTACLE, IN, 0)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(LEFT)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 23) {
                log = "Enemy : left (right, Obstacle center, Player right move left)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 24) {
                log = "Enemy : left (right, Obstacle center, Player center move left)"
                CASE_X_OBSTACLE(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 25) {
                log = "Enemy : center (left, Obstacle right, Player right move center)"
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(CENTER)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 26) {
                log = "Enemy : left (center, Obstacle right, Player right move center)"
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(CENTER)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 27) {
                log = "Enemy : left (center, Obstacle right, Player center)"
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_X_PLAYER(CENTER)
                enemy.resetCaseX(player, obstacle, board)
            } else if (testNumber == 28) {
                log = "Enemy : center (right, Obstacle left, Player right)"
                CASE_X_OBSTACLE(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_X_PLAYER(RIGHT)
                enemy.resetCaseX(player, obstacle, board)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(CASE, log)
        }

        fun TEST_SPRITE() {
            val log: String
            Opponent.reinit()
            Opponent.resetCount = Opponent.maxResetCount
            Opponent.outCount = Opponent.maxResetCount - 2
            Opponent.speedCount = Opponent.maxResetCount - 2
            RESET_SCENE()
            scene.isDemo = false
            PRERESET_CHARACTERS()

            fun DO_TEST_SPRITE(sprite: Sprite) {
                var previousImageIndice: Int = -1

                while (true) {
                    if (previousImageIndice != sprite.getImageIndice()) {
                        previousImageIndice = sprite.getImageIndice()
                        val spriteLog: String = ("max=" + NUMBER(sprite.counter.max)
                                + ", value=" + NUMBER(sprite.counter.value)
                                + ", image=" + NUMBER(sprite.getImageIndice()))
                        ADD_LOG(SPRITE, "getImageIndice", spriteLog)
                    }

                    sprite.updateImage()

                    if (sprite.counter.value == 0) {
                        break
                    }
                }
            }

            if (testNumber == 29) {
                log = "Sprite : loop, max=2*(images-1)"
                enemy.counter.max = 2 * (enemy.sheet[0].size - 1)
                enemy.counter.value = 0
                DO_TEST_SPRITE(enemy)
            } else if (testNumber == 30) {
                log = "Sprite : loop, max=2*(images-1)-1"
                enemy.counter.max = 2 * (enemy.sheet[0].size - 1) - 1
                enemy.counter.value = 0
                DO_TEST_SPRITE(enemy)
            } else if (testNumber == 31) {
                log = "Sprite : loop, max=2*(images-1)+1"
                enemy.counter.max = 2 * (enemy.sheet[0].size - 1) + 1
                enemy.counter.value = 0
                DO_TEST_SPRITE(enemy)
            } else if (testNumber == 32) {
                log = "Sprite : loop, max=(images-1)"
                enemy.counter.max = enemy.sheet[0].size - 1
                enemy.counter.value = 0
                DO_TEST_SPRITE(enemy)
            } else if (testNumber == 33) {
                log = "Sprite : loop, max=(images-1)-1"
                enemy.counter.max = enemy.sheet[0].size - 2
                enemy.counter.value = 0
                DO_TEST_SPRITE(enemy)
            } else if (testNumber == 34) {
                log = "Sprite : loop, max=(images-1)+1"
                enemy.counter.max = enemy.sheet[0].size
                enemy.counter.value = 0
                DO_TEST_SPRITE(enemy)
            } else if (testNumber == 35) {
                log = "Sprite : loop, max=high"
                enemy.counter.max = enemy.highCounterMax
                enemy.counter.value = 0
                DO_TEST_SPRITE(enemy)
            } else if (testNumber == 36) {
                log = "Sprite : loop, max=low"
                enemy.counter.max = enemy.lowCounterMax
                enemy.counter.value = 0
                DO_TEST_SPRITE(enemy)
            } else if (testNumber == 37) {
                log = "Sprite : not loop, max=images"
                player.place.counter.max = player.place.sheet[0].size
                DO_TEST_SPRITE(enemy)
            } else if (testNumber == 38) {
                log = "Sprite : not loop, max=images-1"
                player.place.counter.max = player.place.sheet[0].size - 1
                DO_TEST_SPRITE(player.place)
            } else if (testNumber == 39) {
                log = "Sprite : not loop, max=images+1"
                player.place.counter.max = player.place.sheet[0].size + 1
                DO_TEST_SPRITE(player.place)
            } else if (testNumber == 40) {
                log = "Sprite : not loop, max=low"
                player.place.counter.max = player.lowCounterMax
                DO_TEST_SPRITE(player.place)
            } else if (testNumber == 41) {
                log = "Sprite : not loop, max=high"
                player.place.counter.max = player.highCounterMax
                DO_TEST_SPRITE(player.place)
            } else if (testNumber == 42) {
                log = "Counter : updateMax, value>newMax"
                player.place.counter.max = Int.MAX_VALUE
                player.place.counter.value = player.place.counter.max - 1
                player.place.counter.updateMax(player.place.counter.max - 1)
                player.reinit(board)
            } else if (testNumber == 43) {
                log = "Counter : updateMax, value=newMax"
                player.place.counter.max = Int.MAX_VALUE - 1
                player.place.counter.value = player.place.counter.max - 1
                player.place.counter.updateMax(Int.MAX_VALUE)
                player.reinit(board)
            } else if (testNumber == 44) {
                log = "Counter : getRatio, ratio>=1"
                player.place.counter.max = Int.MAX_VALUE
                player.place.counter.value = 2147483583 + 1
                player.place.counter.getRatio()
                player.reinit(board)
            } else if (testNumber == 45) {
                log = "Counter : getRatio, ratio<1, ratio=minRatio"
                player.place.counter.max = Int.MAX_VALUE
                player.place.counter.value = 2147483583
                player.place.counter.getRatio()
                player.reinit(board)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(SPRITE, log)
        }

        fun TEST_WARP() {
            val log: String
            Opponent.reinit()
            Opponent.resetCount = Opponent.maxResetCount
            Opponent.outCount = Opponent.maxResetCount - 2
            RESET_SCENE()
            scene.isDemo = false
            PRERESET_CHARACTERS()

            if (testNumber == 46) {
                log = "Warp : Enemy entering full, Obstacle leaving bump"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(IN)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -3)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                obstacle.update(board)
            } else if (testNumber == 47) {
                log = "Warp : Enemy entering last, Obstacle leaving then bump"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(IN, -2, board.caseH)
                enemy.update(board)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -3)
                obstacle.update(board)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
            } else if (testNumber == 48) {
                log = "Warp : Enemy leaving then bump, Obstacle leaving then bump ap out"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -10)
                enemy.update(board)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -10)
                obstacle.update(board)
                BUMP_OPPONENT(OBSTACLE, OUT, 0)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(WARP, log)
        }

        fun TEST_BUMP() {
            val log: String
            Opponent.reinit()
            Opponent.resetCount = Opponent.maxResetCount
            Opponent.outCount = Opponent.maxResetCount - 2
            Opponent.speedCount = Opponent.maxResetCount - 2
            RESET_SCENE()
            scene.isDemo = false
            PRERESET_CHARACTERS()

            if (testNumber == 49) {
                log = "Obstacle : KO (1 pixel xMin/yMin)"
                CASE_X_PLAYER(RIGHT, -MOVE_TWO, -1)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(IN, -MOVE_TWO, 3 * board.caseH + 1)
            } else if (testNumber == 50) {
                log = "Obstacle : KO (1 pixel xMax/yMax)"
                CASE_X_PLAYER(LEFT, -1, 1)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(IN, -1, 5 * board.caseH - 1)
            } else if (testNumber == 51) {
                log = "Obstacle : KO (1 pixel xMax/yMin)"
                CASE_X_PLAYER(LEFT, MOVE_TWO, 1)
                MOVE_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(IN, -MOVE_TWO, 3 * board.caseH + 1)
            } else if (testNumber == 52) {
                log = "Obstacle : KO (1 pixel xMin/yMax)"
                CASE_X_PLAYER(RIGHT, 1, -1)
                MOVE_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(IN, -1, 5 * board.caseH - 1)
            } else if (testNumber == 53) {
                log = "Enemy/Obstacle : KO (Player bump Enemy then Obstacle 1 step)"
                CASE_X_PLAYER(RIGHT, -player.bumpCounterMax - MOVE_ONE - 1)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE + 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO + 1)
            } else if (testNumber == 54) {
                log = "Enemy/Obstacle : KO (Player bump Enemy and Obstacle)"
                CASE_X_PLAYER(LEFT, -1)
                MOVE_PLAYER(CENTER)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE + 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE + 1)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(BUMP, log)
        }

        fun TEST_PATTERN() {
            val log: String
            Opponent.reinit()
            Opponent.resetCount = Opponent.maxResetCount
            Opponent.outCount = Opponent.maxResetCount - 2
            Opponent.speedCount = 0
            RESET_SCENE()
            PRERESET_CHARACTERS()

            if (testNumber == 55) {
                log = "Enemy : ko, before=-1"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE + 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, LAST) - MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 56) {
                log = "Enemy : ko, Player center move left, before=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE - MOVE_ONE + 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, LAST) - MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 57) {
                log = "Enemy : ko, Player right move left, before=-1"
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - MOVE_ONE + 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, LAST) - MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 58) {
                log = "Enemy : ko, Player center move left, opponent before last=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE + 1)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 59) {
                log = "Enemy : ko, Player right move left, opponent before last=-1"
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO + 1)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 60) {
                log = "Enemy : ok, out, after free=0"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -TO_CASE_Y(ENEMY, LAST) + MOVE_ONE - 1)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 61) {
                log = "Enemy : ok, out, after free=1"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -TO_CASE_Y(ENEMY, LAST) + MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 62) {
                log = "Enemy : ok, Player center move left, out, after free=0"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 1)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 63) {
                log = "Enemy : ok, Player right move left, out, after free=0"
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 1)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 64) {
                log = "Enemy : ok, bump, after free=0"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -TO_CASE_Y(ENEMY, LAST) + MOVE_ONE - 2)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 65) {
                log = "Enemy : ok, bump, after free=1"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -TO_CASE_Y(ENEMY, LAST) + MOVE_ONE - 2)
                BUMP_OPPONENT(OBSTACLE, OUT, -2)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 66) {
                log = "Enemy : ok, Player center move left, bump, after free=0"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 2)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 67) {
                log = "Enemy : ok, Player on Obstacle/center move left, bump, after free=0"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 2)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 68) {
                log = "Enemy : ok, Player right move left, bump, after free=0"
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 2)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 69) {
                log = "Enemy : ok, Player on Obstacle/right move left, bump, after free=0"
                CASE_X_PLAYER(RIGHT, -1)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 2)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 70) {
                log = "Enemy : ko, out, after free=-1"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -TO_CASE_Y(ENEMY, LAST) + MOVE_ONE - 2)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 71) {
                log = "Enemy : ko, Player center move left, out, after free=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 2)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 72) {
                log = "Enemy : ko, Player right move left, out, after free=-1"
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 2)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 73) {
                log = "Enemy : ko, bump, after free=-1"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -TO_CASE_Y(ENEMY, LAST) + MOVE_ONE - 3)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 74) {
                log = "Enemy : ko, Player center move left, bump, after free=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 3)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 75) {
                log = "Enemy : ko, Player on Obstacle/center move left, bump, after free=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 3)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 76) {
                log = "Enemy : ko, Player right move left, bump, after free=-1"
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 3)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 77) {
                log = "Enemy : ko, Player on Obstacle/right move left, bump, after free=-1"
                CASE_X_PLAYER(RIGHT, -1)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST)
                BUMP_OPPONENT(OBSTACLE, OUT, -1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, OUT) - MOVE_ONE + 3)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 78) {
                log = "Enemy : ok, opponent after last=0"
                CASE_X_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 79) {
                log = "Enemy : ok, opponent after last=1"
                CASE_X_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - 1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 80) {
                log = "Enemy : ok, Player center move left, opponent after last=0"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - MOVE_ONE)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE - MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 81) {
                log = "Enemy : ok, Player center move left, opponent after last=1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - MOVE_ONE - 1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE - MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 82) {
                log = "Enemy : ok, Player right move left, opponent after last=0"
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - MOVE_TWO)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 83) {
                log = "Enemy : ok, Player right move left, opponent after last=1"
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - MOVE_TWO - 1)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - MOVE_ONE)
                enemy.checkCaseX(player, obstacle, board)
            } else if (testNumber == 84) {
                log = "Obstacle : ko, before=-1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE + 1)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, LAST) - MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 85) {
                log = "Obstacle : ko, Player center move right, before=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE - MOVE_ONE + 1)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, LAST) - MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 86) {
                log = "Obstacle : ko, Player left move right, before=-1"
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - MOVE_ONE + 1)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -TO_CASE_Y(OBSTACLE, LAST) - MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 87) {
                log = "Obstacle : ko, Player center move right, opponent before last=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE + 1)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 88) {
                log = "Obstacle : ko, Player left move right, opponent before last=-1"
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO + 1)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 89) {
                log = "Obstacle : ok, out, after free=0"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -TO_CASE_Y(OBSTACLE, LAST) + MOVE_ONE - 1)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 90) {
                log = "Obstacle : ok, out, after free=1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -TO_CASE_Y(OBSTACLE, LAST) + MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 91) {
                log = "Obstacle : ok, Player center move right, out, after free=0"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 1)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 92) {
                log = "Obstacle : ok, Player left move right, out, after free=0"
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 1)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 93) {
                log = "Obstacle : ok, bump, after free=0"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -TO_CASE_Y(OBSTACLE, LAST) + MOVE_ONE - 2)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 94) {
                log = "Obstacle : ok, bump, after free=1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -TO_CASE_Y(OBSTACLE, LAST) + MOVE_ONE - 2)
                BUMP_OPPONENT(ENEMY, OUT, -2)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 95) {
                log = "Obstacle : ok, Player center move right, bump, after free=0"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 2)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 96) {
                log = "Obstacle : ok, Player on Enemy/center move right, bump, after free=0"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 2)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 97) {
                log = "Obstacle : ok, Player left move right, bump, after free=0"
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 2)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 98) {
                log = "Obstacle : ok, Player on Enemy/left move right, bump, after free=0"
                CASE_X_PLAYER(LEFT, 1)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 2)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 99) {
                log = "Obstacle : ko, out, after free=-1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -TO_CASE_Y(OBSTACLE, LAST) + MOVE_ONE - 2)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 100) {
                log = "Obstacle : ko, Player center move right, out, after free=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 2)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 101) {
                log = "Obstacle : ko, Player left move right, out, after free=-1"
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 2)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 102) {
                log = "Obstacle : ko, bump, after free=-1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -TO_CASE_Y(OBSTACLE, LAST) + MOVE_ONE - 3)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 103) {
                log = "Obstacle : ko, Player center move right, bump, after free=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 3)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 104) {
                log = "Obstacle : ko, Player on Enemy/center move right, bump, after free=-1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 3)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 105) {
                log = "Obstacle : ko, Player left move right, bump, after free=-1"
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 3)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 106) {
                log = "Obstacle : ko, Player on Enemy/left move right, bump, after free=-1"
                CASE_X_PLAYER(LEFT, 1)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST)
                BUMP_OPPONENT(ENEMY, OUT, -1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -TO_CASE_Y(ENEMY, OUT) - MOVE_ONE + 3)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 107) {
                log = "Obstacle : ok, opponent after last=0"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 108) {
                log = "Obstacle : ok, opponent after last=1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - 1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 109) {
                log = "Obstacle : ok, Player center move right, opponent after last=0"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE - MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 110) {
                log = "Obstacle : ok, Player center move right, opponent after last=1"
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - MOVE_ONE - 1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE - MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 111) {
                log = "Obstacle : ok, Player left move right, opponent after last=0"
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - MOVE_TWO)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else if (testNumber == 112) {
                log = "Obstacle : ok, Player left move right, opponent after last=1"
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - MOVE_TWO - 1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - MOVE_ONE)
                obstacle.checkCaseX(player, enemy, board)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(PATTERN, log)
        }

        fun TEST_DEMO() {
            val log: String
            Opponent.reinit()
            Opponent.resetCount = Opponent.maxResetCount
            Opponent.outCount = Opponent.maxResetCount - 2
            Opponent.speedCount = Opponent.maxResetCount - 2
            RESET_SCENE()
            PRERESET_CHARACTERS()

            if (testNumber == 113) {
                log = "Enemy, both right"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE - 1)
            } else if (testNumber == 114) {
                log = "Enemy, both right (Obstacle just)"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 115) {
                log = "Enemy, just center"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - 1)
            } else if (testNumber == 116) {
                log = "Enemy, just center bump"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE + 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
            } else if (testNumber == 117) {
                log = "Enemy, just center (Obstacle out)"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -1)
            } else if (testNumber == 118) {
                log = "Enemy, just center (Obstacle bump)"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST)
                BUMP_OPPONENT(OBSTACLE, LAST, 1)
            } else if (testNumber == 119) {
                log = "Enemy, alone right"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 120) {
                log = "Enemy, alone right bump"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE + 1)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 121) {
                log = "Enemy, alone right (Obstacle out)"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(OUT, -1)
            } else if (testNumber == 122) {
                log = "Enemy, alone right (Obstacle bump)"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST)
                BUMP_OPPONENT(OBSTACLE, LAST, 1)
            } else if (testNumber == 123) {
                log = "Obstacle, both left"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE - 1)
            } else if (testNumber == 124) {
                log = "Obstacle, both left (Enemy just)"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
            } else if (testNumber == 125) {
                log = "Obstacle, just center"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - 1)
            } else if (testNumber == 126) {
                log = "Obstacle, just center bump"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE + 1)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
            } else if (testNumber == 127) {
                log = "Obstacle, just center (Enemy out)"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -1)
            } else if (testNumber == 128) {
                log = "Obstacle, just center (Enemy bump)"
                CASE_X_PLAYER(RIGHT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST)
                BUMP_OPPONENT(ENEMY, LAST, 1)
            } else if (testNumber == 129) {
                log = "Obstacle, alone right"
                CASE_X_PLAYER(CENTER)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
            } else if (testNumber == 130) {
                log = "Obstacle, alone right bump"
                CASE_X_PLAYER(CENTER)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE + 1)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
            } else if (testNumber == 131) {
                log = "Obstacle, alone right (Enemy out)"
                CASE_X_PLAYER(CENTER)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -1)
            } else if (testNumber == 132) {
                log = "Obstacle, alone right (Enemy bump)"
                CASE_X_PLAYER(CENTER)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST)
                BUMP_OPPONENT(ENEMY, LAST, 1)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(DEMO, log)
        }

        fun TEST_WRONG() {
            val log: String
            Opponent.reinit()
            Opponent.resetCount = Opponent.maxResetCount
            Opponent.outCount = Opponent.maxResetCount - 2
            Opponent.speedCount = Opponent.maxResetCount - 2
            RESET_SCENE()
            scene.hasWrongMove = true
            PRERESET_CHARACTERS()

            if (testNumber == 133) {
                log = "2 : Player left, Enemy left, Obstacle center, wait Enemy"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
            } else if (testNumber == 134) {
                log = "3 : Player left, Enemy left, Obstacle center, move Obstacle last"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 135) {
                log = "3 : Player left, Enemy left, Obstacle center, move Obstacle out-1"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE - 1)
            } else if (testNumber == 136) {
                log = "2 : Player left, Enemy left, Obstacle center out, wait Enemy"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 137) {
                log = "3 : Player left, Enemy left, Obstacle right, move Obstacle last"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
            } else if (testNumber == 138) {
                log = "3 : Player left, Enemy left, Obstacle right, move Obstacle out-1"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(OUT, -MOVE_TWO - 1)
            } else if (testNumber == 139) {
                log = "2 : Player left, Enemy left, Obstacle right out, wait Enemy"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(OUT, -MOVE_TWO)
            } else if (testNumber == 140) {
                log = "3 : Player left, Enemy center, Obstacle right, move Enemy last"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - 1)
            } else if (testNumber == 141) {
                log = "3 : Player left, Enemy center, Obstacle right, move Enemy out-1"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -MOVE_ONE - 1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO - 1)
            } else if (testNumber == 142) {
                log = "3 : Player left, Enemy center, Obstacle right, move Enemy (equal)"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
            } else if (testNumber == 143) {
                log = "3 : Player left, Enemy center, Obstacle right, move Obstacle last"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE - 1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
            } else if (testNumber == 144) {
                log = "3 : Player left, Enemy center, Obstacle right, move Obstacle out-1"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE - 1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(OUT, -MOVE_TWO - 1)
            } else if (testNumber == 145) {
                log = "3 : Player left, Enemy center out, Obstacle right, move Obstacle last"
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_TWO)
            } else if (testNumber == 146) {
                log = "3 : Player left move right, Enemy left, Obstacle center"
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(IN)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -1)
            } else if (testNumber == 147) {
                log = "2 : Player center, Obstacle left, Enemy center, wait Enemy"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE + 1)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 148) {
                log = "2 : Player center, Obstacle left out, Enemy center, wait Enemy"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 149) {
                log = "3 : Player center, Obstacle left, Enemy center, move Obstacle last"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 150) {
                log = "3 : Player center, Obstacle left, Enemy center, move Obstacle out-1"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE - 1)
            } else if (testNumber == 151) {
                log = "3 : Player center, Obstacle left, Enemy right, move Enemy last"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE - 1)
            } else if (testNumber == 152) {
                log = "3 : Player center, Obstacle left, Enemy right, move Enemy out-1"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE - 1)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE - 1)
            } else if (testNumber == 153) {
                log = "3 : Player center, Obstacle left out, Enemy right, move Enemy last"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 154) {
                log = "3 : Player center, Obstacle left, Enemy right, move Enemy last (equal)"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 155) {
                log = "2 : Player right, Obstacle right, Enemy center, wait Obstacle"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 156) {
                log = "3 : Player right, Obstacle right, Enemy center, move Enemy last"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 157) {
                log = "3 : Player right, Obstacle right, Enemy center, move Enemy out-1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -MOVE_ONE - 1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 158) {
                log = "2 : Player right, Obstacle right, Enemy center out, wait Obstacle"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 159) {
                log = "3 : Player right, Obstacle right, Enemy left, move Enemy last"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 160) {
                log = "3 : Player right, Obstacle right, Enemy left, move Enemy out-1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(OUT, -MOVE_TWO - 1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 161) {
                log = "2 : Player right, Obstacle right, Enemy left out, wait Obstacle"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(OUT, -MOVE_TWO)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 162) {
                log = "3 : Player right, Obstacle center, Enemy left, move Obstacle last"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 163) {
                log = "3 : Player right, Obstacle center, Enemy left, move Obstacle out-1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_TWO - 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE - 1)
            } else if (testNumber == 164) {
                log = "3 : Player right, Obstacle center, Enemy left, move Obstacle (equal)"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 165) {
                log = "3 : Player right, Obstacle center, Enemy left, move Enemy last"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE - 1)
            } else if (testNumber == 166) {
                log = "3 : Player right, Obstacle center, Enemy left, move Enemy out-1"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(OUT, -MOVE_TWO - 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE - 1)
            } else if (testNumber == 167) {
                log = "3 : Player right, Obstacle center out, Enemy left, move Enemy last"
                CASE_X_PLAYER(RIGHT)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_TWO)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 168) {
                log = "3 : Player right move left, Obstacle right, Enemy center"
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
                CASE_X_ENEMY(CENTER)
                CASE_Y_ENEMY(OUT, -1)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(IN)
            } else if (testNumber == 169) {
                log = "2 : Player center, Enemy right, Obstacle center, wait Obstacle"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE + 1)
            } else if (testNumber == 170) {
                log = "2 : Player center, Enemy right out, Obstacle center, wait Obstacle"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 171) {
                log = "3 : Player center, Enemy right, Obstacle center, move Enemy last"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 172) {
                log = "3 : Player center, Enemy right, Obstacle center, move Enemy out-1"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE - 1)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 173) {
                log = "3 : Player center, Enemy right, Obstacle left, move Obstacle last"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE - 1)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 174) {
                log = "3 : Player center, Enemy right, Obstacle left, move Obstacle out-1"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE - 1)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE - 1)
            } else if (testNumber == 175) {
                log = "3 : Player center, Enemy right out, Obstacle left, move Obstacle last"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else if (testNumber == 176) {
                log = "3 : Player center, Enemy left, Obstacle right, move Obstacle last (equal)"
                CASE_X_PLAYER(CENTER)
                CASE_X_ENEMY(LEFT)
                CASE_Y_ENEMY(LAST, -MOVE_ONE)
                CASE_X_OBSTACLE(RIGHT)
                CASE_Y_OBSTACLE(LAST, -MOVE_ONE)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(WRONG, log)
        }

        fun TEST_SPEED() {
            val log: String
            Opponent.reinit()
            Opponent.maxResetCount = 4
            RESET_SCENE()
            scene.maxDemoCount = 4 * Opponent.maxResetCount
            scene.demoCount = scene.maxDemoCount / 2

            if (testNumber == 177) {
                log = "Obstacle OK, Enemy OK, out together, last (speed=1.0)"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 178) {
                log = "Obstacle KO, Enemy OK, out together, last (speed=0.5)"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE)
            } else if (testNumber == 179) {
                log = "Obstacle OK, Enemy KO, out together, last (speed=0.5)"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 180) {
                log = "Obstacle KO, Enemy KO, out together, last (speed=0.0)"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE)
            } else if (testNumber == 181) {
                log = "Obstacle OK, Enemy OK, out together, end (score=1.0)"
                Opponent.resetCount = 4
                Opponent.outCount = 2
                Opponent.speedCount = 2
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 182) {
                log = "Obstacle KO, Enemy OK, out together, end (score=0.75)"
                Opponent.resetCount = 4
                Opponent.outCount = 2
                Opponent.speedCount = 2
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE)
            } else if (testNumber == 183) {
                log = "Obstacle OK, Enemy KO, out together, end (score=0.75)"
                Opponent.resetCount = 4
                Opponent.outCount = 2
                Opponent.speedCount = 2
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 184) {
                log = "Obstacle KO, Enemy KO, out together, end (score=0.5)"
                Opponent.resetCount = 4
                Opponent.outCount = 2
                Opponent.speedCount = 2
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE)
            } else if (testNumber == 185) {
                log = "Obstacle OK, Enemy OK, out together, last Enemy (speed=1.0)"
                Opponent.resetCount = 3
                Opponent.outCount = 1
                Opponent.speedCount = 1
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 186) {
                log = "Obstacle KO, Enemy OK, out together, last Enemy (speed=1.0)"
                Opponent.resetCount = 3
                Opponent.outCount = 1
                Opponent.speedCount = 1
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE)
            } else if (testNumber == 187) {
                log = "Obstacle OK, Enemy KO, out together, last Enemy (speed=0.5)"
                Opponent.resetCount = 3
                Opponent.outCount = 1
                Opponent.speedCount = 1
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 188) {
                log = "Obstacle KO, Enemy KO, out together, last Enemy (speed=0.5)"
                Opponent.resetCount = 3
                Opponent.outCount = 1
                Opponent.speedCount = 1
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE)
            } else if (testNumber == 189) {
                log = "Obstacle OK (first, speed=0.5), Enemy OK (second, speed=1.0), last"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_TWO)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 190) {
                log = "Obstacle KO (first, speed=0.0), Enemy OK (second, speed=0.5), last"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_TWO)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE)
            } else if (testNumber == 191) {
                log = "Obstacle OK (first, speed=0.5), Enemy KO (second, speed=0.5), last"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_TWO)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
            } else if (testNumber == 192) {
                log = "Obstacle KO (first, speed=0.0), Enemy KO (second, speed=0.0), last"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_TWO)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_ONE)
                BUMP_OPPONENT(OBSTACLE)
            } else if (testNumber == 193) {
                log = "Enemy OK (first, speed=0.5), Obstacle OK (second, speed=1.0), last"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_TWO)
            } else if (testNumber == 194) {
                log = "Enemy OK (first, speed=0.5), Obstacle KO (second, speed=0.5), last"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_TWO)
                BUMP_OPPONENT(OBSTACLE)
            } else if (testNumber == 195) {
                log = "Enemy KO (first, speed=0.0), Obstacle OK (second, speed=0.5), last"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_TWO)
            } else if (testNumber == 196) {
                log = "Enemy KO (first, speed=0.0), Obstacle KO (second, speed=0.0), last"
                Opponent.resetCount = 2
                Opponent.outCount = 0
                Opponent.speedCount = 0
                PRERESET_CHARACTERS()
                CASE_X_PLAYER(LEFT)
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(OUT, -MOVE_ONE)
                BUMP_OPPONENT(ENEMY)
                CASE_X_OBSTACLE(CENTER)
                CASE_Y_OBSTACLE(OUT, -MOVE_TWO)
                BUMP_OPPONENT(OBSTACLE)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(SPEED, log)
            ADD_LOG(SPEED, "stop", "\n" + Opponent.getLog())
        }

        fun TEST_SOUND() {
            val log: String
            Opponent.reinit()
            RESET_SCENE()
            scene.isDemo = false

            fun PLACE_OPPONENTS() {
                CASE_X_ENEMY(RIGHT)
                CASE_Y_ENEMY(LAST)
                CASE_X_OBSTACLE(LEFT)
                CASE_Y_OBSTACLE(LAST)
            }

            fun REINIT_SOUNDS() {
                PRERESET_CHARACTERS()
                sounds.reinit()
                sounds.update()
                PLACE_OPPONENTS()
            }

            PLACE_OPPONENTS()

            if (testNumber == 197) {
                log = "ok : vacuum"
                REINIT_SOUNDS()
                sounds.setPlayEventVacuum()
            } else if (testNumber == 198) {
                log = "ok : warp Player (ko : vacuum)"
                sounds.setPlayEventWarp(player)
            } else if (testNumber == 199) {
                log = "ok : warp Player Enemy (ko : vacuum)"
                sounds.setPlayEventWarp(enemy)
            } else if (testNumber == 200) {
                log = "ok : warp Player Enemy Obstacle (ko : vacuum)"
                sounds.setPlayEventWarp(obstacle)
            } else if (testNumber == 201) {
                log = "ok : move (ko : vacuum, warp Player Enemy Obstacle)"
                sounds.setPlayEventMove()
            } else if (testNumber == 202) {
                log = "ok : bump (ko : vacuum, warp Player Enemy Obstacle, move)"
                sounds.setPlayEventBump()
            } else if (testNumber == 203) {
                log = "ok : cutscene (ko : vacuum, warp Player Enemy Obstacle, move, bump)"
                sounds.setPlayEventCutscene()
            } else if (testNumber == 204) {
                log = "(pause) (ko : vacuum, warp Player Enemy Obstacle, move, bump, cutscene)"
                sounds.setPauseEvent()
            } else if (testNumber == 205) {
                log = "(resume) ok : cutscene (ko : vacuum, warp Player Enemy Obstacle, move, bump)"
                sounds.setResumeEvent()
            } else if (testNumber == 206) {
                log = "ok : bump (ko : vacuum, warp Player Enemy Obstacle, move)"
                sounds.setStopEventCutscene()
            } else if (testNumber == 207) {
                log = "ok : move (ko : vacuum, warp Player Enemy Obstacle)"
                sounds.setStopEventBump()
            } else if (testNumber == 208) {
                log = "ok : warp Player Enemy Obstacle (ko : vacuum)"
                sounds.setStopEventMove()
            } else if (testNumber == 209) {
                log = "ok : warp Player Enemy (ko : vacuum)"
                sounds.setStopEventWarp(obstacle)
            } else if (testNumber == 210) {
                log = "ok : warp Player (ko : vacuum)"
                sounds.setStopEventWarp(enemy)
            } else if (testNumber == 211) {
                log = "ok : vacuum"
                sounds.setStopEventWarp(player)
            } else if (testNumber == 212) {
                log = "nothing"
                sounds.setStopEventVacuum()
            } else if (testNumber == 213) {
                log = "ok : bump (ko : vacuum, warp Player Enemy)"
                REINIT_SOUNDS()
                sounds.setPlayEventBump()
                sounds.setPlayEventWarp(enemy)
                sounds.setPlayEventWarp(player)
                sounds.setPlayEventVacuum()
            } else if (testNumber == 214) {
                log = "ok : move (ko : vacuum, warp Player Enemy)"
                sounds.setPlayEventMove()
                sounds.setStopEventBump()
            } else if (testNumber == 215) {
                log = "ok : warp Obstacle (ko : vacuum)"
                sounds.setPlayEventWarp(obstacle)
                sounds.setStopEventWarp(enemy)
                sounds.setStopEventMove()
                sounds.setStopEventWarp(player)
            } else if (testNumber == 216) {
                log = "ok : move (ko : vacuum)"
                sounds.setPlayEventMove()
                sounds.setStopEventWarp(obstacle)
            } else if (testNumber == 217) {
                log = "ok : cutscene (ko : vacuum)"
                sounds.setPlayEventCutscene()
                sounds.setStopEventMove()
            } else if (testNumber == 218) {
                log = "ok : cutscene (ko : vacuum, move)"
                sounds.setPlayEventMove()
            } else if (testNumber == 219) {
                log = "ok : move (ko : vacuum, warp Obstacle)"
                sounds.setStopEventMove()
                sounds.setPlayEventMove()
                sounds.setPlayEventWarp(obstacle)
                sounds.setStopEventCutscene()
            } else if (testNumber == 220) {
                log = "ok : bump (ko : warp Obstacle, move)"
                sounds.setPlayEventBump()
                sounds.setStopEventVacuum()
            } else if (testNumber == 221) {
                log = "ok : move (ko : vacuum, warp Obstacle)"
                sounds.setPlayEventMove()
                sounds.setPlayEventWarp(obstacle)
                sounds.setPlayEventVacuum()
                sounds.setStopEventBump()
            } else if (testNumber == 222) {
                log = "ok : cutscene (ko : vacuum)"
                sounds.setPlayEventCutscene()
                sounds.setStopEventMove()
                sounds.setStopEventWarp(obstacle)
            } else if (testNumber == 223) {
                log = "ok : vacuum"
                sounds.setStopEventCutscene()
                sounds.setStopEventMove()
            } else if (testNumber == 224) {
                log = "nothing"
                sounds.setStopEventVacuum()
            } else {
                log = ""
            }

            sounds.update()

            ADD_TITLE_LOG(SOUND, log)
        }

        fun TEST_SCENE() {
            val log: String
            Opponent.reinit()
            enemy.reinit(board)
            obstacle.reinit(board)
            scene.isIntro = false
            scene.isOutro = false
            scene.isDemo = true
            scene.baseMaxSteps = Opponent.maxResetCount
            scene.maxDemoCount = Opponent.maxResetCount / 2
            scene.demoCount = 0
            scene.demoCaseXCount = 0
            scene.demoCaseX = 1
            button.gameOver = false

            fun TEST_INTRO() {
                scene.isIntro = true
                screen.needReinit = true
                player.setCaseY(board.caseOut, board)
            }

            fun TEST_OUTRO() {
                Opponent.outCount = Opponent.maxResetCount
                player.reinit(board)
                player.updateCounterMax()
                player.setCaseY(board.caseLast, board)
                scene.startOutro()
            }

            fun TEST_NEED_WRONG_MOVE() {
                while (true) {
                    scene.needWrongMove()
                    Opponent.resetCount++

                    if (Opponent.resetCount == Opponent.maxResetCount) {
                        break
                    }
                }
            }

            if (testNumber == 225) {
                log = "updateDemoCount"
                scene.demoCount = 0

                while (true) {
                    scene.updateDemoCount()

                    if (scene.demoCaseXCount == 0 && scene.demoCount == 0) {
                        break
                    }
                }
            } else if (testNumber == 226) {
                log = "needWrongMove (demoCount=0)"
                scene.demoCount = 0
                TEST_NEED_WRONG_MOVE()
            } else if (testNumber == 227) {
                log = "needWrongMove (demoCount=ceil(maxDemoCount/2))"
                scene.demoCount = ceil(float(scene.maxDemoCount) / 2)
                TEST_NEED_WRONG_MOVE()
            } else if (testNumber == 228) {
                log = "needWrongMove (demoCount=maxDemoCount-1)"
                scene.demoCount = scene.maxDemoCount - 1
                TEST_NEED_WRONG_MOVE()
            } else if (testNumber == 229) {
                log = "intro (demo=false, y=0, placeValue=0, placeMax=0, 00/10, 0'00\"00)"
                Opponent.outCount = Opponent.maxResetCount
                Opponent.bumpCount = Opponent.maxResetCount
                scene.isDemo = false
                scene.scrollingY = 0
                scene.placeCounterValue = 0
                scene.placeCounterMax = 0
                button.gameOver = true
                Utility.gameTicks = 0 / Utility.frameTime
                TEST_INTRO()
            } else if (testNumber == 230) {
                log = "intro (demo=false, y=max, placeValue=MAX-1, placeMax=MAX, 07/10, 1'15\"52)"
                Opponent.outCount = Opponent.maxResetCount
                Opponent.bumpCount = Opponent.maxResetCount * 3 / 10
                scene.isDemo = false
                scene.scrollingY = 2 * scrolling.caseH - 1
                scene.placeCounterValue = Int.MAX_VALUE - 1
                scene.placeCounterMax = Int.MAX_VALUE
                button.gameOver = true
                Utility.gameTicks = (1 * 60 * 1000 + 15 * 1000 + 520) / Utility.frameTime
                TEST_INTRO()
            } else if (testNumber == 231) {
                log = "intro (demo=false, y=max, placeValue=MAX/2, placeMax=MAX, 01/10, 0'59\"04)"
                Opponent.outCount = Opponent.maxResetCount
                Opponent.bumpCount = Opponent.maxResetCount * 9 / 10
                scene.isDemo = false
                scene.scrollingY = 2 * scrolling.caseH - 1
                scene.placeCounterValue = Int.MAX_VALUE / 2
                scene.placeCounterMax = Int.MAX_VALUE
                button.gameOver = true
                Utility.gameTicks = (59 * 1000 + 40) / Utility.frameTime
                TEST_INTRO()
            } else if (testNumber == 232) {
                log = "outro (demo=false, steps=fast)"
                Opponent.bumpCount = 0
                Opponent.speedCount = Opponent.maxResetCount - 2
                scene.isDemo = false
                TEST_OUTRO()
            } else if (testNumber == 233) {
                Opponent.bumpCount = 0
                log = "intro after outro (demo=false, 10/10, 9'59\"99)"
                Opponent.outCount = Opponent.maxResetCount
                scene.isDemo = false
                button.gameOver = true
                Utility.gameTicks = (10 * 60 * 1000) / Utility.frameTime
                TEST_INTRO()
            } else if (testNumber == 234) {
                log = "outro (demo=false, steps=slow)"
                Opponent.bumpCount = Opponent.maxResetCount
                Opponent.speedCount = 0
                scene.isDemo = false
                TEST_OUTRO()
            } else if (testNumber == 235) {
                log = "intro after outro (demo=false, 01/10, 1'00\"96)"
                Opponent.outCount = Opponent.maxResetCount
                Opponent.bumpCount = Opponent.maxResetCount * 9 / 10
                scene.isDemo = false
                button.gameOver = true
                Utility.gameTicks = (1 * 60 * 1000 + 960) / Utility.frameTime
                TEST_INTRO()
            } else if (testNumber == 236) {
                log = "outro (demo=false, steps=middle)"
                Opponent.bumpCount = Opponent.maxResetCount / 2
                Opponent.speedCount = Opponent.maxResetCount / 2
                scene.isDemo = false
                TEST_OUTRO()
            } else if (testNumber == 237) {
                log = "intro after outro (base=MOVE_TWO, case=right)"
                scene.baseMaxSteps = MOVE_TWO
                scene.demoCaseXCount = 1
                TEST_INTRO()
            } else if (testNumber == 238) {
                log = "outro (steps=slow, left move right, case=center)"
                Opponent.bumpCount = Opponent.maxResetCount
                Opponent.speedCount = 0
                scene.demoCaseX = CENTER
                TEST_OUTRO()
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
            } else if (testNumber == 239) {
                log = "intro after outro (base=MOVE_ONE, case=right)"
                scene.baseMaxSteps = MOVE_ONE
                scene.demoCaseXCount = 1
                TEST_INTRO()
            } else if (testNumber == 240) {
                log = "outro (steps=fast, left move right, case=left)"
                Opponent.bumpCount = 0
                Opponent.speedCount = Opponent.maxResetCount - 2
                scene.demoCaseX = LEFT
                TEST_OUTRO()
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
            } else if (testNumber == 241) {
                log = "intro after outro (base=MOVE_ONE-1, case=left)"
                scene.baseMaxSteps = MOVE_ONE - 1
                scene.demoCaseXCount = 2
                TEST_INTRO()
            } else if (testNumber == 242) {
                log = "outro (base=2*MOVE_ONE, steps=fast, center move right, case=center)"
                Opponent.bumpCount = 0
                Opponent.speedCount = Opponent.maxResetCount - 2
                scene.baseMaxSteps = 2 * MOVE_ONE
                scene.demoCaseX = CENTER
                TEST_OUTRO()
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
            } else if (testNumber == 243) {
                log = "intro after outro (case=center)"
                scene.demoCaseXCount = 0
                TEST_INTRO()
            } else if (testNumber == 244) {
                log = "outro (base=MOVE_TWO+MOVE_ONE, steps=fast, center move right, case=left)"
                Opponent.bumpCount = 0
                Opponent.speedCount = Opponent.maxResetCount - 2
                scene.baseMaxSteps = MOVE_TWO + MOVE_ONE
                scene.demoCaseX = LEFT
                TEST_OUTRO()
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
            } else if (testNumber == 245) {
                log = "intro after outro (base=MOVE_ONE+1, case=right)"
                scene.baseMaxSteps = MOVE_ONE + 1
                scene.demoCaseXCount = 1
                TEST_INTRO()
            } else if (testNumber == 246) {
                log = "outro (base=2*MOVE_TWO, steps=fast, right move left, case=right)"
                Opponent.bumpCount = 0
                Opponent.speedCount = Opponent.maxResetCount - 2
                scene.baseMaxSteps = 2 * MOVE_TWO
                scene.demoCaseX = RIGHT
                TEST_OUTRO()
                CASE_X_PLAYER(RIGHT)
                MOVE_PLAYER(LEFT)
            } else if (testNumber == 247) {
                log = "intro after outro (base=MOVE_ONE-2, case=right)"
                scene.baseMaxSteps = MOVE_ONE - 2
                scene.demoCaseXCount = 1
                TEST_INTRO()
            } else if (testNumber == 248) {
                log = "outro (base=2*MOVE_TWO-1, steps=fast, left move right, case=left)"
                Opponent.bumpCount = 0
                Opponent.speedCount = Opponent.maxResetCount - 2
                scene.baseMaxSteps = 2 * MOVE_TWO - 1
                scene.demoCaseX = LEFT
                TEST_OUTRO()
                CASE_X_PLAYER(LEFT)
                MOVE_PLAYER(RIGHT)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(SCENE, log)
        }

        fun TEST_MOVE() {
            val log: String
            Opponent.reinit()
            Opponent.resetCount = Opponent.maxResetCount
            Opponent.outCount = Opponent.maxResetCount - 2
            Opponent.speedCount = 0
            RESET_SCENE()
            PRERESET_CHARACTERS()

            if (testNumber == 249) {
                log = "left, set center"
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_PLAYER(LEFT)
                player.setMoveEvent(CENTER, board)
            } else if (testNumber == 250) {
                log = "left to center, set right (hasEvent, eventCaseX=center)"
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_X_PLAYER(LEFT)
                player.setMoveEvent(CENTER, board)
                player.setMoveEvent(RIGHT, board)
            } else if (testNumber == 251) {
                log = "left to center, not set center (hasEvent, eventCaseX=center)"
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_PLAYER(LEFT)
                player.setMoveEvent(CENTER, board)
                player.setMoveEvent(CENTER, board)
            } else if (testNumber == 252) {
                log = "left, not set left"
                CASE_X_ENEMY(CENTER)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_PLAYER(LEFT)
                player.setMoveEvent(LEFT, board)
            } else if (testNumber == 253) {
                log = "left to center, set left (hasEvent, eventCaseX=center)"
                CASE_X_ENEMY(CENTER)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_PLAYER(LEFT)
                player.setMoveEvent(CENTER, board)
                player.setMoveEvent(LEFT, board)
            } else if (testNumber == 254) {
                log = "left move center, set right (isMove, eventCaseX=center)"
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_X_PLAYER(LEFT, 1)
                MOVE_PLAYER(CENTER)
                player.setMoveEvent(RIGHT, board)
            } else if (testNumber == 255) {
                log = "left move center, not set center (isMove, eventCaseX=center)"
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_PLAYER(LEFT, 1)
                MOVE_PLAYER(CENTER)
                player.setMoveEvent(CENTER, board)
            } else if (testNumber == 256) {
                log = "center move right, set center (previousX=nextX, isMove, eventCaseX=right)"
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                player.setMoveEvent(CENTER, board)
            } else if (testNumber == 257) {
                log = "left move center, set left (isMove, eventCaseX=center)"
                CASE_X_ENEMY(CENTER)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_PLAYER(LEFT, 1)
                MOVE_PLAYER(CENTER)
                player.setMoveEvent(LEFT, board)
            } else if (testNumber == 258) {
                log = "left move center to left, not set left (isMove, hasEvent, eventCaseX=left)"
                CASE_X_ENEMY(CENTER)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_PLAYER(LEFT, 1)
                MOVE_PLAYER(CENTER)
                player.setMoveEvent(LEFT, board)
                player.setMoveEvent(LEFT, board)
            } else if (testNumber == 259) {
                log = "center move right to right, set center (isMove, hasEvent, eventCaseX=right)"
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(RIGHT)
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                player.setMoveEvent(RIGHT, board)
                player.setMoveEvent(CENTER, board)
            } else if (testNumber == 260) {
                log = "center move right to center, set right (isMove, hasEvent, eventCaseX=center)"
                CASE_X_ENEMY(LEFT)
                CASE_X_OBSTACLE(CENTER)
                CASE_X_PLAYER(CENTER)
                MOVE_PLAYER(RIGHT)
                player.setMoveEvent(CENTER, board)
                player.setMoveEvent(RIGHT, board)
            } else {
                log = ""
            }

            ADD_TITLE_LOG(MOVE, log)
        }

        fun CHECK_TEST() {
            if (IS(TEST)) {
                if (Opponent.outCount == -1) {
                    PRINT_LOG(TEST)
                    testNumber++

                    if (IS(CASE)) {
                        TEST_CASE()
                    } else if (IS(SPRITE)) {
                        TEST_SPRITE()
                    } else if (IS(WARP)) {
                        TEST_WARP()
                    } else if (IS(BUMP)) {
                        TEST_BUMP()
                    } else if (IS(PATTERN)) {
                        TEST_PATTERN()
                    } else if (IS(DEMO)) {
                        TEST_DEMO()
                    } else if (IS(WRONG)) {
                        TEST_WRONG()
                    } else if (IS(SPEED)) {
                        TEST_SPEED()
                    } else if (IS(SOUND)) {
                        TEST_SOUND()
                    } else if (IS(SCENE)) {
                        TEST_SCENE()
                    } else if (IS(MOVE)) {
                        TEST_MOVE()
                    } else {
                        ADD_TITLE_LOG(DEBUG, "end")
                        PRINT_LOG(DEBUG)
                        button.gameMenu = true
                        button.gameOver = true
                        button.gamePlay = false
                        RESET_SCENE()
                        screen.needReinit = true
                    }

                    testStop = true
                    previousNeedReinit = screen.needReinit
                    previousIsDemo = scene.isDemo
                }

                if (!IS(SOUND)) {
                    sounds.reinit()
                }
            }
        }

        fun CHECK_TEST_STOP() {
            if (IS(TEST)) {
                if (
                    IS(CASE)
                    || IS(SPRITE)
                    || IS(SOUND)
                    || (IS(SCENE) && !scene.isCutscene())
                    || (!IS(SCENE) && Opponent.outCount == Opponent.maxResetCount)
                ) {
                    ADD_LOG(SPEED, "stop", "\n" + Opponent.getLog())
                    button.gamePlay = true
                    button.gameOver = false
                    screen.needReinit = false
                    Opponent.outCount = -1
                }

                if (
                    !player.isMove
                    && (IS(PATTERN) || IS(DEMO) || IS(WRONG))
                    && (player.hasMoveEvent || (IS(WRONG) && scene.wrongMoveKind != 0))
                ) {
                    testStop = true

                    if (IS(WRONG) && scene.wrongMoveKind == 1) {
                        scene.wrongMoveKind = 0
                    }
                }

                if (!player.isBump) {
                    if (enemy.isBump && enemy.counter.isStarted && player.isOn(enemy, board)) {
                        val log: String = enemy.name + ", bump=" + enemy.getStepsBump()
                        ADD_LOG(TEST, "alreadyBump", log)
                        PRINT_LOG(TEST)
                        STOP()
                    }

                    if (
                        obstacle.isBump
                        && obstacle.counter.isStarted
                        && player.isOn(obstacle, board)
                    ) {
                        val log: String = obstacle.name + ", bump=" + obstacle.getStepsBump()
                        ADD_LOG(TEST, "alreadyBump", log)
                        PRINT_LOG(TEST)
                        STOP()
                    }
                }

                if (testStop) {
                    testStop = false

                    if (IS(PATTERN) || IS(DEMO) || IS(WRONG)) {
                        val log: String = ("\n" + obstacle.getLog(board)
                                + "\n" + enemy.getLog(board)
                                + "\n" + player.getLog(board))
                        ADD_LOG(TEST, "stop", log)
                    }

                    PRINT_LOG(TEST)
                    STOP()
                }
            }
        }

        fun CHECK_TEST_CLICK() {
            if (IS(TEST)) {
                if (!button.gamePlay) {
                    STOP()
                } else {
                    UNSTOP()
                }
            }
        }

        fun STOP() {
            button.gamePlay = false
            scene.isDemo = false
        }

        fun UNSTOP() {
            button.gamePlay = true
            scene.isDemo = previousIsDemo

            if (previousNeedReinit) {
                screen.needReinit = true
                previousNeedReinit = false
            } else {
                screen.needReinit = false
            }
        }

        fun NUMBER(number: Int): String {
            return String.format("%-4d", number)
        }

        fun FLOAT(number: Float): String {
            return String.format("%.2f", number)
        }

        fun STRING(string: String): String {
            return String.format("%-8s", string)
        }

        fun CASE_NAME(caseX: Int): String {
            val caseName: String

            if (caseX == 0) {
                caseName = "left"
            } else if (caseX == 1) {
                caseName = "center"
            } else if (caseX == 2) {
                caseName = "right"
            } else {
                caseName = "unknown"
            }

            return caseName
        }
    }
}