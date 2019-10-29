#!/usr/bin/env groovy
import groovy.transform.Field

import static com.connexta.ci.jenkins.pipeline.constants.Docker.DOCKERD_DEFAULT_LOG
import static com.connexta.ci.jenkins.pipeline.constants.Docker.DOCKERD_INSECURE_REG_OPTION

@Field String log = DOCKERD_DEFAULT_LOG
@Field String dockerdCmd = "dockerd"

/**
* Start a docker daemon
* This step will start a docker daemon as a background process.
**/
def call(body) {
  timeout(time: 10, unit: 'MINUTES') {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    if (config.insecureRegistries != null) {
      if (config.insecureRegistries instanceof Collection) {
        Collection<String> reg = config.insecureRegistries
        reg.each {
          dockerdCmd = "${dockerdCmd} ${DOCKERD_INSECURE_REG_OPTION} ${it}"

        }
      } else if (config.insecureRegistries instanceof String) {
        dockerdCmd = "${dockerdCmd} ${DOCKERD_INSECURE_REG_OPTION} ${config.insecureRegistries}"
      }
    }

    if (config.log != null) {
      log = config.log
    }

    if (! isUnix()) {
      return
    }
  
    sh "nohup ${dockerdCmd} > ${log} 2>&1 &"
  }
}
