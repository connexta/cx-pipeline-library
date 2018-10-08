package com.connexta.ci.jenkins.pipeline.constants

class Maven {
    static final String LARGE_OPTS = '-Xmx8192M -Xss128M -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC'
    static final String NIX_RANDOM = '-Djava.security.egd=file:/dev/./urandom'
    static final String DEFAULT_MAVEN = 'M3'
    static final String DEFAULT_JAVA = 'jdk8-latest'
    static final String DEFAULT_GLOBAL_SETTINGS = 'default-global-settings'
    // TODO: need to think about whether this is a good default or not
    static final String DEFAULT_SETTINGS = 'codice-maven-settings'
    static final String BATCH_MODE = '-B'
    static final String DEFAULT_GOALS = 'install'
    static final String PROJECT_LIST = "-pl"
    static final String DISABLE_SNAPSHOT_UPDATES = '-nsu'
    static final String DISABLE_DOWNLOAD_PROGRESS = '-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn'
}
