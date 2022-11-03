# Background
The repository is the republication package of paper---ICSE_2023_Task Context: A Tool for Predicting Code Context Models for Software Development Tasks. The repository has packaged the project as a deployable plug-in (**Task Context**), and researches can directly integrate the Task Context into the local Eclipse environment.

# Install
1. **Obtain the Task Context**: Download this repository and unzip it, and then copy the Task Context (**edu.zju.icsoft.taskcontext_1.0.0.202211031347.jar**) from it.
2. **Integrate the Task Context into Eclipse**: Paste the Task Context into the **dropins** folder in the Eclipse root directory.
3. Restart Eclipse.

# Configure
Before performing development tasks, researchers should open **Task Context** view and **Suggestions** view as follow:  

Click **Window** -> **Show View** -> **Other...** -> **InterestView**. Open **Task Context** view and **Suggestions** view.   

Researchers could adjust the two views to a comfortable position and size.

# Usage
When performing programming tasks, developers will find task-related code elements by navigating and searching. Specifically, developers may access code elements of interest by clicking on it in the code structure view or in the code search results.  

Task Context automatically tracks the code element click behavior in both the code structure view (e.g., **Package Explorer**, **Project Explorer**, **Outline**, **Call Hierarchy**, **Type Hierarchy**, **Task Context**, and **Suggestions**) and the search results view (i.e., the result view of **Java Search**).

<img src="https://user-images.githubusercontent.com/94530603/199675774-a84d0af9-fb24-4b5f-b571-a6e487497fb0.png" width="300px">

