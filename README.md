
===========

Tasks ahead
-----------

* Player controls
	- [ ] movement keys with option to extract key mapping to configuration file
	- [v] mouse screen scroll and zoom
	- [ ] con trol modes - battler and builder
	- [ ] connect spider to mouse crosshair
	
* Level layout
	- [v] add asteroid parameters
	- [v] fix body to sprite coordinates
	- [ ] load units
	
* Units
	- [ ] spider
	- [ ] ant
	- [ ] 
	
* Bugs
	- [v] returns to menu on game resize
	
Project layout
--------------
* eir - game core resources and code
	** assets - all resources folder
		*** levels - level description file and level-specific resources
		*** models - game element resourses - images, mesh files, etc
		*** skins - user interface elements
	** libs 
		*** dev - development-time libraries, sources, profiling, etc
		*** main - runtime libraries
	** src - sources, duh

* eir-desktop - desktop main 

* eir-android - android main 

* eir-html - html5 main 

* eir-ios - ios main 

* eir-workbench - tools and place to store work in progress
	** images - raw images that need processing
	** lib - tools and their dependencies
	** objects - ready to use resources (most of them are already in eir/assets)
	** src - more tools
	** uiskin - huh, unify, dunno
	
Tools	
-----
* Body editor "aurelienribon.bodyeditor.ui.Main" in eir-workbench
* To convert font to libgdx format - execute "eir-workbench/lib/hiero.jar"
* Use TexturePacker to pack images into TextureAtlas format