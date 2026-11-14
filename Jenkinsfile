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
        DOCKER_HOST = 'tcp://docker:2376'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage ('Test') {
            steps {
                sh 'mvn -B clean test -Dselenide.headless=true'
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