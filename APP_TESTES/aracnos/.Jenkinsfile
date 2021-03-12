pipeline {
   agent any
       environment {
        registry = "liviorodrigues/aracnos"
        registryCredential = 'docker_hub'
        dockerImage = 'aracnos'
    }
   
    stages {

        stage('Clean Workspace') {
            //when { expression { !params.ROOLBACK } }
            steps {
                deleteDir()
              }
        }
    
        stage('Cloning Git') {
        when { expression { !params.ROOLBACK } }
        steps {
            gitCloneFrom('master','gitlab_jenkis','http://gitlab.devops/gitlab/aracnos.git' )
            script{
                def pom_version = readMavenPom().getVersion()
                echo "VERSION: ${pom_version}" 
            }
            sh "mkdir -p deploy"
            dir("deploy"){
                gitCloneFrom('master','gitlab_jenkis','http://gitlab.devops/gitlab/ansible_file_deploy.git' )
            }
          }
        }
        
        
        stage('Build JAR') {
          when { expression { !params.ROOLBACK } }
          steps {
                
            sh "mvn -B -DskipTests clean package"
            //stash name:"jar", includes:"target/myappapi-0.0.0-SNAPSHOT.jar"
          }
        }
        
        stage('Test') {
            when { expression { !params.ROOLBACK } }
            steps {
                sh 'mvn test'
              }
              post {
                always {
                  junit 'target/surefire-reports/*.xml'
                }
              }
        }
        
        stage('Building image') {
            when { expression { !params.ROOLBACK } }
            steps{
                script {
                dockerImage = docker.build(registry + ":$BUILD_NUMBER" )
                sh 'docker build -t liviorodrigues/aracnos":$BUILD_NUMBER" . '
                }
            }
        }

        stage('Register Image') {
            when { expression { !params.ROOLBACK } }
            steps{
                script {
                    docker.withRegistry( '', registryCredential ) {
                    dockerImage.push()
                }
            }
          }
        }        

        stage('Deploy') {
            when { expression { !params.ROOLBACK } }
            steps{
                script {
                       dir("deploy"){
                           deployWithBuildNumber(env.BUILD_NUMBER)
                       }
                }
            }
        }

    }
    
}


def gitCloneFrom(branch,credentialsId,url ){
    try{
        git branch: "${branch}", credentialsId: "${credentialsId}", url: "${url}"
    } catch(Exception e) {
        println("Exception: ${e}")
    }
}

def deployWithBuildNumber(buildNumber){
    try{
       sh "ansible-playbook -i hosts app_provisioning.yml --extra-vars REGISTER=${buildNumber} -vv"
    } catch(Exception e) {
       println("Exception: ${e}")
    }
}