# TrackMaster

Second project assignment for the **Mobile Development** module at **Griffith College**.

Open-source repository: https://github.com/matiboux/griffith-android-trackmaster (public since May 11, 2020).

Play Store page: [TrackMaster](https://play.google.com/store/apps/details?id=com.matiboux.griffith.trackmaster).

Alternatively, see the [latest release](https://github.com/matiboux/griffith-android-trackmaster/releases/latest) for downloading the APK.


## Documentation

Available online at [opensource.matiboux.com/griffith-android-trackmaster](https://opensource.matiboux.com/griffith-android-trackmaster)

Also available for download: [Documentation.pdf](Documentation.pdf)


## Milestones

### Coding Milestones (70%)

- [x] Generate the shell of an application consisting of two activities. The second activity should
      only be triggered when recording is stopped. (10%)
- [x] Add support for starting and stopping the recording of GPS points. When a new recording is
      started, a GPX file should be started (filename is current date and time, GPX file must be in
      external/shared storage in a directory called GPStracks) and GPS points should be written to it as
      each GPS point is recieved. When recording stops close the file and move to the next activity. (20%)
- [x] In the second activity, generate the statistics for average speed, total distance, time taken and
      minimum and maximum altitude. These should then be displayed. (30%)
- [ ] Add in a custom view to the second activity that will represent a graph of the speed over the
      entire journey. You may assume that for the Y axis, speed will only be between 0 and 10 km/h.
	  For conversion purposes you may assume that 1 m/s is equal to 3.6 Km/h. (60%)
- [ ] Add a custom feature of your choosing. The relevance, usefuleness and quality of implementation
      will be taken into account for grading (70%)

### Documentation Milestones (30%)

- [ ] Document why you designed the UI the way you did. Include adequate graphics such as wireframe
      diagrams and screenshots. Detail the key choices taken in application navigation, widget layout
	  and position and how they support user interaction. Don’t include instructions on how to operate
	  the App or the UI. (15%)
- [ ] Give a high level description of all methods in your Java code including the data structures used. (30%)


## Authors

- **Mathieu GUÉRIN** – [matiboux.me](https://matiboux.me/)
