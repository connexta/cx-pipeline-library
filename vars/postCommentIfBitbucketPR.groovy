#!/usr/bin/env groovy
import groovy.transform.Field

@Field String curlCommand

/**
 * Posts a comment to a Pull Request hosted on a BitBucket server.
 **/
def call(String message, String credentialsToken, String bitbucketUrl, String project, String repo) {
    if (env.CHANGE_ID == null) {
        echo "Change ID was null"
    } else {
        echo "Attempting to comment on the BitBucket PR."
        if (message != null && credentialsToken != null && bitbucketUrl != null && project != null && repo != null) {
            curlCommand = "curl -X POST -m 10 -d \'{ \"text\" : \"${message}\" }\' -H \"Authorization: Basic ${credentialsToken}\" -H \"Content-Type: application/json\" -H \"Accept: application/json\" ${bitbucketUrl}/rest/api/1.0/projects/${project}/repos/${repo}/pull-requests/${env.CHANGE_ID}/comments"
            sh "${curlCommand}"
        }
    }
}
