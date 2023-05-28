# ShoesStore App
# ShoesIn

This App has been developed as part of the Udacity Android Kotlin Developer Nanodegree Course for the Exercise Project "Shoe Store app". In this project I applyed my skills in building layouts and navigation files by building a Shoe Store app. The app is building with 6 screens:

* Login
* Regiester
* Welcome
* Instruction
* Shoe list
* Shoe detail

|Login|Register|Welcome screen |
|---|---|---|
|![Login](https://github.com/MhmdIbrahim1/ShoesStore_NewVersion/assets/107378571/fb5d9965-9530-4171-be83-416dd7ffaa68)|![reg](https://github.com/MhmdIbrahim1/ShoesStore_NewVersion/assets/107378571/55d32078-abec-4288-a42d-0a05406f9400)|![Welcome](https://github.com/MhmdIbrahim1/ShoesStore_NewVersion/assets/107378571/4d8080ab-c551-495e-8998-713bb57a3e9b)
|Instruction Screen|List|Add|search|
|![instruction](https://github.com/MhmdIbrahim1/ShoesStore_NewVersion/assets/107378571/21ea30e6-227a-4055-bf4b-f9afc3a6fa39)|![list](https://github.com/MhmdIbrahim1/ShoesStore_NewVersion/assets/107378571/3bbcf2b0-3629-49a6-8bf3-e1bd48268fbc)|![add](https://github.com/MhmdIbrahim1/ShoesStore_NewVersion/assets/107378571/05ec632f-97e1-4afd-8309-b61381b224cb)
|Search by name|Menu|deleteAll|
|![search](https://github.com/MhmdIbrahim1/ShoesStore_NewVersion/assets/107378571/87d949cb-8361-47ef-81a7-e201f0124f4f)|![menu](https://github.com/MhmdIbrahim1/ShoesStore_NewVersion/assets/107378571/df5567f6-440a-4af5-853b-a129b3d41262)|![d](https://github.com/MhmdIbrahim1/ShoesStore_NewVersion/assets/107378571/e99989eb-61b4-4ba4-a1ec-f288b3b474e5)


---

## Rubric followed for the Project

### Code Quality

* Correctly use ViewModel and LiveData lifecycle classes and Room Database in an Android app -
	* The Detail screen needs to add the new item to the view model. The listing screen should be listening to that model and show the new item.

* Correctly implement Single Activity architecture
	* There should only be one activity: MainActivity. Each screen should be a fragment.

* Write error-free code
	* The project's code is error-free.

### Layouts
      
* Create layouts using the correct ViewGroups and Views in an Android app.
	* The project correctly implements LinearLayout and ConstraintLayout to match the complexity of the layout of a page. Using code comments, the project justifies the use of ConstraintLayout or LinearLayout

* Apply Databinding in Layouts to show the correct data to users in multiple layouts.
	1. All layouts will use the <layout> tag to support Databinding.
	2. Detail screen uses the <data> element.
	3. Databinding is set to the appropriate setting in the app build.gradle file 

* Correctly use the <data> and <variable> elements within the layout.
	* The detail layout contains an <data> element with the name of the variable and the class associated with it.
		* All EditViews correctly refer to created class variable

* Create a multi-screened Android Application using Android widgets.
	* The app contains at least 5 screens.
	* The app contains correctly laid-out labels and edit fields for each screen.
	* The app contains button positioned below the text fields

* List screen uses RecuclerView  for showing a list of items
Creates a layout for the item.
	1. A new item layout is created for each item
	2. New item layout is added to LinearLayout
	3. Layout is updated with items added on the detail screen


### Navigation

* Create a navigation file that correctly takes a user from one page to the next in an Android app
	* The app needs to traverse the following screens in the correct order:
      	* Login
        * Regiester
      	* Welcome
      	* Instructions screen
      	* Listing screen
      	* Detail screens
            The app should also be able to navigate back via the back arrow or the back button.
      	* A navigation file has been created that defines a start destination.
      	* All destinations have a fragment, label and action associated with it

* Use Databinding for click listeners on a navigation screen in an Android app.
	1. All code will use the DataBindingUtil class to inflate the layout.
	2. All click listeners are connected via the DataBindingUtil class and uses the NavController to navigate to the next screen.   

* Create a Logout menu to return to the Login screen.
	* This menu will appear only on the Shoe Listing screen and will return the user to the login screen
     
