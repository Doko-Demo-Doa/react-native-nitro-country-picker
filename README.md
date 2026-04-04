# The "Native" country picker for React Native

Country picker, powered by Nitro modules.

This is an **opinionated** native module library for React Native that shows a native country picker (with search functionality). It supports iOS, Android.

- On Android, it's a bridge for [CountryCodePickerCompose](https://github.com/ahmmedrejowan/CountryCodePickerCompose)
- On iOS, it's a bridge for [CountryPickerAKS](https://github.com/aksamitsah/CountryPickerAKS)

## Installation

```sh
npm install react-native-nitro-country-picker react-native-nitro-modules
```

or

```sh
yarn install react-native-nitro-country-picker react-native-nitro-modules
```

> `react-native-nitro-modules` is required as this library relies on [Nitro Modules](https://nitro.margelo.com/).

## Usage

```js
import { pickCountry } from 'react-native-nitro-country-picker';

// ...

const picked = await pickCountry();

const pickedWithCustomTitle = await pickCountry({
  headerTitle: 'Select your country',
});
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
