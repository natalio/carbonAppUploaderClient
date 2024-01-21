pipeline {
    agent any
    environment {
        ESB_DEV_CREDS = credentials('esb-dev-creds')
        ESB_DEV_BASE_URL = credentials('esb-dev-base-url')
        ESB_DEV_SSL_TRUST_STORE_JKS    = credentials('esb-dev-ssl-trust-store-jks')
        ESB_DEV_SSL_TRUST_STORE_PASSWORD = credentials('esb-dev-ssl-trust-store-password')
        ESB_QLD_CREDS = credentials('esb-qld-creds')
        ESB_QLD_BASE_URL = credentials('esb-qld-base-url')
        ESB_QLD_SSL_TRUST_STORE_JKS    = credentials('esb-qld-ssl-trust-store-jks')
        ESB_QLD_SSL_TRUST_STORE_PASSWORD = credentials('esb-qld-ssl-trust-store-password')
        ESB_PROD_SSL_TRUST_STORE_JKS    = credentials('esb-prod-ssl-trust-store-jks')
    }
    parameters {
        string(defaultValue: "", description: 'Diretorido da aplicação', name: 'COMPOSITE_APPLICATION_PATH')
        string(defaultValue: 'dev', description: 'Ambiende de Deploy', name: 'DEPLOY_TO')
    }
    stages {
        stage ('Build') { 
            steps{
                echo 'Repositorio: ' + COMPOSITE_APPLICATION_PATH
                withMaven(
                    maven: 'maven-3', 
                    mavenLocalRepo: '.repository', 
                    traceability: true
                ) {
                    sh "mvn clean install"
                }
            }
        }
      
        stage ('Deploy Dev') {
            when{
                expression { params.DEPLOY_TO == 'dev' }
            }
            steps{
                sh 'cp '+ESB_PROD_SSL_TRUST_STORE_JKS + ' ./target/cert.jks'
                sh 'java -jar ./target/appdeploy-0.0.1-jar-with-dependencies.jar ' + ESB_DEV_BASE_URL + ' ' + ESB_DEV_CREDS_USR + ' ' + ESB_DEV_CREDS_PSW + ' '+ COMPOSITE_APPLICATION_PATH + ' '+ env.WORKSPACE +'/target/cert.jks ' + ESB_DEV_SSL_TRUST_STORE_PASSWORD
            }
        }

        stage ('Deploy QLd') {
            when{
                expression { params.DEPLOY_TO == 'qualidade' }
            }
            steps{
                sh 'java -jar ./target/appdeploy-0.0.1-jar-with-dependencies.jar ' + ESB_QLD_BASE_URL + ' ' + ESB_QLD_CREDS_USR + ' ' + ESB_QLD_CREDS_PSW + ' '+ COMPOSITE_APPLICATION_PATH +' ' + ESB_QLD_SSL_TRUST_STORE_JKS + ' ' + ESB_QLD_SSL_TRUST_STORE_PASSWORD
            }
        }

        stage ('Deploy Prod') {
            when{
                expression { params.DEPLOY_TO == 'prod' }
            }
            steps{
                sh 'java -jar ./target/appdeploy-0.0.1-jar-with-dependencies.jar ' + ESB_QLD_BASE_URL + ' ' + ESB_QLD_CREDS_USR + ' ' + ESB_QLD_CREDS_PSW + ' '+ COMPOSITE_APPLICATION_PATH +' /var/jenkins_home/wso2/esb/dev/wso2carbon.jks ' + ESB_QLD_SSL_TRUST_STORE_PASSWORD
            }
        }
    }

}