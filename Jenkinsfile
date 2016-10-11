#!groovy

def buildVersion = "3.0.${BUILD_NUMBER}"
def commonVersion = "3.0.+"

def javaEnv() {
    def javaHome = tool 'JDK8u66'
    ["PATH=${env.PATH}:${javaHome}/bin", "JAVA_HOME=${javaHome}"]
}

stage('checkout') {
    node {
        try {
            checkout scm
        } catch (e) {
            currentBuild.result = "FAILED"
            notifyFailed()
            throw e
        }
    }
}

stage('build') {
    node {
        try {
            withEnv(javaEnv()) {
                sh "./gradlew --refresh-dependencies clean build sonarqube -PcodeQuality -DgruntColors=false \
                    -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion}"
            }
        } catch (e) {
            currentBuild.result = "FAILED"
            notifyFailed()
            throw e
        }
    }
}

stage('tag and upload') {
    node {
        try {
            withEnv(javaEnv()) {
                sh "./gradlew uploadArchives tagRelease -DnexusUsername=$NEXUS_USERNAME -DnexusPassword=$NEXUS_PASSWORD \
                    -DgithubUser=$GITHUB_USERNAME -DgithubPassword=$GITHUB_PASSWORD -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion}"
            }
        } catch (e) {
            currentBuild.result = "FAILED"
            notifyFailed()
            throw e
        }
    }
}

stage ('propagate') {
    build job: 'intyg-intygstjanst-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
    build job: 'intyg-webcert-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
    build job: 'intyg-minaintyg-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
}

def notifyFailed() {
    emailext (subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
              body: """FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':\n\nCheck console output at ${env.BUILD_URL}""",
              recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider']])
}
