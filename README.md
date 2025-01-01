![v-bank](https://github.com/user-attachments/assets/025b2a41-1e36-497f-baa9-f7267d79f205)

![Discord](https://img.shields.io/discord/1322873747535040512)
![Build Status](https://img.shields.io/github/actions/workflow/status/Varilx-Development/VDiscordIntegration/build.yml?branch=main)
![Release](https://img.shields.io/github/v/release/Varilx-Development/VDiscordIntegration)

# VEconomy Configuration

A economy plugin that works with **Vault** and fully Customizable!

---

# Preview
![bank-show](https://github.com/user-attachments/assets/e972256f-8a85-4f31-a897-3c2deb15b690)


## Configuration Overview

### 1. **Database**
Define the database type and connection details. The plugin supports:
- **MongoDB**: Specify a connection string and database name.
- **SQL**: Provide a JDBC connection string. (No username/password is required for SQLite.)

### 2. **Custom Messages**
Customize messages for server startup, player join/quit, and Discord chat using the MiniMessage format.
We currently support: `de` and `en`

### 3. **Commands**
`/money` - Shows your balance

`/moneyadmin add (player) (amount)` - Giving the Player Money Balance.

`/moneyadmin remove (player) (amount)` - Removing the Player Money Balance.

`/moneyadmin set (player) (amount)` - Sets the Balance on the Amount.

`/moneyadmin reset (player)` - Resets fully the Money Balance from the Player.

`/moneyadmin transactions (player)` - Shows the player's transactions


### 4. **Permissions** 
`vbank.moneyadmin` | Fully configurable in the Config.yml.
- Permission to use the `/moneyadmin` Command.

---

## Setup Instructions 

1. Download and install the plugin on your Minecraft server.
2. Configure the `config.yml` file with your preferred settings:
    - Set the database type and connection details.
    - Define custom messages using MiniMessage.
3. Restart the server to apply the changes.

---

## Example config

```yaml
language: en

start_balance: 1000
```

---

## Example Message Configuration

```yaml
# Using Minimessage https://docs.advntr.dev/minimessage/format.html

# General Messages
currency_name: "coins"
currency_name_plural: "coins"
currency_name_singular: "coin"
prefix: "<bold><dark_gray>[<gradient:#FAF162:#D91A0D>VEconomy</gradient><dark_gray>]<reset><gray> "
user_not_found: "<prefix><red>This user doesnt exist"
no_valid_number: "<prefix><red>Enter a valid number"
no_permission: "<prefix><red>You don't have Permission to do this!"
date_format: "MM/dd/yyyy - hh:mm a"
add: "<green>Add"
remove: "<red>Remove"
pay: "<red>Pay"
pay_receive: "<red>Pay receive"
admin_add: "<green>Add <dark_gray>(<red>Admin<dark_gray>)"
admin_remove: "<green>Remove <dark_gray>(<red>Admin<dark_gray>)"
admin_set: "<green>Set <dark_gray>(<red>Admin<dark_gray>)"
admin_reset: "<green>Reset <dark_gray>(<red>Admin<dark_gray>)"

command_money_own_balance: "<prefix>You have <yellow><balance> <currency_name>"
command_money_pay_self: "<prefix><red>You cannot send money to yourself"
command_money_pay_not_enough_money: "<prefix><red>You don't have enough money"
command_money_pay_success: "<prefix>You have sent <green><receiver> <yellow><amount> <currency_name><gray> to the player"
command_money_pay_success_receiver: "<prefix>The player <green><sender><gray> has sent you <yellow><amount> <currency_name><gray>"

command_moneyadmin_reset_success: "<prefix>The money of <yellow><username> <gray>has been reset"
command_moneyadmin_add_success: "<prefix>The player was given<yellow> <balance> <currency_name>"
command_moneyadmin_set_success: "<prefix>The player was set to<yellow> <balance> <currency_name>"
command_moneyadmin_remove_not_enough_money: "<prefix><red>You cannot bet the money negatively"
command_moneyadmin_remove_success: "<prefix><red>You have removed <yellow><balance> <currency_name> <gray>from the player"

top_item_name: "<green><count><gray># <yellow><username> <dark_gray>(<yellow><balance> <currency_name><dark_gray>)"

# Skulls
transaction_item_add_skull: "https://textures.minecraft.net/texture/5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1"
transaction_item_remove_skull: "https://textures.minecraft.net/texture/4e4b8b8d2362c864e062301487d94d3272a6b570afbf80c2c5b148c954579d46"
back_skull: "https://textures.minecraft.net/texture/bd8a99db2c37ec71d7199cd52639981a7513ce9cca9626a3936f965b131193"
next_skull: "https://textures.minecraft.net/texture/3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716"
admin_reset_skull: "https://textures.minecraft.net/texture/e9cdb9af38cf41daa53bc8cda7665c509632d14e678f0f19f263f46e541d8a30"

# Gui
Gui:
  Top:
    Title: "<prefix> Top 10 Money"
    Items:
      Placeholders:
        1: "GRAY_STAINED_GLASS_PANE"
        2: "WHITE_STAINED_GLASS_PANE"
  Transactions:
    Title: "<prefix> Transactions"
    Items:
      Placeholders:
        1:
          Material: "GRAY_STAINED_GLASS_PANE"
      TransactionItem:
        Name: "<red>Transaction <dark_gray>| <yellow><date>"
        Lore:
          - ""
          - "<dark_gray>| <gray>Amount <dark_gray>» <yellow><amount>"
          - "<dark_gray>| <gray>Balance <dark_gray>» <yellow><balance>"
          - "<dark_gray>| <gray>Type <dark_gray>» <yellow><type>"
      BackItem:
        Name: "<red>Back"
        Lore:
          - ""
          - "<dark_gray>| <gray>Click here to go back one page"
      NextItem:
        Name: "<green>Next"
        Lore:
          - ""
          - "<dark_gray>| <gray>Klicke hier um zur nächsten Seite zu gelangen"

# Commands
Commands:
  Money:
    Name: "money"
    Arguments:
      Top: "top"
      Pay: "pay"
  MoneyAdmin:
    Name: "moneyadmin"
    Permission: "veconomy.moneyadmin"
    Arguments:
      Add: "add"
      Remove: "remove"
      Set: "set"
      Reset: "reset"
      Transactions: "transactions"
    Usage:
      - "<prefix>MoneyAdmin help<dark_gray>:"
      - "<prefix>Use <yellow>/moneyadmin add <dark_gray><<yellow>Player<dark_gray>> <<yellow>Amount<dark_gray>>"
      - "<prefix>Use <yellow>/moneyadmin remove <dark_gray><<yellow>Player<dark_gray>> <<yellow>Amount<dark_gray>>"
      - "<prefix>Use <yellow>/moneyadmin set <dark_gray><<yellow>Player<dark_gray>> <<yellow>Amount<dark_gray>>"
      - "<prefix>Use <yellow>/moneyadmin reset <dark_gray><<yellow>Player<dark_gray>>"
      - "<prefix>Use <yellow>/moneyadmin transactions <dark_gray><<yellow>Player<dark_gray>>"
```

---

## Example Database Configuration

```yaml
type: Sqlite # Avaiable types: mongo, mysql, sqlite


# MONGO
Mongo:
  connection-string: "mongodb://<username>:<password>@<host>:<port>/"
  database: "db"


# SQL
SQL:
  connection-string: "jdbc:sqlite:plugins/VEconomy/database.db"
  username: "username" # Not required for sqlite
  password: "password" # Not required for sqlite
```


---

---
## Requirements
* VaultAPI
---



## Notes

- The MiniMessage format is highly flexible for styling and formatting messages. Refer to the [MiniMessage documentation](https://docs.advntr.dev/minimessage/format.html) for more details.
- SQLite is the simplest database option as it doesn’t require additional setup.

<a href="https://discord.gg/ZPyb9g6Gs4">
    <img src="https://github.com/user-attachments/assets/e2c942ae-d79a-4606-b4b0-240fd92c9a90" alt="Join our Discord for help" width="400">
</a>
