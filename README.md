<img src="https://user-images.githubusercontent.com/48272857/69966580-0b5b6380-155a-11ea-9a33-4309eb6de386.png" width=300px>

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

    <img src="https://user-images.githubusercontent.com/48272857/70027084-a13bd080-15e4-11ea-9dfe-3c99e2fa6159.png" width=300px>

    ① Update lately obstacle image <br>
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

        <img src="https://user-images.githubusercontent.com/48272857/70027852-7488b880-15e6-11ea-866a-2d531017690f.png" width=300px>

        Click button to start in background

    2. In Background

        <img src="https://user-images.githubusercontent.com/48272857/70027857-76eb1280-15e6-11ea-8165-4c60343de400.png" width=300px>

        Click on the notification to enter the app
        
        <img src="https://user-images.githubusercontent.com/48272857/70042691-ac9df480-1602-11ea-85fa-0170de92fbba.png" width=300px>

        Click back button to exit the app

3. Total Process

    <img src="https://user-images.githubusercontent.com/48272857/70041751-0d2c3200-1601-11ea-8c6d-7b1a5f7bc782.gif" width=300px>

