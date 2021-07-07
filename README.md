# Installation

## Setup environment

This setup guide is intended for:
- IDE: IntelliJ (Just get the latest version)
- SDK: 11.0.11.
- JavaFX: 16

###### SDK setup
1. In IntelliJ navigation bar, go to ` File -> Project Structure -> Platform Settings -> SDKs -> Add new SDK -> Download JDK `
2. Select version 11 and then Download.

###### Add JavaFX to Project Library
1. Click [here](https://gluonhq.com/products/javafx/) and download JavaFX 16 corresponding to your OS.
2. Extract the zip file and remember its file path.
3. Open IntelliJ, go to ` File -> Project Structure -> Libraries -> New Project Library -> Java `
4. Navigate the JavaFX 16 file path and select the **lib** folder.
5. **Apply** and **OK**

###### Configure build and run
1. In IntelliJ navigation bar, go to ` Run -> Edit CXonfigurations `
2. In the Build and run section, nagivate *VM options*. If there is no such option, select Modify options and check *Add VM options*.
3a. For Windows, copy and paste this ` --module-path "\path\to\javafx-sdk-15.0.1\lib" --add-modules javafx.controls,javafx.fxml `
3b. For MacOS/Linux, copy and paste this ` --module-path /path/to/javafx-sdk-15.0.1/lib --add-modules javafx.controls,javafx.fxml `
4. Change the *path/to/javafx-sdk...* to the actual path of the JavaFX lib. This is the path you selected when adding JavaFX to Project Library. Note: the path should end with */lib* (or *\lib* if you're using Windows)
5. **Apply** and **OK**

Run the Main class to test if you have installed correctly.
