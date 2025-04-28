import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  ScrollView,
} from 'react-native';
import ActionSheet, {
  type ActionSheetInterface,
} from 'react-native-action-sheet';

function App() {
  return (
    <ScrollView
      style={styles.scroll}
      contentContainerStyle={styles.scrollContent}
    >
      <Section title="Action Sheet" />
      <ActionSheetButton title="2 options" />
      <ActionSheetButton
        title="with cancel"
        actionSheetParams={{
          options: ['Cancel', 'Choose from library', 'Take photo'],
          cancelButtonIndex: 0,
        }}
      />
      <ActionSheetButton
        title="with desctructive button"
        actionSheetParams={{
          options: ['First', 'Second', 'Remove'],
          destructiveButtonIndex: 2,
          android: {
            cancelable: false,
          },
        }}
      />
      <ActionSheetButton
        title="with tintColor"
        actionSheetParams={{
          title: 'Change profile photo',
          message: 'Choose your best ever photo 😉',
          options: ['Test'],
          tintColor: '#00FF0F',
        }}
      />
      <ActionSheetButton
        title="with cancelButtonTintColor"
        actionSheetParams={{
          title: 'Change profile photo',
          message: 'Choose your best ever photo 😉',
          options: ['Button 1', 'Button 2', 'Cancel'],
          cancelButtonIndex: 2,
          cancelButtonTintColor: '#00FF0F',
        }}
      />
      <ActionSheetButton
        title="with disabledButtonIndices"
        actionSheetParams={{
          title: 'Change profile photo',
          message: 'Choose your best ever photo 😉',
          options: ['Cancel', 'Button 1', 'Button 2'],
          cancelButtonIndex: 0,
          disabledButtonIndices: [1],
        }}
      />
      <ActionSheetButton
        title="with disabled color"
        actionSheetParams={{
          title: 'Change profile photo',
          message: 'Choose your best ever photo 😉',
          options: ['Button 1', 'Button 2'],
          disabledButtonIndices: [0],
          disabledButtonTintColor: '#00FF0F',
        }}
      />
      <ActionSheetButton
        title="with long message"
        actionSheetParams={{
          title: 'Close this thing',
          message:
            'Are you sure you want to close it! This is a long message that should be wrapped in the action sheet. It should be long enough to test the wrapping of the message in the action sheet.',
          options: ['First', 'Second', 'Remove'],
          destructiveButtonIndex: 2,
          android: {
            cancelable: true,
          },
        }}
      />
      <Section title="Share" />
      <ShareButton />
    </ScrollView>
  );
}

const ActionSheetButton = ({
  title,
  actionSheetParams,
}: {
  title: string;
  actionSheetParams?: Parameters<
    ActionSheetInterface['showActionSheetWithOptions']
  >[0];
}) => (
  <TouchableOpacity
    style={styles.button}
    onPress={() => {
      ActionSheet.showActionSheetWithOptions(
        {
          options: ['Choose from library', 'Take a photo'],
          ...actionSheetParams,
        },
        (buttonIndex: number) => {
          switch (buttonIndex) {
            case 0:
            case 1:
            case 2:
            default:
              break;
          }
        }
      );
    }}
  >
    <Text style={styles.text}>{title || 'Show ActionSheet'}</Text>
  </TouchableOpacity>
);

const ShareButton = () => (
  <TouchableOpacity
    style={[styles.button, styles.shareButton]}
    onPress={() => {
      ActionSheet.showShareActionSheetWithOptions(
        {
          url: 'https://www.google.com',
          subject: 'You search engine',
          message: "It's popular",
          excludedActivityTypes: ['com.android.bluetooth'],
        },
        () => {}, //Failure callback
        () => {} //Success callback
      );
    }}
  >
    <Text style={styles.text}>SHARE</Text>
  </TouchableOpacity>
);

const Section = ({ title }: { title: string }) => (
  <View style={styles.section}>
    <View style={styles.line} />
    <Text style={styles.sectionTitle}>{title}</Text>
  </View>
);

const styles = StyleSheet.create({
  scroll: {
    flex: 1,
    backgroundColor: '#fff',
  },
  scrollContent: {
    alignItems: 'center',
    justifyContent: 'center',
    flexGrow: 1,
  },
  button: {
    width: '70%',
    maxWidth: 320,
    height: 48,
    marginVertical: 6,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#3B3B98',
    borderRadius: 8,
  },
  shareButton: {},
  text: {
    color: '#fff',
    fontSize: 14,
    fontWeight: '200',
  },
  section: {
    width: '100%',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 36,
    marginBottom: 16,
  },
  line: {
    position: 'absolute',
    height: StyleSheet.hairlineWidth,
    width: '80%',
    backgroundColor: '#dddccc',
  },
  sectionTitle: {
    position: 'absolute',
    backgroundColor: 'white',
    paddingHorizontal: 8,
    fontWeight: '500',
    color: '#dddccc',
  },
});

export default App;
