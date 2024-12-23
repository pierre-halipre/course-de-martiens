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

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer

class Sound(soundId: Int, maxVolume: Float) {
    var name: String = ""
    val sound: MediaPlayer = MediaPlayer()
    val fileId: AssetFileDescriptor = Utility.resources.openRawResourceFd(soundId)
    val maxVolume: Float = maxVolume
    var volume: Int = 0
    var playing: Boolean = false
    var muted: Boolean = false
    var paused: Boolean = false
    var hasPlayEvent: Boolean = false
    var hasStopEvent: Boolean = false
    var hasMuteEvent: Boolean = false
    var hasUnmuteEvent: Boolean = false
    var hasPauseEvent: Boolean = false
    var hasResumeEvent: Boolean = false

    init {
        sound.setDataSource(fileId)
        sound.setLooping(true)
        sound.setVolume(0F, 0F)
        sound.prepare()
        sound.start()
    }

    fun reinit() {
        playing = false
        muted = false
        paused = false
        hasPlayEvent = false
        hasStopEvent = false
        hasMuteEvent = false
        hasUnmuteEvent = false
        hasPauseEvent = false
        hasResumeEvent = false
    }

    fun free() {
        sound.stop()
        sound.reset()
        sound.release()
    }

    fun setVolume() {
        if (volume == 0 && playing && !muted && !paused) {
            sound.setVolume(maxVolume, maxVolume)
            volume = 1
        } else if (volume == 1 && (!playing || muted || paused)) {
            sound.setVolume(0F, 0F)
            volume = 0
        }
    }

    fun update() {
        val previousVolume: Int = volume
        val previousPlaying: Boolean = playing
        val previousMuted: Boolean = muted
        val previousPaused: Boolean = paused

        if (hasStopEvent) {
            hasStopEvent = false
            playing = false
        }

        if (hasPlayEvent) {
            hasPlayEvent = false
            playing = true
        }

        if (hasUnmuteEvent) {
            hasUnmuteEvent = false
            muted = false
        }

        if (hasMuteEvent) {
            hasMuteEvent = false
            muted = true
        }

        if (hasResumeEvent) {
            hasResumeEvent = false
            paused = false
        }

        if (hasPauseEvent) {
            hasPauseEvent = false
            paused = true
        }

        setVolume()

        val currentVolume: Int = volume
        val currentPlaying: Boolean = playing
        val currentMuted: Boolean = muted
        val currentPaused: Boolean = paused

        if (
            currentVolume != previousVolume
            || currentPlaying != previousPlaying
            || currentMuted != previousMuted
            || currentPaused != previousPaused
        ) {
            val log: String = (name
                    + ", volume=" + previousVolume + "->" + currentVolume
                    + ", playing=" + previousPlaying + "->" + currentPlaying
                    + ", muted=" + previousMuted + "->" + currentMuted
                    + ", paused=" + previousPaused + "->" + currentPaused)
            Test.ADD_LOG(Test.SOUND, "update", log)
        }
    }

    fun isPlaying(): Boolean {
        val result: Boolean

        if ((playing && !hasStopEvent) || hasPlayEvent) {
            result = true
        } else {
            result = false
        }

        return result
    }

    fun setPlayEvent() {
        hasPlayEvent = true
    }

    fun setStopEvent() {
        hasStopEvent = true
    }

    fun setMuteEvent() {
        hasMuteEvent = true
    }

    fun setUnmuteEvent() {
        hasUnmuteEvent = true
    }

    fun setPauseEvent() {
        hasPauseEvent = true
    }

    fun setResumeEvent() {
        hasResumeEvent = true
    }
}

class Sounds {
    val cutscene: Sound = Sound(R.raw.cutscene, 0.5F)
    val bump: Sound = Sound(R.raw.bump, 0.25F)
    val move: Sound = Sound(R.raw.move, 0.125F)
    val warp: Sound = Sound(R.raw.warp, 0.25F)
    val vacuum: Sound = Sound(R.raw.vacuum, 0.125F)
    var hasWarpPlayer: Boolean = false
    var hasWarpEnemy: Boolean = false
    var hasWarpObstacle: Boolean = false
    var hasPauseEvent: Boolean = false
    var hasResumeEvent: Boolean = false

    init {
        cutscene.name = "Cutscene"
        bump.name = "Bump"
        move.name = "Move"
        warp.name = "Warp"
        vacuum.name = "Vacuum"
    }

    fun reinit() {
        cutscene.reinit()
        bump.reinit()
        move.reinit()
        warp.reinit()
        vacuum.reinit()
        hasWarpPlayer = false
        hasWarpEnemy = false
        hasWarpObstacle = false
        hasPauseEvent = false
        hasResumeEvent = false
    }

    fun free() {
        cutscene.free()
        bump.free()
        move.free()
        warp.free()
        vacuum.free()
    }

    fun update() {
        if (hasResumeEvent) {
            hasResumeEvent = false
            cutscene.setResumeEvent()
            bump.setResumeEvent()
            move.setResumeEvent()
            warp.setResumeEvent()
            vacuum.setResumeEvent()
        }

        if (hasPauseEvent) {
            hasPauseEvent = false
            cutscene.setPauseEvent()
            bump.setPauseEvent()
            move.setPauseEvent()
            warp.setPauseEvent()
            vacuum.setPauseEvent()
        }

        checkStopEventCutscene()
        checkPlayEventCutscene()
        checkStopEventBump()
        checkPlayEventBump()
        checkStopEventMove()
        checkPlayEventMove()
        checkStopEventWarp()
        checkPlayEventWarp()
        checkStopEventVacuum()
        checkPlayEventVacuum()

        cutscene.update()
        bump.update()
        move.update()
        warp.update()
        vacuum.update()
    }

    fun setPlayEventCutscene() {
        cutscene.setPlayEvent()
    }

    fun setStopEventCutscene() {
        cutscene.setStopEvent()
    }

    fun checkPlayEventCutscene() {
        if (cutscene.hasPlayEvent) {
            bump.setMuteEvent()
            move.setMuteEvent()
            warp.setMuteEvent()
            vacuum.setMuteEvent()
        }
    }

    fun checkStopEventCutscene() {
        if (cutscene.hasStopEvent) {
            bump.setUnmuteEvent()

            if (!bump.isPlaying()) {
                move.setUnmuteEvent()

                if (!move.isPlaying()) {
                    warp.setUnmuteEvent()

                    if (!warp.isPlaying()) {
                        vacuum.setUnmuteEvent()
                    }
                }
            }
        }
    }

    fun setPlayEventBump() {
        bump.setPlayEvent()
    }

    fun setStopEventBump() {
        bump.setStopEvent()
    }

    fun checkPlayEventBump() {
        if (bump.hasPlayEvent) {
            if (cutscene.isPlaying()) {
                bump.setMuteEvent()
            }

            move.setMuteEvent()
            warp.setMuteEvent()
            vacuum.setMuteEvent()
        }
    }

    fun checkStopEventBump() {
        if (bump.hasStopEvent) {
            if (!cutscene.isPlaying()) {
                move.setUnmuteEvent()

                if (!move.isPlaying()) {
                    warp.setUnmuteEvent()

                    if (!warp.isPlaying()) {
                        vacuum.setUnmuteEvent()
                    }
                }
            }
        }
    }

    fun setPlayEventMove() {
        move.setPlayEvent()
    }

    fun setStopEventMove() {
        move.setStopEvent()
    }

    fun checkPlayEventMove() {
        if (move.hasPlayEvent) {
            if (cutscene.isPlaying() || bump.isPlaying()) {
                move.setMuteEvent()
            }

            warp.setMuteEvent()
            vacuum.setMuteEvent()
        }
    }

    fun checkStopEventMove() {
        if (move.hasStopEvent) {
            if (!cutscene.isPlaying() && !bump.isPlaying()) {
                warp.setUnmuteEvent()

                if (!warp.isPlaying()) {
                    vacuum.setUnmuteEvent()
                }
            }
        }
    }

    fun setPlayEventWarp(character: Character) {
        if (!hasWarpPlayer && !hasWarpEnemy && !hasWarpObstacle) {
            warp.setPlayEvent()
        }

        if (character is Player) {
            hasWarpPlayer = true
        } else if (character is Enemy) {
            hasWarpEnemy = true
        } else {
            hasWarpObstacle = true
        }
    }

    fun setStopEventWarp(character: Character) {
        if (character is Player) {
            hasWarpPlayer = false
        } else if (character is Enemy) {
            hasWarpEnemy = false
        } else {
            hasWarpObstacle = false
        }

        if (!hasWarpPlayer && !hasWarpEnemy && !hasWarpObstacle) {
            warp.setStopEvent()
        }
    }

    fun checkPlayEventWarp() {
        if (warp.hasPlayEvent) {
            if (cutscene.isPlaying() || bump.isPlaying() || move.isPlaying()) {
                warp.setMuteEvent()
            }

            vacuum.setMuteEvent()
        }
    }

    fun checkStopEventWarp() {
        if (warp.hasStopEvent) {
            if (!cutscene.isPlaying() && !bump.isPlaying() && !move.isPlaying()) {
                vacuum.setUnmuteEvent()
            }
        }
    }

    fun setPlayEventVacuum() {
        vacuum.setPlayEvent()
    }

    fun setStopEventVacuum() {
        vacuum.setStopEvent()
    }

    fun checkPlayEventVacuum() {
        if (vacuum.hasPlayEvent) {
            if (cutscene.isPlaying() || bump.isPlaying() || move.isPlaying() || warp.isPlaying()) {
                vacuum.setMuteEvent()
            }
        }
    }

    fun checkStopEventVacuum() {
        if (vacuum.hasStopEvent) {
            vacuum.setStopEvent()
        }
    }

    fun setPauseEvent() {
        hasPauseEvent = true
    }

    fun setResumeEvent() {
        hasResumeEvent = true
    }
}