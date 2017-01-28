# dabBot [![CircleCI](https://circleci.com/gh/sponges/dabBot.svg?style=svg)](https://circleci.com/gh/sponges/dabBot)
A public discord music bot.

## Setup
1. Compile using maven

        mvn clean package        
1. Create `config.toml`

        dev    = true if development instance
        token  = "Bot application token"
        owner  = "Owner discord user id"
        regex  = "Command parser regex pattern"
        game   = "Current playing game"
        invite = "Bot invite link"
        about  = "About text"
        join   = "Server join message"
        carbon = "carbonitex.net api key"
        dbots  = "bots.discord.pw api key"
    Sample pattern: `^\\?([a-zA-Z]+)(?:\\s+)?(.*)?` - for `?` as the prefix.
1. Create `hikari.properties`

        dataSourceClassName=org.postgresql.ds.PGSimpleDataSource
        dataSource.user=
        dataSource.password=
        dataSource.databaseName=
        dataSource.portNumber=
        dataSource.serverName=
1. Run the bot

        java -jar target/JavaMusicBot.jar

## Dependencies
Dependencies are managed by Maven. See the maven [pom.xml](https://github.com/sponges/JavaMusicBot/blob/master/pom.xml) file.

## License
Licensed under Creative Commons Attribution NonCommercial (CC-BY-NC). See the `LICENSE` file in the root directory for 
full license text.

License summary (not legal advise, read the full license)
![](https://im.not.ovh/FfaTma29YrybOca.png)

Source: [tldrlegal.com](https://tldrlegal.com/license/creative-commons-attribution-noncommercial-(cc-nc)#summary)