Developing flow
===============

## Create your own branch for developing
You can either
* create a branch under yhvictor/bbuhot or
* fork the project and modify on your forked project.

Example for first approach:
```
BRANCH_NAME=yhvictor/modify_md  # Change the name to your choice.
git pull  # sync with remote before creating new branch
git checkout -b $BRANCH_NAME origin/master
git push --set-upstream origin $BRANCH_NAME
```
Then developing on your branch.

## Before uploading to remote branch.
Formating your code before pushing to remote:
* For backend
  * Run it in Intellij.
* For frontend
```
ng lint --fix
```

Testing the code
* For backend
```
bazel test //javatests/...
```
* For frontend
```
ng test
```

## Upload to your remote branch.
```
git push
```
Then submit pull request to master branch from github UI.
#### Solve conflict
In case of conflict, instead of doing it on github UI, do it on command line.
```
git pull
git merge origin/master  # (alternative is rebase) then solve your conflict here.
```
Then run `git mergetool` if necessary.

## Submit your code to master
Please make the commit meaningful and use "squash and merge".
