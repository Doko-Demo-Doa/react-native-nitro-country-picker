import CountryPickerAKS
import NitroModules
import React
import UIKit

class NitroCountryPicker: HybridNitroCountryPickerSpec {
  private var lastPickedCountry: IPickedCountry? = nil
  private var pendingPickPromise: Promise<Variant_NullType_IPickedCountry>? = nil

  private func makeError(_ message: String) -> Error {
    return NSError(
      domain: "NitroCountryPicker", code: 1, userInfo: [NSLocalizedDescriptionKey: message])
  }

  @MainActor
  private func resolvePendingPick(country: IPickedCountry?) {
    guard let promise = pendingPickPromise else {
      return
    }
    pendingPickPromise = nil

    let result: Variant_NullType_IPickedCountry = country.map { .second($0) } ?? .first(.null)
    promise.resolve(withResult: result)
  }

  @MainActor
  private func rejectPendingPick(error: Error) {
    guard let promise = pendingPickPromise else {
      return
    }
    pendingPickPromise = nil

    promise.reject(withError: error)
  }

  @MainActor
  private func openPicker(
    options: PickCountryOptions?,
    promise: Promise<Variant_NullType_IPickedCountry>
  ) {
    if pendingPickPromise != nil {
      promise.reject(withError: makeError("Country picker is already open."))
      return
    }
    pendingPickPromise = promise

    guard let presenter = RCTPresentedViewController() else {
      rejectPendingPick(
        error: makeError("No active UIViewController available to present country picker."))
      return
    }

    // CountryPickerAKS does not expose a dedicated header-title config yet.
    _ = options?.headerTitle

    CountryPicker.show(from: presenter) { [weak self] result in
      guard let self else { return }
      switch result {
      case .success(let selected):
        let picked = IPickedCountry(
          name: selected.name,
          dialCode: selected.dial_code,
          code: selected.code
        )
        self.lastPickedCountry = picked
        self.resolvePendingPick(country: picked)
      case .failure(let error):
        switch error {
        case .NotSelected:
          self.resolvePendingPick(country: nil)
        default:
          self.rejectPendingPick(error: error)
        }
      }
    }
  }

  func pickCountry(options: PickCountryOptions?) throws -> Promise<Variant_NullType_IPickedCountry>
  {
    let promise = Promise<Variant_NullType_IPickedCountry>()
    Task { @MainActor [weak self] in
      guard let self else { return }
      self.openPicker(options: options, promise: promise)
    }
    return promise
  }

  func getLastPickedCountry() throws -> Variant_NullType_IPickedCountry {
    if let picked = lastPickedCountry {
      return .second(picked)
    }
    return .first(.null)
  }
}
