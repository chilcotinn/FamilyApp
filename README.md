# FamilyApp

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

- Third screen - Share Shopping list. ShopList contains shopItems.
  Delete ShopList by AlertDialog. Delete ShopItem by swiping.
  Remove ShopItems by swiping. Editing ShopList don't available.
  When ShopList deleted, all include ShopItems also deleting.
  If the user is not authorized, the ShopList is unavailable.

- Fourth screen - Chat for one message. If the user is not authorized, the chat is
  unavailable. Messages are not stored in the local database.

- Fifth screen - Settings screen.