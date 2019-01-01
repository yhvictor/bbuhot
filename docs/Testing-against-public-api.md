## Server address
165.227.17.140:8080

## Account
username: admin password: 123

A valid auth request: 
```json
{
	"uid": 1,
	"auth": "f864Wjt+ccE9euGuZQppnfu5aeSSuWkuVPt91ou9mcUAtMwHgvTfDoqX0nT2fgOb6ykQ22WzfOPZVxoHwT7I",
	"saltKey": "T9Zz8d5b" 
}
```

## API
#### entry list
https://github.com/yhvictor/bbuhot/blob/master/bbuhot_server/src/main/java/com/bbuhot/server/service/ServiceModule.java

#### Proto definition
https://github.com/yhvictor/bbuhot/blob/master/bbuhot_protobuf/src/main/proto/bbuhot/service/game.proto

#### Json used for testing
https://github.com/yhvictor/bbuhot/tree/master/bbuhot_server/src/test/resources/com/bbuhot/server/service

#### Test
https://github.com/yhvictor/bbuhot/blob/master/bbuhot_server/src/test/java/com/bbuhot/server/service/AdminGameServicesTest.java

#### Postman
https://documenter.getpostman.com/view/6166669/Rzn6u2rf#8ef4f111-9c15-48f9-86da-67b210f51e5c