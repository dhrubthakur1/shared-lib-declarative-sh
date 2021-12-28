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
     steps.echo "Test called......${steps}"
    steps.sh "mvn test"
  }
  
  public void startBuild() {
     steps.echo "Build called......${steps}"
    steps.sh "mvn clean package  -DskipTests"
  }
  
  public void archive() {
     steps.echo "Archive called......${steps}"
    steps.archiveArtifacts artifacts: 'target/*.war', fingerprint: true
  }
}
