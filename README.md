# Hub
Launcher for websites and local programs

## Installing Hub
### Compiling Files
Have java-jdk installed in order to compile the files yourself. Compile hub/window/MainWindow.java, this will compile all necessary class files needed. Download the jar to avoid compilation https://github.com/venkat3g/Hub/releases/

### Already Compiled / Downloaded jar 
Create a new folder for the 'Hub' as having multiple 'Hubs' in the same folder will not work. 
##### If you have compiled the java files yourself, type java hub.window.MainWindow from the directory of the folder containing the Hub.
Launch the Hub. Create a shortcut (applies to jar) to your desktop if you would like. Go ahead and add websites and programs to your Hub. 

## Adding Images to each button
By default each program you add will take a copy of the image and save it into the (Hub folder)/Images folder.
If you would like to add your own images, right click the button that you will like to customize. 
From here click "Change Image" and select any Image you like.
(try to keep the file size of the images low as it will lead to longer load times of Hub)
##### Currently Hub accepts jpg, png.

## Running Local Server
Go to the system tray on your computer, will work on most OSs (have tested in Windows, Ubuntu, Mint Linux), right click on the 'Hub', then click "Start Local Server". 

## Connecting to Hub.
To connect to the Hub remotely use the phone app (Android app): https://github.com/venkat3g/HubRemoteTest. Click "Add Network" then type in Name, IP address and port 1024.

## Changing 'Hub' Port
In order to change port manually (only change in special cases i.e. running multiple 'Hubs' on the network) go to Resources folder and open Hub_Name.txt and add a new line with the new port you would like. If you do not see Hub_Name.txt, have your Hub open go File>Name then enter a Name for the 'Hub'. Now you should see the Hub_Name.txt file, otherwise create file called Hub_Name.txt with the first line containing the Name of the 'Hub' and the second line containing the port.

## Notes
##### Most programs will be added not all, have currently exe and lnk types have been tested.
##### Hub will stay in the background by default, in order to close: go to your system tray, right click the icon for 'Hub' and hit Exit.
##### "Connect Online" is still being tested, may or may not be shown when right-clicked.
