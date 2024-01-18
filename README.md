# FamilyApp

Telegram: https://t.me/chilcotin

MinSdk 23

Include:

- Room;
- Android navigation component;
- Dagger hilt;
- LivaData;
- ViewModel.


Testing and Debugging:

- LeakCanary;
- Firebase Crashlytics.

Application:

- First screen - Offline Todo list. Remove items by swiping. Cancel button returns the
  TodoItem (SnakeBar).

- Second screen - Online Todo list. Shared Todo with Firebase backend. 
  Remove items by swiping. Cancel button return the Share TodoItem (SnakeBar).
  If the user is not authorized, the TodoList is unavailable.

- Third screen - Share Shopping list. In progress...

- Fourth screen - Chat for one message. If the user is not authorized, the chat is
  unavailable. Messages are not stored in the local database.

- Fifth screen - Settings screen. In progress...