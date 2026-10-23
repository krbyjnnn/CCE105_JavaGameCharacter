# Game Character Manager

A Java Swing application for creating game characters and making them fight.

## Features

- Four character types: Warrior, Mage, Archer, Assassin, each with its own fixed damage values
- One main screen with the character list (name and role), live search, and Add / Edit / Remove / Fight buttons
- Add and Edit open a popup form; Fight opens a popup battle between the two selected characters
- Characters are saved to `characters.txt` and loaded on startup
- Turn-based fight with three skills per character: two attacks and a special ability
- Cooldowns: a skill used recently is locked for the next two uses
- Special effects: **Stun** (the attacker takes another turn) and **Mute** (the opponent can't use their special next turn)
- Dark theme, health bars, and green/red status messages
- Input validation with clear error messages

## Damage

| Type | Attack 1 | Attack 2 | Special |
|------|----------|----------|---------|
| Warrior | 80 | 120 | 200 |
| Mage | 60 | 100 | 250 |
| Archer | 70 | 110 | 220 |
| Assassin | 90 | 140 | 180 |

Every character starts a fight with 1000 health.

## Concepts Applied

- **Abstraction:** `GameCharacter` is an abstract class with abstract methods `getType`, `attackOne`, `attackTwo` and `specialAbility`. `Warrior`, `Mage`, `Archer` and `Assassin` implement them.
- **ADT:** `CharacterManager` stores characters in an `ArrayList` (list). `FightDialog` tracks each fighter's skill cooldown with a `Queue`.
- **GUI:** built with Java Swing (`JFrame`, `JDialog`, `JTable`, `JComboBox`, `JProgressBar`, `JButton`) and styled in `Theme`.
- **User input:** characters are added, searched, edited and removed through the list and popup form, and fights are played with the skill buttons. Names and skill names must be 1 to 20 characters, and character names must be unique.

## How to Run

Requires JDK 11 or newer.

```
javac *.java
java CharacterApp
```

## Files

- `GameCharacter.java`: abstract base class and validation
- `Warrior.java`, `Mage.java`, `Archer.java`, `Assassin.java`: character types
- `StatusEffect.java`: Stun, Mute, None
- `CharacterManager.java`: list handling, saving and loading
- `CharacterApp.java`: main screen (list, search, buttons) and main method
- `CharacterDialog.java`: add / edit popup
- `FightDialog.java`: fight popup
- `Theme.java`: colors