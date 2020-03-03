declare module "react-native-action-sheet" {
  export interface ActionSheetType {
    showActionSheetWithOptions: (
      options: {
        title?: string;
        options: string[];
        cancelButtonIndex?: number;
        destructiveButtonIndex?: number;
        message?: string;
        tintColor?: string;
        android?: {
          headerColor?: string;
          headerTextColor?: string;
          cancelable?: boolean;
        };
      },
      callback: (buttonIndex: number) => void
    ) => void;
    showShareActionSheetWithOptions: (
      options: {
        message?: string;
        url?: string;
        subject?: string;
        excludedActivityTypes?: string[];
        android?: {
          dialogTitle?: string;
          includedActivityTypes?: string[];
        };
      },
      failureCallback: (error: Error) => void,
      successCallback: (success: boolean, method: string) => void
    ) => void;
  }

  const ActionSheet: ActionSheetType;

  export default ActionSheet;
}
