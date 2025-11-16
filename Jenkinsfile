pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/madecaro/Proyecto-Gestion-Software' 
            }
        }

        stage('Build') {
            steps {
                sh 'echo "building the app"'  // Aquí puedes agregar tu comando de compilación
            }
        }

        stage('Test') {
            steps {
                sh 'echo "Running tests"'  // Aquí puedes agregar el comando para ejecutar tus pruebas
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo "deploying"'  // Aquí puedes agregar tu comando de despliegue
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

