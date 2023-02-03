package com.lazygeniouz.air.utils.runtime

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Execute **su** based commands via [java.lang.Runtime.getRuntime].
 */
object Runtime {

    // Well, you know about this guy!
    private val runtime by lazy { java.lang.Runtime.getRuntime() }

    /**
     * Checks if the phone has root access.
     */
    fun hasSu(): Boolean {
        return exec("id").readFirstLine()?.lowercase()?.contains("uid=0") == true
    }

    /**
     * Execute a command as a root user.
     *
     * **Note**: No need to add **su -c** to the command, they will added before the execution.
     */
    fun exec(command: String): Process {
        return runtime.exec("su -c $command")
    }

    /**
     * Run a command for searching something in the data/data directory.
     *
     * This operation requires `--mount-master`.
     */
    fun find(command: String): Process {
        return runtime.exec("su --mount-master -c find /data/data/ $command")
    }

    // Read the first line from the output.
    private fun Process.readFirstLine(): String? {
        return BufferedReader(InputStreamReader(inputStream)).readLine().also { destroy() }
    }

    // Read lines from the output.
    fun Process.readLines(): List<String> {
        return BufferedReader(InputStreamReader(inputStream)).readLines().also { destroy() }
    }
}