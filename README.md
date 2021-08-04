# PokeAverage
A CLI Application to retrieve and analyze data from Pokemon. This grants insight to the user about the general idea of what each type is good at, and grants them the knowledge to build out their own teams.


## Actors / Features
- A User can:
	- Specify a csv file to create a database file from
	- Specify a db file to save and load from
	- Specify a search to use (Pokemon ID, Pokemon Name, Type to average)
	- Save the search result to a file or output to the console

## Building
>mvn compile

## Usage
>mvn exec:java

Additionally, there are some flags that can be set on the command line:
	- "--csv" or "-c": Specify your csv file to load. Default is pokemon.csv.
	- "--database" or "-d": Specify your db file to save/load from. Default is pokemon.db.
	- "--search" or "-s" Specify your search query. No default, but will go into GUI mode without one.
	- "--search-type" or "-t": Specify which search function to use. Same as the GUI list, command line search on types will need one.
	- "--force-reload" or "-F": Force the database to reload from the csv.
	- "--output" or "-o": Specify where to save the search query to.
