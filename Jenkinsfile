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
        GIT_REPOSITORY = 'https://github.com/jafernandez73/test'
      }
      steps {
        git(url: '${GIT_REPOSITORY}', branch: 'master', credentialsId: 'githubtest', changelog: true, poll: true)
      }
    }
    stage('Build') {
      steps {
        sh 'runBuild'
      }
    }
  }
}