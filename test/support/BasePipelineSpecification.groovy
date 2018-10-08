package support

import spock.lang.Specification

/**
 * Borrowed/Adapted from: https://github.com/macg33zr/pipelineUnit/blob/master/pipelineTests/groovy/testSupport/PipelineTestHelper.groovy
 */
class BasePipelineSpecification extends Specification {

    @Delegate TestHelper testHelper

    def setup() {
        testHelper = new TestHelper()
        testHelper.setUp()
    }
}
