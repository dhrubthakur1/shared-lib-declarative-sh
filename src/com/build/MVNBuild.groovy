package com.build;

public class MVNBuild{
  def steps
  
  public MVNBuild(steps) {
    this.steps = steps
  }
  
  public void clean() {
     steps.echo "clean called......${steps}"
    steps.sh "mvn clean"
  }
  
 public void mvnTest() {
   steps.sh 'pwd'
   steps.sh 'ls -ltr'
     steps.echo "Test called......${steps}"
    steps.sh "mvn test"
  }
  
  public void startBuild() {
    steps.echo "Build called......${steps}"
    if(steps.isUnix()){
      steps.echo "Running on Unix box"
      steps.sh "mvn clean package  -DskipTests"
    } else {
      steps.echo "Running on window system......"
      steps.bat "mvn clean package  -DskipTests"
    }
    
  }
  
  public void archive() {
     steps.echo "Archive called......${steps}"
    steps.archiveArtifacts artifacts: 'target/*.war', fingerprint: true
  }
}
