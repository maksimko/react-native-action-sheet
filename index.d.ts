declare module "react-native-action-sheet" {
  export interface ActionSheetType {
    showActionSheetWithOptions: (
      options: {
        options: string[];
        cancelButtonIndex?: number;
        destructiveButtonIndex?: number;
        title?: string;
        message?: string;
        anchor?: number;
        tintColor?: string;
        android?: {
          color?: string;
          textColor?: string;
          cancelable?: boolean;
        };
      },
      callback: (buttonIndex: number) => void
    ) => void;
    showShareActionSheetWithOptions: (
      options: {
        url?: string;
        message?: string;
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
