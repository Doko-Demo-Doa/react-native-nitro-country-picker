import { Text, View, StyleSheet, Pressable } from 'react-native';
import { multiply, show } from 'react-native-nitro-country-picker';

const result = multiply(3, 3);

export default function App() {
  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>

      <Pressable
        onPress={show}
        style={{ padding: 20, backgroundColor: 'blue' }}
      >
        <Text>Testettt</Text>
      </Pressable>
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
});
