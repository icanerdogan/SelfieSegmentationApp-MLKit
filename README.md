<h1>Selfie Segmentation App</h1> 
<p>Welcome to SelfieSnap: Selfie Studio AI, the ultimate selfie editing app powered by cutting-edge artificial intelligence! Unleash your creativity and take your selfies to the next level with our advanced technology that seamlessly separates your face from the background, opening up a world of exciting possibilities.</p>
<a href="https://play.google.com/store/apps/details?id=com.ibrahimcanerdogan.selfiesegmentationapp"><img width="90" height="90" src="https://img.icons8.com/?size=512&id=L1ws9zn2uD01&format=png"/></a>

<p>ML Kit's selfie segmentation API allows developers to easily separate the background from users within a scene and focus on what matters. Adding cool effects to selfies or inserting your users into interesting background environments has never been easier.</p>
<p>The selfie segmentation API takes an input image and produces an output mask. By default, the mask will be the same size as the input image. Each pixel of the mask is assigned a float number that has a range between [0.0, 1.0]. The closer the number is to 1.0, the higher the confidence that the pixel represents a person, and vice versa.</p>
<p>The API works with static images and live video use cases. During live video, the API will leverage output from previous frames to return smoother segmentation results.</p>

<img src="https://github.com/icanerdogan/SelfieSegmentationApp-MLKit/assets/52867508/93fa0194-e4d7-4feb-b2ff-b50322a9790d">

<h2> Key capabilities </h2>
<ul>
  <li>Cross-platform support Enjoy the same experience on both Android and iOS.</li>
  <li>Single or multiple user support Easily segment multiple people or just a single person without changing any settings.</li>
  <li>Full and half body support The API can segment both full body and upper body portraits and video.</li>
  <li>Real time results The API is CPU-based and runs in real time on most modern smartphones (20 FPS+) and works well with both still image and live video streams.</li>
  <li>Raw size mask support The segmentation mask output is the same size as the input image by default. The API also supports an option that produces a mask with the model output size instead (e.g. 256x256). This option makes it easier to apply customized rescaling logic or reduces latency if rescaling to the input image size is not needed for your use case.</li>
</ul>

<h2> Pose Detection App Preview </h2>

Main Screen        |  Segment       
:-------------------------:|:-------------------------:|
![](https://github.com/icanerdogan/SelfieSegmentationApp-MLKit/assets/52867508/dfca1f22-f891-4837-9841-0185ae7f3188) | ![](https://github.com/icanerdogan/SelfieSegmentationApp-MLKit/assets/52867508/6d1d17a7-ccb8-4f3d-aa4a-e9ee64936eeb)

## License

````
MIT License

Copyright (c) 2023 İbrahim Can Erdoğan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

