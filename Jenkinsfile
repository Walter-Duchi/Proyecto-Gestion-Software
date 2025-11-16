pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/Parth2k3/test-flask'  // URL de tu repositorio
            }
        }

        stage('Build') {
            steps {
                sh 'echo "building the app"'  // Agrega aquí el comando de construcción de tu aplicación (p.ej., mvn clean install para Maven)
            }
        }

        stage('Test') {
            steps {
                sh 'echo "Running tests"'  // Agrega aquí el comando para ejecutar tus pruebas, p.ej., mvn test o pytest
            }
        }

        stage('Deploy') {
            steps {
                sh 'echo "deploying"'  // Agrega aquí el comando para desplegar tu aplicación (p.ej., un script de despliegue)
            }
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
