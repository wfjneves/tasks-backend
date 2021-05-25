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
                    bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://localhost:9000 -Dsonar.login=822890b0c468dea0df241beafe5f8fbd0df685e9 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test**,**/model/**,**Application.java,**RootController.java "
                }
            }
        }
        stage('Quality Gate') {
            steps {
                sleep(10)
                timeout(time:1, unit: 'MINUTES'){
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Deploy Backend') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }
        stage('API Test') {
            steps {
                dir('api-test') {
                    git credentialsId: 'github_login', url: 'https://github.com/wfjneves/task-api-test.git'
                    bat 'mvn test'
                }
            }
        }
        stage('Deploy FrontEnd') {
            steps {
                dir('frontend') {
                    git credentialsId: 'github_login', url: 'https://github.com/wfjneves/tasks-frontend.git'
                    bat 'mvn clean package'
                    deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001')], contextPath: 'tasks', war: 'target/tasks.war'
                }
            }
        }
        stage('Functional Test') {
            steps {
                dir('functionalTest') {
                    git credentialsId: 'github_login', url: 'https://github.com/wfjneves/tasks-functional-tests.git'
                    bat 'mvn test'
                }
            }
        }
        stage('Deploy to Production') {
            steps {
                bat 'docker build .';
                bat 'docker-compose up -d';
            }
        }
        stage('Helth Check') {
        	steps {
        	 sleep(5)
        	 dir('functionalTest'){
        	 	bat 'mvn verify -Dskip.surefire.test';
        	 
        	 }
        	}
        	
        }
    }
    post {
    	always {
    		junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functionalTest/target/surefire-reports/*.xml, functionalTest/target/failsafe-reports/*IT.xml ';
    	}
    	unsuccessful {
    		emailext attachLog: true, body: 'the log is attached', subject: 'Failed the build $BUILD_NUMBER', to: 'wesleyfelipe.jn+jenkins@gmail.com'
    	}
    	fixed {
    		emailext attachLog: true, body: 'the log is attached', subject: 'Fixed the build $BUILD_NUMBER', to: 'wesleyfelipe.jn+jenkins@gmail.com'
    	}
    }
    
}