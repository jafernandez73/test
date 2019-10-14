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
    stage('Init Checks') {
      steps {
        sh 'echo "Init Checks"'
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
            sh 'echo "Security Static Code Test"'
          }
        }
        stage('Commit Test?') {
          steps {
            sh 'echo "Commit Test"'
          }
        }
      }
    }
    stage('Archive Actifact') {
      steps {
        sh 'echo "Archive Actifact"'
      }
    }
    stage('Container Pre-Checks') {
      steps {
        sh 'echo "Container Pre-Checks"'
      }
    }
    stage('Container Build') {
      steps {
        sh 'echo ""'
      }
    }
    stage('Container Post-Checks') {
      steps {
        sh 'echo "Container Post-Checks"'
      }
    }
    stage('Release Candidate Tag') {
      steps {
        sh 'echo "Release Candidate Tag"'
      }
    }
    stage('PRE Pre-Deploy') {
      steps {
        sh 'echo "PRE Pre-Deploy"'
        sh 'echo "Database PRE pre-deploy actions"'
        sh 'echo "API gateway PRE pre-deploy actions"'
        sh 'echo "API manager PRE pre-deploy actions"'
        sh 'echo "..."'
      }
    }
    stage('PRE Deploy') {
      steps {
        sh 'echo "PRE Deploy"'
      }
    }
    stage('PRE Post-Deploy') {
      steps {
        sh 'echo "PRE Post-Deploy"'
        sh 'echo "Database PRE post-deploy actions"'
        sh 'echo "API gateway PRE post-deploy actions"'
        sh 'echo "API manager PRE post-deploy actions"'
        sh 'echo "..."'
      }
    }
    stage('PRE Smoke Test') {
      steps {
        sh 'echo "PRE Smoke Test"'
      }
    }
    stage('ITSM PRE Update') {
      steps {
        sh 'echo "ITSM PRE Update"'
      }
    }
    stage('Release Tag') {
      steps {
        sh 'echo "Release Tag"'
      }
    }
    stage('PRO Pre-Deploy') {
      steps {
        sh 'echo "PRO Pre-Deploy"'
        sh 'echo "Database PRO pre-deploy actions"'
        sh 'echo "API gateway PRO pre-deploy actions"'
        sh 'echo "API manager PRO pre-deploy actions"'
        sh 'echo "..."'
      }
    }
    stage('PRO Deploy') {
      steps {
        sh 'echo "PRO Deploy"'
      }
    }
    stage('PRO Post-Deploy') {
      steps {
        sh 'echo "PRO Post-Deploy"'
        sh 'echo "Database PRO post-deploy actions"'
        sh 'echo "API gateway PRO post-deploy actions"'
        sh 'echo "API manager PRO post-deploy actions"'
        sh 'echo "..."'
      }
    }
    stage('PRO Smoke Test') {
      steps {
        sh 'echo "PRO Smoke Test"'
      }
    }
    stage('ITSM PRO Update') {
      steps {
        sh 'echo "ITSM PRO Update"'
      }
    }    
  }
}
