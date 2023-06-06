# ZTRIPLE Android Proof of Concept

This repository is a part of a proof of concept developed as part of a graduation research project at the Amsterdam University of Applied Science. 
The project aims to establish synergy between a mobile (Android) streaming application and a smart TV streaming application. 
This repository includes a Android application which can connect with Samsung and LG TVs and launch the the corresponsing smart TV application using the DIAL protocol. 
When both mobile and smart TV app are connected they can communicate using the WebSocket protocol. 
The primary function of this Android app is streaming, granting users the ability to control their smart TV with their mobile app. Users can perform a range of actions, including playing the desired video, pausing or playing it, rewinding, fast-forwarding, and seeking to specific points in the video. 
<br>
<br>
**NOTE:** This app serves as a proof of concept and is designed to work specifically with the ZTRIPLE smart TV app, which is not installed on smart TVs by default. 
However, you can still experience the functionality by modifying the app name within the LauncherHelper class. By changing it from "ztriple" to "YouTube," the app will attempt to launch the YouTube app instead. 
Since YouTube is typically installed on Samsung and LG smart TVs, it should open successfully when the app establishes a connection with the smart TV.

## Mobile client repository
The following link redirects to the mobile (Android) client repository: https://github.com/ZepaMK/ZTRIPLE-Android.<br>
The server allows communication between the mobile client and the smart TV client.
It's important to note that the smart TV application repository is not publicly accessible due to confidentiality rules.

# Features
* Node.js and Express server script
* Socket.IO integration for real-time biderectional communication

# Setup and Installation
To set up and run the proof of concept, follow the instructions below:

1. Clone this repository to your local machine.
2. Ensure that Node.js is installed on your system.
3. Open a terminal and navigate to the repository's directory.
4. Install the project dependencies by running the following command:
```
npm install
```
5. Start the server script by running the following command:
```
node server.js
```
6. If the console outputs "SERVER IS RUNNING" the server is running and ready to handle incoming connections.
