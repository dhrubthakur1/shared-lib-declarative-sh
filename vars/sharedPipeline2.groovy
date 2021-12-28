import com.checkout.CheckOut;
import com.build.MVNBuild;
import com.deploy.DeployToTomcat;

def call(Map conf=[:]) {
	
  pipeline {
       agent none
       stages {  	     
	   stage("Checkout Code for read Spec file") {
		agent any	   
               steps {		       
                 script{                                      
                   sh "echo ${conf.url}"
		   new CheckOut(this).startBuild(conf)
		   sh 'echo "read yml start"'
		   def buildData = readYaml (file: 'build.yml') 
		   def deployData = readYaml (file: 'deploy.yml') 
		   sh 'echo "read yml start"'
		   sh "echo ${buildData}"	
		   sh "echo ${deployData}"
	           env.buildRequired=buildData.application.buildRequired
		   conf.put('isBuildRequired', buildData.application.buildRequired);
		   conf.put('buildType', buildData.application.buildType);
		   conf.put('deployRequired', deployData.application.deployRequired);
	           conf.put('tomcatId', deployData.application.tomcatId);
		   conf.put('tomcatUrl', deployData.application.tomcatUrl);
		   conf.put('contextPath', deployData.application.contextPath);	 		   
		   sh "echo ${env.buildRequired}"
		   sh "echo conf: ${conf}" 
                 }
               }
           }  
	stage("Build Process start"){		
	when {
        	expression { conf.buildType == "Java" && conf.isBuildRequired == "Yes" }
          }
		//agent { dockerfile true }   
		agent {docker {
                    image 'maven:latest'                    
                    reuseNode true
                }}
			/*tools {
           			maven 'MAVEN_PATH'
          			jdk 'JAVA_HOME'
       				}*/	  

			stages{
				stage("Tools initialization") {
				       steps {
					   sh "mvn --version"
					   sh "java -version"
				       }
				   }
				stage("Checkout Code") {
				       steps {		       
					 cleanWs()
					 script{                   					  
					   sh "echo ${conf.url}"
					   new CheckOut(this).startBuild(conf)					   
					 }
				       }
				   }  
				stage("Running Testcase") {
				      steps {					   
					script{
					   new MVNBuild(this).mvnTest()
					  }					
				       }
           			}
				stage("Packing Application") {
				       steps {					  
					 script{					   
					  new MVNBuild(this).startBuild()					  
					 }
				       }
				   }
				stage ('Archive Artifacts') {
				  steps {
				    script{
				      new MVNBuild(this).archive()
				    }				   
				  }
				}			
			}		
		} 
	       stage("Deploy Process start"){		
		agent any
		when {
        		expression { conf.deployRequired == "Yes" }
          	}
		steps{              
		      script{
			new DeployToTomcat(this).deploy(conf)
		      }
	       }	
	   }	       
       }
   }
}
