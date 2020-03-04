import React from "react";
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  ScrollView
} from "react-native";
import ActionSheet from "react-native-action-sheet";

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
          android: {
            cancelable: false
          },
          options: ["Cancel", "Choose from library", "Take photo"],
          cancelButtonIndex: 0
        }}
      />
      <ActionSheetButton
        title="with title"
        actionSheetParams={{ title: "Change profile photo" }}
      />
      <ActionSheetButton
        title="with title and message"
        actionSheetParams={{
          title: "Change profile photo",
          message: "Choose your best ever photo 😉"
        }}
      />
      <ActionSheetButton
        title="with header color"
        actionSheetParams={{
          title: "Change profile photo",
          message: "Choose your best ever photo 😉",
          android: { header: { color: "#BAFF94", textColor: "#5A5959" } }
        }}
      />
      <ActionSheetButton
        title="with dark header"
        actionSheetParams={{
          title: "Change profile photo",
          message: "Choose your best ever photo 😉",
          android: { header: { color: "#2C3E50" } }
        }}
      />
      <ActionSheetButton
        title="with desctructive button"
        actionSheetParams={{
          options: ["First", "Second", "Remove"],
          destructiveButtonIndex: 2
        }}
      />
      <Section title="Share" />
      <ShareButton />
    </ScrollView>
  );
}

const ActionSheetButton = ({ title, actionSheetParams }) => (
  <TouchableOpacity
    style={styles.button}
    onPress={() => {
      ActionSheet.showActionSheetWithOptions(
        {
          options: ["Choose from library", "Take a photo"],
          ...actionSheetParams
        },
        buttonIndex => {
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
    <Text style={styles.text}>{title || "Show ActionSheet"}</Text>
  </TouchableOpacity>
);

const ShareButton = () => (
  <TouchableOpacity
    style={[styles.button, styles.shareButton]}
    onPress={() => {
      ActionSheet.showShareActionSheetWithOptions(
        {
          url: "https://www.google.com",
          subject: "You search engine",
          message: "It's popular",
          excludedActivityTypes: ["com.android.bluetooth"],
          android: {
            dialogTitle: "Share with",
            includedActivityTypes: [
              // 'com.facebook.katana',
              // 'com.ghisler.android.TotalCommander'
            ]
          }
        },
        () => {}, //Failure callback
        () => {} //Success callback
      );
    }}
  >
    <Text style={styles.text}>SHARE</Text>
  </TouchableOpacity>
);

const Section = ({ title }) => (
  <View style={styles.section}>
    <View style={styles.line} />
    <Text style={styles.sectionTitle}>{title}</Text>
  </View>
);

const styles = StyleSheet.create({
  scroll: {
    flex: 1,
    backgroundColor: "#fff"
  },
  scrollContent: {
    alignItems: "center",
    justifyContent: "center",
    flexGrow: 1
  },
  button: {
    width: "70%",
    maxWidth: 320,
    height: 48,
    marginVertical: 6,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#3B3B98",
    borderRadius: 8
  },
  shareButton: {},
  text: {
    color: "#fff",
    fontSize: 14,
    fontWeight: "200"
  },
  section: {
    width: "100%",
    justifyContent: "center",
    alignItems: "center",
    marginTop: 36,
    marginBottom: 16
  },
  line: {
    position: "absolute",
    height: StyleSheet.hairlineWidth,
    width: "80%",
    backgroundColor: "#dddccc"
  },
  sectionTitle: {
    position: "absolute",
    backgroundColor: "white",
    paddingHorizontal: 8,
    fontWeight: "500",
    color: "#dddccc"
  }
});

export default App;
