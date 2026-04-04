package com.margelo.nitro.nitrocountrypicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rejowan.ccpc.CCPUtils
import com.rejowan.ccpc.Country
import com.rejowan.ccpc.CountryPickerDialog
import com.rejowan.ccpc.PickerCustomization

class CountryPickerActivity : ComponentActivity() {
  companion object {
    const val EXTRA_HEADER_TITLE = "com.margelo.nitro.nitrocountrypicker.HEADER_TITLE"
    private const val EXTRA_RESULT_NAME = "com.margelo.nitro.nitrocountrypicker.RESULT_NAME"
    private const val EXTRA_RESULT_DIAL_CODE = "com.margelo.nitro.nitrocountrypicker.RESULT_DIAL_CODE"
    private const val EXTRA_RESULT_CODE = "com.margelo.nitro.nitrocountrypicker.RESULT_CODE"

    fun intentResultToPickedCountry(resultCode: Int, data: Intent?): IPickedCountry? {
      if (resultCode != Activity.RESULT_OK || data == null) {
        return null
      }

      val name = data.getStringExtra(EXTRA_RESULT_NAME) ?: return null
      val dialCode = data.getStringExtra(EXTRA_RESULT_DIAL_CODE) ?: return null
      val code = data.getStringExtra(EXTRA_RESULT_CODE) ?: return null
      return IPickedCountry(name = name, dialCode = dialCode, code = code)
    }
  }

  private var hasCompletedPick = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val headerTitle = intent.getStringExtra(EXTRA_HEADER_TITLE)?.trim()?.takeIf { it.isNotEmpty() }
    val pickerCustomization = if (headerTitle == null) {
      PickerCustomization()
    } else {
      PickerCustomization(headerTitleText = headerTitle)
    }

    setContent {
      MaterialTheme {
        var selectedCountry by remember {
          mutableStateOf(
            CCPUtils.getCountryAutomatically(this@CountryPickerActivity)
              ?: Country.UnitedStates
          )
        }

        CountryPickerDialog(
          onDismissRequest = {
            hasCompletedPick = true
            setResult(Activity.RESULT_CANCELED)
            finish()
          },
          onItemClicked = { country ->
            selectedCountry = country
            hasCompletedPick = true
            val resultIntent = Intent().apply {
              putExtra(EXTRA_RESULT_NAME, country.countryName)
              putExtra(EXTRA_RESULT_DIAL_CODE, country.countryCode)
              putExtra(EXTRA_RESULT_CODE, country.countryIso)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            Toast.makeText(
              this@CountryPickerActivity,
              country.countryName,
              Toast.LENGTH_SHORT
            ).show()
            finish()
          },
          listOfCountry = Country.getAllCountries(),
          selectedCountry = selectedCountry,
          pickerCustomization = pickerCustomization
        )
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    if (!hasCompletedPick) {
      setResult(Activity.RESULT_CANCELED)
      hasCompletedPick = true
    }
  }
}
