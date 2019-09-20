# Acadia-Visitor-Study

## About
Acadia Visitor Study is an Android application developed by James Plante (https://github.com/jwplante) and Joseph Hogan (https://github.com/Kajgohan)  as a part of 
the WPI Acadia Visitor Study: A Mobile Tracking Application IQP for Summmer 2019 in Bar Harbor, Maine. The application's purpose is to assess the viability of 
collecting visitor data through GPS to enable park rangers to manage visitor congestion in Acadia National Park. The application is intended to track visitors 
as they travel throughout Mount Desert Island and the Schoodic Penninsula and record surveys if applicable. The application utilizes a custom server back-end and 
only tracks location data in Mount Desert Island and the Schoodic Penninsula, so any derivitives will need to deploy both the application's associated server and change 
this geofence for usage.

## Building
1. Clone the project to the directory of your choosing. The most common way to do this is the `git clone` command.
2. When opening Android Studio, click on `Open An Existing Project`, then navigate to the root of the git repository that
was just created. Click on the `AcadiaVisitorStudy` folder to open the project.
3. Click on the hammer icon to build the project as any normal project.
4. Installation instructions can vary from device to device, but the most common method of doing this is through ADB. Building the application to an APK can
occasionally fail due to Google Play Protect.


## Notice
This application is based on Google's 'Location Updates Using a Foreground Service' code sample, which aided in the quick development of this application.
The link to the associated code can be found here: https://github.com/googlesamples/android-play-location.
