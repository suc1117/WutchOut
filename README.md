<a id=top></a>

# WutchOut 
<img src="https://user-images.githubusercontent.com/48272857/70604670-5fda9f00-1c3c-11ea-83d1-828135ad58dc.png" width=300px>

## General info
Android Application for [5678](https://github.com/suc1117/5678 "5678")

Very Simple Application for project

It help check accident location information also distance to user location

When user come nearby accident location, It notice through notification even if user not using phone

Download : [google drive](https://drive.google.com/file/d/1qZLCeruprxjseQ0McW7ng6tFVaqShuP7/view?usp=sharing)

## Technologies
Project is created with:
* Android Studio (SDK 23)

(Tested in SDK 26 = Galaxy S8)

## Functions

1. Application Layout

    <img src="https://user-images.githubusercontent.com/48272857/70604664-5ea97200-1c3c-11ea-8c0f-a633a4b30099.png" width=300px>

    ① Update lately obstacle image <br> 
    (click the image to show accident location detail in Google Map) <br>
    ② Scrollable accident record list. It's contain about time and gps<br>
    ③ Update GPS about lately accident <br>
    ④ Update Time about lately accident <br>
    ⑤ Update Predict distance (Vehicle to Obstalce) <br> 
    ⑥ Update GPS about User current location <br>
    ⑦ Measure distance user to accident location <br>
    ⑧ Start application in the background button <br>

2. Notification<a id="notify"></a>

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

        <a id="back"></a>

        <img src="https://user-images.githubusercontent.com/48272857/70145615-e1ce4380-16e3-11ea-90ca-e93b741da7d0.png" width=300px>


        Click back button twice to exit the app

3. Google Map

    <img src="https://user-images.githubusercontent.com/48272857/70145221-ef36fe00-16e2-11ea-82af-69b8aa0b74d8.png" width=300px>

    User can check accident location in map(①)

    Click the button return to main page

4. Total Process

    <img src="https://user-images.githubusercontent.com/48272857/70041751-0d2c3200-1601-11ea-8c6d-7b1a5f7bc782.gif" width=300px>
    (old version)
    
## 주요 코드 설명

1. ConnectFTP.java

    Apache의 [Commons-Net](https://commons.apache.org/ "Commons-Net") 라이브러리를 이용하였습니다. 이 라이브러리는 telnet, NTP, FTP등 다양한 일반적인 프로토콜을 제공하고 통신 기능을 쉽게 사용할 수 있습니다.

    안드로이드 스튜디오에 라이브러리를 추가하는 방법과 FTP 기능 구현은 다음 웹페이지를 참고하였습니다 <br> 
    (출처 : [웹페이지](https://hiiambk.tistory.com/351 "web"))

    FTPClient를 이용하여 FTP서버와의 연결, 목록받기, 다운로드 등을 구현하였습니다. <br>
    (참고 : [문서](https://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html "docs"))

    구현한 함수들은 다음과 같습니다.

    ```java
    public boolean ftpConnect(String host, String username, String password, int port)
    public boolean ftpDisconnect()
    public boolean ftpChangeDirctory(String directory) // 미사용
    public String ftpGetDirectory()
    public String[][] ftpGetFileList(String directory)
    public boolean ftpRenameFile(String from, String to) // 미사용
    public boolean ftpDownloadFile(String srcFilePath, String desFilePath)
    ```
    #### ftpConnect
    FTP서버와의 연결 기능을 제공합니다.

    #### ftpGetFileList
    FTP서버의 모든 파일리스트를 가져와서 파일과 폴더로 나누어 2차원 배열로 가져올 수 있는 기능을 제공합니다. <br>

    ```java
    // code line 69
    for(FTPFile file : ftpFiles) {
                String fileName = file.getName();
                if (file.isFile()) {
                    fileList[i][0] = fileName;
                    fileList[i][1] = "(File)";
                }
                else {
                    fileList[i][0] = fileName;
                    fileList[i][1] = "(Directory)";
                }
                i++;
            }
    ```
    다음 코드 부분과 같이 반환될 2차원 String 배열에서 첫번째 인덱스에 파일 이름을 넣고 두번째 인덱스에 형식 정보를 넣었습니다. <br>
    (현재 프로젝트상에선 이 부분을 적극 사용하진 않으나, 추후 시간대별로 폴더로 정리할 경우를 생각하여 구현해놓았습니다.) <br>

    #### ftpDownloadFile
    FTP 서버의 파일을 다운로드 받는 기능을 제공합니다. 어플 화면에 낙하물 이미지를 띄워주기 위해 사용합니다. <br> 첫번째 인자에 ftp의 경로포함 파일명, 두번째 인자에 다운받을곳의 경로포함 파일명을 넣어주면 동작합니다.

    **작동되지 않는 경우** [클릭](#imagedownload)하여 참고

2. RecyclerViewAdapter.java

    리스트 뷰는 성능 상의 이슈도 해결해주면서, 많은 타입의 뷰들을 가독성 있게 보여줄 수 있습니다. <br>
    어플의 History 레이아웃으로 사용하였습니다.

    출처를 참고하여 저희에게 필요한 부분으로 수정하여 구현하였습니다. <br>
    (출처: [웹페이지](https://sh-itstory.tistory.com/32 "web") & [Github](https://github.com/aiex1234/RecyclerView "github"))

    myFile.java 라는 생성자를 만들어 리스트 배열로 사용하였습니다. <br>
    파일명과 파일형식을 String 타입으로 가지도록 하였습니다.

3. GoogleMapActivity.java

    1. Google Developers Console 사이트 [링크](https://console.developers.google.com/apis/dashboard "링크")에 접속하여 프로젝트 만들기를 클릭합니다.
    
    2. 프로젝트 이름을 적고 만들기를 클릭합니다.

    3. Google Maps Android API를 사용하려면 추가 설정이 필요합니다.  API 및 사용 서비스 사용 설정을 클릭합니다.

    4. google maps android를 검색하여 Maps SDK for Android를 선택합니다. 

    5. 사용 설정을 클릭합니다.

    6. API가 활성화 되었습니다. 인증 설정을 하기 위해 사용자 인증 정보를 클릭합니다. 

    7. 사용자 인증 정보 만들기를 클릭하여 보이는 메뉴에서 API 키를 클릭합니다.

    8. 키 제한을 클릭합니다.

    9. 앞에서 생성된 API 키에 사용 제한을 둘 수 있는 웹페이지가 보입니다. 애플리케이션 제한사항, API 제한사항을 설정할 수 있습니다.

    10. Android 앱을 선택하고 항목 추가를 클릭합니다.

    11. Google Maps Android API를 사용할 안드로이드 프로젝트의 패키지 이름과 Android Studio가 설치된 컴퓨터에서 생성된 SHA-1 인증서 지문이 필요합니다.

    12. SHA-1 인증서 지문을 얻기 위한 과정부터 진행합니다.  윈도우키 + R을 누른 후 cmd를 입력하고 엔터를 눌러서 명령 프롬프트 창을 엽니다. 

    13. 윈도우에서는  다음 명령으로 SHA-1 인증서 지문을 획득할 수 있습니다.
    ("C:\Program Files\Android\Android Studio\jre\bin\keytool" -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android) 
    SHA1 옆에 있는 문자열을 복사해 둡니다.

    14. Google Developers Console 사이트에  복사해두었던 패키지 이름과 SHA-1 인증서 지문을 붙여넣기하고 완료를 클릭합니다.  

    15. API 제한사항에서  키 제한을 선택하고 콤보박스에서 Maps SDK for Android를 체크합니다.  이제 저장을 클릭합니다.

    16. API 키를 복사해둡니다.

    17. 매네페스트 파일 AndroidManifest.xml의 application 태그 하위요소로 meta-data 태그를 사용하여 복사해둔 API키를 입력해줍니다.

    18. Google Maps Android API를 사용하려면 Google Play services 라이브러리 패키지를 설치해줘야 합니다.<br>
    안드로이드 스튜디오로 돌아와서 메뉴에서 Tools > SDK Manager를 선택합니다.
    SDK Tools 탭을 클릭하고 Google Play services 항목을 체크하고 Apply를 클릭하여 설치를 진행합니다.

    19.  모듈 app의 build.gradle에 Google Play Services 라이브러리를 프로젝트에서 사용한다고 추가해줘야 합니다.<br>
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
    implementation 'com.google.android.gms:play-services-location:17.0.0'
    추가해 준 후 , Sync Now를 클릭합니다.

    다음 내용은 [링크](https://webnautes.tistory.com/647?category=618190 "링크")를 참고했습니다.

    진행하던중에  Android Studio 3.3으로 업데이트후 다음과 같은 에러가 발생했습니다. 

    ```
    E/AndroidRuntime: FATAL EXCEPTION: Thread-6
    Process: com.tistory.webnautes.googlemapsandroidapiexample, PID: 7919
    java.lang.NoClassDefFoundError: Failed resolution of: Lorg/apache/http/ProtocolVersion;
        at ez.b(:com.google.android.gms.dynamite_mapsdynamite@14799088@14.7.99 (100800-223214910):3)
        at ey.a(:com.google.android.gms.dynamite_mapsdynamite@14799088@14.7.99 (100800-223214910):3)
        at fa.a(:com.google.android.gms.dynamite_mapsdynamite@14799088@14.7.99 (100800-223214910):15)
        at com.google.maps.api.android.lib6.drd.al.a(:com.google.android.gms.dynamite_mapsdynamite@14799088@14.7.99 (100800-223214910):6)
        at ed.a(:com.google.android.gms.dynamite_mapsdynamite@14799088@14.7.99 (100800-223214910):21)
        at ed.run(:com.google.android.gms.dynamite_mapsdynamite@14799088@14.7.99 (100800-223214910):8)
     Caused by: java.lang.ClassNotFoundException: Didn't find class "org.apache.http.ProtocolVersion" on path: DexPathList[[zip file "/data/user_de/0/com.google.android.gms/app_chimera/m/0000000d/MapsDynamite.apk"],nativeLibraryDirectories=[/data/user_de/0/com.google.android.gms/app_chimera/m/0000000d/MapsDynamite.apk!/lib/x86_64, /system/lib64]]
     ```


     매네페스트 파일 AndroidManifest.xml의 <application> 태그 하위요소로 다음 한줄을 추가해서 해결했습니다.<br>

     ```
     uses-library android:name="org.apache.http.legacy" android:required="false"
     ```

     이제 구글맵에 현재위치를 표시해보겠습니다.

     현재위치를 표시하기위해 추가한 코드내용은 다음링크를 참고하였습니다.
     [링크](https://github.com/suc1117/WutchOut/blob/master/app/src/main/java/com/example/wutchout/GoogleMapActivity.java "링크")







4. MainActivity.java

#### 환경 설정 
먼저 사용할 FTP서버 환경을 맞춰야 합니다 <br>

```java
/// code line : 124
mainThread = new Thread(new Runnable() {
    @Override
        public void run() {
            while (!threadStop) {
                status = false;
                status = connectFTP.ftpConnect(
                        "(IP 주소)"
                        "(서버 ID)",
                        "(서버 PW)",
                        21);
    ...
``` 

성공적으로 연결이 되면 FTP 서버의 파일 리스트를 받아옵니다. <br>
이는 History 목록을 업데이트하는데 사용되고 최근 사고정보를 받아오는데 사용합니다 <br>

#### 낙하물 이미지 업데이트 
이미지를 받아오는 과정에서 데이터 낭비를 막기 위해서 sharedPreference를  이용하여 이전 데이터와 비교하여 사고정보가 업데이트되기 전엔 이미지를 받아오지 않도록 하였습니다.

```java
/// code line : 144
if (!(sharePref.getString("latelyAccident", "0").equals(latelyAccidentFile))) {
                            editor.putString("latelyAccident", latelyAccidentFile);
                            editor.commit();
                            getImageThread.start();
                        }
```

#### 시행 착오 : UI
개발 중에 쓰레드 상에서 UI를 업데이트할 때 오류가 발생했었는데 다음과 같은 기능을 제공하고 있었습니다.

```java
/// code line : 157
runOnUiThread(new Runnable() {
    @Override
    public void run() { 
        ...
    }
```
이를 쓰레드 내부에서 선언하여 UI 업데이트 관련 코드를 작성하면 오류가 발생하지 않습니다.

#### 데이터 파싱

이 프로젝트에선 FTP에 낙하물 이미지가 업로드 될때 다음과 같은 형식을 갖습니다. <br> 
<u>이와 같은 형식이 아니라면 **파싱과정에서 오류가 생깁니다.**</u><br>

```
2019-12-03 16-58-02 ,36.834034,127.179329,3.65879564.jpg

(년-월-일 시-분-초 ,위도,경도,예측거리.jpg) (GPS 소수점 올림은 기본값 6자리입니다.)
```

다음은 시간정보와 위치정보를 파싱하는 코드입니다.

```java
getMyLocation();
FileParsingArray = latelyAccidentFile.split(" ");
time_val = FileParsingArray[0] + " " + FileParsingArray[1];
FileGpsArray = latelyAccidentFile.split(",");
gps_lat = FileGpsArray[1];
gps_lon = FileGpsArray[2];
pred_dist = FileGpsArray[3].substring(0, FileGpsArray[3].length() - 4);
```

#### 이미지 다운로드<a id="imagedownload"></a>

이미지는 휴대폰 내부저장소에 어플 폴더 내부에 저장되도록 설계하였습니다.<br>
첫 이미지를 다운받을 땐 Files라는 하위폴더를 생성하고 그 안에 main.png를 생성합니다. <br>
이 후로는 main.png에 덮어씌우는 형태입니다.
```java
// code line : 195
getImageThread = new Thread(new Runnable() {
    public void run() {
        ...
        try {
            mainThread.join();
            file.createNewFile();
        } catch (Exception e){}
        connectFTP.ftpDownloadFile(currentPath +currentFileList[last_index][0], file.toString());
    }
});
```

#### 사용자 위치정보 받아오기
휴대폰 GPS 센서를 이용하여 주기적으로 사용자의 위치정보를 받아옵니다. <br>
```java
// code line : 217
 private void getMyLocation() {
    LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            float accuracy = location.getAccuracy();
            ...
```
다음 코드를 통해 경고 범위 안에 들어왔을 때 과정을 진행합니다. <br>
([경고 알림 발생](#notification), 화면 깨우기)

다음 코드를 통해 위치정보를 얼마의 주기를 가지고 업데이트할 지 결정하고 이를 수행합니다.
```java
// code line : 263
Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            1000,
            1,
            gpsLocationListener);
    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
            1000,
            1,
            gpsLocationListener);
```

#### 알림 구현<a id="notification"></a>

사용자가 어플을 실행 후 화면을 보고있거나 백그라운드에서 실행되고 있거나 화면이 꺼졌을 때 작동되도록 하였습니다. <br>
SDK 26 이후로는 알림채널을 만들어줘야 작동하기 때문에 버전을 따로 분류하여 다음과 같은 코드를 통해 구현하였습니다.

```java
// code line : 293
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    int importance = NotificationManager.IMPORTANCE_HIGH;
    NotificationChannel mChannel = notifManager.getNotificationChannel(id);
    if (mChannel == null) {
        // id, title, importance가 없으면 채널 생성이 안됩니다.
        mChannel = new NotificationChannel(id, title, importance);
        ...
        notifManager.createNotificationChannel(mChannel);
    }
}
else {
        builder = new NotificationCompat.Builder(context, id);
        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentTitle(aMessage) // required
                ...
    }
    Notification notification = builder.build();
    notifManager.notify(NOTIFY_ID, notification);
}
```
유저가 낙하물의 일정 반경안에 들어오게되면 알림을 띄웁니다. <br>
화면이 꺼져있어도 가능하고, 백그라운드에서 실행중일때도 가능합니다.

```java
// code line : 239
if (distance != 0 && distance <= warnDistance && distanceUnit=="M" && threadStop!=true) {
    String strDistance = String.format("%.2f", distance);
    createNotification("WARNING! Remain Distance To Accident : "+strDistance+"M !!!",MainActivity.this);
    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn(); 
    // check if screen is on
    if (!isScreenOn) {
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
        wl.acquire(3000); 
    }
}
```

#### 어플리케이션 종료 
어플 내 백그라운드 실행버튼이 따로 있습니다. 따라서 뒤로가기를 누르게 되면 바로 어플이 onDestroy 되어 완전히 종료되기 때문에 실수를 방지하기 위해 뒤로가기를 두번 눌러야 종료되게 처리하였습니다. [이미지](#back)<br>

```java
@Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"Press the back button again to exit...",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }
```
[맨 위로](#top)
