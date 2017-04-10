# Manual Testing Script
For : `[T15-B1][TaskCrusher]`

---
## Initialization - loading sample data
- Before you load the sample data, please make sure that the sample data file is in the same directory as `taskcrusher.jar`.
- Now let's run `taskcrusher.jar`. It is programmed to load default sample data at the start, so you should already see data inside. However, for this time let us load `SampleData.xml`.

To load the sample data into TaskCrusher, enter the following command:

`load SampleData.xml`

The command will load SampleData.xml into TaskCrusher, showing 33 Tasks and 17 Events. You will also notice that the bottom right corner of the window says `SampleData.xml`. The footer indicates the name of the current file used as storage.

## Guiding through the sample data
- Let's guide through the lists currently showing. When the application launches or a new file is loaded, it will display both complete and incomplete items. When you scroll all the way to the bottom of the lists, you should see 5 tasks and 1 event with a green tick. The green tick indicates whether or not the item has been marked as complete or incomplete.
- The two lists are sorted in terms of earliest deadline/timeslot in order for the user to see what are the tasks and events that need the most attention now. When the deadlines or timeslots are the same for two tasks/events, whichever has the higher priority assigned will be shown above.
- Also, as you may have noticed, all tasks and events marked as complete are moved to the end of the lists.
- Lastly, the events whose time slot has passed or tasks which are overdue will display overdue status, where they have the red overdue icon and timeslots/deadlines coloured in red.

Now let us use the commands to interact with TaskCrusher.

## The commands

### Help command

Command:

`help`

Expected result: a help window showing TaskCrusher's UserGuide appears.

### List commands

`list`

Expected result: only the tasks and events which are yet to be marked as complete are shown. The number of tasks displayed should now be 28, and the number of events displayed should now be 16.

`list complete`:
Expected result: only the tasks and events which are marked as complete are shown. The number of tasks displayed should be 5, and the number of events displayed should be 1.

`list all`
Expected result: all the tasks and events, complete or incomplete, are displayed. The number of tasks displayed should be back to 33, and events 17.

`list d/tomorrow`

Expected result: only the tasks whose deadline is up to tomorrow, and events whose timeslot(s) fall within the range up to to tomorrow are displayed.

`list d/today to tomorrow`
Expected result: only the tasks whose deadline falls within today to tomorrow, and events whose timeslot(s) overlaps with the range today to tomorrow are displayed.

For `list d/DATE` type of commands, you may also try with different values for DATE as stated in our UserGuide.

### Add commands

Command:

`add t learn Japanese p/2 //this task has no deadline`

Expected result: a task named "learn Japanese" without a deadline, with the description "this task has no deadline" and with priority = 2, is added to the task list. It would be inserted into the appropriate position in the list according to the sorting principle explained earlier, and therefore would show at the very bottom of the list as it has no deadline.

Command:

`add t submit another application d/tomorrow 10am`

Expected result: a task with the name "submit another application" whose deadline is tomorrow 10am is added to the task list.

Command:
`add e do the demo for software d/today 8pm to 10pm`

Expected result: an event with the name "do the demo for software" whose timeslot is from 8pm to 10pm today is added to the event list.

Command:
`add e event clash d/2017-10-12 12am to 2017-10-15`
This would yield error as its timeslot clashes with the event island trip. You can force this addition by typing in

`add e event clash d/2017-10-12 12am to 2017-10-15 --force`

### Confirm command

Command:
`confirm e 16 1`
Expected result: confirms the multiple-booked timeslot of event 16 meet friend from Australia to the first timeslot.

### Mark command

Command:

`mark t 1 complete`

Expected result: The first task on the list with index 1 (computer lab) is marked as complete and sent to the complete section of the list at the back.

Command:
`mark e 19 incomplete`

Expected result: event 19 tree planting would now be marked as incomplete an put back to the top of the list, since this is already overdue.

### Delete command

Command:

`delete t 1`

Expected result: The first task on the task list (marriage invite) with index 1 would be deleted from the list.

Command:

`delete e 2`

Expected result: The second event on the list (CCA meeting) with index 2 would be deleted from the list.

### Edit command

Command:

`edit t 1 //this must be done p/3`

Expected outcome:  Task 1 in the task list will now have the new description "this must be done" and priority = 3.

### Switch command

Command:
`switch e 3 d/today"

Expected result: converts event 3 (do the demo for software) into a task whose deadline is by today.

### Find command

Command:

`find fitness`

Expected result: All tasks and events with the keyword "fitness" are displayed. There should be 0 tasks and 2 events listed.

### Undo command

Command:
`list all`
`delete t 1`

`undo`

The deleted task is back to the list in the original position.

### Redo command

Command:

`redo`

Expected result: re-does the undoed command, `delete t 1`.

### Clear command

Command:

`clear`

Expected result: all items displayed by TaskCrusher are cleared.

* `undo` to undo the `clear` command If you wish to recover the data.

### Exit command

Command:

`exit`

Expected result: The application exits.
