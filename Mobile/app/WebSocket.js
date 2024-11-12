import { StatusBar } from "expo-status-bar";
import { useEffect, useState } from "react";
import {
  StyleSheet,
  Text,
  View,
  TextInput,
  TouchableOpacity,
  Alert,
  KeyboardAvoidingView,
  Platform,
  Button,
} from "react-native";
import { FontAwesome } from "@expo/vector-icons";
import { Link, router } from "expo-router";
import { Image } from "expo-image";
import * as Notifications from "expo-notifications";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { useFonts } from "expo-font";
import * as SplashScreen from "expo-splash-screen";
import { FlashList } from "@shopify/flash-list";

SplashScreen.preventAutoHideAsync();

export default function App() {
  const [getText, setTest] = useState();
  const [getMessages, setMessages] = useState([]);
  //   const [getConnection, setConnection] = useState(
  //     new WebSocket(process.env.EXPO_PUBLIC_API_URL + "/ChatterHub/A")
  //   );

  //   const ws = ;
  useEffect(() => {
    const getConnection = new WebSocket(
      process.env.EXPO_PUBLIC_API_URL + "/ChatterHub/SocketLoadChat"
    );
    getConnection.onopen = () => {
      // connection opened

      let data = {
        user_id : "1",
        other_user_id : "2"
      }
      getConnection.send(JSON.stringify(data)); // send a message
    };

    getConnection.onmessage = (e) => {
      // a message was received
    //   setMessages([...getMessages, e.data]);
      console.log(e.data);
    };

    getConnection.onerror = (e) => {
      // an error occurred
      console.log("On error");
    };

    getConnection.onclose = (e) => {
      // connection closed
      console.log("On close");
    };
  }, []);

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.input}
        value={getText}
        onChangeText={(text) => {
          setTest(text);
        }}
      />

      <Button
        title="Click"
        onPress={() => {
          //   console.log(getText);
          setTest("");
          setMessages([...getMessages, getText]);
        //   getConnection.send(getText);
        }}
      />

      <FlashList
        data={getMessages}
        renderItem={({ item }) => <Text>{item}</Text>}
        estimatedItemSize={100}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    paddingHorizontal: 20,
    backgroundColor: "#fff",
  },
  icon: {
    alignSelf: "center",
    marginBottom: 20,
  },
  image: {
    width: 250,
    height: 250,
    alignSelf: "center",
  },
  input: {
    height: 50,
    borderColor: "#ccc",
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 15,
    marginVertical: 10,
  },
  button: {
    backgroundColor: "#007bff",
    paddingVertical: 15,
    borderRadius: 8,
    marginVertical: 10,
  },
  buttonText: {
    color: "#fff",
    textAlign: "center",
    fontWeight: "bold",
  },
  footer: {
    flexDirection: "row",
    justifyContent: "center",
    marginTop: 20,
  },
  link: {
    color: "#007bff",
  },
});
