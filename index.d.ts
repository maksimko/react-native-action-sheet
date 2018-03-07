declare module "react-native-action-sheet" {
  export interface ActionSheetType {
      showActionSheetWithOptions(options: any, Callback<void>);
  }

  const ActionSheet: ActionSheetType

  export default ActionSheet
}
