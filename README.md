# Background
The repository is the republication package of paper---ICSE_2023_Task Context: A Tool for Predicting Code Context Models for Software Development Tasks. The repository has packaged the project as a deployable plug-in (**Task Context**), and researches can directly integrate the Task Context into the local Eclipse environment.

# Install
1. **Obtain the Task Context**: Download this repository and unzip it, and then copy the Task Context (*edu.zju.icsoft.taskcontext_1.0.0.202211031347.jar*) from it.
2. **Integrate the Task Context into Eclipse**: Paste the Task Context into the *dropins* folder in the Eclipse root directory.
3. Restart Eclipse.

# Configure  
1. **Open Views**  
Before performing development tasks, researchers should open *Task Context* view and *Suggestions* view as follow:  
Click *Window* -> *Show View* -> *Other...* -> *InterestView*. Open *Task Context* view and *Suggestions* view.   
Researchers could adjust the two views to a comfortable position and size.  

2. **Parameters**   
**time sliding**: The *time sliding* parameter is used to filter the most recently accessed code elements in the *Task Context* view and generate recommended code elements in the *Suggestions* view based on these filtered code elements.  
**prediction step**: The *prediction step* parameter is used to specify the range of recommended code elements, the more the number of steps, the larger the recommended range.

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199682334-1116cc78-b3ec-4dcb-996a-9ab8ba4526cb.png" width="300px">
</div>

# Usage
When performing programming tasks, the developer will find task-related code elements by navigating and searching. Specifically, the developer may access code elements of interest by clicking on it in code structure views or in the code search results view.  

Task Context automatically tracks the code element click behavior in both the code structure view (e.g., *Package Explorer*, *Project Explorer*, *Outline*, *Call Hierarchy*, *Type Hierarchy*, *Task Context*, and *Suggestions*) and the search results view (i.e., the result view of *Java Search*).

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199675774-a84d0af9-fb24-4b5f-b571-a6e487497fb0.png" width="300px">
</div>

For instance, Task Context will automatically capture the code element *InterestCodeView*, and recommend elements in the *Suggestions* view that may be of interest to the developer.
When clicking on a code element in the *Suggestions* view, the editor jumps to the source code of that code element.

<div align=center>
<img src="https://user-images.githubusercontent.com/94530603/199679819-aa3c563e-0276-463a-ba48-7ffd7bb6160e.png" width="600px">
</div>





