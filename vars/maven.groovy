#!/usr/bin/env groovy

/**
* Execute a maven build step.
* Wraps usage of 'withMaven' and takes additional options
**/
def call(body) {

  final String LARGE_OPTS = '-Xmx8192M -Xss128M -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC'
  final String NIX_RANDOM = '-Djava.security.egd=file:/dev/./urandom'
  final String DEFAULT_MAVEN = 'M3'
  final String DEFAULT_JAVA = 'jdk8-latest'
  final String DEFAULT_GLOBAL_SETTINGS = 'default-global-settings'
  // TODO: need to think about whether this is a good default or not
  final String DEFAULT_SETTINGS = 'codice-maven-settings'
  final String BATCH_MODE = '-B'
  final String DEFAULT_GOALS = 'install'
  final String PROJECT_LIST = "-pl"
  final String DISABLE_DOWNLOAD_PROGRESS = '-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn'

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  def m2Opts = ""
  def goals = config.goals ?: DEFAULT_GOALS
  def m2Args = "${goals} ${BATCH_MODE} ${DISABLE_DOWNLOAD_PROGRESS}"
  def mavenVersion = config.version ?: DEFAULT_MAVEN
  def javaVersion = config.java ?: DEFAULT_JAVA
  def globalSettings = config.globalSettings ?: DEFAULT_GLOBAL_SETTINGS
  def settings = config.settings ?: DEFAULT_SETTINGS

  // If this is running on *nix then set the random device to /dev/urandom
  if (isUnix()) {
    m2Opts = "${m2Opts} ${NIX_RANDOM}"
  }
  
  if (config.size != null) {
    if (config.size == "large") {
      m2Opts = "${m2Opts} ${LARGE_OPTS}"
    }
  }

  if (config.modules != null) {
    m2Args = "${m2Args} ${PROJECT_LIST} ${config.modules}"
  }

  if (config.opts != null) {
    m2Args = "${m2Args} ${config.opts}"
  }

  withMaven(
    maven: mavenVersion, 
    jdk: javaVersion, 
    globalMavenSettingsConfig: globalSettings, 
    mavenSettingsConfig: settings, 
    mavenOpts: m2Opts) {
      
    if (isUnix()) {
        sh "mvn ${m2Args}"
      } else {
        bat "mvn ${m2Args}"
      }
  }
}
