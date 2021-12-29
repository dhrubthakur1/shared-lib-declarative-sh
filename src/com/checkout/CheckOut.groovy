package com.checkout;

public class CheckOut {
  def  steps
  
  public CheckOut(steps) {
    steps.echo(steps.getClass().toString())
    steps.echo(this.steps.getClass().toString())
     steps.echo(steps.getClass().getSuperclass().toString())
    steps.echo(this.steps.getClass().getSuperclass().toString())
    this.steps = steps
  }
  
  public void startBuild(Map conf = [:]) {
    steps.echo '"CheckOut Startbuild is called"'
    steps.echo "${steps.getClass().toString()} ${conf.url}  ${conf.branch}"    
    steps.sh "ifconfig"
    steps.checkout([
                    $class: 'GitSCM',
                    branches: [[name:  conf.branch ]],
                    userRemoteConfigs: [[ url: conf.url ]]
                  ])
  }
}
