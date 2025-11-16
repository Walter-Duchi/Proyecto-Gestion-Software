pipeline {
    agent any

    environment {
        IMAGE_NAME = 'jenkins/jenkins:lts-jdk17'  // Nombre de tu imagen Docker
        CONTAINER_NAME = 'fervent_lumiere'  // Nombre del contenedor Docker
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Descargar el código del repositorio
                git branch: 'main', url: 'https://github.com/madecaro/Proyecto-Gestion-Software'
            }
        }

        stage('Build') {
            steps {
                script {
                    // Construir la imagen Docker
                    echo "Construyendo la imagen Docker..."
                    sh 'docker build -t ${IMAGE_NAME} .'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Ejecutar las pruebas dentro de un contenedor Docker
                    echo "Ejecutando pruebas unitarias en Docker..."
                    sh 'docker run --rm ${IMAGE_NAME} ./ruta/a/tus/pruebas'  // Ajusta el comando según cómo ejecutes las pruebas
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Desplegar la aplicación en un contenedor Docker
                    echo "Desplegando la aplicación en Docker..."

                    // Detener y eliminar el contenedor si ya existe
                    sh 'docker stop ${CONTAINER_NAME} || true'
                    sh 'docker rm ${CONTAINER_NAME} || true'

                    // Ejecutar el contenedor en segundo plano
                    sh 'docker run -d --name ${CONTAINER_NAME} -p 8080:8080 ${IMAGE_NAME}'
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
