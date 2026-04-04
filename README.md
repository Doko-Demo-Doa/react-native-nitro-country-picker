# react-native-nitro-country-picker

A native country picker for React Native, powered by Nitro Modules.

This library provides a single async API that opens a native picker and returns structured country data.

- Android native implementation: [CountryCodePickerCompose](https://github.com/ahmmedrejowan/CountryCodePickerCompose)
- iOS native implementation: [CountryPickerAKS](https://github.com/aksamitsah/CountryPickerAKS)

## Features

- Native picker UI on both iOS and Android
- Promise-based API: `pickCountry(options?)`
- Typed result object with `name`, `dialCode`, and `code`
- `null` result when user dismisses without selection
- Last selection cache via `getLastPickedCountry()`
- Optional picker title input: `headerTitle` (currently applied on Android)

## Requirements

| Platform | Minimum |
| -------- | ------- |
| iOS      | 15.1+   |
| Android  | API 24+ |

> Note: This package depends on [react-native-nitro-modules](https://nitro.margelo.com/).

## Installation

```bash
npm install react-native-nitro-country-picker react-native-nitro-modules
```

or

```bash
yarn add react-native-nitro-country-picker react-native-nitro-modules
```

### iOS setup

```bash
cd ios && pod install
```

## Usage

```tsx
import { pickCountry } from 'react-native-nitro-country-picker';

const country = await pickCountry();
```

### With options

```tsx
import { pickCountry } from 'react-native-nitro-country-picker';

const country = await pickCountry({
  headerTitle: 'Select your country',
});
```

## Returned Data Examples

### 1) Handle selected vs dismissed

```tsx
import { pickCountry } from 'react-native-nitro-country-picker';

const picked = await pickCountry();

if (picked) {
  console.log('Country name:', picked.name);
  console.log('Dial code:', picked.dialCode);
  console.log('ISO code:', picked.code);
} else {
  console.log('Picker dismissed without selection');
}
```

### 2) Build display text from returned data

```tsx
import { pickCountry } from 'react-native-nitro-country-picker';

const picked = await pickCountry();
const label = picked
  ? `${picked.name} (${picked.dialCode}, ${picked.code})`
  : 'No country selected';

console.log(label);
```

### 3) Read last picked country

```tsx
import {
  getLastPickedCountry,
  pickCountry,
} from 'react-native-nitro-country-picker';

await pickCountry();

const last = getLastPickedCountry();
if (last) {
  console.log('Last picked:', last.name, last.dialCode, last.code);
}
```

## API

### `pickCountry(options?) => Promise<IPickedCountry | null>`

Opens the native picker and resolves with:

- `IPickedCountry` when user selects a country
- `null` when user dismisses/cancels

Options:

- `headerTitle?: string`

### `getLastPickedCountry() => IPickedCountry | null`

Returns the most recent selected country for the current app runtime session.

## Type Shapes

```ts
type IPickedCountry = {
  name: string;
  dialCode: string;
  code: string;
};

type PickCountryOptions = {
  headerTitle?: string;
};
```

## Example App

To run the included example app:

```bash
cd example
yarn install
yarn ios
# or
yarn android
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for development workflow and contribution guidelines.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
