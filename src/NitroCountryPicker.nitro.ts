import type { HybridObject } from 'react-native-nitro-modules';

// interface IPickedCountry {
//   name: string;
//   dialCode: string;
//   code: string;
// }

export interface NitroCountryPicker
  extends HybridObject<{ ios: 'swift'; android: 'kotlin' }> {
  multiply(a: number, b: number): number;

  show(): void;
}
