# Background
The repository is the replication package of the paper---ICSE_2023_Task Context: A Tool for Predicting Code Context Models for Software Development Tasks. The repository has packaged the project as a deployable plug-in (**Task Context**), and researchers can directly integrate *Task Context* into the local Eclipse environment.

# Requirements    
We use the following requirements for development and testing:   
- [JDK: 17.0.2](https://www.oracle.com/java/technologies/downloads/#java17)      
- [IDE: Eclipse IDE for Enterprise Java and Web Developers](https://www.eclipse.org/downloads/packages/release/2022-09/r/eclipse-ide-enterprise-java-and-web-developers)  


# Install   
1. **Download Task Context**: [edu.zju.icsoft.taskcontext_1.0.0.202211161857.jar](https://github.com/icsoft-zju/Task_Context/blob/master/edu.zju.icsoft.taskcontext_1.0.0.202211161857.jar).
2. **Integrate Task Context into Eclipse**: Paste *Task Context* into the *dropins* folder in the Eclipse root directory.
3. Restart Eclipse.



<div align=center>
<img width="521" alt="image" src="https://user-images.githubusercontent.com/94530603/202163828-64dd0f07-7885-4450-a012-609ab7967cf0.png">
</div>


# Configure  
1. **Open Views**  
Before performing development tasks, researchers should open the *Task Context* view and the *Suggestions* view as follow:  
Click *Window* -> *Show View* -> *Other...* -> *InterestView*. Open the *Task Context* view and the *Suggestions* view.   
Researchers could adjust the two views to a comfortable position and size.  

2. **Parameters**   
**Time Window**: The *Time Window* parameter is used to filter the most recently accessed code elements in the *Task Context* view and generate recommended code elements in the *Suggestions* view based on these filtered code elements.  
**Prediction Steps**: The *Prediction Steps* parameter is used to specify the range of recommended code elements, the more the number of steps, the larger the recommended range.


<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/202165690-f2e75e73-536b-4b25-90aa-0e9a05dd0248.png" width="400px">
</div>

# Usage
When performing programming tasks, the developer will find task-related code elements by navigating and searching. Specifically, the developer may access code elements of interest by clicking on them in code structure views or in the code search results view.  

*Task Context* automatically tracks the code element click behavior in both the code structure view (e.g., *Package Explorer*, *Project Explorer*, *Outline*, *Call Hierarchy*, *Type Hierarchy*, *Task Context*, and *Suggestions*) and the search results view (i.e., the result view of *Java Search*).

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




For instance, *Task Context* will automatically capture the code element *InterestCodeView*, and recommend elements in the *Suggestions* view that may be of interest to the developer.
When clicking on a code element in the *Suggestions* view, the editor jumps to the source code of that code element.

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199679819-aa3c563e-0276-463a-ba48-7ffd7bb6160e.png" width="700px">
</div>

Note that when the recommendation range is large and many code elements are involved, *Task Context* may take too long to recommend. At this point, *Task Context* will pop up a timeout reminder and the developer can lower the parameter *Prediction Steps* to narrow the recommendation.

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/202164310-ef2754f4-38c5-431c-81f8-1d8fe42c56de.png" width="250px">
</div>

# Source Code Introduction

## 1. edu.zju.icsoft.taskcontext.view
In this package, we create the *Task Context* view and the *Suggestions* view.  
### 1.1 Task Context view   
- The file *InterestCodeView.java* implements the creation, integration, and various functions of the view. Including capturing code elements accessed by developers, building the initial context model, filtering the recent task context model by parameter *Time Window*, expanding the recent task context model by parameter *Prediction Steps*, assigning stereotype roles, performing pattern matching, and generating recommended code elements.  
- The file *InterestCodeWindow* is responsible for the view's elements (e.g., tree) and its layout.

### 1.2 Suggestions view  
- The file *PredictCodeView.java* implements the creation, integration, and various functions of the view (e.g., sorting the list of recommended code elements, and positioning to the editor when clicking on a recommended code element).  
- The file *PredictCodeWindow* is responsible for the view's elements (e.g., table) and its layout.  

## 2. edu.zju.icsoft.taskcontext.util

### 2.1 analysis   
This package is used to parse the java project and get the structural relationship of the code elements within the project.

### 2.2 graph   
This package is used to store and packaging of code elements, structural relationships between code elements, patterns, and code context models.  


### 2.3 jstereocode
This package is used to assign stereotype roles for code elements.

### 2.4 vf3     
This package is used to perform pattern matching. 

## 3. pattern_mylyn  
This package is used to store topological patterns.

# License  
This code is open-source software licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0.html). Feel free to use it accordingly.


# Contributing  
## Author  
Intelligent Computing and Software Center of Zhejiang University (icsoft-zju)   
## Contributor
- Yuhang Lin (GitHub: Alin-kk, email: lin_yh@zju.edu.cn)
- Yifeng Wang (GitHub: Evan-AFeng, email: yifeng.wang@zju.edu.cn)
