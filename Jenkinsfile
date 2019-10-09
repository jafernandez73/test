pipeline {
  agent none
  stages {
    stage('Init') {
      steps {
        sh 'echo "Init"'
      }
    }
    stage('Chechout') {
      agent {
        docker {
          image 'git'
        }

      }
      environment {
        dev = ''
      }
      steps {
        git(url: 'df', branch: 'dfdf', credentialsId: 'dffd')
      }
    }
  }
}