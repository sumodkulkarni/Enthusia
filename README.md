VJTI Fests
==========

** NOTE **
----------

This is a base project you will use for developing the final application. Do not commit to this repository or make any changes to the existing code.

Before continuing you must have the following software installed

+ [Android Studio](http://developer.android.com/sdk/installing/studio.html)
+ [Android SDK Tools](http://developer.android.com/tools/sdk/tools-notes.html)
+ API 19, 14
+ All stuff under Extras in SDK Manager

About Programming the Application

Activities
==========

All the activites layout files must start with the prefix 'activity_' and must conatin name of the festival as an identifier.

e.g: activity_enthusia_start.xml, enthusia_listitem_row.xml

All the java files must contain the festival name as the prefix

e.g: EnthusiaStartActivity.java

Resources
=========

All IDs in the layout file must start with the name of the layout file as prefix

e.g: splash_textview (Splash Activity), enthusia_start_textview_welcome (Id for a TextView in Start Activity)

All the resources must also contain the festival name as the prefix

e.g: enthusia_fest_name is a string resource