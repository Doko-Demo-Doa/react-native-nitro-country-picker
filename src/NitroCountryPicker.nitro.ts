import type { HybridObject } from 'react-native-nitro-modules';

export interface IPickedCountry {
  name: string;
  dialCode: string;
  code: string;
}

export interface PickCountryOptions {
  headerTitle?: string;
}

export interface NitroCountryPicker extends HybridObject<{
  ios: 'swift';
  android: 'kotlin';
}> {
  pickCountry(options?: PickCountryOptions): Promise<IPickedCountry | null>;

  getLastPickedCountry(): IPickedCountry | null;
}
