# Easy Grader

CS 591 Object Oriented Design and Development in Java @ Boston University

https://github.com/a502151508/ood-grading

## Group Members

[Emmanuel Amponsah](https://github.com/emampons)

[Yifei Feng](https://github.com/yifeifeng)

[Xiaotong Niu](https://github.com/sylvia0801)

[Sichi Zhang](https://github.com/a502151508)

## Instuctions

To install all dependencies:
```
Download all of the Java files in the src directory, the libraries files named sqlite-jdbc-3.23.1.jar 

and javacsv2.0(jb51.net) for CSV and SQLite database server we provided, and our database file named 

easy_grader.db. 

Include sqlite-jdbc-3.23.1.jar and javacsv2.0(jb51.net) in the Libraries section in Java Build Path 

for this project.
```

To execute the script:
```
Execute Login.Java file, enter code "591" as a secret key and click "Login" button. Any other entries 

would result in invalid password and would not log in to the system.
``` 

### Class Management

To add a class:
```
Click "Add Class" button, enter the class name and semester desired for the new class, and click 

"Save" button.
```

To delete a class:
```
Select the class that is going to be deleted, and click "Remove Class" button.
```

To add or edit existed grading criteria of a class:
```
Select the class that is going to be edited, and click "Class Settings" button.
```

To add a task category in the grading criteria of a class:
```
Randomly select a task category, click "Add Category" button, enter the name and the percentage 

desired for the new category, and click "Save" button.

Click "Save" button on the grading criteria setting page and close the grading criteria setting 

page once done with editing the class's grading criteria.
```

To add a sub-task category in the grading criteria of a class for a certain task category:
```
Select the parent task category that the sub-task category is going to be added in, click "Add 

Sub-Category" button, enter the name and the percentage desired for the new category, and click 

"Save" button.

Click "Save" button on the grading criteria setting page and close the grading criteria setting 

page once done with editing the class's grading criteria.
```

To edit an existed task category or a sub-task category in the grading criteria of a class:
```
Select the task category that is going to be edited, click "Edit" button, enter the name and the 

percentage desired for the category, and click "Save" button.

Click "Save" button on the grading criteria setting page and close the grading criteria setting 

page once done with editing the class's grading criteria.
```

To delete a task category or a sub-task category in the grading criteria of a class:
```
Select the task category that is going to be deleted, and click "Delete" button.

Click "Save" button on the grading criteria setting page and close the grading criteria setting 

page once done with editing the class's grading criteria.
```

### Grade Management

To go to the grade view page of a class:
```
Select the class that the grade view is going to be seen or edited, and click "Grade View" button.
```

To enter grade of a certain assignment:
```
Click the assignment name shown on the page, enter grades for students.
```

### Student Management

To go to the student view of a class:
```
Select the class that the student view is going to be seen or edited, and click "Student View" button.
```

To add a student:
```
Click "Add Student" button on the student view page, enter first name, last name and BUID number 

(with U) desired for the new student, and click "Add Student" button.
```

To delete a student:
```
Select the student that is going to be deleted, and click "Remove Student" button.
```

To load student information from a csv file:
```
Click "Load Student from CSV" button, select the csv file that is going to be loaded, and click 

"Open" button.
```

To view grades of a student:
```
Select the student that grades are going to be viewed, and click "Show Grades" button.
```

To search for a student:
```
Enter name or BUID number of the student that is going to be searched in the text field provided 

in the student view page.
```
