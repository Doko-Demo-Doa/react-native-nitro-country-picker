package com.margelo.nitro.nitrocountrypicker

import android.util.Log
import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class NitroCountryPickerPackage : BaseReactPackage() {
    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        return null
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
        return ReactModuleInfoProvider {
          val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()
          moduleInfos[NitroCountryPicker.NAME] = ReactModuleInfo(
            NitroCountryPicker.NAME,
            NitroCountryPicker.NAME,
            false,
            needsEagerInit = true,
            isCxxModule = false,
            isTurboModule = true
          )
          moduleInfos
        }
    }

    companion object {
        init {
            System.loadLibrary("nitrocountrypicker")
            Log.d(NitroCountryPicker.NAME, "Hello from init")
        }
    }
}
