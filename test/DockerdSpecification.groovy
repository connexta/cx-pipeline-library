import com.connexta.ci.jenkins.pipeline.constants.Docker
import spock.lang.Specification
import spock.lang.Unroll
import support.BasePipelineSpecification

class DockerdSpecification extends BasePipelineSpecification {
    def stepName = 'vars/dockerd.groovy'

    def "handling missing log option"() {
        given:
        def mavenParams = {}
        def script = loadScript(stepName)

        when: "dockerd step is run"
        script.call(mavenParams)

        then:
        script.getProperty('log') == Docker.DOCKERD_DEFAULT_LOG
    }

    def "handling missing insecureRegistries option"() {
        given:
        def mavenParams = {}
        def script = loadScript(stepName)

        when: "dockerd step is run"
        script.call(mavenParams)

        then:
        !script.getProperty('dockerdCmd').contains(Docker.DOCKERD_INSECURE_REG_OPTION)
    }

    def "handling log option"() {
        given:
        def mavenParams = { log = '/foo/bar.log' }
        def script = loadScript(stepName)

        when: "dockerd step is run"
        script.call(mavenParams)

        then:
        script.getProperty('log').equals('/foo/bar.log')
    }

    def "handling insecureRegistry list option"() {
        given:
        def mavenParams = { insecureRegistries = ['foo', 'bar']}
        def script = loadScript(stepName)

        when: "dockerd step is run"
        script.call(mavenParams)

        then:
        script.getProperty('dockerdCmd').contains("${Docker.DOCKERD_INSECURE_REG_OPTION} foo")
        script.getProperty('dockerdCmd').contains("${Docker.DOCKERD_INSECURE_REG_OPTION} bar")
    }

    def "handling insecureRegistry string option"() {
        given:
        def mavenParams = { insecureRegistries = "foo" }
        def script = loadScript(stepName)

        when: "dockerd step is run"
        script.call(mavenParams)

        then:
        script.getProperty('dockerdCmd').contains("${Docker.DOCKERD_INSECURE_REG_OPTION} foo")
    }
}
