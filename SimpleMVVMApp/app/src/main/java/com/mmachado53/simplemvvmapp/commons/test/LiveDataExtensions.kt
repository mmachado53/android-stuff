package com.mmachado53.simplemvvmapp.commons.test

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * https://github.com/android/architecture-components-samples/blob/da5103e6f904513bd08eac1bb5cc17dfbf81fbcd/LiveDataSample/app/src/test/java/com/android/example/livedatabuilder/util/LiveDataTestUtil.kt#L30
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

@Throws(InterruptedException::class)
fun <T> getOrAwaitValue(liveData: LiveData<T>): T? {
    val data = arrayOfNulls<Any>(1)
    val latch = CountDownLatch(1)
    val observer: Observer<T> = object : Observer<T> {
        override fun onChanged(@Nullable o: T) {
            data[0] = o
            latch.countDown()
            liveData.removeObserver(this)
        }
    }
    liveData.observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)
    return data[0] as T?
}
