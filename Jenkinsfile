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