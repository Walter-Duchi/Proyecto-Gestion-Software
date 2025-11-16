pipeline {
    agent any  // Indica que el pipeline puede ejecutarse en cualquier nodo disponible

    tools {
        maven 'Pipeline Maven Integration'  // Usa el nombre de la instalación de Maven configurada en Jenkins
    }

    stages {
        // Etapa de Build: Compilación del código
        stage('Build') {
            steps {
                echo 'Compilando el código...'
                sh 'mvn clean install'  // Ejecuta Maven para compilar el proyecto
            }
        }

        // Etapa de Test: Ejecuta las pruebas unitarias
        stage('Test') {
            steps {
                echo 'Ejecutando pruebas unitarias...'
                sh 'mvn test'  // Ejecuta las pruebas con Maven
            }
        }

        // Etapa de Deploy: Despliega el código en un entorno de prueba (Docker o servidor local)
        stage('Deploy') {
            steps {
                echo 'Desplegando la aplicación en Docker...'
                sh 'docker-compose up -d'  // Si estás usando Docker, ejecuta el despliegue
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
