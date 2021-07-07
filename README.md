# Golfbook backend

This is the backend for my full stack application idea about golf score keeping and sharing. It's composed of a RESTFUL API to interact with server and a Websocket to provide automatic update.
I use Docker to deploy to the server.
My work is mainly influenced by [KtorEasy](https://github.com/mathias21/KtorEasy.git) project made by mathias21.

## Golfbook API

### Tools

- server framework : Ktor
- Database : POSTGRESQL
- sql framework : Exposed
- injection : Koin


### API

#### Player

List all players. Returns a list of player.
```
GET /player 
```
Get a player. Returns a player if found.
```
GET /player/{playerId}
```
Add player. It takes a PostPlayerBody as a paramater. Returns a Player.
```
POST /player 
```

Update Player. It takes a PutPlayerBody as a parameter. Returns a Player.
```
PUT /player/{playerId}
```

Delete player. Returns a boolean.
```
DELETE /player/{playerId}
```

#### Course

List all courses. Returns a list of courses.
```
GET /course
```
Get a course. Returns a courseDetails if found. 
```
GET /course/{courseId}
```
Add course. It takes a PostCourseBody as a parameter. Returns a CourseDetails.
```
POST /course
```
Update Course. It takes a PutCourseBody as a parameter. Returns a CourseDetails.
```
PUT /course/{courseId}
```
Delete course. Returns a boolean.
```
DELETE /course/{courseId}
```

#### Game

CRUD operations on game
```
POST /game
GET /game
GET /game/{gameId}
PUT /game/{gameId}
DELETE /game/{gameId}
```
Get and update operations on the scorebook of a game.
```
GET /game/{gameId}/scorebook
PUT /game/{gameId}/scorebook
```

#### Tournament

CRUD operations on tournaments.

```
POST /tournament
GET /tournament
GET /tournament/{tournamentId}
PUT /tournament/{tournamentId}
```

Get leaderboard of a tournament.
```
GET /tournament/{tournamentId}/leaderboard
```

## Golfbook websocket

TODO

## TODOS

- place exceptions on bad db calls
