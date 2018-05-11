[![Build Status](https://travis-ci.org/WeDism/WeatherSwingViewer.svg?branch=master)](https://travis-ci.org/WeDism/WeatherSwingViewer) 
[![codecov](https://codecov.io/gh/WeDism/WeatherSwingViewer/branch/master/graph/badge.svg)](https://codecov.io/gh/WeDism/WeatherSwingViewer)

# This is a free application for weather the viewer. You can download from url: [WeatherViewer](https://github.com/WeDism/WeatherSwingViewer/releases)
![Weather Viewer](/src/main/resources/images/PartlyCloudy.png)

## Table of contents
1. [How compile and run app](#steps-to-run-on-windows-system)
1. [Tested systems](#tested-systems)
1. [Tutorial](#use-case-tutorial)
   1. [Startup](#startup)
   1. [Changing current location](#changing-current-location)
   1. [View forecast of the workweek](#view-forecast-of-the-workweek)

### For compile and run this app you have to do next steps
#### Steps to run on windows system
1. Download and install jdk 8
1. Download and install maven 3
1. Open the environment variable window
1. And add into variable path string with the path to maven and jdk 8
1. Download and extract files from zip
1. Into folder root project you have to run build.bat file
1. Will be created WeatherSwingViewer-1.0-full.jar into folder with title target
1. Run WeatherSwingViewer-1.0-full.jar

### Tested systems
1. Windows 8
1. Raspbian (arm32 linux based)

### Use case tutorial
#### Startup
On startup you are viewed splash screen:

![splash screen](/src/main/resources/gifs/loading.gif)

After success initializing the application you have to see main form. 

![start up main form](/readme_images/startup_main_form.png)

#### Changing current location
For changing current location you can click ***${Settings}*** and choose ***${Change Location}*** menu item. 
You can see empty ***${Choose location}*** form in next image with unchecked ***${City is find}*** checkbox.

![choose location form](/readme_images/empty_choose_location_form.png)

Next first step you have to input city in text box under ***${Search city}*** label and in second step should be selected
combobox with needed country under ***${Select country}*** label. 
After these steps you have to click on ***${Search}*** button for search. And you see search your location. 
This is action you can see the next image.

![searching location form](/readme_images/searching_choose_location_form.png)

If your data is valid you should see form with checked (it't good 👍) ***${City is find}*** checkbox. As in the following image.

![true filled choose location form](/readme_images/true_filled_choose_location_form.png)

After input data click ***${OK}*** button for view current and forecast weather.

![main form after valid data](/readme_images/main_form_after_valid_data.png)

#### View forecast of the workweek
For detailed forecast on the 5 days view click on ***${Forecast for the Workweek}*** panel.

![forecast for workweek panel](/readme_images/forecast_for_workweek_panel.png)

If you double click on the row in the column you can see the dialog with detailed information forecast each 3 hours.

![forecast per 3 hours dialog](/readme_images/forecast_per_3_hour_dialog.png)