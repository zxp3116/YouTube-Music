# YouTube-Music
須開啟YouTube相關的APP，只需有手機網路即可進行在線抓歌，支援背景播放，且無任何廣告。
1.客製化歌單：可自己修改歌單的內容及名稱，增添喜歡的歌曲或移除不想要的歌曲。

2.三種播放模式：分別是循環播放(歌單中第一首播放到最後一首)、隨機播放(從歌單中隨機挑選歌曲播放)、單曲循環(同一首歌曲不斷播放)。

3.資料本地儲存：無論建立或修改後，歌單裡的內容資料將儲存在手機裡，實現真正意義上的「YouTube MP3」，但歌曲播放仍須有網路支援。

原理：首先使用volley框架作為網路請求，透過「YouTube Data API」進行資料的爬蟲，並將回傳的JSON格式資料以AsyncTask異步任務加載到自訂的ListVie上進行呈現，同時使用Handler達到動態更新UI介面

使用到的第三方類庫：gson、volley、PierfrancescoSoffritti/android-youtube-player、glide、viewmodel-ktx、livedata-ktx、CustomPopwindow
