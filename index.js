import React from 'react'
import { NativeModules, Platform, ActionSheetIOS } from 'react-native'

const ActionSheet = Platform.OS === 'ios' ? ActionSheetIOS : NativeModules.ActionSheet

export default ActionSheet
