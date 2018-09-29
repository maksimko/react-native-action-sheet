# react-native-action-sheet
Provides common interface in React Native to show ActionSheet on Android and iOS. 
Interface is the same as [ActionSheetIOS.](https://facebook.github.io/react-native/docs/actionsheetios)

## Installation
### iOS integration

Nothings here. It uses ActionSheetIOS.

### Android integration

ReactNative project
- Add dependency to package.json `yarn add react-native-action-sheet`

Android project
- Add project dependency
  **settings.gradle**
  ```gradle
  include ':react-native-action-sheet'
  project(':react-native-action-sheet').projectDir = new File(rootProject.projectDir, '../../android')
  ```

  **app.gradle**
  ```gradle
  dependencies {
      ...
      implementation project(':react-native-action-sheet')
  }
  ```

  **React native host**
  ```Java
  override fun getPackages(): List<ReactPackage> {
      return Arrays.asList(
              MainReactPackage(),
              ActionSheetPackage()
      )
  }
  ```

## Usage
### React Native
```JavaScript
import ActionSheet from 'react-native-action-sheet'
...
ActionSheet.showActionSheetWithOptions(
    {
      options: ['Cancel', 'Choose from library', 'Take photo', 'Delete'],
      cancelButtonIndex: 0,
      destructiveButtonIndex: 3,
      title: 'Change profile photo',
      message: 'Please select appropriate photo ot you',
      android: { 
          header: { 
              color: '#BAFF94',  
              textColor: '#5A5959' 
          } 
      }
    },
    buttonIndex => {...}
  )
```

```JavaScript
import ActionSheet from 'react-native-action-sheet'
...
ActionSheet.showShareActionSheetWithOptions(
  {
    url: 'https://www.google.com',
    subject: 'You search engine',
    message: "It's popular",
    excludedActivityTypes: ['com.android.bluetooth'],
    android: {
      dialogTitle: 'Share with',
      includedActivityTypes: [
        // 'com.facebook.katana',
        // 'com.ghisler.android.TotalCommander'
      ]
    }
  },
  () => {}, //Failure callback
  () => {}  //Success callback
)
```
