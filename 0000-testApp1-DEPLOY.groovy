def politiquesFile
def indicadorsFile
def deployUtilities
env.TITOL = "Projecte 0000-testApp1-DEPLOY"
env.OBSERVACIONS = "Observacions de 0000-testApp1-DEPLOY"
env.DELETE_WORK_DIR = "FALSE"
env.GIT_REPO = "git.intranet.gencat.cat/3048/test-app.git"
env.DOCKER_BUILDER_VERSION = ""
env.DOCKER_BUILDER_DOCKERFILE_PATH = ""
env.DOCKER_BUILDER_DOCKERFILE_NAME = ""
env.DOCKER_BUILDER_IMAGE_NAME = ""
env.DOCKER_BUILDER_ARGS = ""
env.DOCKER_BUILDER_USER = ""
env.DOCKERFILE_NAME = "Dockerfile"
env.DOCKERFILE_PATH = "/treball"
env.DOCKERIMAGE_NAME = "test-app"
env.DOCKER_CHECKS = "FALSE"
env.DOCKER_REGISTRY_PROJECT = "0000-test"
env.KUBERNETES_ENV = "BLUEMIX"
env.KUBERNETES_LOG_PATH = "kubernetes"
env.REGISTRY_NAMESPACE = "gencatcloud"
env.KUBERNETES_NAMESPACE_PRE = "0000-test-app"
env.KUBERNETES_NAMESPACE_PRO = "0000-test-app"
env.KUBERNETES_DEPLOYMENT_NAME_PRE = "test-app1"
env.KUBERNETES_DEPLOYMENT_NAME_PRO = "test-app1"
env.BLUEMIX_KUBERNETES_REGION_PRE = "eu-gb"
env.BLUEMIX_KUBERNETES_REGION_PRO = "eu-gb"
env.BLUEMIX_REGISTRY_REGION_PRE = "eu-gb"
env.BLUEMIX_REGISTRY_REGION_PRO = "eu-gb"
env.KUBERNETES_CLUSTER_PRE = "cluster_pre"
env.KUBERNETES_CLUSTER_PRO = "cluster_pro"
env.KUBERNETES_WAIT = "60"
env.DOCKER_ANALYSYS_OUTPUT_FOLDER = "/analysis"
env.DOCKER_ANALYSYS_OUTPUT_FILENAME = "test-appSecurityAnalysis.out"
env.EMAIL_NOTIFICATIONS = "suport.cloud@gencat.cat"
def resultado
def resultadoInt
def resultadoPre
def resultadoPro

try
{
    // Inici Init
    stage ('Init')
    {
        try
        {
            node ('Slave_Docker')
            {
                // Global definitions
                deployUtilities = load "${env.pathTasquesAnt}" + 'deployUtilitiesV2.groovy'

                if ("${env.DELETE_WORK_DIR}" != "null" && "${env.DELETE_WORK_DIR}" == "TRUE")
                {
                    //Netegem el directori de treball
                    dir('treball')
                    {
                        deleteDir()
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new hudson.AbortException("S'ha produït una excepció als SETTINGS INICIALS \n " + e)
        }
    }
    // Fi Init

    // Inici CHECKOUT
    stage ('Checkout')
    {
        try
        {
            node ('Slave_Docker')
            {
                // Global definitions
                ////politiquesFile = load "${env.pathTasquesAnt}" + 'politiquesPipelines.groovy'
                ////indicadorsFile = load "${env.pathTasquesAnt}" + 'indicadorsPipelines.groovy'
                println("CODI: ${CODI_APLICACIO} , NOM: ${NOM_APLICACIO} , BUILD: ${env.BUILD_NUMBER}" )
                //indicadorsFile.desaTempsCicleIni("${CODI_APLICACIO}", "${NOM_APLICACIO}", "${env.BUILD_NUMBER}")
                // Load properties File.
                //hashPropietats = readProperties file: "${env.APP_FILE_PROPERTIES}"

                //<GencatCloud>De moment no validem politiques
                //resultadoInt = politiquesFile.checkPolitica("${CODI_APLICACIO}", "${NOM_APLICACIO}", "TIPUS_DESPL_INT")
                //resultadoPre = politiquesFile.checkPolitica("${CODI_APLICACIO}", "${NOM_APLICACIO}", "TIPUS_DESPL_PRE")
                //resultadoPro = politiquesFile.checkPolitica("${CODI_APLICACIO}", "${NOM_APLICACIO}", "TIPUS_DESPL_PRO")

            }

            node ('Slave_Docker')
            {
                repositoryPath = "https://${env.GIT_REPO}"
                env.WORKSPACE = pwd()
                dir('treball')
                {
                    git changelog: false, credentialsId: env.SIC_SCM_CREDENTIALS, poll: false, url: "${repositoryPath}" , branch: "master"
                }

                env.VERSIO = deployUtilities.getVersio()
            }
        }
        catch (Exception e)
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE CHECKOUT \n " + e)
        }
    }
    // Fi CHECKOUT

    // Inici Check TAG DEFINITIU
    stage ('Check Tag DEFINITIU') 
    {
        try 
        {
            node ('Slave_Docker')
            {
                dir('treball')
                {
                    // Check Tag definitiu
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.SIC_SCM_CREDENTIALS, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']])
                    {
                        def URL_REPO = "https://${GIT_USERNAME}:${GIT_PASSWORD}@${env.GIT_REPO}"
                        tagNumber = sh(returnStdout: true, script: "git ls-remote --tags ${URL_REPO} | grep -v '\\^{}' | cut -f 3 -d '/' | grep -E '^${env.VERSIO}\$' | sort | wc -l")
                        tagNumber = tagNumber.trim()
                        if (!"0".equals(tagNumber))
                        {
                            error("Ja hi ha un tag definitiu amb la versió ${env.VERSIO}")
                        }
                    }
                }
            }
        } 
        catch (Exception e) 
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE CHECK TAG DEFINITIU \n " + e)
        }
    }
    // Fi Check TAG DEFINITIU

    // Inici BUILD
    stage ('Build')
    {
        try
        {
            node ('Slave_Docker')
            {
                dir('treball')
                {
                    env.BUILD_CMD = "${env.mvnHome}/bin/mvn clean package -Dmaven.test.skip=true"
                    if ("${env.DOCKER_BUILDER_VERSION}" != "null" && "${env.DOCKER_BUILDER_VERSION}" != "")
                    { //Docker builder
                        env.DOCKER_BUILDER_TAG="${env.DOCKER_BUILDER_IMAGE_NAME}:${env.DOCKER_BUILDER_VERSION}".toLowerCase()
                        if ("${env.DOCKER_BUILDER_IMAGE_NAME}".contains("gencatcloud"))
                        { //gencatCloud docker builder
                            env.DOCKER_BUILDER_TAG="${env.DOCKER_REGISTRY_SERVER}/${env.DOCKER_BUILDER_TAG}"
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.DOCKER_REGISTRY_CREDENTIALS, usernameVariable: 'REGISTRY_USERNAME', passwordVariable: 'REGISTRY_PASSWORD']])
                            {
                                sh "${env.parentShellHome}/dockerPullRegistry.sh ${DOCKER_BUILDER_TAG}"
                            }
                        }
                        else
                        { //custom docker builder
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.DOCKER_REGISTRY_CREDENTIALS, usernameVariable: 'REGISTRY_USERNAME', passwordVariable: 'REGISTRY_PASSWORD']])
                            {
                                sh "${env.parentShellHome}/dockerBuildRegistryNoPush.sh ${WORKSPACE}${env.DOCKER_BUILDER_DOCKERFILE_PATH} ${env.DOCKER_BUILDER_DOCKERFILE_NAME} ${env.DOCKER_BUILDER_TAG}"
                            }
                        }
                        sh "${env.parentShellHome}/dockerRunCommand.sh ${env.DOCKER_BUILDER_TAG} '' ${env.DOCKER_BUILDER_USER} '${env.DOCKER_BUILDER_ARGS}'"
                        sh "${env.parentShellHome}/dockerRemoveImage.sh ${env.DOCKER_BUILDER_TAG}"
                    }
                    else if ("${env.BUILD_CMD}"?.trim())
                    { //Non Docker builder
                        sh "${env.BUILD_CMD}"
                    }
                    //else No build
                }
            }

            node ('Slave_Docker')
            {
                ////indicadorsFile.desaBuildEstat("${CODI_APLICACIO}", "${NOM_APLICACIO}", "${env.BUILD_NUMBER}", "OK")
            }
        }
        catch (Exception e)
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE BUILD \n " + e)
        }
    }
    // Fi BUILD

    // Inici Unit TEST
    stage ('Unit Test')
    {
        try
        {
            node ('Slave_Docker')
            {
                //<GencatCloud>De moment no validem politiques de test
                //resultado = politiquesFile.checkPolitica("${CODI_APLICACIO}", "${NOM_APLICACIO}", "Unit test")
                resultado = "NO_TEST"
            }

            node ('Slave_Docker')
            {
                env.TEST_CMD = "${env.mvnHome}/bin/mvn test -f ./pom.xml"
                if (resultado == "REQUIRED" && "${env.TEST_CMD}"?.trim())
                {
                    sh "${env.TEST_CMD}"
                }
            }
        }
        catch (Exception e)
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE TEST \n " + e)
        }
    }
    // Fi Unit TEST

    // Inici AEC
    stage ('Anàlisi estàtic de codi')
    {
        // TODO: Integrar amb eina anàlisi statics
    }
    // Fi ACE

    // Inici Commit TEST
    stage ('Commit Test')
    {
        try
        {
            //TODO: Definir com es realitzaran aquests test i si la seva execució es controlarà per polítiques
            node ('Slave_Docker')
            {
                ////indicadorsFile.desaTempsCommitTest("${CODI_APLICACIO}", "${NOM_APLICACIO}", "${env.BUILD_NUMBER}")
            }
        }
        catch (Exception e)
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE COMMIT TEST \n " + e)
        }
    }
    // Fi Commit TEST

    // Inici Docker checks
    stage ('Docker checks')
    {
        try
        {
            node ('Slave_Docker')
            {
                if ("${env.DOCKER_CHECKS}" != "null" && "${env.DOCKER_CHECKS}" == "TRUE")
                {
                    sh "${env.parentShellHome}/dockerfileChecks.sh ${WORKSPACE}${env.DOCKERFILE_PATH}/${env.DOCKERFILE_NAME}"
                }
            }
        }
        catch (Exception e)
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE DOCKER CHECKS \n " + e)
        }
    }
    //Fi Docker checks

    // Inici Docker build
    stage ('Docker build')
    {
        try
        {
            node ('Slave_Docker')
            {
                dir('treball')
                {
                    env.DOCKER_TAG="${env.DOCKERIMAGE_NAME}:${env.VERSIO}".toLowerCase()
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.DOCKER_REGISTRY_CREDENTIALS, usernameVariable: 'REGISTRY_USERNAME', passwordVariable: 'REGISTRY_PASSWORD']])
                    {
                        sh "${env.parentShellHome}/dockerBuildRegistry.sh ${WORKSPACE}${env.DOCKERFILE_PATH} ${env.DOCKERFILE_NAME} ${env.DOCKER_REGISTRY_PROJECT} ${env.DOCKER_TAG}"
                    }

                }
            }
        } 
        catch (Exception e)
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE DOCKER CHECKS \n " + e)
        }
    }
    //Fi Docker build

    // Inici Docker analysis
    stage ('Docker analysis')
    {
        try
        {
            node ('Slave_Docker')
            {
                env.DOCKERIMAGE = "${env.DOCKER_TAG}"
                sh "${env.parentShellHome}/analyzeDockerImage.sh ${env.DOCKERIMAGE} ${WORKSPACE}${env.DOCKERFILE_PATH}${env.DOCKER_ANALYSYS_OUTPUT_FOLDER} ${env.DOCKER_ANALYSYS_OUTPUT_FILENAME}"
                sh "${env.parentShellHome}/dockerRemoveImage.sh ${env.DOCKERIMAGE}"
            }
        } 
        catch (Exception e) 
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE DOCKER ANALYSIS \n " + e)
        }
    }
    //Fi Docker analysis

    // Inici INT
    stage ('INT')
    {
        //Al cloud no aplica l'entorn de INT
    }
    // Fi INT

    // Inici Generació TAG BUILD
    stage ('Generació Tag BUILD')
    {
        try
        {
            node ('Slave_Docker')
            {
                dir('treball')
                {
                    // Si el PipeLine ha arribat fins aquí, la versió de codi és prou estable com per mereixer la  generació del tag de Build
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.SIC_SCM_CREDENTIALS, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']])
                    {
                        def URL_REPO = "https://${GIT_USERNAME}:${GIT_PASSWORD}@${env.GIT_REPO}"
                        buildTagNumber = sh(returnStdout: true, script: "git ls-remote --tags ${URL_REPO} | grep -v '\\^{}' | cut -f 3 -d '/' | grep -E '^${env.VERSIO}B' | sort | wc -l")
                        buildTagNumber = sh(returnStdout: true, script: "printf \"%03d\" ${buildTagNumber}")
                        sh(script: "git config user.email 'sic.ctti@gencat.cat'")
                        sh(script: "git config user.name 'SIC'")
                        sh(script: "git tag -a ${env.VERSIO}B${buildTagNumber} -m 'Tag de build generat per Jenkins'")
                        sh(script: "git push ${URL_REPO} --tags")
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE Generació Tag BUILD \n " + e)
        }
    }
    // Fi Generació TAG BUILD

    // Inici Smoke TEST
    stage ('Smoke Test')
    {
        //Al cloud no aplica l'entorn de INT
    }
    // Fi Smoke TEST

    // Inici PRE
    stage ('PRE')
    {
        def userInput1 = input(
            id: 'userInput1', message: 'L\'aplicació es desplegarà a Preproducció, premi Continuar.', parameters: [
             [$class: 'TextParameterDefinition', defaultValue: 'Observacions', description: 'Es procedirà a desplegar a Pre', name: '']
        ])

        try
        {
            node ('Slave_Docker')
            {
                if ("${env.KUBERNETES_ENV}" == "BLUEMIX")
                {
                    echo "Bluemix deployment"
                    env.KUBERNETES_CREDENTIALS = "${BLUEMIX_CREDENTIALS}"
                }
                else
                {
                    echo "no environment"
                }
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.DOCKER_REGISTRY_CREDENTIALS, usernameVariable: 'REGISTRY_USERNAME', passwordVariable: 'REGISTRY_PASSWORD']])
                {
                    sh "${env.parentShellHome}/dockerPullRegistry.sh ${DOCKER_REGISTRY_SERVER}/${env.DOCKER_REGISTRY_PROJECT}/${env.DOCKERIMAGE}"
                    sh "${env.parentShellHome}/dockerTag.sh ${DOCKER_REGISTRY_SERVER}/${env.DOCKER_REGISTRY_PROJECT}/${env.DOCKERIMAGE} ${env.DOCKERIMAGE}"
                }
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.KUBERNETES_CREDENTIALS, usernameVariable: 'BLUEMIX_LOGIN_USER', passwordVariable: 'BLUEMIX_LOGIN_PASSWORD']])
                {
                    sh "${env.bluemixKubernetesShellHome}/kubernetesBluemixDeploy.sh ${env.KUBERNETES_NAMESPACE_PRE} ${env.KUBERNETES_DEPLOYMENT_NAME_PRE} ${env.DOCKERIMAGE} ${env.KUBERNETES_WAIT} ${env.REGISTRY_NAMESPACE} ${env.KUBERNETES_CLUSTER_PRE} ${env.BLUEMIX_KUBERNETES_REGION_PRE} ${env.BLUEMIX_REGISTRY_REGION_PRE}"
                }
                sh "${env.parentShellHome}/dockerRemoveImage.sh ${env.DOCKERIMAGE}"
                sh "${env.parentShellHome}/dockerRemoveImage.sh ${DOCKER_REGISTRY_SERVER}/${env.DOCKER_REGISTRY_PROJECT}/${env.DOCKERIMAGE}"
            }
        } 
        catch (Exception e) 
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE PRE \n " + e)
        }
    }
    // Fi PRE

    // Inici Smoke TEST
    stage ('Smoke Test') {
        def userInput2 = input(
            id: 'userInput2', message: 'Proves a Pre correctes? L\'aplicació es desplegarà a PRODUCCIÓ, premi Continuar.', parameters: [
             [$class: 'TextParameterDefinition', defaultValue: 'Observacions', description: 'Es procedirà a desplegar a PRO', name: '']
        ])
    }
    // Fi Smoke TEST

    // Inici Acceptancy TEST
    stage ('Acceptancy Test') {

    }
    // Fi Acceptancy TEST

    // Inici Exploratory TEST
    stage ('Exploratory Test') {

    }
    // Fi Exploratory TEST


    // Inici Generació TAG DEFINITIU
    stage ('Generació Tag DEFINITIU') 
    {
        try 
        {
            node ('Slave_Docker')
            {
                dir('treball')
                {
                    // Si el PipeLine ha arribat fins aquí, la versió de codi mereix la  generació del tag definitiu
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.SIC_SCM_CREDENTIALS, usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']])
                    {
                        def URL_REPO = "https://${GIT_USERNAME}:${GIT_PASSWORD}@${env.GIT_REPO}"
                        sh(script: "git tag -a ${env.VERSIO} -m 'Tag definitiu generat per Jenkins'")
                        sh(script: "git push ${URL_REPO} --tags")
                    }
                }
            }
        } 
        catch (Exception e) 
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE TAG DEFINITIU \n " + e)
        }
    }
    // Fi Generació TAG DEFINITIU

    // Inici PRO
    stage ('PRO') 
    {
        try 
        {
            node ('Slave_Docker')
            {
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.DOCKER_REGISTRY_CREDENTIALS, usernameVariable: 'REGISTRY_USERNAME', passwordVariable: 'REGISTRY_PASSWORD']])
                {
                    sh "${env.parentShellHome}/dockerPullRegistry.sh ${DOCKER_REGISTRY_SERVER}/${env.DOCKER_REGISTRY_PROJECT}/${env.DOCKERIMAGE}"
                    sh "${env.parentShellHome}/dockerTag.sh ${DOCKER_REGISTRY_SERVER}/${env.DOCKER_REGISTRY_PROJECT}/${env.DOCKERIMAGE} ${env.DOCKERIMAGE}"
                }
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: env.KUBERNETES_CREDENTIALS, usernameVariable: 'BLUEMIX_LOGIN_USER', passwordVariable: 'BLUEMIX_LOGIN_PASSWORD']])
                {
                    sh "${env.bluemixKubernetesShellHome}/kubernetesBluemixDeploy.sh ${env.KUBERNETES_NAMESPACE_PRO} ${env.KUBERNETES_DEPLOYMENT_NAME_PRO} ${env.DOCKERIMAGE} ${env.KUBERNETES_WAIT} ${env.REGISTRY_NAMESPACE} ${env.KUBERNETES_CLUSTER_PRO} ${env.BLUEMIX_KUBERNETES_REGION_PRO} ${env.BLUEMIX_REGISTRY_REGION_PRO}"
                }
                sh "${env.parentShellHome}/dockerRemoveImage.sh ${env.DOCKERIMAGE}"
                sh "${env.parentShellHome}/dockerRemoveImage.sh ${DOCKER_REGISTRY_SERVER}/${env.DOCKER_REGISTRY_PROJECT}/${env.DOCKERIMAGE}"
            }
        } 
        catch (Exception e) 
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE PRO \n " + e)
        }
    }
    // Fi PRO

    // Inici Smoke TEST
    stage ('Smoke Test') 
    {
        try 
        {
            def userInput3 = input(
                id: 'userInput3', message: 'Proves PRO correctes?', parameters: [
                 [$class: 'TextParameterDefinition', defaultValue: 'Observacions', description: 'OK', name: 'commitTest']
            ])
        } 
        catch (Exception e) 
        {
            throw new hudson.AbortException("S'ha produït una excepció al STAGE SMOKE TEST \n " + e)
        }
    }
    // Fi Smoke TEST
} 
catch (Exception e) 
{
    node ('Slave_Docker')
    {
        println("-----------------> EXCEPCION <-----------------")
        println (e)
        currentBuild.result = 'FAILURE'
        emailext subject: "${env.JOB_NAME} - Build # ${env.BUILD_NUMBER} - FAILURE!", to: "${env.EMAIL_NOTIFICATIONS}", body: "${e.message}"
    }
}
