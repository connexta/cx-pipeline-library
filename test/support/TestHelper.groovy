package support

import com.lesfurets.jenkins.unit.BasePipelineTest

/**
 * Borrowed/Adapted from https://github.com/macg33zr/pipelineUnit/blob/master/pipelineTests/groovy/testSupport/PipelineTestHelper.groovy
 */
class TestHelper extends BasePipelineTest {

    /**
     * Override the setup for our purposes
     */
    @Override
    void setUp() {

        // Scripts (Jenkinsfiles etc) loaded from root of project directory and have no extension by default
        helper.scriptRoots = ['']
        helper.scriptExtension = ''

        // Setup the parent stuff
        super.setUp()

        registerDeclarativeMethods()
    }

    void registerDeclarativeMethods() {
        helper.registerAllowedMethod('isUnix', { return true })
        helper.registerAllowedMethod('withMaven', [Map.class, Closure.class], { Map map, Closure c ->
            c.delegate = binding
            c.call()
        })
        helper.registerAllowedMethod("bat", [Map.class], null)
        helper.registerAllowedMethod("bat", [String.class], null)
    }
}
