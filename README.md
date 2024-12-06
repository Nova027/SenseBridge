# AI Buzz Phase 2 Submission

## Idea Details
- Team Name: SenseBridge
- Idea Title: SenseBridge – A Unified Accessibility App

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
   - This is our primary part, intended to be the user-end application. The android app is developed fully in Kotlin, using Jetpack Compose and modern android design philosophies. You can find the kotlin code in app/src/main/java/com/example/sensebridge, you can see the kt files for each class - MainActivity (start point), 3 available screens (HomeScreen, SceneDescription, Screen3 (placeholder for now)), CameraControl (meant to get setup camera with some analyser back-end, and get preview from camera feed, if applicable), and ImageAnalyserAlgo class (meant to receive ImageProxy from camera for analysis, and call applicable model preprocessing/inferencing/postprocessing on the same). There's an auto-generated (but customized) folder ui.theme for app's color scheme, and a models folder to store the classes for each distinct AI-hub model used, and a parent Model class. There's also AppData.kt to store different shared data classes and variables.
   - As of now, we have only implemented OpenAI clip image-encoder in our project, so it's directly written in Model class, but will be shifted as we add more. The intention, for example, would be to have ClipModel as a wrapper class implementing an interface to use both image and text encoders, and also contain externally callable functions to have useful post-processing and others (if applicable), based on model input/outputs.
   - To write about Model class………..
   - As of now, ImageAnalyserAlgo, though most of it is generalizable, has few lines which would not work for all models, and the flow here is also currently supporting Clip image-encoding.  To generalize further (would also be applicable in some parts of Model class implementation), we plan to use sealed classes and/or lambdas. The analyze function is called fully asychronously by camera controller, when frames are available. In analyze, we convert the image (assuming it is RGBA_8888 imageproxy, which is what we stick throughout, instead of YUV) to bitmap, and then call the model-specific preprocessing, inference, and postprocessing. Finally, the onResult lambda is called, which helps to set variables in main thread, which in turn, is meant to trigger some action to display something in main thread.
   - CameraControl class uses cameraController as of now, as it is easier to setup for preview (and more direct user control from the preview viewfinder like zoom & TTF), and we used it mainly for demo purpose as we want to show the real-time camera preview and associated output. In a final app, there's not much use of preview, especially for scene description which is meant for visual disabilities. Currently we have kept the ImageAnalyserAlgo variable, since our purposes require analysis everytime when using camera, and we setup few things for image analysis during camera setup - setting the analyser object and executor (a separate thread), backpressure strategy to drop frames and keep only latest frames if image analysis takes long time (which it should with these heavy models) (this way we are guaranteed the output generated will be from the currently relevant frame, not an older frame), and also set RGBA_8888 image format. There's also a switchCamera function which we were using earlier to associate with the onClick of an IconButton overlaid on camera preview, to switch front/rear camera (however, is currently removed to just show the embedding values for demo purpose). And there's also a preview composable, which binds the camcontroller's lifecycle to current screen's life cycle (so, controller is freed when current screen is removed from NavBackstackEntry).
   - During onCreate of MainActivity, we request permissions if not yet granted, and define an executor with a new thread meant for running all the ML models and associated pre/post-processing in a separate backend. Since we currently don't plan to support concurrently running multiple features, we reuse the same thread across all features. However, it is kept separate from the main UI thread, since these are heavy processes which need to happen asynchronously. The LLM, when added, will also need its own thread as it requires background presence for many features. All of the feature screens are intended to be running within a single activity (MainActivity), implemented using NavHost, linking the root composable of each particular screen's UI, and startDestination defined as the home-screen.
   - Home screen displays a list (scrollable LazyColumn) of previous sessions, also intended to store and display transcriptions for each session. There are floating action buttons added for voice control (via WavLM & Llama LLM), and for adding a new session. Voice control is yet to be implemented, but the add session button opens a dialog box listing the list of available features, and based on selection (if valid), appropriate screen navigation happens using the NavHost navController.
   - The only meaningful feature screen currently implemented is SceneDescription. It initializes the Model, ImageAnalyserAlgo and CameraControl objects, and sets them up within a disposable effect, that will only run the first time SceneDescriptionUI is composed in its lifecycle (ie, only when we enter this screen, not whenever something changes in the screen's UI in recompositions). The onResult of imageAnalyser is set to take few values from the output image embedding by the clip model used internally from analyze function. There's a column with white background covering most of the screen (meant for debugging but also used in demo), which shows the display value as text on screen, recomposing whenever display state variable is changed, reflecting the newly changed value.
   - .. Summary or more?

2. Python notebook demo of OpenAI clip tflite models (image and text encoder as available in AI-hub) and outputs similarity values (will refer as "Clip-python").
   - The code uses the CLIP (Contrastive Language–Image Pre-training) model to calculate the similarity between an image and a list of text descriptions. It utilizes pre-trained TensorFlow Lite models from AIHub for image and text encoding, allowing for efficient inference.
   - Model Loading: Loads TensorFlow Lite interpreters for the image and text encoders from .tflite files. Loads the CLIP model and its preprocessing function using clip.load.
   - Inference Function: Defines a function inference_model to perform inference with the given interpreter and input data. Converts the input tensor to a NumPy array, sets it as input to the interpreter, invokes the interpreter, and retrieves the output tensor.
   - Image and Text Processing: Loads an image using PIL.Image and preprocesses it using the CLIP preprocessing function. Defines a list of text descriptions.
   - Similarity Calculation: Obtains image features by running the image through the image encoder. Iterates through the text descriptions: Tokenizes the text using clip.tokenize. Obtains text features by running the tokenized text through the text encoder. Calculates the similarity (probability) between the image and text features using dot product. Prints the probability for each text description.
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
