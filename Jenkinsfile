#!groovy

def buildVersion  = "3.1.${BUILD_NUMBER}"
def commonVersion = "3.1.+"

stage('checkout') {
    node {
        util.run { checkout scm }
    }
}

stage('build') {
    node {
        shgradle "--refresh-dependencies clean build sonarqube -PcodeQuality -DgruntColors=false \
                  -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion}"
    }
}

stage('tag and upload') {
    node {
        shgradle "uploadArchives tagRelease -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion}"
    }
}

stage ('propagate') {
    build job: 'intyg-intygstjanst-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
    build job: 'intyg-webcert-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
    build job: 'intyg-minaintyg-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
}
