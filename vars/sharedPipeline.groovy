import com.checkout.CheckOut;
import com.build.MVNBuild;
import com.deploy.DeployToTomcat;
//import groovy.yaml.YamlSlurper;

def call(def conf=[:]) {
	def checkOut = new CheckOut(this)
	def mvnBuild = new MVNBuild(this)
	def deployToTomcat = new DeployToTomcat(this)
	
  pipeline {
       agent any
       tools {
           maven 'MAVEN_PATH'
          jdk 'JAVA_HOME'
       }
       stages {
           stage("Tools initialization") {
               steps {
                   bat "mvn --version"
                   bat "java -version"
               }
           }
	   stage("Checkout Code") {
               steps {		       
                 script{                   
                   bat 'echo "${checkOut}"'
                   bat "echo ${conf.url}"
		   checkOut.startBuild(conf)
		   bat 'echo "read yml start"'
		   def datas = readYaml (file: 'build.yml') 
		   bat 'echo "read yml start"'
		   bat "echo ${datas}"
		   bat "echo ${datas.application.buildRequired}"
	           bat "echo ${datas.application.buildType}"
			bat "echo ${env.buildRequired}"
	           env.buildRequired="${datas.application.buildRequired}"
		   bat "echo ${env}"
		  bat "echo ${env.buildRequired}"
		   bat "echo conf.put('datas.application.buildRequired','yes')"
		   bat echo "conf.put('datas.application.buildType', 'java')"
		   bat "echo Hi......."
		   bat "echo conf.get('datas.application.buildRequired')"
		   bat "echo conf.datas.application.buildRequired"
                   /*checkout([
                    $class: 'GitSCM',
                    branches: [[name:  conf.branch ]],
                    userRemoteConfigs: [[ url: conf.url ]]
                  ])*/
                 }
               }
           }
           stage("Cleaning workspace") {
               steps {
                   //bat "mvn clean"
                 script{                   
                   bat 'echo "${mvnBuild}"'
                   bat 'echo "Build...."' 
                    mvnBuild.clean()
                 }
               }
           }
           stage("Running Testcase") {
              steps {
                   //bat "mvn test"
                script{
                  mvnBuild.mvnTest()
                }
               }
           }
           stage("Packing Application") {
               steps {
                   //bat "mvn package -DskipTests"
                 script{
                  mvnBuild.startBuild()
                 }
               }
           }
        stage ('Archive Artifacts') {
          steps {
            script{
              mvnBuild.archive()
            }
            //archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            //cleanWs()
          }
        }
       stage("Deploy"){
            steps{              
              script{
		deployToTomcat.deploy(conf)
              }
                //deploy adapters: [tomcat8(credentialsId: 'tomcatadmin', path: '', url: 'http://localhost:7070')], contextPath: 'spring4', onFailure: false, war: 'target/helloworld.war'                
            }
        }
       }
   }
}
