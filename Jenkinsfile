pipeline {
    agent any

    stages {
        stage('Github pull') {
            steps {
                git branch: 'master', credentialsId: 'GitHub_credentials', url: 'https://github.com/hrithik-hs/NFAtoDFA_springboot-backend'
            }
        }
        stage('Maven Test') {
            steps {
                script{
                    sh 'mvn clean test'
                }
            }
        }
        stage('Maven build') {
            steps {
                script{
                    sh 'mvn clean install'
                }
            }
        }
        stage('Docker build') {
            steps{
                script {
                    imageName=docker.build "hrithikhs/springboot-backend"
                }
            }
        }
        stage('Docker push image') {
            steps {
                script{
                    docker.withRegistry('','DockerHub_Credentials'){
                    imageName.push()
                    }
                }
            }
        }
        stage('Ansible pull image') {
            steps {
                ansiblePlaybook becomeUser: null, colorized: true, disableHostKeyChecking: true, installation: 'Ansible', inventory: 'inventory', playbook: 'playbook.yml', sudoUser: null
            }
        }
    }
}