package com.margelo.nitro.nitrocountrypicker

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.facebook.proguard.annotations.DoNotStrip
import com.facebook.react.bridge.UiThreadUtil
import com.margelo.nitro.NitroModules
import com.margelo.nitro.core.NullType
import com.margelo.nitro.core.Promise
import java.util.concurrent.atomic.AtomicInteger


@DoNotStrip
class NitroCountryPicker : HybridNitroCountryPickerSpec() {
  companion object {
    const val NAME = "NitroCountryPicker"

    @Volatile
    private var lastPickedCountry: IPickedCountry? = null

    @Volatile
    private var pendingPickPromise: Promise<Variant_NullType_IPickedCountry>? = null

    private val launcherCounter = AtomicInteger(0)

    private fun setLastPickedCountry(country: IPickedCountry) {
      lastPickedCountry = country
    }

    @Synchronized
    private fun resolvePendingPick(country: IPickedCountry?) {
      val promise = pendingPickPromise ?: return
      pendingPickPromise = null
      val result = if (country == null) {
        Variant_NullType_IPickedCountry.create(NullType.NULL)
      } else {
        Variant_NullType_IPickedCountry.create(country)
      }
      promise.resolve(result)
    }

    @Synchronized
    private fun setPendingPickPromise(promise: Promise<Variant_NullType_IPickedCountry>): Boolean {
      if (pendingPickPromise != null) {
        return false
      }
      pendingPickPromise = promise
      return true
    }

    @Synchronized
    private fun rejectPendingPick(error: Throwable) {
      val promise = pendingPickPromise ?: return
      pendingPickPromise = null
      promise.reject(error)
    }

    private fun nextLauncherKey(): String {
      return "NitroCountryPicker#${launcherCounter.incrementAndGet()}"
    }
  }

  override fun pickCountry(options: PickCountryOptions?): Promise<Variant_NullType_IPickedCountry> {
    val activity = NitroModules.applicationContext?.currentActivity
      ?: return Promise.rejected<Variant_NullType_IPickedCountry>(
        IllegalStateException("No current Activity available to present country picker.")
      )
    val componentActivity = activity as? ComponentActivity
      ?: return Promise.rejected<Variant_NullType_IPickedCountry>(
        IllegalStateException("Current Activity must be a ComponentActivity to register for activity results.")
      )

    val promise = Promise<Variant_NullType_IPickedCountry>()
    val accepted = setPendingPickPromise(promise)
    if (!accepted) {
      return Promise.rejected<Variant_NullType_IPickedCountry>(
        IllegalStateException("Country picker is already open.")
      )
    }

    UiThreadUtil.runOnUiThread {
      var launcher: ActivityResultLauncher<Intent>? = null
      try {
        val intent = Intent(activity, CountryPickerActivity::class.java)
        val headerTitle = options?.headerTitle?.trim()?.takeIf { it.isNotEmpty() }
        if (headerTitle != null) {
          intent.putExtra(CountryPickerActivity.EXTRA_HEADER_TITLE, headerTitle)
        }

        launcher = componentActivity.activityResultRegistry.register(
          nextLauncherKey(),
          ActivityResultContracts.StartActivityForResult()
        ) { result ->
          try {
            val picked = CountryPickerActivity.intentResultToPickedCountry(result.resultCode, result.data)
            if (picked != null) {
              setLastPickedCountry(picked)
            }
            resolvePendingPick(picked)
          } catch (error: Throwable) {
            rejectPendingPick(error)
          } finally {
            launcher?.unregister()
            launcher = null
          }
        }

        launcher?.launch(intent)
      } catch (error: Throwable) {
        try {
          launcher?.unregister()
        } catch (_: Throwable) {
          // Ignore cleanup errors while handling the original failure.
        }
        rejectPendingPick(error)
      }
    }

    return promise
  }

  override fun getLastPickedCountry(): Variant_NullType_IPickedCountry {
    val picked = lastPickedCountry
    return if (picked == null) {
      Variant_NullType_IPickedCountry.create(NullType.NULL)
    } else {
      Variant_NullType_IPickedCountry.create(picked)
    }
  }
}
