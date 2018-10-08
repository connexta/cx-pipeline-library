import com.connexta.ci.jenkins.pipeline.constants.Maven
import com.lesfurets.jenkins.unit.MethodCall
import spock.lang.Unroll
import support.BasePipelineSpecification

class MavenSpecification extends BasePipelineSpecification {

    def stepName = 'vars/maven.groovy'

    @Unroll
    def "handling missing #name option"() {
        given:
        def mavenParams = {}
        def script = loadScript(stepName)

        when: "maven step is run"
        script.call(mavenParams)

        then: "the default #name value should be used"
        script.getProperty(property) == value

        where:
        name                || property         | value
        'java'              || 'javaVersion'    | Maven.DEFAULT_JAVA
        'version'           || 'mavenVersion'   | Maven.DEFAULT_MAVEN
        'goals'             || 'goals'          | Maven.DEFAULT_GOALS
        'globalSettings'    || 'globalSettings' | Maven.DEFAULT_GLOBAL_SETTINGS
        'settings'          || 'settings'       | Maven.DEFAULT_SETTINGS
        'updateSnapshots'   || 'nsu'            | true
    }

    @Unroll
    def "handling #name option"() {
        given:
        def script = loadScript(stepName)

        when: "maven step is run"
        script.call(mavenParams)

        then: "the #property value should be set"
        script.getProperty(property) == value

        where:
        name                | mavenParams                   || property         | value
        'java'              | { java = 'foo' }              || 'javaVersion'    | 'foo'
        'version'           | { version = 'foo' }           || 'mavenVersion'   | 'foo'
        'goals'             | { goals = 'foo bar' }         || 'goals'          | 'foo bar'
        'globalSettings'    | { globalSettings = 'foo' }    || 'globalSettings' | 'foo'
        'settings'          | { settings = 'foo' }          || 'settings'       | 'foo'
        'updateSnapshots'   | { updateSnapshots = false }   || 'nsu'            | false
    }

    def "handling windows systems"() {
        given:
        helper.registerAllowedMethod('isUnix', {return false})
        def mavenParams = {}
        def script = loadScript(stepName)

        when: "maven step is run"
        script.call(mavenParams)

        then: "only windows methods should be used"
        def shSteps = helper.callStack.findAll { call ->
            call.methodName == "sh"
        }.collect()
        shSteps.size() == 0
        def batSteps = helper.callStack.findAll { call ->
            call.methodName == "bat"
        }.collect()
        batSteps.size() == 1
    }

    def "handling unix systems"() {
        given:
        def mavenParams = {}
        def script = loadScript(stepName)

        when: "maven step is run"
        script.call(mavenParams)

        then: "only linux methods should be used"
        def shSteps = helper.callStack.findAll { call ->
            call.methodName == "sh"
        }.collect()
        shSteps.size() == 1
        def batSteps = helper.callStack.findAll { call ->
            call.methodName == "bat"
        }.collect()
        batSteps.size() == 0
        script.getProperty('m2Opts').contains(Maven.NIX_RANDOM)
    }

    def "handle large size option"() {
        given:
        def script = loadScript(stepName)
        def mavenParams = {size = 'large'}

        when: "maven step is run"
        script.call(mavenParams)

        then: "'large' maven memory options should be added"
        script.getProperty('m2Opts').contains(Maven.LARGE_OPTS)
    }

    def "handle modules option"() {
        given:
        def script = loadScript(stepName)
        def mavenParams = {modules = "foo, bar"}

        when: "maven step is run"
        script.call(mavenParams)

        then: "maven arguments should contain a project list"
        script.getProperty('m2Args').contains("${Maven.PROJECT_LIST} foo, bar")
    }

    def "handle opts option"() {
        given:
        def script = loadScript(stepName)
        def options = '-Dfoo.bar=baz -Dsome.option=something'
        def mavenParams = {opts = options}

        when: "maven step is run"
        script.call(mavenParams)

        then: "maven args should contain the opts"
        script.getProperty('m2Args').contains(options)
    }
}
