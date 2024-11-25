# AI Buzz Phase 2 Submission

## Idea Details
- Team Name: SenseBridge
- Idea Title: SenseBridge – A Unified Accessibility App
- Idea url: https://aibuzz.qualcomm.com/idea/3567

- Team Members
  - Swarnava Das
  - Haresh Gaikwad
  - Atanu Debnath

- Programming language used: Kotlin, C++, Python (to prepare models)

- AI Hub Model links
  - https://aihub.qualcomm.com/models/openai_clip
  - https://aihub.qualcomm.com/models/trocr
  - https://aihub.qualcomm.com/models/mediapipe_hand
  - https://aihub.qualcomm.com/models/huggingface_wavlm_base_plus
  - https://aihub.qualcomm.com/mobile/models/llama_v3_8b_chat_quantized

- Target device
  - [ ] PC
  - [x] Mobile
  - [ ] Others: 


## Implementation Summary
<!-- 
Write a summary of what you have developed and how user can navigate the code base. 
Mention important files/functions to check, limitations of current implementation and future scope.
-->
Currently, there are 3 distinct parts in our project, since integration is pending, specifically - 
1. Android app which allows creating sessions for each available feature, with Clip tflite image encoder enabled on camera feed (will refer as "Android-app").
   - To write.
2. Python notebook demo of OpenAI clip tflite models (image and text encoder as available in AI-hub) and outputs similarity values (will refer as "Clip-python").
   - To write.
3. Python notebook demo of Hand Landmark processing and flow to predict sign language with our custom NN model based on landmarks (will refer as "Handsign-python").
   - To write.

## Installation & Setup steps
<!-- 
Mention in detail how a reviewer can install and run your project. Preferable include a script to automate the setup.
Make sure to include the pre-requisite packages/assumptions (e.g. Java, Android Studio) in detail.
-->
Setup for each section (Please reach out if there is confusion in running) --
1. Android-app (Android Studio project directory : android/SenseBridge)
   - Required software to build the project - Android Studio (preferably latest version) with Kotlin 2.0. Download Android SDK 35 during or after installation, through Android studio.
   - Need an android device to test, preferably mobile, else can download emulator AVD in android studio. We have kept minSDK as 24, so should not have issues if target device has Android ver > 7.0.
   - Open the android/SenseBridge folder in android studio. Within app/src/main, create an assets folder. Make sure to download "openai_clip-clipimageencoder.tflite" from AI-hub link and place in this folder, make sure the filename matches.
   - Now you can build and load the app into your device for testing. All external dependencies required, are downloaded and included via gradle automatically.
2. Handsign-python (file : python/Hand_LandMark_To_Sign_Detection.ipynb)
   - Requires python3 to run it. One easy way to run it is to install Anaconda, which comes with jupyter notebook, which can be used to step through and run the ipynb. Or can upload to Google Colab as the code is not CCI.
   - Necessary dependencies to "pip install" before running : numpy, cv2, PIL, qai_hub_models, pandas. Model is used directly from qai_hub, so downloading from AI-hub won't be needed.
   - After this, you can run individual cells one by one in jupyter notebook, and the last cell will open a video capture window, where you can show hand-signs which can be detected by the final model.
3. Clip-python (file : python/Clip_AIBUZZ.ipynb)
   - Also needs python3 and jupyter notebook, but installs pip dependencies directly through code. No further code dependencies.
   - Needs the model files "openai_clip-cliptextencoder.tflite" and "openai_clip-clipimageencoder.tflite" from AI-hub, with the same name, in file location.
   - The code expects an image with a certain filename, which is currently kept as "a_photo_of_a_television_studio.png", but it's upto the tester they can use any image, and modify filename in 2nd last cell.

## Expected output / behaviour
<!-- 
Provide details of expected behaviour and output.
Mention how the reviewer can validate the prototype is doing what it is intended to.
If your prototype requires some files / data for evaluation, make sure to provide the files along with instructions on using them.
-->
1. Android-app
   - You can test a half of the "Scene description feature", which displays the first 8 values of the openAI clip image embedding based on real-time camera feed. (Refer demo video for a sample use).
2. Handsign-python
   - You can run individual cells one by one in jupyter notebook, and the last cell will open a video stream window, where you can show ASL hand-signs which can be detected by the final model.
3. Clip-python
   - In 2nd last cell, can change filename in the line "image = preprocess(Image.open("a_photo_of_a_television_studio.png")).unsqueeze(0)" based on whatever image file you want to evaluate.
   - In 2nd last cell, can modify text list "text_list = ["a photo of a cat", "a photo of a dog", "a photo of a television studio", "a photo of a flying cat"]" based on what texts you want to compare the image with.
   - In last cell, final output should come as a list of scores (dot products of image and text embeddings) for each given text, giving an order of similarities with given image.
## Any additional steps required for running
- [x] NA
<!-- 
Mention any additional requirements here. If not, leave the NA.
-->

## Submission Checklist
- [x] Recorded video
- [x] Readme updated with required fields
- [ ] Dependency installation scripts added -- (only pip install is required for landmark notebook)
- [ ] Startup script added -- (NA)
- [x] Idea url updated in Readme
