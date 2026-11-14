pipeline {
    agent any

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    tools {
        jdk 'JDK-21'
        maven 'Maven-3.9'
    }

    environment {
            TESTCONTAINERS_RYUK_DISABLED = 'true'
            TESTCONTAINERS_HOST_OVERRIDE = 'host.docker.internal'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage ('Test') {
            steps {
                sh '''
                    mvn -B clean test \
                    -Dselenide.headless=true \
                    -Dselenide.browserBinary=/usr/bin/chromium \
                    -Dwebdriver.chrome.driver=/usr/bin/chromedriver \
                    -Dchromeoptions.args="--no-sandbox,--disable-dev-shm-usage,--disable-gpu"
                '''
            }
            post {
                always {
                    allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}