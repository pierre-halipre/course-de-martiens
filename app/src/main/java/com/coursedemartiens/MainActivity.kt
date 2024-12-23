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
import android.os.Bundle

private const val TEST = false

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Test.SET(TEST)
        Test.PRINT_DEBUG("onCreate", "begin")
        super.onCreate(savedInstanceState)
        val screen: Screen = Screen(this, this)
        setContentView(screen)
        Test.PRINT_DEBUG("onCreate", "end")
    }

    override fun onStop() {
        Test.PRINT_DEBUG("onStop", "begin")
        super.onStop()
        Test.PRINT_DEBUG("onStop", "end")
    }

    override fun onDestroy() {
        Test.PRINT_DEBUG("onDestroy", "begin")
        super.onDestroy()
        Test.PRINT_DEBUG("onDestroy", "end")
        exit()
    }

    override fun finish() {
        Test.PRINT_DEBUG("finish", "begin")
        super.finish()
        Test.PRINT_DEBUG("finish", "end")
    }
}