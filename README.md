# Banking Application

This project is a banking application where users can deposit and withdraw money from their bank
account. It supports viewing past transactions and ensures no withdrawal occurs if the balance is
insufficient.

## Screens
[Watch the video](https://github.com/user-attachments/assets/6eb2340f-9a3b-4b07-b178-bc31dd969ec9)


## Features

- **Deposit and Withdraw**: Users can deposit and withdraw money from their accounts. Withdrawals
  are blocked if the balance is insufficient.
- **Transaction History**: Users can view their past transactions.
- **Balance Update**: The balance is automatically updated when a transaction is made (deposit or
  withdrawal).
- **Error Handling**: Informative messages are shown when errors occur, such as insufficient funds.

## Architecture

- **MVVM Architecture**: The application follows the MVVM (Model-View-ViewModel) pattern to separate
  concerns and improve testability.
- **Room Database**: Used for local storage of transaction records. A trigger is used to update the
  account balance upon each transaction.
- **Compose UI**: The UI is built using Jetpack Compose for a modern, declarative approach.
- **Hilt for Dependency Injection**: Used for injecting dependencies in a clean and modular way.

## Refactoring Strategy

1. **Modularization**: The project is organized to separate core functionality (such as data and
   domain logic) from feature-specific code within the app module. This approach enhances
   testability and maintainability.
2. **UI Tests**: Focused on implementing UI tests to ensure the integrity of the UI and prevent
   regressions during refactoring.
3. **Sealed Classes for Navigation**: Refactored the navigation setup to use sealed classes instead
   of hardcoded strings, improving type safety and maintainability.
4. **Transaction History Feature**: Introduced a feature to view past transactions. The feature
   leverages Room for persistent storage and displays the history in a clean, scrollable list.

## Setup Instructions

1. **Clone the Repository**:
    ```
    git clone git@github.com:swarnsingh/code-challenge-android-mobile.git
    cd <project_directory>
    ```

2. **Install Dependencies**:
    ```
    ./gradlew build
    ```

3. **Run the Application**: The app can be launched on an emulator or a physical device.

## Testing Strategy

- **Unit Tests**: Unit tests have been written to verify the correctness of business logic, such as
  deposit/withdrawal calculations.
- **UI Tests**: UI tests were added to ensure that the user interface behaves as expected. This
  includes tests for the deposit/withdraw buttons, transaction history display, and error handling.
- **Regression Testing**: UI tests ensure that no regressions are introduced during refactoring.

## Assumptions

- The API for fetching transactions is not yet available, so transactions are handled locally using
  Room.
- The database schema for transactions and balances was set up with triggers to update the balance
  when a transaction is performed (deposit or withdrawal).

## Improvements Deferred

- **Network Layer**: The app currently uses Room for local storage. Future improvements could
  include implementing a network layer to sync data with a backend.
- **Advanced Error Handling**: While basic error handling is implemented, more sophisticated error
  handling strategies can be considered.

## Contributions

Feel free to open issues or submit pull requests for improvements!


