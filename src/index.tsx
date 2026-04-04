import { NitroModules } from 'react-native-nitro-modules';
import type {
  IPickedCountry,
  NitroCountryPicker,
  PickCountryOptions,
} from './NitroCountryPicker.nitro';

const NitroCountryPickerHybridObject =
  NitroModules.createHybridObject<NitroCountryPicker>('NitroCountryPicker');

export function pickCountry(
  options?: PickCountryOptions
): Promise<IPickedCountry | null> {
  return NitroCountryPickerHybridObject.pickCountry(options);
}

export function getLastPickedCountry(): IPickedCountry | null {
  return NitroCountryPickerHybridObject.getLastPickedCountry();
}
