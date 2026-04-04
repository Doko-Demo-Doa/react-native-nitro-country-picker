import NitroModules

class NitroCountryPicker: HybridNitroCountryPickerSpec {
  private var lastPickedCountry: IPickedCountry? = nil

  func pickCountry(options: PickCountryOptions?) throws -> Promise<Variant_NullType_IPickedCountry>
  {
    return Promise<Variant_NullType_IPickedCountry>.resolved(withResult: .first(.null))
  }

  func getLastPickedCountry() throws -> Variant_NullType_IPickedCountry {
    if let picked = lastPickedCountry {
      return .second(picked)
    }
    return .first(.null)
  }
}
