import support.BasePipelineSpecification

class BitbucketPRCommentSpecification extends BasePipelineSpecification {
    def stepName = 'vars/postCommentIfBitbucketPR.groovy'

    @Override
    def setup() {
        addEnvVar("CHANGE_ID", "123")
    }

    def "sends curl command with all expected parameters"() {
        given:
        def script = loadScript(stepName)

        when: "curl step is run"
        script.call("message", "token", "url", "project", "repo")

        then:
        script.getProperty('curlCommand').contains('curl')
        script.getProperty('curlCommand').contains('message')
        script.getProperty('curlCommand').contains('token')
        script.getProperty('curlCommand').contains('url')
        script.getProperty('curlCommand').contains('project')
        script.getProperty('curlCommand').contains('repo')
        script.getProperty('curlCommand').contains('123')
    }

    def "does not send curl command with missing CHANGE_ID"() {
        given:
        addEnvVar("CHANGE_ID", null)
        def script = loadScript(stepName)

        when: "curl step is run"
        script.call("", "", "", "", "")

        then:
        script.getProperty('curlCommand') == null
    }

    def "does not send curl command with missing message"() {
        given:
        def script = loadScript(stepName)

        when: "curl step is run"
        script.call(null, "", "", "", "")

        then:
        script.getProperty('curlCommand') == null
    }

    def "does not send curl command with missing credentialsToken"() {
        given:
        def script = loadScript(stepName)

        when: "curl step is run"
        script.call("", null, "", "", "")

        then:
        script.getProperty('curlCommand') == null
    }

    def "does not send curl command with missing bitbucketUrl"() {
        given:
        def script = loadScript(stepName)

        when: "dockerd step is run"
        script.call("", "", null, "", "")

        then:
        script.getProperty('curlCommand') == null
    }

    def "does not send curl command with missing project"() {
        given:
        def script = loadScript(stepName)

        when: "curl step is run"
        script.call("", "", "", null, "")

        then:
        script.getProperty('curlCommand') == null
    }

    def "does not send curl command with missing repo"() {
        given:
        def script = loadScript(stepName)

        when: "curl step is run"
        script.call("", "", "", "", null)

        then:
        script.getProperty('curlCommand') == null
    }
}
