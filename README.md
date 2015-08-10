# version-find
An example using the maven-git-commit-id plugin to make SCM info available at runtime. 

This project contains a library: `version-find` and an example that shows the library in action.
Run `.run-sample.sh` to run the demo. 

The example should output: 

```
Building library.
Building sample:
  * parent-proj
  * my-app-1
  * my-app-2
  * my-app-3
  * main-proj
Running sample:
my-app-3 -> Version [buildUser=plasma147@hotmail.com, commit=90203980ad87dba8597973e4675a88bfcf1e07ff, buildTime=10.08.2015 @ 22:14:31 BST]
my-app-2 -> Version [buildUser=user2@address.com, commit=607f4b340797e36406f5df46f891e9df1011ed60, buildTime=10.08.2015 @ 22:14:27 BST]
my-app-1 -> Version [buildUser=user1@address.com, commit=33f0a974dad2c971e2bc3bc331864ffeccf7c176, buildTime=10.08.2015 @ 22:14:24 BST]
deployable-app -> Version [buildUser=plasma147@hotmail.com, commit=f6846d24ff1f1237c22205bd613ad1f90eb8b8bf, buildTime=10.08.2015 @ 22:14:35 BST]
-FIN-
```

(May take a while to run the first time round, due to having to download maven dependencies.)

The example contains 4 maven projects all with git commit history. They all extend the parent project to inherit the config of the git commit plugin:


