import React from "react";
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  NativeModules
} from "react-native";
import ActionSheet from "react-native-action-sheet";

const ActionSheetButton = ({ title, additionalParams }) => (
  <TouchableOpacity
    style={styles.button}
    onPress={() => {
      ActionSheet.showActionSheetWithOptions(
        {
          options: ["Choose from library", "Take photo"],
          ...additionalParams
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
export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <ActionSheetButton title="2 options" />
        <ActionSheetButton
          title="with cancel"
          additionalParams={{
            options: ["Cancel", "Choose from library", "Take photo"],
            cancelButtonIndex: 0
          }}
        />
        <ActionSheetButton
          title="with title"
          additionalParams={{ title: "Change profile photo" }}
        />
        <ActionSheetButton
          title="with message"
          additionalParams={{
            title: "Change profile photo",
            message: "Please select appropriate photo ot you"
          }}
        />
        <ActionSheetButton
          title="with header color"
          additionalParams={{
            title: "Change profile photo",
            message: "Please select appropriate photo ot you",
            android: { headerColor: "#BAFF94", headerTextColor: "#5A5959" }
          }}
        />
        <ActionSheetButton
          title="with dark header"
          additionalParams={{
            title: "Change profile photo",
            message: "Please select appropriate photo ot you",
            android: { headerColor: "#2C3E50" }
          }}
        />
        <ActionSheetButton
          title="with desctructive button"
          additionalParams={{
            options: ["First", "Second", "Remove"],
            destructiveButtonIndex: 2
          }}
        />
        <TouchableOpacity
          style={styles.button}
          onPress={() => {
            ActionSheet.showShareActionSheetWithOptions(
              {
                url: "wow://url",
                subject: "Title",
                message: "Message",
                excludedActivityTypes: ["com.android.bluetooth"],
                android: {
                  dialogTitle: "Share with",
                  includedActivityTypes: [
                    //   "com.facebook.katana",
                    //   "com.ghisler.android.TotalCommander"
                  ]
                }
              },
              () => {},
              () => {}
            );
          }}
        >
          <Text style={styles.text}>Show Share</Text>
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center"
  },
  button: {
    width: "70%",
    height: 48,
    marginVertical: 6,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "blue"
  },
  text: {
    color: "#fff",
    fontSize: 17,
    fontWeight: "500"
  }
});
