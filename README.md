# cx-pipeline-library
Jenkins Shared Pipeline Library containing common tasks used across projects

## Usage

To use the functions in this library add the following line to the top of a `Jenkinsfile`

```groovy
@Library('github.com/connexta/cx-pipeline-library@master') _
```
## Included Functions

### maven
 
Wraps up some of the common options passed to maven in pipelines for multiple projects and attempts to provides some 'sane' defaults based on common usages

* Providing a size will automatically set java memory values
* Maven version defaults to the label `M3` if not provided
* Java version defaults to the label `jdk8-latest` if not provided
* globalSettings defaults to the label `default-global-settings` if not provided
* settings defaults to the label `codice-maven-settings` if not provided
* If size is not provided no memory options will be set
* goals defaults to `install` if not provided 

```groovy
maven {
  version = 'Maven 3.5.4'
  java = 'jdk8-latest'
  globalSettings = 'default-global-settings'
  settings = 'codice-maven-settings'
  size = 'large'
  modules = '!foo, bar, baz'
  opts = '-Dsome.additional.options'
  goals = 'clean install'
}
```
### quickbuild

Simplifies performing a 'quick build' by handling providing the correct options

* Uses the `maven` function
* Provides additional options to handle quick builds
```groovy
quickbuild {
  version = 'Maven 3.5.4
  java = 'jdk8-latest'
  globalSettings = 'default-global-settings'
  settings = 'codice-maven-settings'
  size = 'large'
  modules = '!foo, bar, baz'
  opts = '-Dsome.additional.options'
}
```
