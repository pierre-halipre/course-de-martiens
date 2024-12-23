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

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceView

class Screen(context: Context, activity: Activity) : SurfaceView(context), Runnable {
    val activity: Activity = activity
    val thread: Thread = Thread(this)
    var running: Boolean = false
    var needReinit: Boolean = false
    var hasEvent: Boolean = false
    var xEvent: Int = 0
    var yEvent: Int = 0
    var paint: Paint = Paint()

    val background: Background
    val button: Button
    val scrolling: Scrolling
    val text: Text
    val number: Number
    val board: Board
    val scene: Scene
    val player: Player
    val enemy: Enemy
    val obstacle: Obstacle
    val opponents: List<Opponent>

    init {
        Utility.init(context)

        val smallCaseW: Int = Utility.unitCaseW
        val smallCaseH: Int = Utility.unitCaseH
        val tallCaseW: Int = 4 * Utility.unitCaseW
        val tallCaseH: Int = 4 * Utility.unitCaseH
        val buttonsCaseW: Int = 6 * Utility.unitCaseW
        val buttonsCaseH: Int = 4 * Utility.unitCaseH
        val textCaseW: Int = 12 * Utility.unitCaseW
        val textCaseH: Int = 2 * Utility.unitCaseH

        background = Background(smallCaseW, smallCaseH, 14, 6, Utility.x, Utility.y)
        button = Button(
            buttonsCaseW, buttonsCaseH, 2, 1, background.x + smallCaseW, background.y + smallCaseH
        )
        scrolling = Scrolling(
            smallCaseW, smallCaseH, 14, 18, background.x, background.y + background.h
        )
        board = Board(
            tallCaseW, tallCaseH, 3, 4, scrolling.x + smallCaseW, scrolling.y + smallCaseH
        )
        text = Text(textCaseW, textCaseH, 1, 6, board.x, board.y)
        number = Number(smallCaseW, textCaseH, 12, 6, text.x, text.y)
        scene = Scene(board)
        player = Player(board, scrolling)
        enemy = Enemy(player, board, scrolling)
        obstacle = Obstacle(enemy, board, scrolling)
        opponents = listOf<Opponent>(enemy, obstacle)

        start()
    }

    fun start() {
        var log: String = "thread "

        try {
            thread.start()
            log += "OK"
            Test.PRINT_DEBUG("start", log)
        } catch (e: IllegalThreadStateException) {
            log += "KO"
            Test.PRINT_DEBUG("start", log)
            exit()
        }
    }

    fun stop() {
        var log: String = "thread "

        try {
            thread.join()
            log += "OK"
            Test.PRINT_DEBUG("stop", log)
        } catch (e: InterruptedException) {
            log += "KO"
            Test.PRINT_DEBUG("stop", log)
            exit()
        }
    }

    fun reinit() {
        Utility.resetGameTicks()
        Utility.resetPreviousDate()
        Utility.sounds.reinit()
        Opponent.reinit()

        player.reinit(board)
        enemy.reinit(board)
        obstacle.reinit(board)
        scrolling.reinit()
        scene.updateDemoCount()
        scene.startIntro()
    }

    override fun run() {
        running = true
        reinit()

        Egg.set(player, enemy, obstacle, text, board)
        Test.INIT(player, enemy, obstacle, board, scrolling, scene, button, this)

        while (running) {
            if (Utility.notifyGame()) {
                if (hasEvent) {
                    updateEvent()
                    hasEvent = false
                }

                if (button.gamePlay || scene.isDemo) {
                    if (needReinit) {
                        reinit()
                        needReinit = false
                        Egg.switch(player, enemy, obstacle)
                    }

                    Utility.updateGameTicks()

                    if (scene.isCutscene()) {
                        if (scene.isIntro) {
                            scene.updateIntro(player, scrolling, board)
                        } else {
                            scene.updateOutro(player, scrolling, board)

                            if (!scene.isOutro) {
                                needReinit = true

                                if (!scene.isDemo) {
                                    button.gameOver = true
                                    button.gamePlay = false
                                    Utility.sounds.setPauseEvent()
                                }
                            }
                        }
                    }

                    player.performUpdate(board)

                    if (!scene.isCutscene() && !needReinit) {
                        enemy.performUpdate(board, scene)
                        obstacle.performUpdate(board, scene)

                        player.performBump(enemy, obstacle, board)

                        enemy.performReset(player, obstacle, board)
                        obstacle.performReset(player, enemy, board)

                        enemy.performOut(player, obstacle, scrolling)
                        obstacle.performOut(player, obstacle, scrolling)

                        if (Opponent.isMaxOutCount()) {
                            scene.startOutro()
                        }
                    }

                    scrolling.update()

                    Test.CHECK_TEST()

                    scene.updateDemo(player, enemy, obstacle, board)

                    Test.CHECK_TEST_STOP()
                }

                draw()
                Utility.sounds.update()
            }
        }

        Utility.free()
    }

    private fun draw() {
        try {
            if (holder.surface.isValid) {
                val canvas: Canvas = holder.lockCanvas()
                canvas.drawARGB(255, 25, 17, 55)
                scrolling.draw(canvas, paint)
                player.drawPlaceOnBoard(board, canvas, paint)
                enemy.drawOnBoardAndScrolling(board, scrolling, canvas, paint)
                obstacle.drawOnBoardAndScrolling(board, scrolling, canvas, paint)
                player.drawOnBoardAndScrolling(board, scrolling, canvas, paint)
                background.draw(canvas, paint)
                button.draw(canvas, paint)

                if (!button.gamePlay) {
                    scrolling.drawTransparency(canvas, paint)

                    if (button.gameMenu) {
                        text.drawTitle(canvas, paint)
                    } else if (button.gameOver) {
                        text.drawNumbers(canvas, paint)
                        number.draw(canvas, paint)
                    }
                }

                holder.unlockCanvasAndPost(canvas)
            } else {
                Test.PRINT_DEBUG("draw", "error")
            }
        } catch (e: Exception) {
            Test.PRINT_DEBUG("draw", "exception")
            exit()
        }
    }

    fun updateEvent() {
        var log: String = ""

        if (board.isInside(xEvent, yEvent)) {
            if (board.isInsidePlace(yEvent)) {
                if (button.gamePlay) {
                    player.setMoveEvent(board.toCaseX(xEvent), board)
                }
            } else {
                Egg.check(player, enemy, obstacle, text)
            }

            log += "board"
        } else if (button.isInside(xEvent, yEvent)) {
            val caseX: Int = button.toCaseX(xEvent)
            val caseY: Int = button.toCaseY(yEvent)
            button.checkGamePlay(caseX, caseY)
            button.checkGameQuit(caseX, caseY)

            if (button.gameQuit) {
                log += "quit"
                running = false
            } else if (button.gameMenu || (button.gamePlay && button.gameOver)) {
                needReinit = true
                log += "reinit="

                if (button.gamePlay && button.gameOver) {
                    if (button.gameMenu) {
                        button.gameMenu = false
                    }

                    button.gameOver = false
                    scene.isDemo = false
                    log += "play"
                } else {
                    if (!button.gameOver) {
                        button.gameOver = true
                    }

                    if (button.gamePlay) {
                        button.gamePlay = false
                    }

                    scene.isDemo = true
                    log += "menu"
                }
            } else {
                if (button.gamePlay) {
                    Utility.sounds.setResumeEvent()
                } else {
                    Utility.sounds.setPauseEvent()
                }

                Utility.resetPreviousDate()
                log += "playing=" + button.gamePlay
            }

            Test.CHECK_TEST_CLICK()
        } else {
            log += "nothing"
        }

        Test.PRINT_DEBUG("updateEvent", log)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        if (!hasEvent) {
            xEvent = int(motionEvent.x)
            yEvent = int(motionEvent.y)

            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val willGameQuit: Boolean = button.willGameQuit(xEvent, yEvent)
                hasEvent = true

                if (willGameQuit) {
                    stop()
                    activity.finish()
                }
            } else if (
                motionEvent.action == MotionEvent.ACTION_MOVE
                && board.isInside(xEvent, yEvent)
                && board.isInsidePlace(yEvent)
            ) {
                hasEvent = true
            }
        }

        performClick()

        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}