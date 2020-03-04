# react-native-action-sheet

ActionSheet for both Android and iOS.

- Mimics [ActionSheetIOS](https://facebook.github.io/react-native/docs/actionsheetios) interface
- Native Android implementation on BottomSheetDialog
- iOS zero dependency (rely on ActionSheetIOS)

## Installation

```sh
yarn add react-native-action-sheet
```

## Linking

#### Automatic

```sh
react-native link react-native-action-sheet
```

#### Manual

**iOS integration**

Nothings here. It uses ActionSheetIOS.
Replace your imports of `ActionSheetIOS` to `import ActionSheet from "react-native-action-sheet"`

**Android integration**

**settings.gradle**

```groovy
include ':react-native-action-sheet'
project(':react-native-action-sheet').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-action-sheet/android')
```

**app.gradle**

```groovy
dependencies {
    ...
    implementation project(':react-native-action-sheet')
}
```

**Native App**

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

**ActionSheet**

```JavaScript
import ActionSheet from 'react-native-action-sheet'
...
ActionSheet.showActionSheetWithOptions(
    {
      options: ['Cancel', 'Choose from library', 'Take photo', 'Delete'],
      cancelButtonIndex: 0,
      destructiveButtonIndex: 3,
      title: 'Change profile photo',
      message: 'Please choose your best ever photo',
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

**Sharing dialog**

```JavaScript
import ActionSheet from 'react-native-action-sheet'
...
ActionSheet.showShareActionSheetWithOptions(
  {
    url: 'https://www.google.com',
    subject: 'Your search engine',
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

### Props

Refer to [ActionSheetIOS](https://facebook.github.io/react-native/docs/actionsheetios)

**Android only props** passed under `android` props key

`ActionSheet.showActionSheetWithOptions`

| Prop name    | Description                             | Type               | Required |
| ------------ | --------------------------------------- | ------------------ | -------- |
| `color`      | header background color                 | string (HEX value) | false    |
| `textColor`  | header text color                       | string (HEX value) | false    |
| `cancelable` | can be closed by taping outside: `true` | boolean            | false    |

`ActionSheet.showShareActionSheetWithOptions`

| Prop name               | Description                  | Type     | Required |
| ----------------------- | ---------------------------- | -------- | -------- |
| `dialogTitle`           | Android chooser dialog text  | string   | false    |
| `includedActivityTypes` | Whitelist activities to show | string[] | false    |
