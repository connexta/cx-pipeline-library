#!/usr/bin/env groovy

/**
* Execute a maven 'quick build'
* A 'quick build' will skip all tests and static analysis
* The purpose is to populate the local maven repo with the project 
* artifacts and dependencies prior to running other build steps.
**/
def call(body) {

  final String GIB_ENABLED = '-Dgib.enabled=true'
  final String GIB_BRANCH = '-Dgib.referenceBranch=/refs/remotes/origin/$CHANGE_TARGET'
  final String GOALS = 'clean install'
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()
  
  def incrementalOpts = "${GIB_ENABLED} ${GIB_BRANCH}"
  
  if (config.opts != null) {
    qbOpts = "${qbOpts} ${config.opts}"
  }

  quickBuild {
    version = config.version
    java = config.java
    globalSettings = config.globalSettings
    settings = config.settings
    size = config.size
  }
  maven {
    version = config.version
    java = config.java
    globalSettings = config.globalSettings
    settings = config.settings
    size = config.size
    modules = "!${config.itests}"
    opts = incrementalOpts
    goals = GOALS
    updateSnapshots = false
  }
  maven {
    version = config.version
    java = config.java
    globalSettings = config.globalSettings
    settings = config.settings
    size = config.size
    modules = config.itests
    opts = incrementalOpts
    goals = GOALS
    updateSnapshots = false
  }
}
