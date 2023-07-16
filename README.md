# Encrypted Chat App (Android - Java)

This is an encrypted chat application for Android, developed in Java, that provides secure communication between users by encrypting and decrypting messages using the AES (Advanced Encryption Standard) algorithm. The app stores the encrypted messages in Firebase Realtime Database, ensuring the confidentiality of the communication.

## Features

- Secure end-to-end encrypted messaging using AES encryption.
- User-friendly interface for a seamless chat experience.
- Real-time message updates using Firebase Realtime Database.
- Account creation and authentication for secure access.
- Secure storage of encrypted messages on the server.

## Prerequisites

To run the application and make modifications, you need to have the following prerequisites installed:

- Android Studio (v4.1 or higher)
- Java Development Kit (JDK 8 or higher)
- Firebase account with a project set up
- Google Play services and Firebase dependencies added to the project

## Installation

Follow these steps to install and set up the application:

1. Clone the repository:

```bash
git clone https://github.com/LiwaaCoder/finalProjectChat.git
```

2. Open Android Studio and import the project:

   - Click on "Open an existing Android Studio project."
   - Navigate to the cloned repository's location and select the project directory.

3. Configure Firebase:

   - Create a new Firebase project at [https://console.firebase.google.com](https://console.firebase.google.com).
   - Add an Android app to your Firebase project, providing the necessary details.
   - Download the `google-services.json` file from the Firebase console.
   - Place the `google-services.json` file inside the `app` directory of your Android Studio project.

4. Build and run the project on an emulator or physical device.

## Usage

1. Sign up for a new account or log in to an existing account.
2. Create or join a chat room.
3. Start sending messages in the chat room.
4. All messages will be encrypted using AES and securely stored in the Firebase Realtime Database.
5. Users in the same chat room will be able to see and decrypt the messages in real time.

## Security

This application uses the AES (Advanced Encryption Standard) algorithm for message encryption and decryption. AES is a widely used symmetric encryption algorithm known for its security and efficiency.

The encryption key is securely generated and shared between the sender and the recipient. The key is never stored or transmitted over the network, ensuring that only the intended recipient can decrypt the messages.

By leveraging Firebase Realtime Database, the app provides real-time updates and secure storage for the encrypted messages. Firebase ensures data security by implementing appropriate authentication and access control mechanisms.

## Contributions
Liwaa Hosh & Anna Telisov
