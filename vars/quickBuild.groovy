#!/usr/bin/env groovy

/**
* Execute a maven 'quick build'
* A 'quick build' will skip all tests and static analysis
* The purpose is to populate the local maven repo with the project 
* artifacts and dependencies prior to running other build steps.
**/
def call(body) {

  final String SKIP_STATIC = '-DskipStatic=true'
  final String SKIP_TESTS = '-DskipTests=true'
  final String GOALS = 'clean install'
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()
  
  def qbOpts = "${SKIP_STATIC} ${SKIP_ITESTS}}
  
  if (config.opts != null) {
    qbOpts = "${qbOpts} ${config.opts}"
  }

  maven {
    version = config.version
    java = config.java
    globalSettings = config.globalSettings
    settings = config.settings
    size = config.size
    modules = config.modules
    opts = qbOpts
    goals = GOALS
  }
}
