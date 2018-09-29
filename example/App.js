import React from 'react'
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  NativeModules
} from 'react-native'
import ActionSheet from 'react-native-action-sheet'

const ActionSheetButton = ({ title, actionSheetParams }) => (
  <TouchableOpacity
    style={styles.button}
    onPress={() => {
      ActionSheet.showActionSheetWithOptions(
        {
          options: ['Choose from library', 'Take a photo'],
          ...actionSheetParams
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
    }}
  >
    <Text style={styles.text}>{title || 'Show ActionSheet'}</Text>
  </TouchableOpacity>
)
export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <ActionSheetButton title="2 options" />
        <ActionSheetButton
          title="with cancel"
          actionSheetParams={{
            options: ['Cancel', 'Choose from library', 'Take photo'],
            cancelButtonIndex: 0
          }}
        />
        <ActionSheetButton
          title="with title"
          actionSheetParams={{ title: 'Change profile photo' }}
        />
        <ActionSheetButton
          title="with message"
          actionSheetParams={{
            title: 'Change profile photo',
            message: 'Please select appropriate photo ot you'
          }}
        />
        <ActionSheetButton
          title="with header color"
          actionSheetParams={{
            title: 'Change profile photo',
            message: 'Please select appropriate photo ot you',
            android: { header: { color: '#BAFF94', textColor: '#5A5959' } }
          }}
        />
        <ActionSheetButton
          title="with dark header"
          actionSheetParams={{
            title: 'Change profile photo',
            message: 'Please select appropriate photo ot you',
            android: { header: { color: '#2C3E50' } }
          }}
        />
        <ActionSheetButton
          title="with desctructive button"
          actionSheetParams={{
            options: ['First', 'Second', 'Remove'],
            destructiveButtonIndex: 2
          }}
        />
        <TouchableOpacity
          style={styles.button}
          onPress={() => {
            ActionSheet.showShareActionSheetWithOptions(
              {
                url: 'https://www.google.com',
                subject: 'You search engine',
                message: "It's popular",
                excludedActivityTypes: ['com.android.bluetooth'],
                android: {
                  dialogTitle: 'Share with',
                  includedActivityTypes: [
                    'com.facebook.katana',
                    'com.ghisler.android.TotalCommander'
                  ]
                }
              },
              () => {},
              () => {}
            )
          }}
        >
          <Text style={styles.text}>Show Share</Text>
        </TouchableOpacity>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center'
  },
  button: {
    width: '70%',
    height: 48,
    marginVertical: 6,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'blue'
  },
  text: {
    color: '#fff',
    fontSize: 17,
    fontWeight: '500'
  }
})
