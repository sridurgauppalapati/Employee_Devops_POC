pipeline {
    agent any

    environment {
        APP_NAME = 'employee-app'
        IMAGE_NAME = 'employee-app'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        APP_URL = "${params.APP_URL ?: 'http://localhost:8080'}"
        DOCKER_REGISTRY = "${params.DOCKER_REGISTRY ?: ''}"
    }

    parameters {
        string(name: 'APP_URL', defaultValue: 'http://localhost:8080', description: 'Base URL for smoke tests')
        string(name: 'DOCKER_REGISTRY', defaultValue: '', description: 'Optional registry prefix, e.g. myregistry.io/team')
        booleanParam(name: 'RUN_DEPLOY', defaultValue: false, description: 'Deploy to Kubernetes after image build')
        booleanParam(name: 'SEND_NOTIFICATION', defaultValue: false, description: 'Send email notification on completion')
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('CI Build') {
            steps {
                sh 'mvn clean test -pl employee-app -am'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'employee-app/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package Application') {
            steps {
                sh 'mvn -pl employee-app -am package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Push Image') {
            when {
                expression { return env.DOCKER_REGISTRY?.trim() }
            }
            steps {
                script {
                    def remoteTag = "${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
                    dockerImage.tag(remoteTag)
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-registry-credentials') {
                        dockerImage.push("${IMAGE_TAG}")
                    }
                }
            }
        }

        stage('Deploy') {
            when {
                expression { return params.RUN_DEPLOY }
            }
            steps {
                sh """
                    kubectl apply -f k8s/deployment.yaml
                    kubectl set image deployment/${APP_NAME} ${APP_NAME}=${IMAGE_NAME}:${IMAGE_TAG} || true
                """
            }
        }

        stage('Post Deployment Validation') {
            when {
                expression { return params.RUN_DEPLOY }
            }
            steps {
                sh "kubectl rollout status deployment/${APP_NAME} --timeout=180s"
            }
        }

        stage('Start App For Smoke Tests') {
            when {
                expression { return !params.RUN_DEPLOY }
            }
            steps {
                sh '''
                    docker compose down || true
                    docker compose up -d --build
                '''
                script {
                    if (isUnix()) {
                        sh '''
                            for i in $(seq 1 30); do
                              if curl -fsS http://localhost:8080/login > /dev/null; then
                                exit 0
                              fi
                              sleep 5
                            done
                            exit 1
                        '''
                    } else {
                        bat '''
                            powershell -Command "$ready=$false; 1..30 | ForEach-Object { try { Invoke-WebRequest -UseBasicParsing http://localhost:8080/login | Out-Null; $ready=$true; break } catch { Start-Sleep -Seconds 5 } }; if (-not $ready) { exit 1 }"
                        '''
                    }
                }
            }
        }

        stage('Trigger Selenium Tests') {
            steps {
                sh """
                    mvn test -pl automation-tests \
                      -DskipTests=false \
                      -DsuiteXmlFile=SmokeSuite.xml \
                      -Dbase.url=${APP_URL} \
                      -Dheadless=true
                """
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'automation-tests/target/surefire-reports/*.xml'
                    allure includeProperties: false, jdk: '', results: [[path: 'automation-tests/target/allure-results']]
                }
            }
        }

        stage('Generate Report') {
            steps {
                sh 'mvn -pl automation-tests allure:report'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'automation-tests/target/site/allure-maven-plugin/**', allowEmptyArchive: true
                }
            }
        }

        stage('Notify') {
            when {
                expression { return params.SEND_NOTIFICATION }
            }
            steps {
                emailext(
                    subject: "Employee App Deployment - Build #${env.BUILD_NUMBER}",
                    body: """
                        Deployment Successful
                        Environment : QA
                        Version : v1.0.${env.BUILD_NUMBER}
                        Build URL : ${env.BUILD_URL}
                        Smoke report : ${env.BUILD_URL}allure
                    """,
                    to: '${DEFAULT_RECIPIENTS}'
                )
            }
        }
    }

    post {
        always {
            sh 'docker compose down || true'
        }
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Review Allure and Surefire reports.'
        }
    }
}
