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
        sh 'echo "Checkout"'
      }
    }
    stage('Init Checks') {
      steps {
        sh 'echo "Init Checks"'
      }
    }
    stage('ITSM Register') {
      steps {
        sh 'echo "ITSM Register"'
      }
    }
    stage('Build') {
      steps {
        sh 'echo "Build"'
      }
    }
    stage('Code Test') {
      parallel {
        stage('Unit Test') {
          steps {
            sh 'echo "Unit Test"'
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
    stage('Build Management') {
      parallel {
        stage('Build Tag') {
          steps {
            sh 'echo "Build Tag"'
          }
        }
        stage('Artifact Archive') {
          steps {
            sh 'echo "Artifact Archive"'
          }
        }
      }
    }
    stage('Container Pre-Checks') {
      steps {
        sh 'echo "Container Pre-Checks"'
      }
    }
    stage('Container Build') {
      steps {
        sh 'echo "Container Build"'
      }
    }
    stage('Container Post-Checks') {
      steps {
        sh 'echo "Container Post-Checks"'
      }
    }
    stage('Release Candidate Management') {
      parallel {
        stage('Release Candidate Tag') {
          steps {
            sh 'echo "Release Candidate Tag"'
          }
        }
        stage('Artifact Release Candidate Update') {
          steps {
            sh 'echo "Artifact Release Candidate Update"'
          }
        }
      }
    }
    stage('PRE Pre-Deploy') {
      steps {
        sh 'echo "PRE Pre-Deploy"'
        sh 'echo "Database PRE Pre-deploy actions"'
        sh 'echo "API gateway PRE Pre-deploy actions"'
        sh 'echo "API manager PRE Pre-deploy actions"'
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
        sh 'echo "Database PRE Post-deploy actions"'
        sh 'echo "API gateway PRE Post-deploy actions"'
        sh 'echo "API manager PRE Post-deploy actions"'
        sh 'echo "..."'
      }
    }
    stage('PRE Test') {
      parallel {
        stage('PRE Smoke Test') {
          steps {
            sh 'echo "PRE Smoke Test"'
          }
        }
        stage('Stress Test') {
          steps {
            sh 'echo "PRE Stress Test"'
          }
        }
        stage('Acceptance Test') {
          steps {
            sh 'echo "Acceptance Test"'
          }
        }
        stage('Exploratory Test') {
          steps {
            sh 'echo "Exploratory Test"'
          }
        }
      }
    }
    stage('ITSM PRE Update') {
      steps {
        sh 'echo "ITSM PRE Update"'
      }
    }
    stage('Release Management') {
      parallel {
        stage('Release Tag') {
          steps {
            sh 'echo "Release Tag"'
          }
        }
        stage('Artifact Release Update') {
          steps {
            sh 'echo "Artifact Release Update"'
          }
        }
      }
    }
    stage('PRO Pre-Deploy') {
      steps {
        sh 'echo "PRO Pre-Deploy"'
        sh 'echo "Database PRO Pre-deploy actions"'
        sh 'echo "API gateway PRO Pre-deploy actions"'
        sh 'echo "API manager PRO Pre-deploy actions"'
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
        sh 'echo "Database PRO Post-deploy actions"'
        sh 'echo "API gateway PRO Post-deploy actions"'
        sh 'echo "API manager PRO Post-deploy actions"'
        sh 'echo "..."'
      }
    }
    stage('PRO Test') {
      parallel {
        stage('PRO Smoke Test') {
          steps {
            sh 'echo "PRO Smoke Test"'
          }
        }
      }
    }
    stage('ITSM PRO Update') {
      steps {
        sh 'echo "ITSM PRO Update"'
      }
    }
    stage('Service Tasks') {
      parallel {
        stage('Tracking configuration') {
          steps {
            sh 'echo "Tracking configuration"'
          }
        }
        stage('...') {
          steps {
            sh 'echo "..."'
          }
        }
      }
    }
  }
}