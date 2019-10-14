pipeline {
  agent {
    node {
      label 'Slave_Docker'
    }

  }
  stages {
    stage('Init') {
      steps {
        sh 'echo "Init"'
      }
    }
    stage('Checkout') {
      steps {
        sh 'echo ""'
      }
    }
    stage('ITSM Register ') {
      steps {
        sh 'echo "compila"'
      }
    }
    stage('Build') {
      steps {
        sh 'echo ""'
      }
    }
    stage('Code Test') {
      parallel {
        stage('Unit Test') {
          steps {
            sh 'echo ""'
          }
        }
        stage('Quality Static Code Test') {
          steps {
            sh 'echo "Quality Static Code Test"'
          }
        }
        stage('Security Static Code Test') {
          steps {
            sh 'wcho "Security Static Code Test"'
          }
        }
      }
    }
    stage('Commit Test') {
      steps {
        sh 'echo ""'
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