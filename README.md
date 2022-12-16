#### What app does

1.The app list outs the images from the pixabay api on first place with default query fruits 
2. If user clicks on any list, the confirmation dialog being rendered
3. Once confirmed, it moved to Image details screen for better image resolution along with details


### TECH stack used
1. View binding (I guess, Data binding is slow compared to view binding because it's works on annotation processing concept)
2. Paging      (Seems like there is a bug in Paging library RemoteMediator's class, sometimes it prefetches the data for next three to four pages ahead, 
                Need to investigate, Correct me if I am wrong please. )
3. Coroutines
4. Hilt for DI
5. Navigation library
6. Kotlin
7. Android SDK
8. MVVM
9. Room
10. Flow
11. Clean architecture

##### TODOs
1. Mapping DTO to model or vice versa
2. Due to time constrains, unable to write UI and unit test cases. 
3. Need to polish the UI a bit


