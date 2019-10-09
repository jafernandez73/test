pipeline {
  agent {
    kubernetes {
      containerTemplate {
        name 'maven'
        image 'maven:3.3.9-jdk-8-alpine'
        ttyEnabled true
        command 'cat'
      }

    }

  }
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
        git(url: 'https://github.com/jafernandez73/test', branch: 'master', credentialsId: 'githubtest', changelog: true, poll: true)
      }
    }
    stage('Build') {
      steps {
        sh 'runBuild'
      }
    }
  }
}