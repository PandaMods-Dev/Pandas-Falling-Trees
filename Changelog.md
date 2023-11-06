## Version 0.7 Changelog
**Note:** For this version i completely recoded the mod but some parts ended up getting removed in favor of better performance and less packet sending.

* **What has been recoded**
  * Block detection algorithm.
  * Tree rendering.
  * API.
  * Tree animation.
  * Config.


* **What has been fixed**
  * Tree fall SFX will now only get played when a log of a tree is broken.
  * Tree fall SFX will now be effected by distance (Fixed by [Darien Gillespie](https://github.com/Dariensg)).


* **What has been changed**
  * The tree detection algorithm now ignore persistent leaves.
  * The tree fall SFX packet will now only be sent to players in a radius of 32 blocks of the tree.
  * "One block mining" will now be referred to as "Crouch mining"


* **What has been removed**
  * One block mining toggle keybind (Might get re added later in development)