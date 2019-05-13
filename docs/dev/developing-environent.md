Developing environent
=====================

This guide using Linux(Ubuntu 18.04) as the developing environment.
It's possible to setup Windows to develop the whole project, although not documented here.

## Common
#### Run apt udate
```
sudo apt update
```
#### Install git
```
sudo apt install git
```
#### Configure git to use ssh key
Follow https://help.github.com/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent/

#### Clone project
```
PARENT_FOLDER=~/IdeaProjects  # Make your choice.
cd $PARENT_FOLDER
git clone git@github.com:yhvictor/bbuhot.git
```

## Backend
Backend is using:
* [dagger2](http://square.github.io/dagger/)
* [protopuf](https://developers.google.com/protocol-buffers/)
* [hibernate](http://hibernate.org/)

We are using bazel for compiling, for installation, see:
[bazel](https://docs.bazel.build/versions/master/install-ubuntu.html)

And in order to produce high quality java code, we are using:
* google-java-format
* google-error-prone
  * There's also custom error prone written for this project.

#### Install Java 8
We are using java 8 for developing.

Run below command to install it:
```
sudo apt install openjdk-8-jdk
```
If that's not the only version of java you intalled,follow [this link](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-18-04) to choose the right java version.

#### Get Intellij IDEA (Community version)
I'd suggest to use intellij for developing. It's free and effective.
Follow [this link](https://www.jetbrains.com/idea/download/#section=linux) to install.

Also get the bazel plugin for intellij.

#### Install apache, php, mysql and discuz
By now, we need to develop with old discuz client.
For this part, follow [this link](deploy-backend.md).
This part will be optional in future.

#### Build / test the project
We have gradle tasks to build & test the project. You can either run them from command line or use intellij to trigger. For command line:
```
bash gradlew :bbuhot_server:test
```

## Frontend
Frontend is using:
* [angular](http://angular.io)
* [typescript](https://www.typescriptlang.org/)
* [ant design](https://ng.ant.design/docs/introduce/zh)

We are using tools for better developing:
* [lanhu](https://lanhuapp.com/web/#/item/board?pid=dc502362-c9c4-402a-b88b-a491dc5712d3)
* [trello](https://trello.com/user55247310)

#### Install npm and angular cli tool
npm:
```
sudo apt install npm
```
angular cli:
```
sudo npm install -g @angular/cli
```

#### Prepare npm packages
```
cd $PARENT_FOLDER/bbuhot/bbuhot_client &&
npm install
```

#### Serve the project
```
npm start
```
Then the pages will be served at http://localhost:4200/v2/

## What to do next
See [developing flow](developing-flow.md) for how to submit the code to git repo.
