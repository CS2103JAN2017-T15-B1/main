# TaskCrusher - User Guide

By : `Team T15B1`  &nbsp;&nbsp;&nbsp;&nbsp; Since: `Feb 2017`  &nbsp;&nbsp;&nbsp;&nbsp; Licence: `MIT`

1. [Introduction](#1-introduction)
2. [Quick Start](#2-quick-start)
3. [Features](#3-features)
4. [Date Formats](#4-date-formats)
5. [Command Summary](#5-command-summary)
6. [FAQ](#6-faq)

## 1. Introduction
TaskCrusher is a task and event manager that combines a GUI with command line-like input for users who want functionality without leaving the keyboard.

## 2. Quick Start

0. Ensure you have Java version `1.8.0_60` or later installed in your Computer.<br>

   > Having any Java 8 version is not enough. <br>
   > This app will not work with earlier versions of Java 8.

1. Download the latest `TaskCrusher.jar` from the [releases](../../../releases) tab.
2. Copy the file to the folder you want to use as the home folder for TaskCrusher.
3. Double-click the file to start the app. The GUI should appear in a few seconds.
   > <img src="docs/images/Ui.png" width="600">

4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. typing **`help`** and pressing <kbd>Enter</kbd> will open the help window.
5. Some example commands you can try:
   * **`help`** : displays help documentation
   * **`add t`:**  `add t Cook Omelets p/3 //Make enough for 5` adds a task with no deadline named "Cook Omelets" with priority 3 and description "Make enough for 5"
6. Refer to the [Features](#3-features) section below for details of each command.<br>

## 3. Features

**Understanding command format:**
   * Words in `UPPER_CASE` are parameters
   * Items in `[SQUARE_BRACKETS]` can be omitted. If omitted, they are given a default value.
   * Force options and prefixed parameters (e.g. `d/` or `p/`) can be entered in any order

**Table of parameters:**

| Parameter(s)          | Acceptable formats                       | Default value         |
|-----------------------|------------------------------------------|-----------------------|
| Name                  | Alphanumeric characters                  | N/A, can't be omitted |
| Location, Description | Alphanumeric characters                  | No value              |
| Tag                   | Alphanumeric characters                  | No value              |
| Priority              | 0, 1, 2, 3                               | 0                     |
| List                  | t, e                                     | N/A, can't be omitted |
| Timeslot              | See [Date Formats](#4-date-formats)      | N/A, can't be omitted |
| Deadline              | See [Date Formats](#4-date-formats)      | No value              |
| Index number          | 1 to number of last item on list         | N/A, can't be omitted |
| Slot number           | 1, 2, 2003                               | N/A, can't be omitted |
| Filepath              | Acceptable filepath as defined by system | N/A, can't be omitted |

### 3.1. View help

**View help window**:
Format: `help`

### 3.2. Add an item

Items in TaskCrusher are either tasks or events. <br>
Tasks and events are distinct: <br>
   * Tasks are jobs that must be done by a certain date or by an unspecified point in time, and thus may or may not possess a deadline.
   * Events are commitments that take up a certain time frame and thus must possess a timeslot. 
   * Users, by default, are prevented from adding events that clash with preexisting events. 
   * This can be overridden by using the --force option detailed below.

**Add event**: <br>
Format: `add e EVENT_NAME [--force] d/TIMESLOT [d/TIMESLOT_2] [p/PRIORITY] [l/LOCATION] [//DESCRIPTION] [t/TAG_1] … [t/TAG_N]`
   * If the force option is specified, checking for clashes is disabled.
   * Although at least one timeslot must be specified, up to three timeslots are supported in order to support tentative events. Timeslots can later be confirmed using the `confirm` command
   * More than one tag can be specified <br>
Example: `add e Review Session --force d/today d/tomorrow p/2 l/Auditorium`

**Add task**: <br>
Format: `add t TASK_NAME [d/DEADLINE]  [p/PRIORITY] [//DESCRIPTION] [t/TAG_1] … [t/TAG_N]`
   * If a deadline is specified, only one should be specified
   * More than one tag can be specified <br>
Example: `add t Cook Omelets p/3 //Make enough for 5`

### 3.3 List items

Lists are automatically sorted in chronological order. <br>
Tasks with no deadlines can be found at end of the task list.

**List all active/incomplete tasks and events**: <br>
Format: `list`<br>

**List all complete tasks and events**: <br>
Format: `list complete`<br>

**List all tasks and events, both complete and incomplete**: <br>
Format: `list all`<br>

**List all tasks and events on or before a given date**: <br>
Format: `list d/DEADLINE`
   * Tasks with no deadlines will not appear <br>
Example: `list d/next Tuesday`

**List all tasks and events that overlap with a given time frame**: <br>
Format: `list d/TIMESLOT`
   * Tasks with no deadlines will not appear <br>
Example: `list d/May 5, 2013 to May 8, 2017`

### 3.4 Edit items

One or more fields of a given item can be edited
The item is specified by specifying which list to target (task or event) and its index/numbering within that list.

**Edit one or more of an item’s fields**: <br>
Format: `edit LIST INDEX_NUMBER [prefix_1/NEW_VALUE_1] … [prefix_N/NEW_VALUE_N]`<br>
Example: `edit t 1 d/tomorrow at noon p/3 //Include footnotes`

### 3.5 Mark items as complete/incomplete:

The item is specified by specifying which list to target (task or event) and its index/numbering within that list.

**Mark item as complete**:
Format: `mark LIST INDEX_NUMBER complete`<br>
Example: `mark t 5 complete`

**Mark item as incomplete**:
Format: `mark LIST INDEX_NUMBER incomplete`<br>
Example: `mark e 15 incomplete`

### 3.6. Switch item type:

Tasks can be switched to become events and vice versa. <br>
The item is specified by specifying which list to target (task or event) and its index/numbering within that list.

**Switch task to event**: <br>
Format: `switch t INDEX_NUMBER d/TIMESLOT`
   * Note that the timeslot cannot be omitted <br>
Example: `switch t 2 d/today to tomorrow`

**Switch event to task**: <br>
Format: `switch e INDEX_NUMBER [d/DEADLINE]`
   * Events switched to tasks will lose their locations <br>
Example: `switch e 1`

### 3.7. Confirm timeslots:

Events with more than one timeslot are tentative events. <br>
They can be confirmed by selecting which timeslot to finalize. Timeslots are indexed from 1-3, from left to right.

**Confirm event timeslot**:<br>
Format: `confirm e INDEX_NUMBER SLOT_NUMBER` <br>
Example: `confirm e 14 1`

### 3.8. Find items by keywords

**Find items with fields that match keywords**:<br>
Format: `find KEYWORD_1 … [KEYWORD_N]`
   * Name, description and location are searched through
   * More than one keyword can be specified
   * Items from both task and event lists displayed <br>
Example: `find buy 7Eleven`

### 3.9. Delete an item

The item is specified by specifying which list to target (task or event) and its index/numbering within that list.

**Delete an item**: <br>
Format: `delete LIST INDEX_NUMBER` <br>
Example: `delete e 11`

### 3.10. Clear lists

Clears both task and event lists, regardless of whether an item is complete or incomplete

**Clear lists**: <br>
Format: `clear` <br>

### 3.11 Undo last command

All commands that modify the data stored can be undone within the same session. <br>
Specifically, all commands except list, find, help, exit can be undone. <br>
As many undo commands as there were commands that modified the data may be executed. <br>
Undone commands may be redone.

**Undo last data-modifying command**: <br>
Format: `undo` <br>

### 3.12 Redo last undone command

All undone commands may be redone as long as no other data-modifying commands have been executed since the undo. <br>
After a data-modifying command is executed, any previously undone commands can no longer be redone.

**Redo last undone command**: <br>
Format: `redo` <br>

### 3.13 Load storage file

TaskCrusher can switch between preexisting storage files, and create new storage files.

**Load existing file**: <br>
Format: `load FILEPATH`<br>
Example: `load ./preexistingfile.xml`

**Load new file**: <br>
Format: `load new FILEPATH`<br>
   * TaskCrusher creates this file. If file already exists, it will not create a new one. <br>
Example: `load new ./newfile.xml`

### 3.14 Exit the program

**Exit program**: <br>
Format: `exit`<br>

## 4. Date Formats

   > Warning: not following these date formats may produce erroneous deadlines and timeslots!

Dates are broken down into:
   * DATE, which defines month, day and year
   * TIME, which defines time during the given date <br>
See end of section for valid ways of stating DATE and TIME

### 4.1 Deadline formats
DATE TIME
   * The fully specified format, date and time accepted as given
DATE
   * Date is as given, time is inferred to be 11:59pm
TIME
   * Date is inferred to be today, time is as given

### 4.2 Timeslot formats
DATE TIME to DATE TIME
   * The fully specified format, works as intended
DATE TIME to TIME
   * End date is inferred to be the same as the start date, times are as given
DATE to DATE
   * Start time is inferred to be 12:00am of the start date
   * End time is inferred to be 11:59pm of the end date
   * Dates are as given
TIME to TIME
   * Start and end dates inferred to be today
   * Times are as given

### 4.3 Date and Time Formats
Legend: <br>
MM - numerical representation of month of the year, e.g. 03 or 3 <br>
Month - month of the year, spelled out, e.g. March or Mar <br>
“-” is a separator, “/” can also be used

**Acceptable date formats**:
   * `YYYY-MM-DD`
   * `MM-DD-YYYY`
   * `MM-DD-YY`
   * `MM-DD`
   * `DD-Month-YY`
   * `Month DD, YYYY`
   * `Month DD`
   * `Today, tomorrow, next week, etc.`
   * `Monday, Tuesday, Wednesday, etc.`

**Unacceptable date formats**:
   * `YYYY Month DD`
   * `YYYY` (will be interpreted as time)
   * `J32hdk3ew` (gibberish)

**Time formats**:
   * `HH:MM` (specify `am` or `pm` if 12 hr time, otherwise 24h)
   * `HHMM` (colon can be omitted)

## 5. Command Summary

   * **Help**
      * Display help
         * Format: `help`
   * **Add**
      * Add event
         * Format: `add e EVENT_NAME [--force] d/TIMESLOT [d/TIMESLOT_2] [p/PRIORITY] [l/LOCATION] [//DESCRIPTION] [t/TAG_1] … [t/TAG_N]`
         * Example: `add e Review Session --force d/today d/tomorrow p/2 l/Auditorium`
      * Add task
         * Format: `add t TASK_NAME [d/DEADLINE]  [p/PRIORITY] [//DESCRIPTION] [t/TAG_1] … [t/TAG_N]`
         * Example: `add t Cook Omelets p/3 //Make enough for 5`
   * **List**
      * List only active/incomplete
         * Format: `list`
      * List only complete
         * Format: `list complete`
      * List all
         * Format: `list all`
      * List items until date
         * Format: `list d/DEADLINE`
         * Example: `list d/next Tuesday`
      * List items overlapping time frame
         * Format: `list d/TIMESLOT`
         * Example: `list d/May 5, 2013 to May 8, 2017`
   * **Edit**
      * Edit one or more of item’s fields
         * Format: `edit LIST INDEX_NUMBER [prefix_1/NEW_VALUE_1] … [prefix_N/NEW_VALUE_N]`
         * Example: `edit t 1 d/tomorrow at noon p/3 //Include footnotes`
   * **Mark**
      * Mark item complete
         * Format: `mark LIST INDEX_NUMBER complete
         * Example: `mark t 5 complete`
      * Mark item incomplete
         * Format: `mark LIST INDEX_NUMBER incomplete`
         * Example: `mark e 15 incomplete`
   * **Switch**
      * Switch task to event
         * Format: `switch t INDEX_NUMBER d/TIMESLOT`
         * Example: `switch t 2 d/today to tomorrow`
      * Switch event to task
         * Format: `switch e INDEX_NUMBER [d/DEADLINE]`
         * Example: `switch e 1`
   * **Confirm**
      * Confirm event timeslot
         * Format: `confirm e INDEX_NUMBER SLOT_NUMBER`
         * Example: `confirm e 14 1`
   * **Find**
      * Find items by keyword(s)
         * Format: `find KEYWORD_1 … [KEYWORD_N]`
         * Example: `find buy 7Eleven`
   * **Delete**
      * Delete item
         * Format: `delete LIST INDEX_NUMBER`
         * Example: `delete e 11`
   * **Clear**
      * Clear lists
         * Format: `clear`
   * **Undo**
      * Undo last data-modifying command
         * Format: `undo`
   * **Redo**
      * Redo last undone command
         * Format: `redo`
   * **Load**
      * Load existing file
         * Format: `load FILEPATH`
         * Example: `load ./preexistingfile.xml`
      * Load new file
         * Format: `load new FILEPATH`<br>
         * Example: `load new ./newfile.xml`
   * **Exit**
      * Exit program
         * Format: `exit`

## 6. FAQ

**Q**: How do I create my own storage file to import into TaskCrusher?<br>
**A**: Create an .xml file with your data, following the format in the default userInbox.xml file created by TaskCrusher. userInbox.xml is found in the data/ folder

**Q**: How do I import my own storage file into TaskCrusher?<br>
**A**: Use the load command as described above

**Q**: How do I transfer my data to another computer?<br>
**A**: Install TaskCrusher on the other computer, copy and load the data file of your previous TaskCrusher installation.
