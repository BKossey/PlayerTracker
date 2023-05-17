# Player Tracker

## League of Legends stats and match history 

**Purpose of the application**:
- Record match history
- Translate that match history into stats.  Win rate,
  kill/death/assist ratios, rank growth and decay, etc.
- Each game to be broken down and recorded based on champion played with
  most stats seperate for each individual champion, with the exception of 
  the rank growth and decay being an overall record as well as an overall
  win/loss record.
- Application should allow you to easily view and analyze your stats

The application is to be used by an individual *League of Legends* player.

Such applications are important and interesting tools for players to keep track
of their season and how they are performing. I'm a pretty avid League of Legends
player myself and think it would be interesting to create and design my own 
application to do so.


## User Stories

As a user, I want to be able to:
- Have an initial player profile
- View my profile
- Update my profile
- Add a Match to my Match History
- View my Match History
- Select a champion and view my stats with them
- Save all associated profile data
- Be able to load a saved user profile


## Phase 4: Task 2

- Event log cleared.
- Added Jinx game to match history.
- Added Jinx game to match history.
- Added Ezreal game to match history.
- Added Twitch game to match history.
- Logged matches from saved profile
- Added Jinx game to match history.
- Filtered match history to Jinx.
- Disabled match history filter.

## Phase 4: Task 3
**Class Diagram**

- PlayerTrackerApp always has 1 keypad, 1 Profile, and 1 Champion
(selected champion). Additionally, it will always have 1 JsonReader 
and JsonWriter
- Profile, Match, and Champion all implement Writable
- Profile always has 5 Champions and may have 0 or arbitrary Matches
- Match always has 1 Champion
- EventLog can have 0 or arbitrary Events

**With more time:** 

My main focus would be cleaning up cohesion. I Feel like my PlayerTrackerApp
has a LOT of stuff in it and certain things could be moved out into seperate classes

- Seperate the console based program and the GUI program into seperate classes
- Move all the various panels (MatchRecorder, Match History, Filter, etc) into their own
classes like KeyPad
- Move all the actions into a class
- Go through some of the larger and more complicated methods and see if parts can be
extracted to make their functions clearer and more organized




## CODE REFERENCED AND USED :
https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
