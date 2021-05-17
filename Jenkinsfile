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
        stage('Sonar Analysis') {
            environment {
                scannerHome= tool 'SONNAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://localhost:9000 -Dsonar.login=822890b0c468dea0df241beafe5f8fbd0df685e9 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**, **/src/test**,**/model/**,**Application.java, **RootController.java "
                }
            }
        }     
        
    }
}