pipeline {
  agent {
    node {
      label 'Slave_Docker'
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
        sh 'echo ""'
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
    stage('Desplegar PRE') {
      steps {
        sh 'echo ""'
      }
    }
    stage('Tests PRE') {
      parallel {
        stage('Tests usuari') {
          steps {
            sh 'echo ""'
          }
        }
        stage('Tests rendiment PRE') {
          steps {
            sh 'echo ""'
          }
        }
      }
    }
    stage('Desplegar PRO') {
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