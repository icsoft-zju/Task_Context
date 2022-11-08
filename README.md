# Background
The repository is the replication package of paper---ICSE_2023_Task Context: A Tool for Predicting Code Context Models for Software Development Tasks. The repository has packaged the project as a deployable plug-in (**Task Context**), and researches can directly integrate *Task Context* into the local Eclipse environment.


# Requirement   
We use the following requirements for development and testing:   
- JDK: 17.0.2  
```     
https://www.oracle.com/java/technologies/downloads/#java17   
```   
- IDE: Eclipse IDE for Enterprise Java and Web Developers
```   
https://www.eclipse.org/downloads/packages/release/2022-09/r/eclipse-ide-enterprise-java-and-web-developers     
```  


# Install   
1. **Obtain Task Context**: Download this repository and unzip it, and then copy *Task Context* (*edu.zju.icsoft.taskcontext_1.0.0.202211081403.jar*) from it.
2. **Integrate Task Context into Eclipse**: Paste *Task Context* into the *dropins* folder in the Eclipse root directory.
3. Restart Eclipse.


<div align=center>
<img width="521" alt="image" src="https://user-images.githubusercontent.com/94530603/200495584-fafa00c3-88bd-49ba-a262-45cc87226162.png">
</div>


# Configure  
1. **Open Views**  
Before performing development tasks, researchers should open *Task Context* view and *Suggestions* view as follow:  
Click *Window* -> *Show View* -> *Other...* -> *InterestView*. Open *Task Context* view and *Suggestions* view.   
Researchers could adjust the two views to a comfortable position and size.  

2. **Parameters**   
**time sliding**: The *time sliding* parameter is used to filter the most recently accessed code elements in *Task Context* view and generate recommended code elements in the *Suggestions* view based on these filtered code elements.  
**prediction step**: The *prediction step* parameter is used to specify the range of recommended code elements, the more the number of steps, the larger the recommended range.

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199682334-1116cc78-b3ec-4dcb-996a-9ab8ba4526cb.png" width="300px">
</div>

# Usage
When performing programming tasks, the developer will find task-related code elements by navigating and searching. Specifically, the developer may access code elements of interest by clicking on it in code structure views or in the code search results view.  

Task Context automatically tracks the code element click behavior in both the code structure view (e.g., *Package Explorer*, *Project Explorer*, *Outline*, *Call Hierarchy*, *Type Hierarchy*, *Task Context*, and *Suggestions*) and the search results view (i.e., the result view of *Java Search*).

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199675774-a84d0af9-fb24-4b5f-b571-a6e487497fb0.png" width="400px">
</div>

</br>

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199950470-104f3dad-3d8a-4559-999f-717ac17eeb3c.png" width="400px">
</div>
 
</br> 
  
<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199950858-07a167e1-571f-496b-9791-492c2f20cd4c.png" width="400px">
</div>




For instance, Task Context will automatically capture the code element *InterestCodeView*, and recommend elements in the *Suggestions* view that may be of interest to the developer.
When clicking on a code element in the *Suggestions* view, the editor jumps to the source code of that code element.

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199679819-aa3c563e-0276-463a-ba48-7ffd7bb6160e.png" width="700px">
</div>

Note that when the recommendation range is large and many nodes are involved, *Task Context* may take too long to recommend. At this point, *Task Context* will pop up a timeout reminder and the developer can lower the parameter *prediction step* to narrow the recommendation.

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199949355-8b034a93-90a0-4e8b-9d6e-5d06d28516c8.png" width="250px">
</div>

# Source Code Introduction

## 1. edu.zju.icsoft.taskcontext.view
In this package, we create *Task Context* view and *Suggestions* view.  
### 1.1 Task Context view   
- The file *InterestCodeView.java* implements the creation, integration and various functions of the view. Including capturing code elements accessed by developers, building the initial context model, filtering recent task context model by parameter *time sliding*, expanding recent task context model by parameter *prediction step*, assigning stereotype role, performing pattern matching, and generating recommended code elements.  
- The file *InterestCodeWindow* is responsible for the view's elements (e.g., tree) and its layout.

### 1.2 Suggestions view  
- The file *PredictCodeView.java* implements the creation, integration and various functions of the view (e.g., sorting the list of recommended code elements, positioning to the editor when clicking on a recommended code element).  
- The file *PredictCodeWindow* is responsible for the view's elements (e.g., table) and its layout.  

## 2. edu.zju.icsoft.taskcontext.util

### 2.1 analysis   
This package is used to parse the java project and get the structural relationship of the code elements within the project.

### 2.2 graph   
This package is used to storage and packaging of code elements, structural relationships between code elements, patterns, and code context models.  


### 2.3 jstereocode
This package is used to assign stereotype role for code elements.

### 2.4 vf3     
This package is used to perform pattern matching. 

## 3. pattern_mylyn  
This package is used to store topological patterns.

# License  
This code is open source software licensed under the Apache 2.0 License. Feel free to use it accordingly.
