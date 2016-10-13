#!groovy

def buildVersion  = "3.0.${BUILD_NUMBER}"
def commonVersion = "3.0.+"

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
        bGradle "./gradlew --refresh-dependencies clean build sonarqube -PcodeQuality -DgruntColors=false \
                 -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion}"
    }
}

stage('tag and upload') {
    node {
        bGradle "./gradlew uploadArchives tagRelease -DnexusUsername=$NEXUS_USERNAME -DnexusPassword=$NEXUS_PASSWORD -DgithubUser=$GITHUB_USERNAME \
                 -DgithubPassword=$GITHUB_PASSWORD -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion}"
    }
}

stage ('propagate') {
    build job: 'intyg-intygstjanst-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
    build job: 'intyg-webcert-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
    build job: 'intyg-minaintyg-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
}
