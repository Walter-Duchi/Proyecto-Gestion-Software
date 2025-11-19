pipeline {
    agent any

    tools {
        maven 'M3'
    }

    stages {

        // Build
        stage('Build') {
            steps {
                echo 'Iniciando compilación...'
                sh 'mvn clean compile'
            }
        }

        // Test
        stage('Test') {
            steps {
                echo 'Ejecutando pruebas unitarias...'
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        // Package
        stage('Package') {
            steps {
                echo 'Empaquetando artefacto...'
                sh 'mvn package -DskipTests'
            }
        }

        // Deploy Local
        stage('Deploy (Local)') {
            steps {
                echo 'Desplegando aplicación en entorno de prueba local...'
                sh 'mkdir -p deploy'
                sh 'cp target/*.jar deploy/'
                echo 'Despliegue completado en la carpeta /deploy.'
            }
        }
    }

    // -----------------------------------------
    // NOTIFICACIONES POR CORREO (ÉXITO / ERROR)
    // -----------------------------------------
    post {

        success {
            emailext(
                to: 'waltduchi@gmail.com',
                subject: "Jenkins: Build EXITOSO del proyecto",
                body: """
                Hola Walter,

                El pipeline terminó **correctamente** 

                Proyecto: ${env.JOB_NAME}
                Build #: ${env.BUILD_NUMBER}
                Estado: ÉXITO
                URL del build: ${env.BUILD_URL}

                Saludos,
                Jenkins 
                """
            )
        }

        failure {
            emailext(
                to: 'waltduchi@gmail.com',
                subject: " Jenkins: Build FALLÓ",
                body: """
                Hola Walter,

                El pipeline ha **fallado**

                Proyecto: ${env.JOB_NAME}
                Build #: ${env.BUILD_NUMBER}
                Estado: ERROR
                URL del build: ${env.BUILD_URL}

                Revisa el log cuanto antes 
                """
            )
        }

        always {
            emailext(
                to: 'waltduchi@gmail.com',
                subject: " Jenkins: Reporte de Tests del Build ${env.BUILD_NUMBER}",
                body: """
                Hola Walter,

                Aquí tienes el reporte de pruebas del build ${env.BUILD_NUMBER}.

                Proyecto: ${env.JOB_NAME}
                URL del build: ${env.BUILD_URL}

                Saludos,
                Jenkins 
                """
            )
        }
    }
}
