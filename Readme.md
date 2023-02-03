# AIR

AIR is short for **Advertising Identifier Reset**.\
A utility app for **Rooted Devices** to clear / reset Advertising Identifier.

The App handles all the device formats i.e.

- Devices with GMS Installed
- Devices without GMS Installation (Huawei, Other Core Chinese phones, etc.)

## How AIR works

For devices with GMS Installed, the process is pretty straightforward which simply deletes the
file `adid_settings.xml`!

On devices without GMS, you'll have to paste the `Advertising Identifier` ***once*** so that the app
can traverse the filesystem & store the path of the file that contains the identifier. This is used
then to fetch the identifier which can be deleted anytime (if it exists).\
**Note:** There can be instances where multiple files contain the provided identifier, the path of
the first file is used in that case.\n
Yes I do plan to do something about that in the future.

### But Why?

- I was bored 😤
- I don't want that blue colored big bad to show me Ads about the things I've just thought about 😂
- I didn't wanna navigate to Settings everytime 🤷‍♂️

<p align="center">
<img src="https://user-images.githubusercontent.com/20625965/216534765-fa9189a8-79ce-4c99-b815-838316be5bcd.jpeg" width=50% height=50%>
</p>

## TODO

- Add `WorkManager` support for periodically resetting the Advertising Identifier.