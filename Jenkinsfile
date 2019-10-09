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
    stage('Inicialitzacio') {
      steps {
        sh 'echo "Init"'
      }
    }
    stage('Descarrega codi') {
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
    stage('Compila aplicacio') {
      steps {
        sh 'runBuild'
      }
    }
    stage('Tests unitaris') {
      steps {
        sh 'echo ""'
      }
    }
    stage('Desplegament PRE') {
      steps {
        sh 'echo ""'
      }
    }
    stage('Tests usuari PRE') {
      parallel {
        stage('Tests usuari') {
          steps {
            sh 'echo ""'
          }
        }
        stage('Test rendiment PRE') {
          steps {
            sh 'echo ""'
          }
        }
      }
    }
    stage('Desplegament PRO') {
      steps {
        sh 'echo ""'
      }
    }
    stage('Tests usuari PRO') {
      steps {
        sh 'echo ""'
      }
    }
  }
}