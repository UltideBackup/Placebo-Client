package com.placebo.Utils

class DelayHelper {
    private var task: (() -> Unit)? = null
    private var executionTime: Long = 0L
    fun runAfter(ms: Long, block: () -> Unit) {
        executionTime = System.currentTimeMillis() + ms
        task = block
    }
    fun tick() {
        val currentTask = task ?: return
        if (System.currentTimeMillis() >= executionTime) {
            task = null
            currentTask.invoke()
        }
    }

    fun isPending(): Boolean = task != null
}