import { NitroModules } from 'react-native-nitro-modules';
import type { NitroCountryPicker } from './NitroCountryPicker.nitro';

const NitroCountryPickerHybridObject =
  NitroModules.createHybridObject<NitroCountryPicker>('NitroCountryPicker');

export function multiply(a: number, b: number): number {
  return NitroCountryPickerHybridObject.multiply(a, b);
}
