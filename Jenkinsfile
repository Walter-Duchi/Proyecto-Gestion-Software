pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/madecaro/Proyecto-Gestion-Software',
            }
        }

        stage('Build') {
            steps {
                sh 'echo "building the app"'  // Reemplaza con el comando adecuado de construcción
            }
        }

        stage('Test') {
            steps {
                sh 'echo "Running tests"'  // Reemplaza con el comando para ejecutar las pruebas
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo "Deploying the app"'  // Reemplaza con el comando para desplegar
            }
        }
    }

    post {
        success {
            echo 'Pipeline completado con éxito.'
        }
        failure {
            echo 'El pipeline falló.'
        }
    }
}
