# version-find

An example using the [maven-git-commit-id](https://github.com/ktoso/maven-git-commit-id-plugin) plugin to make Git SCM info available at runtime. 

This project contains a library: `version-find` and an example that shows the library in action.
Run `./run-sample.sh` to run the demo. 

The script should then output: 

```
Building library.
Building sample:
  * parent-proj
  * my-app-1
  * my-app-2
  * my-app-3
  * main-proj
Running sample:
my-app-3 -> Version [buildUser=plasma258@address.com, commit=90203980ad87dba8597973e4675a88bfcf1e07ff, buildTime=10.08.2015 @ 22:14:31 BST]
my-app-2 -> Version [buildUser=user2@address.com, commit=607f4b340797e36406f5df46f891e9df1011ed60, buildTime=10.08.2015 @ 22:14:27 BST]
my-app-1 -> Version [buildUser=user1@address.com, commit=33f0a974dad2c971e2bc3bc331864ffeccf7c176, buildTime=10.08.2015 @ 22:14:24 BST]
deployable-app -> Version [buildUser=plasma258@address.com, commit=f6846d24ff1f1237c22205bd613ad1f90eb8b8bf, buildTime=10.08.2015 @ 22:14:35 BST]
-FIN-
```

This may take a while to run the first time round while it downloads any missing dependencies from the maven repos.

The example contains 5 maven projects all with git commit history. 4 projects inherit from the parent-proj pom project to share the same maven-git-commit-id plugin configuration:

```xml
<plugin>
  <groupId>pl.project13.maven</groupId>
  <artifactId>git-commit-id-plugin</artifactId>
  <version>2.1.15</version>
  <executions>
    <execution>
      <goals>
        <goal>revision</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <verbose>false</verbose>
    <generateGitPropertiesFile>true</generateGitPropertiesFile>
    <generateGitPropertiesFilename>src/main/resources/META-INF/version/git-${project.artifactId}.properties</generateGitPropertiesFilename>
    <format>properties</format>
    <skipPoms>true</skipPoms>
    <injectAllReactorProjects>false</injectAllReactorProjects>
    <failOnNoGitDirectory>false</failOnNoGitDirectory>
    <failOnUnableToExtractRepoInfo>true</failOnUnableToExtractRepoInfo>
    <runOnlyOnce>false</runOnlyOnce>
    <useNativeGit>false</useNativeGit>
    <abbrevLength>7</abbrevLength>
    <gitDescribe>
      <skip>false</skip>
      <always>false</always>
      <dirty>-dirty</dirty>
    </gitDescribe>
  </configuration>
</plugin>
```

This configuration allows each of the 4 projects to embed property files containing information about the current SCM state into their jars when built.
The main-proj project has dependencies on the other 3 projects so it's main class can then retrieve and log the version information from each of them:

```java
package com.mycompany.app;

import uk.co.optimisticpanda.versionfind.Versions;

public class Main {

  public static void main(String[] args) {
    new Versions().getVersions().forEach(
      (k, v) -> System.out.println(k + " -> " + v));
    } 
}
```

The version-find library uses the [reflections](https://github.com/ronmamo/reflections) library to retrieve the version information from each jar's META-INF folder and then make it available at runtime. 


