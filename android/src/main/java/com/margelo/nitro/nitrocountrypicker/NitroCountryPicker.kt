package com.margelo.nitro.nitrocountrypicker

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.proguard.annotations.DoNotStrip
import com.facebook.react.bridge.UiThreadUtil
import com.mukesh.countrypicker.CountryPicker
import com.margelo.nitro.NitroModules


@DoNotStrip
class NitroCountryPicker : HybridNitroCountryPickerSpec() {
  override fun multiply(a: Double, b: Double): Double {
    return a * b
  }

  override fun show() {
    val builder =
      CountryPicker.Builder().with(NitroModules.applicationContext!!.baseContext)
        .listener {
            it ->
          Toast.makeText(NitroModules.applicationContext, it.name, Toast.LENGTH_SHORT).show()
          // Code...
        }

    val picker = builder.build();

    UiThreadUtil.runOnUiThread(Runnable {
      picker.showDialog(NitroModules.applicationContext!!.currentActivity as AppCompatActivity)
    })
  }
}
