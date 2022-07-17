MaskDetectCamera
=====================

MaskDetectCamera is a project demo Mask Detection from Camera or Image, use class media.FaceDetector android. Not using 3rd library like OpenCV or Luxand FaceSDK, ...
https://github.com/betri28/FaceDetectCamera is basic detection project.

Features built in:
  - Detect from live Camera and from Photo in Gallery.
  - Detect more face at the same time and draw exactly location in faceview.
  - Each face have a ID.
  - Auto crop small face and display in RecylerView.
  - Calculate FPS (Detected frame per second).
  - Detect mask from the cropped face.
  - Play audios when mask detected or not from face.

##Features

This project use method: convert Frame to bitmap then use media.FaceDetector detect faces in that bitmap. Have 2 ways to convert Frame to bitmap: convert to gray bitmap or convert to RGB bitmap.

  1. Convert Frame to gray bitmap so this way has **High performance** (FPS higher than RGB), butcroped of faces is gray image.
  
  2. Convert Frame to RGB bitmap, Low performance (FPS lower than Gray), but croped of faces is RGB image.



- **Detect Mask from Camera**

  - RGB
    


