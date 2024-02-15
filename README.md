# Tic Tac Toe

## Overview

This is an application for the classic game of Tic Tac Toe, also known as X-Zero. It is a multiplayer game where two players can play against each other. The app supports two modes of playing: single player and double player.

## Features

### Login System

The app includes a login system where a user can sign up and use those credentials as their ID. The user's game statistics, such as wins and losses, are saved and can be viewed on the dashboard. This feature is implemented using Google Firebase.

### Single Player Mode

In this mode, the user plays against the computer. The computer makes its move randomly. The user's wins and losses are recorded and displayed on the dashboard.

### Two Player Mode

In this mode, the user plays against another user. The user's wins and losses are also recorded and displayed on the dashboard. For this mode to work, two players should be online at the same time. This feature is implemented using Google Firebase.

## Accessibility

The app has been checked for accessibility using the Accessibility Scanner.

## Implementation

The app uses Android Navigation Component, with a single activity and three fragments:

- The DashboardFragment is the home screen. If a user is not logged in, it navigates to the LoginFragment.
- The floating button in the dashboard creates a dialog that asks which type of game to create and passes that information to the GameFragment (using SafeArgs).
- The GameFragment UI has a 3x3 grid of buttons. They are initialized in the starter code. Appropriate listeners and game play logic needs to be provided.
- Pressing the back button in the GameFragment opens a dialog that confirms if the user wants to forfeit the game.
- A "log out" action bar menu is shown on both the dashboard and the game fragments. Clicking it logs the user out and shows the LoginFragment.

## Development Information

- Number of Hours Devoted: 50 hours
- Difficulty Level: 10 on a scale of 10
