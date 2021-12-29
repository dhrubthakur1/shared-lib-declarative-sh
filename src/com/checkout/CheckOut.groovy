package com.checkout;

public class CheckOut {
  def steps
  
  public CheckOut(steps) {
    this.steps = steps
  }
  
  public void startBuild(Map conf = [:]) {
    steps.echo '"CheckOut Startbuild is called"'
    steps.echo "${steps.getClass()} ${conf.url}  ${conf.branch}"    
    steps.sh "ifconfig"
    steps.checkout([
                    $class: 'GitSCM',
                    branches: [[name:  conf.branch ]],
                    userRemoteConfigs: [[ url: conf.url ]]
                  ])
  }
}
