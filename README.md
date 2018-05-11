[![Build Status](https://travis-ci.org/WeDism/WeatherSwingViewer.svg?branch=master)](https://travis-ci.org/WeDism/WeatherSwingViewer) 
[![codecov](https://codecov.io/gh/WeDism/WeatherSwingViewer/branch/master/graph/badge.svg)](https://codecov.io/gh/WeDism/WeatherSwingViewer)

# This is a free application for weather the viewer. You can download from url: [WeatherViewer](https://github.com/WeDism/WeatherSwingViewer/releases)
![Weather Viewer](/src/main/resources/images/PartlyCloudy.png)

## For run this app you have to do next steps
### Steps to run on windows system
1. Download and install jdk 8
1. Download and install maven 3
1. Open the environment variable window
1. And add into variable path string with the path to maven and jdk 8
1. Download and extract files from zip
1. Into folder root project you have to run build.bat file
1. Will be created WeatherSwingViewer-1.0-full.jar into folder with title target
1. Run WeatherSwingViewer-1.0-full.jar

### Use case tutorial
Finally you are viewed splash screen:

![splash screen](/src/main/resources/gifs/loading.gif)

After initializing the application you have to saw main form. 

![start up main form](/readme_images/startup_main_form.png)

For changing current location you can click ***${Settings}*** and choose ***${Change Location}***. 
You can see empty ***${Choose location}*** form in next image with unchecked ***${City is find}*** checkbox.

![choose location form](/readme_images/empty_choose_location_form.png)

Next first step you have to input city in text box under ***${Search city}*** label and in  second step should be selected
combobox with needed country under ***${Select country}*** label. 
After these steps you have to click on ***${Search}*** button for search. And you see search your location. See next image.

![searching location form](/readme_images/searching_choose_location_form.png)

If your data is valid you should saw form with checked ***${City is find}*** checkbox. As in the following image.

![true filled choose location form](/readme_images/true_filled_choose_location_form.png)

After input data click ***${OK}*** button for view current and forecast weather.

![main form after valid data](/readme_images/main_form_after_valid_data.png)

For detailed forecast on the 5 days view click on ${Forecast for the workweek}.