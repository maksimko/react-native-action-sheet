import { type ActionSheetIOSOptions, type ShareActionSheetIOSOptions } from 'react-native';
export interface ActionSheetInterface {
    /**
     * Display an iOS action sheet. The `options` object must contain one or more
     * of:
     * - `options` (array of strings) - a list of button titles (required)
     * - `cancelButtonIndex` (int) - index of cancel button in `options`
     * - `destructiveButtonIndex` (int) - index of destructive button in `options`
     * - `title` (string) - a title to show above the action sheet
     * - `message` (string) - a message to show below the title
     */
    showActionSheetWithOptions: (options: ActionSheetIOSOptions & {
        android?: {
            cancelable?: boolean;
        };
    }, callback: (buttonIndex: number) => void) => void;
    /**
     * Display the iOS share sheet. The `options` object should contain
     * one or both of `message` and `url` and can additionally have
     * a `subject` or `excludedActivityTypes`:
     *
     * - `url` (string) - a URL to share
     * - `message` (string) - a message to share
     * - `subject` (string) - a subject for the message
     * - `excludedActivityTypes` (array) - the activities to exclude from the ActionSheet
     *
     * NOTE: if `url` points to a local file, or is a base64-encoded
     * uri, the file it points to will be loaded and shared directly.
     * In this way, you can share images, videos, PDF files, etc.
     */
    showShareActionSheetWithOptions: (options: ShareActionSheetIOSOptions, failureCallback: (error: Error) => void, successCallback: (success: boolean, method: string) => void) => void;
}
declare const ActionSheet: ActionSheetInterface;
export default ActionSheet;
