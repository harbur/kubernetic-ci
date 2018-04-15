#!/usr/bin/groovy
package io.harbur.stages

def job() {
  def properties = new io.harbur.utils.Properties()
  if (properties.project().environments) {
    upgrade()
  }
}

def upgrade() {
  def helm = new io.harbur.cmds.Helm()

  stage ('Environments Upgrade') {
    helm.init()
    helm.addRepos()
    helm.upgrade()
  }
}
