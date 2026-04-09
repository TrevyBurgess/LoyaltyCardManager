# My Card Manager

Phone App for managing cards

# Vibe Coding Commands

Here are the commands I used for setting up the app

## Setup

* Create a multipage-page Android app using Kotlin, Jetpack Compose, and the MVVM design pattern. Use stateless composable. Create a master page for hosting all other pages. The bottom of this page will include a navigation bar. All pages will be hosted here.
* Include @Preview methods for all app pages
* Add a button to the CardsScreen page. When clicked, it will use the camera to scan a QR code or bar code. A popup window will show the scanned QR code or bar code to the user.
* Manual change: Scan result is moved to success (ScanResultDialog) and fail (ScanFailDialog) methods
* Add the image of the bar code or QR code to the dialog in ScanResultDialog
* When a QR code is scanned, identify the type of code scanned. Add this information to the dialog in ScanResultDialog
* Add a text box called Name to the dialog in ScanResultDialog. Include the hint text 'What is your Card Name?'
* Rename the OK button the Scanned Code page to Save. When the button is pressed, save the name, code number, and code type to a JSON file for later use.
* Call the generated JSON file to scanned_codes.json
* Create a responsive grid for displaying information in CardsScreen.kt. For each entry in scanned_codes.json, add an entry to the grid. The entry will contain name, a placeholder icon, and the scanned number
* The previous caused an exception: please check for index out of bound exception
* Add Overflow menu mutton to right of each element of the grid for showing cards
* When a person clicks on an item in the CardsScreen page, open a new popup showing the Name, QR code or bar code, and number
* Add Overflow menu mutton to right of each element of the grid for showing cards. When clicked open a popup menu. This menu will have 2 items Edit, Delete
* When a person presses Delete, open a dialog asking for confirmation. If the user presses 'Yes', delete the record and update the UI
* When a person presses the Edit button, open a brand new dialog. This dialog will show the image of the QR code or bar code, the number and name. The image will be determined by card type
* Show the bar code in the edit dialog
* Add a button to the navigation bar called About. When pressed open a page called About.
* Hide the home button and settings button in the navigation bar. Start app in Cards page
