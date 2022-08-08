# BusinessPlugin
Spigot Plugin to manage business.

Instruction for the correct use of the plugin
1. Your server .jar should be in 1.16.5 version to work properly.
2. You need a SQL database and create your DATABASE with your favourite name, then write it on config.yml

In config.yml you can customize messages and set-up the link with the DB

# Permissions:
Admin:
- azienda.list -> view all the business in the server
- azienda.reload -> reload the config file and the link with your DB
- azienda.admin -> this permission allows the player to create/delete businesses

Employer:
- dipendente.[businessname] -> allows player to make receipt, use BusinessChat.

Deputy director:
- vicedirettore.[businessname] -> Employers permissions + hire employers, make an agreement with employers

Director:
- direttore.[businessname] -> Promote employers, manage the business account

# Commands:
- /azienda list -> view all the business in the server
- /azienda reload -> reload the config file and the link with your DB
- /azienda create [businessname] -> create a business
- /azienda delete [businessname] -> delete a business
#
- /azienda [businessname] add [playername] -> hire a employer
- /azienda [businessname] remove [playername] -> dismiss a employer
- /azienda [businessname] promote [playername] -> promote a player to deputy-director
- /azienda [businessname] degrade [playername] -> degrade a player to employer
- /conto mostra -> show the business account
- /conto deposita [businessname] [money] -> deposit money from your personal account
- /conto preleva [businessname] [money] -> withdraw money from the business account
#
- /scontrino [playername] [money] [businessname] [description] -> give a receipt to a customer
- /chat -> toggle business chat


