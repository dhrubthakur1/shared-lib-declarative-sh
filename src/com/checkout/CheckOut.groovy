package com.checkout;

public class CheckOut {
  def steps
  
  public CheckOut(steps) {
    this.steps = steps
  }
  
  public void startBuild(def conf = [:]) {
    steps.echo '"CheckOut Startbuild is called"'
    steps.echo "${steps} ${conf.url}  ${conf.branch}"    
    steps.checkout([
                    $class: 'GitSCM',
                    branches: [[name:  conf.branch ]],
                    userRemoteConfigs: [[ url: conf.url ]]
                  ])
  }
}
