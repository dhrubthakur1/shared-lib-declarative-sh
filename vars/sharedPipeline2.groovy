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
		   checkOut.startBuild(conf)
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
	/////
       }
   }
}
