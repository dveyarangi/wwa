Yarr!
=====

LibGDX
------
https://code.google.com/p/libgdx/wiki/Introduction

	
Project layout
--------------
* eir - game core resources and code
	- assets - all resources folder
		+ levels - level description file and level-specific resources
		+ models - game element resourses - images, mesh files, etc
		+ skins - user interface elements
	- libs 
		+ dev - development-time libraries, sources, profiling, etc
		+ main - runtime libraries
	- src - sources, duh

* eir-desktop - desktop main 

* eir-android - android main 

* eir-html - html5 main 

* eir-ios - ios main 

* eir-workbench - tools and place to store work in progress
	- images - raw images that need processing
	- lib - tools and their dependencies
	- objects - ready to use resources (most of them are already in eir/assets)
	- src - more tools
	- uiskin - huh, unify, dunno
	
	
Debug
-----------

* Use JVM params to check of GC calls: 
	- -XX:+PrintGC -XX:+PrintGCDetails 
* Hotkeys:
	- K - hide/show navigation mesh
	- J - hide/show coordinates grid (binary scale)
	
Tools	
-----
* Body editor "aurelienribon.bodyeditor.ui.Main" in eir-workbench
* To convert font to libgdx format - execute "eir-workbench/lib/hiero.jar"
* Use TexturePacker to pack images into TextureAtlas format - execute "eir-workbench/lib/gdx-texturepacker.jar"