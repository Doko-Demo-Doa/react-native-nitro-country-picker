import { Text, View, StyleSheet, Pressable } from 'react-native';
import { useState } from 'react';
import { pickCountry } from 'react-native-nitro-country-picker';

export default function App() {
  const [picked, setPicked] =
    useState<Awaited<ReturnType<typeof pickCountry>>>(null);

  const [status, setStatus] = useState('Tap button to pick a country');

  const onPickCountry = async () => {
    setStatus('Opening picker...');
    const result = await pickCountry({
      headerTitle: 'Custom title: Select your country',
    });

    setPicked(result);
    setStatus(result ? 'Country picked!' : 'Picker dismissed');
  };

  return (
    <View style={styles.container}>
      <Text>Country Picker Demo</Text>

      <Pressable onPress={onPickCountry} style={styles.openButton}>
        <Text>Open Picker</Text>
      </Pressable>

      <Text style={styles.statusText}>{status}</Text>

      <Text style={styles.resultText}>
        {picked
          ? `Selected: ${picked.name} (${picked.dialCode}, ${picked.code})`
          : 'Selected: none'}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'white',
  },
  openButton: {
    padding: 20,
    backgroundColor: 'blue',
  },
  statusText: {
    marginTop: 12,
  },
  resultText: {
    marginTop: 16,
  },
});
