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
