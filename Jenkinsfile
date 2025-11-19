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
    // NOTIFICACIONES POR CORREO (ÉXITO / ERROR / SIEMPRE)
    // -----------------------------------------
    post {

        success {
            emailext(
                to: 'waltduchi@gmail.com',
                subject: "Jenkins: Build EXITOSO del proyecto",
                body: """
                    <p>Hola Walter,</p>
                    <p>El pipeline terminó <b>correctamente</b>.</p>
                    <p>
                    Proyecto: ${env.JOB_NAME}<br>
                    Build #: ${env.BUILD_NUMBER}<br>
                    Estado: ÉXITO<br>
                    URL del build: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a>
                    </p>
                    <p>Saludos,<br>Jenkins</p>
                """,
                mimeType: 'text/html'
            )
        }

        failure {
            emailext(
                to: 'waltduchi@gmail.com',
                subject: "Jenkins: Build FALLÓ",
                body: """
                    <p>Hola Walter,</p>
                    <p>El pipeline ha <b>fallado</b>.</p>
                    <p>
                    Proyecto: ${env.JOB_NAME}<br>
                    Build #: ${env.BUILD_NUMBER}<br>
                    Estado: ERROR<br>
                    URL del build: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a>
                    </p>
                    <p>Revisa el log cuanto antes.</p>
                """,
                mimeType: 'text/html'
            )
        }

        always {
            emailext(
                to: 'waltduchi@gmail.com',
                subject: "Jenkins: Reporte Build #${env.BUILD_NUMBER}",
                body: """
                    <p>Hola Walter,</p>
                    <p>El build #${env.BUILD_NUMBER} ha finalizado.</p>
                    <p>
                    Proyecto: ${env.JOB_NAME}<br>
                    URL del build: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a>
                    </p>
                    <p>Saludos,<br>Jenkins</p>
                """,
                mimeType: 'text/html'
            )
        }
    }
}
