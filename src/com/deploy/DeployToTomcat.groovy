package com.deploy;

public class DeployToTomcat{
  def steps
  
  public DeployToTomcat(steps) {
    this.steps = steps
  }
  
    
  public void deploy(def conf = [:]) {
     steps.echo "Deploy to tomcat called......${steps}"
    steps.deploy adapters: [steps.tomcat8(credentialsId: conf.tomcatId, path: '', url: conf.tomcatUrl)], contextPath: conf.contextPath, onFailure: false, war: 'target/*.war'
  }
}
