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

  private func resolvePendingPick(country: IPickedCountry?) {
    guard let promise = pendingPickPromise else {
      return
    }
    pendingPickPromise = nil

    let result: Variant_NullType_IPickedCountry = country.map { .second($0) } ?? .first(.null)
    promise.resolve(withResult: result)
  }

  private func rejectPendingPick(error: Error) {
    guard let promise = pendingPickPromise else {
      return
    }
    pendingPickPromise = nil

    promise.reject(withError: error)
  }

  func pickCountry(options: PickCountryOptions?) throws -> Promise<Variant_NullType_IPickedCountry>
  {
    let promise = Promise<Variant_NullType_IPickedCountry>()

    DispatchQueue.main.async { [weak self] in
      guard let self else { return }
      if self.pendingPickPromise != nil {
        promise.reject(withError: self.makeError("Country picker is already open."))
        return
      }
      self.pendingPickPromise = promise

      guard let presenter = RCTPresentedViewController() else {
        self.rejectPendingPick(
          error: self.makeError("No active UIViewController available to present country picker."))
        return
      }

      // CountryPickerAKS does not expose a dedicated header-title config yet.
      _ = options?.headerTitle

      CountryPicker.show(from: presenter) { result in
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

    return promise
  }

  func getLastPickedCountry() throws -> Variant_NullType_IPickedCountry {
    if let picked = lastPickedCountry {
      return .second(picked)
    }
    return .first(.null)
  }
}
