#!groovy

def buildVersion  = "3.1.${BUILD_NUMBER}"
def commonVersion = "3.1.+"

stage('checkout') {
    node {
        git url: "https://github.com/sklintyg/intygstyper.git", branch: GIT_BRANCH
        util.run { checkout scm }
    }
}

stage('build') {
    node {
        try {
            shgradle "--refresh-dependencies clean build testReport sonarqube -PcodeQuality -PcodeCoverage -DgruntColors=false \
                  -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/allTests', \
                reportFiles: 'index.html', reportName: 'JUnit results'
        }
    }
}

stage('tag and upload') {
    node {
        shgradle "uploadArchives tagRelease -DbuildVersion=${buildVersion} -DcommonVersion=${commonVersion}"
    }
}

stage('propagate') {
    build job: 'intyg-intygstjanst-pipeline', wait: false, parameters: [[$class: 'StringParameterValue', name: 'GIT_BRANCH', value: GIT_BRANCH]]
}
