import groovy.transform.Field

import static com.connexta.ci.jenkins.pipeline.constants.Maven.*

@Field String m2Opts
@Field String goals
@Field String m2Args
@Field String mavenVersion
@Field String javaVersion
@Field String globalSettings
@Field String settings
@Field Boolean nsu

/**
 * Execute a maven build step.
 * Wraps usage of 'withMaven' and takes additional options
 **/
def call(Closure body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  m2Opts = ""
  goals = config.goals ?: DEFAULT_GOALS
  m2Args = "${goals} ${BATCH_MODE} ${DISABLE_DOWNLOAD_PROGRESS}"
  mavenVersion = config.version ?: DEFAULT_MAVEN
  javaVersion = config.java ?: DEFAULT_JAVA
  globalSettings = config.globalSettings ?: DEFAULT_GLOBAL_SETTINGS
  settings = config.settings ?: DEFAULT_SETTINGS
  nsu = (config.updateSnapshots == null) ? true : config.updateSnapshots

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

  if (!nsu) {
    m2Args = "${m2Args} ${DISABLE_SNAPSHOT_UPDATES}"
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
