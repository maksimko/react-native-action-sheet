import React from 'react'
import { StyleSheet, Text, View, TouchableOpacity, NativeModules } from 'react-native'
import ActionSheet from 'react-native-action-sheet'

export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity style={styles.button} onPress={() => {
          ActionSheet.showActionSheetWithOptions(
            {
              options: ['Cancel', 'First option', 'Second option'],
              cancelButtonIndex: 0,
            },
            buttonIndex => {
              switch (buttonIndex) {
                case 0:
                case 1:
                case 2:
                default:
                  break
              }
            }
          )
        }
        }>
          <Text style={styles.text}>Show ActionSheet</Text>
        </TouchableOpacity>
      </View >
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  button: {
    width: '70%',
    height: 64,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: 'blue'
  },
  text: {
    color: '#fff',
    fontSize: 22,
    fontWeight: "700",
  }
});
