#!/usr/bin/groovy
package io.harbur.stages

def checkout() {
  def git = new io.harbur.cmds.Git()

  stage ('Checkout') {
    git.checkout()
  }
}

def jobs() {
  def charts = new io.harbur.stages.Charts()
  def captain = new io.harbur.stages.Captain()
  def environments = new io.harbur.stages.Environments()

  captain.job()
  charts.job()
  environments.job()
}
