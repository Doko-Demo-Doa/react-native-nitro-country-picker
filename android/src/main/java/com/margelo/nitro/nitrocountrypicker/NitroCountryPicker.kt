package com.margelo.nitro.nitrocountrypicker
  
import com.facebook.proguard.annotations.DoNotStrip

@DoNotStrip
class NitroCountryPicker : HybridNitroCountryPickerSpec() {
  override fun multiply(a: Double, b: Double): Double {
    return a * b
  }
}
