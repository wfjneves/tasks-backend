pipeline {
    agent any
    stages {
        stage('build backend') {
            steps {
                bat 'mvn clean package -DskipTests=true'
            }
        }
        stage('unit tests') {
            steps {
                bat 'mvn test'
            }
        }        
    }
}