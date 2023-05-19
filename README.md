A ~~Spigot~~**Paper** plugin that contains API and Util classes for creating plugins much easier.

# About this fork

This fork:

1. has entirely removed the Spigot compatibility and can only run on Paper (1.19.3+)
2. has fully migrated to [MiniMessage](https://docs.advntr.dev/minimessage/index.html) formatting and removed Mojang
   legacy color formatting (&-§-code).
3. will include features from upstream as soon as I have time to do it.

# How to compile it

This fork depends on my private library _mewcore_ therefore this project cannot be compiled without it.

The _mewcore_ project is not ready to get public atm, but I will release it as soon as I have enough time.

If you really wanted to compile it, you can safely remove all the code related to _mewcore_. You can still enjoy all the
nice paper-specific features, including full MiniMessage formatting support in all messages, item name/lore and
inventory title.

# Why Paper?

I once tried to maintain both compatibilities with Spigot and Paper platform, but I gave up eventually due to how
conservative the Spigot API is and most importantly, almost every Minecraft servers are running with Paper nowadays.
There is no point to maintain a project that solely depends on a platform which fewer and fewer people choose to use.

The Paper API has added tons of new methods, which can save a lot of time for developers. Furthermore, a lot of features
that can be easily done in Paper have to hack NMS in Spigot, which is nasty and hassle to maintain.

Although this fork has fully migrated to Paper, a huge amount of work has been done to have full MiniMessage formatting
support for NexEngine and other dependent forks from me (ExcellentShop, ExcellentEnchants and ExcellentCrates). I hope
you find it useful if you are a fan of Paper too :)
