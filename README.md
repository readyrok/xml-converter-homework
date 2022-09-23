# XML-Converter

## Description
XML-Converter is a middleware application that integrates orders with the suppliers of electronic products.
It watches the input folder for new XML files and everytime a new file is added the information in it is 
processed and split into different XML files, one for every supplier in the order and the initial file is deleted afterwards.
The file must be named with the following pattern in order to be valid for processing: "orders##.xml".

For reading the document I used DOM API and for writing the new documents I used Transform API, both provided by Java.
For watching the input folders for new files I have used WatchService interface. The paths to input and output folders have been extracted to a config.properties file and can be easily modified. Errors
are logged using Logger.

## Requirements
Maven and JDK 17 or higher.

## Installation
Clone repo and run Main().



