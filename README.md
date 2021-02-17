# BasicPlugin
## What is it?
This is my first attempt at a plugin (1.16) and is therefore not an example of how a plugin should be written. This plugin performs many tasks related to making a server more "christmas-ey".
## Additions
  + Snow particles around the player that simulate normal minecraft snowfall
  + Snow spawning around the player in places that snow particles spawn
  + Christmas music that plays at regular intervals for all players (synchronized music across players)
  + Addition of the bow "Frosty's Revenge" which does extra poison damage and spawns snow particles and blocks beneath the path of the arrow entity
  + Christmas mob heads dropped by most mobs
  + The ability to trade a dropped mob head for another with any skin on https://minecraft-heads.com/ using the "value" field
## Commands
This is not recommended for public use on your own server as it was just an experiment, but here is a list of commands on the server that you can figure out by looking at the source code.
  + christmasweather
    + SET_WEATHER \[NONE, SNOW, SNOW_STORM\]
    + TOGGLE_WEATHER
    + ADD_MUSICBLACKLIST \[\<player\>\]
    + REMOVE_MUSICBLACKLIST \[\<player\>\]
  + christmashead
    + TRADE \[\<value\>\]
    + GENERATE \[\<value\>\]
    + GIVE \[\<item_id\>\]
