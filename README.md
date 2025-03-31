# ❄️📲 Alpine Hotel (Android App)
Last update: 24/03/2025
## Disclaimer
To make the app functional, a server is required, which is why I am not providing the APK. All the attached files are in Italian. Some of the XML layouts and some of the scripts were added respectively into the "layouts" and "scripts" folders. 
## Introduction
**Alpine Hotel** is an Android app that I developed with a colleague as part of a university project for an exam in the 2024/2025 academic year. This application follows a client-server architecture, with the app serving as the client and a Flask server handling the backend functionality.
We created this app for a fictional hotel and successfully completed our work in less than a month.
## App's features
The software includes a login/sign-in feature that is triggered during app startup. If the user is already logged in, the app skips the login screen and directly displays the home screen. Of course, we have implemented mechanisms to validate the user's input.

Once on the home screen, the user sees a ScrollView displaying details about the hotel and a menu at the bottom of the screen, which allows navigation through different sections of the application. These include the "Profile" section, where the user can view and edit personal data (e-mail, password, phone number, payment method), and the "Booking" section, where the user can book a room and select a specific package to access additional hotel services.

These are just a few features of the app. To explore the others, open the PowerPoint file "Alpine_Hotel_Presentation.pptx".
## Testing
The app runs smoothly on a variety of devices, and we have patched all the bugs we found. We also built a server using Flask to test the app within a subnetwork.
## Key strengths
- **Simplicity:** The app includes only the most useful features, making it simple to use for a wide variety of users.
- **User-friendly UI:** A minimalistic and modern UI is designed to be both visually appealing and intuitive.
- **UI Animations:** Navigating between screens feels smooth thanks to simple transition animations.
- **UI Scalability and Responsiveness:** The UI is primarily built using ConstraintLayout to enhance performance and ensure adaptability across different devices.
## Languages used
- Kotlin
- XML
- SQL
- Python
## Tools used
- Android Studio
- MySQL Workbench
- Git
## My role in the project
- Worked on designing the app's structure, features, and aesthetics.
- Developed the entire UI and wrote Kotlin code for several fragments.
- Implemented UI animations.
- Created the database concept and structure.
