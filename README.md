# WutchOut 

<img src="https://user-images.githubusercontent.com/48272857/70144917-42f51780-16e2-11ea-9634-df2cc783ee5a.png" width=300px>

## General info
Android Application for [5678](https://github.com/suc1117/5678 "5678")

Very Simple Application for project

It help check accident location information also distance to user location

When user come nearby accident location, It notice through notification even if user not using phone

## Technologies
Project is created with:
* Android Studio (SDK 23)

(Tested in SDK 26)

## Functions

1. Application Layout

    <img src="https://user-images.githubusercontent.com/48272857/70145083-a4b58180-16e2-11ea-9b03-5116cc025bf3.png" width=300px>

    ① Update lately obstacle image <br> 
    (click the image to show accident location detail in Google Map) <br>
    ② Scrollable accident record list <br>
    ③ Update GPS and Time about lately accident information <br>
    ④ Update GPS about User current location <br>
    ⑤ Measure distance user to accident location <br>
    ⑥ Start application in the background button <br>

2. Notification

    Notification will trigger when distance(⑤) less than 100M (default)

    You can change this
    
    ```ruby
    float warnDistance=100;
    ```

    Notification will appear to user's phone whatever it do

    1. In Application

        <img src="https://user-images.githubusercontent.com/48272857/70145870-5d2ff500-16e4-11ea-8c84-b1b940057d6f.png" width=300px>

    2. In Background
    
        (Click button to start in background)
        
        <img src="https://user-images.githubusercontent.com/48272857/70027857-76eb1280-15e6-11ea-8165-4c60343de400.png" width=300px>

        Click on the notification to enter the app
        
        <img src="https://user-images.githubusercontent.com/48272857/70145615-e1ce4380-16e3-11ea-90ca-e93b741da7d0.png" width=300px>

        Click back button twice to exit the app

3. Google Map

    <img src="https://user-images.githubusercontent.com/48272857/70145221-ef36fe00-16e2-11ea-82af-69b8aa0b74d8.png" width=300px>

    User can check accident location in map(①)

    click the button to return to main page

4. Total Process

    <img src="https://user-images.githubusercontent.com/48272857/70041751-0d2c3200-1601-11ea-8c6d-7b1a5f7bc782.gif" width=300px>
    (old version)
    
