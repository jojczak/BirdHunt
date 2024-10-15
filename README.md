<p align="center">
  <img alt="eInk Photo Converter logo" src="android/res/mipmap-xxxhdpi/ic_launcher.webp" />
</p>

# Bird Hunt
Bird Hunt is an attempt to create a simple game using the [S Pen Remote SDK](https://developer.samsung.com/galaxy-spen-remote/s-pen-remote-sdk.html). The game takes advantage of the gyroscope in the S Pen, available on certain Galaxy devices, to move the crosshair, simulating the effect of aiming a gun at the phone’s screen. </br>
The game was written in Kotlin using the [libGDX framework](https://github.com/libgdx/libgdx)

## Gameplay
The gameplay is inspired by the classic [Duck Hunt](https://en.wikipedia.org/wiki/Duck_Hunt) from Intelligent Systems for the NES platform.

During each round, a bird flies across the screen, and the player's goal is to aim and shoot it using the S Pen’s motion controls. The player has three bullets in the shotgun, which are replenished after successfully shooting a bird. If the player misses all three shots, the game ends. The player also needs to act quickly, as the bird will fly away after 7 seconds, resulting in a loss.

After every 6 birds are shot, a new round begins. With each round, the bird flies faster, increasing the difficulty. Starting in round 6, two birds will appear on the screen, and from round 11, three birds will need to be shot.

You can see a demonstration of the game in this video:

## Supported devices
<ins>**This game works ONLY on Samsung phones that have an S Pen with Air Actions functionality.**</ins> Even if your device has a stylus but is not a Samsung device, the game will not work, as it uses Samsung's SDK specifically for S Pen.
- Samsung Galaxy Note Series: **Galaxy Note10** and newer
- Samsung Galaxy S Ultra Series: **Galaxy S21 Ultra** and newer
- Samsung Galaxy Tab Series: **Galaxy Tab S6** and newer *(Air Actions depend on the specific S Pen model)*
- Samsung Galaxy Z Fold Series: **Galaxy Z Fold3** and newer *(Air Actions depend on the specific S Pen model)*

## Download
You can download Bird Hunt from the [Releases](https://github.com/j-janczak/BirdHunt/releases) section here on GitHub or from Google Play:

[<img src="gimp/google_play/google_play_badge.png" height="50">](https://play.google.com/store/games?hl=pl)

## Building the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/j-janczak/BirdHunt.git
   ```
2. Open Android Studio and click `File > New > Import Project` and select the BirdHunt repository folder.
3. Select the android module from the run configurations dropdown and click the Run button

Repository also includes an LWJGL module, which allows you to run the game on a computer as a regular Java application. You can aim using the mouse in this mode.

## Screenshots
<img src="gimp/google_play/screenshots/Screenshot_20241015_181911_Bird_Hunt.jpg" width="256"> <img src="gimp/google_play/screenshots/Screenshot_20241015_181734_Bird_Hunt.jpg" width="256"> <img src="gimp/google_play/screenshots/Screenshot_20241015_181822_Bird_Hunt.jpg" width="256"> <img src="gimp/google_play/screenshots/Screenshot_20241015_181955_Bird_Hunt.jpg" width="512">

## Legal
The font used in game is [Pixelify Sans](https://fonts.google.com/specimen/Pixelify+Sans) by Stefie Justprince</br>
All sound effects are from [Pixabay](https://pixabay.com/)</br>
libGDX cross-platform Java game development framework: [https://libgdx.com/](https://libgdx.com/)</br>
S Pen Remote SDK: [https://developer.samsung.com/galaxy-spen-remote/s-pen-remote-sdk.html](https://developer.samsung.com/galaxy-spen-remote/s-pen-remote-sdk.html)

"Samsung" and "S Pen" are registered trademarks of Samsung Electronics Co., Ltd.</br>
This game is not affiliated with, endorsed, or sponsored by Samsung Electronics Co., Ltd.
