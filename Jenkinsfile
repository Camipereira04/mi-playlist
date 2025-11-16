pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Si el job es "Pipeline from SCM", Jenkins igual hace checkout automáticamente,
                // pero esto no molesta y queda explícito.
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Ejecutando mvn clean test...'
                sh './mvnw clean test'
            }
        }

        stage('Package') {
            steps {
                echo 'Empaquetando aplicacion...'
                sh './mvnw package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (isUnix()) {
                        echo 'Desplegando en Mac/Linux...'
                        sh 'chmod +x deploy_mac.sh'
                        sh './deploy_mac.sh'
                    } else {
                        echo 'Desplegando en Windows...'
                        bat 'deploy_windows.bat'
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline OK ✅'
        }
        failure {
            echo 'Pipeline FALLÓ ❌'
        }
    }
}
