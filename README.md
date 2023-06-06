# ZTRIPLE Android Proof of Concept

This repository is a part of a proof of concept developed as part of a graduation research project at the Amsterdam University of Applied Science. 
The project aims to establish synergy between a mobile (Android) streaming application and a smart TV streaming application. 
This repository includes a Android application which can connect with Samsung and LG TVs and launch the the corresponsing smart TV application using the DIAL protocol. 
When both mobile and smart TV app are connected they can communicate using the WebSocket protocol. 
The primary function of this Android app is granting users the ability to control their smart TV. Users can perform a range of actions, including playing the desired video, pausing or playing it, rewinding, fast-forwarding, and seeking to specific points in the video. 
<br>
<br>
**NOTE:** This app serves as a proof of concept and is designed to work specifically with the ZTRIPLE smart TV app, which is not installed on smart TVs by default. 
However, you can still experience the functionality by modifying the **appId** within the *LauncherHelper* class. By changing it from "ztriple" to "YouTube," the app will attempt to launch the YouTube app instead. 
Since YouTube is typically installed on Samsung and LG smart TVs, it should open successfully when the app establishes a connection with the smart TV.

## Mobile client repository
The following link redirects to the Socket.IO server repository: https://github.com/ZepaMK/ZTRIPLE-Server.<br>
The server allows communication between the mobile (Android) client and the smart TV client.
It's important to note that the smart TV (client) repository is not publicly accessible due to confidentiality rules.

# Features
* Connect SDK
* DIAL protocol
* Socket.IO (WebSocket protocol)
* MVVM
* Clean Architecture
* Jetpack Compose
