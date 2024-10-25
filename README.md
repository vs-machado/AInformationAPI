<h1 align="center">AInformation</h1>
<p align="center">An Android demonstration app that generates news summary using AI</p>

<div align="center">
  <table>
    <tr>
      <td><img src="https://github.com/user-attachments/assets/9ea401eb-da08-41a9-89a6-d185bb8226f7" alt="Screenshot 1" width="200"></td>
      <td><img src="https://github.com/user-attachments/assets/e122cbd4-b41c-4500-8057-80cf14633f5c" alt="Screenshot 2" width="200"></td>
      <td><img src="https://github.com/user-attachments/assets/7dbf8602-f53d-4f12-9c80-840d3c5ed950" alt="Screenshot 3" width="200"></td>
    </tr>
    <tr>
     <td><img src="https://github.com/user-attachments/assets/01d8e4f4-61dd-4fe6-9d12-4be3b481e2aa" alt="Screenshot 4" width="200"></td>
     <td><img src="https://github.com/user-attachments/assets/7f2279d0-605e-42cd-93cc-5f7a68cc35b2" alt="Screenshot 5" width="200"></td>
     <td><img src="https://github.com/user-attachments/assets/19164d1b-5b46-4d94-8d7c-1f63d08007b6" alt="Screenshot 6" width="200"></td>
    </tr>
  </table>
</div>

<h2>Features</h2>
<ul>
  <li>Access the full news</li>
  <li>Generate news summaries using AI</li>
</ul>

<h2>Languages supported</h2>
<ul>
  <li>English</li>
  <li>Portuguese - BR (only interface and news description were translated, description translation provided by Gemini)</li>
</ul>

<h2>Libraries used</h2>
<ul>
  <li>Jetpack Compose for UI design</li>
  <li>TikXML for RSS feed parsing</li>
  <li>jsoup for HTML parsing</li>
  <li>GenerativeAI (Gemini library) for AI generations</li>
  <li>Coil for image loading</li>
  <li>Retrofit2 as HTTP client</li>
  <li>Hilt for dependency injection</li>
  <li>JUnit and Mockito for unit testing</li>
  <li>Kapt for annotation processing (KSP was not used due to incompatibilities with TikXML)</li>
</ul>

<h2>Disclaimer</h2>
Do not rely in AI news summary to spread informations, always read the full news before. <br>
Feedx RSS feed used to demonstrate the app logic (it only displays 10 recent news from Associated Press). The RSS feed will be replaced by a news API in a further project.<br>
RSS feed used: https://feedx.net/rss/ap.xml
