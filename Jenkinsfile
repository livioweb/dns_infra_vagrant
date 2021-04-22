def rollback = env.ApllyBuild_NAME.replace("#", "")

pipeline {

    environment {
        registry = "liviorodrigues/apimyhome"
        registryCredential = 'docker_hub_my_app_api'
        dockerImage = 'apimyhome'
    }
    agent any

    parameters {
        booleanParam(name: "ROOLBACK", defaultValue: false)
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
                gitCloneFrom('master','user_pass_gitlab','http://172.17.177.120:10080/root/aracnor_l.git' )
                script{
                    def pom_version = readMavenPom().getVersion()
                    echo "VERSION: ${pom_version}" 
                }
                sh "mkdir -p deploy"
                dir("deploy"){
                    gitCloneFrom('master','user_pass_gitlab','http://172.17.177.120:10080/root/ansible_file_deploy.git' )
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

        stage('SonarQube Analysis') {
            when { expression { !params.ROOLBACK } }
            steps {
                withSonarQubeEnv('sonar_aracnos') {
                        sh "mvn sonar:sonar -Dsonar.projectKey=aracnor_l -Dsonar.host.url=http://172.17.177.140"
                }
            }
        }

        stage("Quality Gate") {
            when { expression { !params.ROOLBACK } }
            steps {
                withSonarQubeEnv('sonar_aracnos') {
                    sh 'mvn -B -X sonar:sonar'
                    timeout(time: 10, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: false
                    }
                }
            }
          }

        stage('Building image') {
            when { expression { !params.ROOLBACK } }
            steps{
                script {
                dockerImage = docker.build(registry + ":$BUILD_NUMBER" )
                sh 'docker build -t liviorodrigues/apimyhome":$BUILD_NUMBER" . '
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

        stage ('Start Deploy') {
            //when { expression { !params.ROOLBACK } }
            steps {
                script {
                    if (params.ROOLBACK) {
                        script{
                            stage ('Only Rollback: ' + rollback) {
                                dir("deploy"){
                                    gitCloneFrom('master','user_pass_gitlab','http://172.17.177.120:10080/root/ansible_file_deploy.git' )
                                    deployWithBuildNumber(rollback)
                                }
                            }
                        }

                    } else {

                        /*script{
                            stage ('Only Rollback: ' + rollback) {
                                dir("deploy"){
                                    deployWithBuildNumber(env.BUILD_NUMBER)
                                }
                            }
                        }*/

                       dir("deploy"){
                           deployWithBuildNumber(env.BUILD_NUMBER)
                       }
                    }
                }

            }
        }

        stage('GIT TAG') {
            when { expression { !params.ROOLBACK } }
            steps {
                dir("deploy"){
                    gitCloneFrom('master','user_pass_gitlab','http://172.17.177.120:10080/root/ansible_file_deploy.git' )
                    sh "git remote add tag_push ssh://git@172.17.177.120:10022/root/aracnor_l.git"
                    
                        sh('''
                            git config user.name 'jenkins'
                            git config user.email 'jenkins@devops.pulse'
                        ''')
                    sh "git tag -a vMaster.${pom_version}.${BUILD_NUMBER} -m '${BUILD_NUMBER}:${pom_version} stable release'"
                    sh("git push tag_push --tags")
                    //gitTagFrom('master','user_pass_gitlab','http://172.17.177.120:10080/root/aracnor_l.git', rollback )

                }
            }
        }

        /*
        stage('Deploy') {
            when { expression { !params.ROOLBACK } }
            steps {
                dir("deploy"){
                    deployWithBuildNumber(env.BUILD_NUMBER)
                }
            }
        }

        stage('Roolback Deploy') {
            when { expression { params.ROOLBACK } }
            steps {
                dir("deploy"){
                    deployWithBuildNumber(rollback)
                }

            }
        }
        */

        stage('Notify GitLab') {
            steps {
                echo 'Notify GitLab'
                updateGitlabCommitStatus name: 'build', state: 'success'
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

def gitTagFrom(branchName, credentialsId, url, buildNumber ){
    try{
//git_lab_token
            dir("deploy"){
                gitCloneFrom('master','user_pass_gitlab',"${url}" )
                //git branch: "master",  url: "ssh://git@172.17.177.120:10022/root/aracnor_l.git"
                sh "git remote add tag_push ssh://git@172.17.177.120:10022/root/aracnor_l.git"
                    sh('''
                        git config user.name 'jenkins'
                        git config user.email 'jenkins@devops.pulse'
                    ''')
                sh "git tag -a vMaster.${buildNumber} -m '${buildNumber} stable release'"
                sh("git push tag_push --tags")
                //gitTagFrom('master','user_pass_gitlab','http://172.17.177.120:10080/root/aracnor_l.git', rollback )
            }
    } catch(Exception e) {
        println("Exception: ${e}")
    }
}